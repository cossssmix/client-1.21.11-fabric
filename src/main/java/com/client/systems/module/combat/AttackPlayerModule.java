package com.client.systems.module.combat;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.client.core.ClientContext;
import com.client.event.client.KeyboardInputEvent;
import com.client.event.player.PlayerTickEvent;
import com.client.event.player.UpdateVelocityEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.client.util.player.MovementController;
import com.client.util.rotation.RotationController;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static com.client.util.MinecraftVariables.mc;
import static com.client.util.MinecraftVariables.mcu;

public final class AttackPlayerModule extends AbstractModule {
    private final RotationController rotationController;
    private final MovementController movementController;
    private Optional<Vector2f> auraRotation;

    public AttackPlayerModule(final ClientContext ctx) {
        super("aura", "автоматически наводиться и бьёт сущность", Category.Combat);

		this.auraRotation = Optional.empty();

        this.rotationController = ctx.getRotationController();
        this.movementController = ctx.getMovementController();

        setKey(GLFW.GLFW_KEY_R);
    }

    @Subscribe
    public void onPlayerTick(final PlayerTickEvent event) {
        final LivingEntity target = StreamSupport.stream(mc.world.getEntities().spliterator(), false)
            .filter(e -> e instanceof LivingEntity)
            .map(e -> (LivingEntity) e)
            .filter(livingEntity -> livingEntity != mc.player)
			.filter(mcu::isLife)
            .filter(livingEntity -> mc.player.distanceTo(livingEntity) <= 3)
            .min((p1, p2) -> Float.compare(mc.player.distanceTo(p1), mc.player.distanceTo(p2)))
            .orElse(null);

        if (target == null) {
            rotationController.reset();
            auraRotation = Optional.empty();
            return;
        }

        final Vec3d hitPoint = this.getClosestHitboxPoint(target);

        final double dx = hitPoint.x - mc.player.getX();
        final double dz = hitPoint.z - mc.player.getZ();
        final double dy = hitPoint.y - mc.player.getEyeY();

        final int randomValue = ThreadLocalRandom.current().nextInt(-5, 6);

        this.auraRotation = Optional.of(new Vector2f(
            (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90f) + randomValue,
            (float) (-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)))) + randomValue
        ));

		this.auraRotation.ifPresent(
			rotation -> rotationController.set(new Vector2f(rotation.x(), rotation.y()))
		);

		this.attackTarget(target);
    }

    private Vec3d getClosestHitboxPoint(final LivingEntity target) {
        final Vec3d eyes = mc.player.getEyePos();
        final Box box = target.getBoundingBox();

        final double x = MathHelper.clamp(eyes.x, box.minX, box.maxX);
        final double y = MathHelper.clamp(eyes.y, box.minY, box.maxY);
        final double z = MathHelper.clamp(eyes.z, box.minZ, box.maxZ);

        return new Vec3d(x, y, z);
    }

    public void attackTarget(final LivingEntity target) {
		if (mcu.canDealFullHit(mc.player)) {
			if (mc.options.jumpKey.isPressed()) {
				if (mcu.canCrit(mc.player)) {
					mcu.attack(target);
				}
				return;
			}

			mcu.attack(target);
		}
    }

    @Subscribe
    public void onUpdateVelocity(final UpdateVelocityEvent event) {
        this.movementController.fixUpdateVelocity(event);
    }

    @Subscribe
    public void onKeyboardInput(final KeyboardInputEvent event) {
        this.movementController.fixKeyboardInput(event);
    }

    @Override
    public void onDisable() {
        rotationController.reset();
    }
}
