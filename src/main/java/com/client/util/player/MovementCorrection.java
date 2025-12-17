package com.client.util.player;

import static com.client.util.IMinecraft.mc;

import com.client.Client;
import com.client.event.player.KeyboardInputEvent;
import com.client.event.player.UpdateVelocityEvent;
import com.client.mixin.accessor.EntityAccessor;
import com.client.util.rotation.SilentRotation;
import com.google.common.eventbus.Subscribe;

import net.minecraft.util.math.MathHelper;

public class MovementCorrection {
	private final SilentRotation silentRotation;

	public MovementCorrection(SilentRotation silentRotation) {
		this.silentRotation = silentRotation;

		Client.getContext().getEventBus().register(this);
	}

    @Subscribe
    public void onUpdateVelocity(UpdateVelocityEvent event) {
		silentRotation.getServerRotation().ifPresent(rotation -> {
       		MovementCorrection.fixUpdateVelocity(event, rotation.x());
		});
    }

    @Subscribe
    public void onKeyboardInput(KeyboardInputEvent event) {
		silentRotation.getServerRotation().ifPresent(rotation -> {
			MovementCorrection.fixKeyboardInput(event, rotation.x());
		});
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

	public static void fixUpdateVelocity(UpdateVelocityEvent event, float yaw) {
		if (Float.isNaN(yaw)) return;

		event.setVelocity(EntityAccessor.movementInputToVelocity(
			event.getMovementInput(),
			event.getSpeed(),
			yaw
		));
	}

    public static void fixKeyboardInput(KeyboardInputEvent event, float yaw) {
		if (Float.isNaN(yaw)) return;
		
        final float forward = event.getMovementForward();
        final float strafe = event.getMovementStrafe();

        final double angle = MathHelper.wrapDegrees(Math.toDegrees(direction(
				mc.player.isGliding() ? yaw : mc.player.getYaw(),
				forward,
				strafe
		)));

        if (forward == 0 && strafe == 0) return;

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
}
