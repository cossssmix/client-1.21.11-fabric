package com.client.module.combat;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.KeyboardInputEvent;
import com.client.event.player.PlayerTickEvent;
import com.client.event.player.SendMovementEvent;
import com.client.event.player.UpdateVelocityEvent;
import com.client.module.AbstractModule;
import com.client.module.EnumCategory;
import com.client.module.ModuleInfo;
import com.client.util.player.Rotations;

import static com.client.util.IMinecraft.mc;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
	name = "test",
	description = "",
	category = EnumCategory.Combat
)
public class Test extends AbstractModule {
	@Getter
	private static float fakeYaw = 90.0f, fakePitch = 0.0f;

	public Test() {
		setKey(GLFW.GLFW_KEY_R);
	}

	@Subscribe
	public void onPlayerTick(PlayerTickEvent event) {
		PlayerEntity target = null;
		double best = 9;

		for (Entity e : mc.world.getEntities()) {
			if (e == mc.player) continue;
			if (!(e instanceof PlayerEntity)) continue;

			double d = mc.player.squaredDistanceTo(e);
			if (d <= best) {
				best = d;
				target = (PlayerEntity) e;
			}
		}

		if (target == null) return;

		double dx = target.getX() - mc.player.getX();
		double dz = target.getZ() - mc.player.getZ();
		double dy = target.getBodyY(0.5) - mc.player.getEyeY();

		fakeYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90f);
		fakePitch = (float) (-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));

		Rotations.rotate(fakeYaw, fakePitch);
	}

	@Subscribe
	public void onSendMovement(SendMovementEvent event) {
		mc.player.setHeadYaw(fakeYaw);
		mc.player.setBodyYaw(fakeYaw);
	}

	@Subscribe
	public void onUpdateVelocity(UpdateVelocityEvent event) {
		if (Float.isNaN(fakeYaw))
			return;

		event.setVelocity(fix(fakeYaw, event.getMovementInput(), event.getSpeed()));
	}

	@Subscribe
	public void onKeyboardInput(KeyboardInputEvent event) {
		if (Float.isNaN(fakeYaw))
			return;
		
		fixMovement(event, fakeYaw);
	}

    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;
        float forward = 1F;
        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;
        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;
        return Math.toRadians(rotationYaw);
    }

    public static void fixMovement(KeyboardInputEvent event, float yaw) {
        final float forward = event.getMovementForward();
        final float strafe = event.getMovementStrafe();

        final double angle = MathHelper.wrapDegrees(Math.toDegrees(direction(mc.player.isGliding() ? yaw : mc.player.getYaw(), forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setMovementForward(closestForward);
        event.setMovementStrafe(closestStrafe);
    }

    private static Vec3d fix(float yaw, Vec3d movementInput, float speed) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7)
            return Vec3d.ZERO;
        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
        float f = MathHelper.sin(yaw * MathHelper.RADIANS_PER_DEGREE);
        float g = MathHelper.cos(yaw * MathHelper.RADIANS_PER_DEGREE);
        return new Vec3d(vec3d.x * (double) g - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) g + vec3d.x * (double) f);
    }
}
