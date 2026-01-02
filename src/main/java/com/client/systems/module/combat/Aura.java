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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static com.client.util.MinecraftVariables.mc;

public final class Aura extends AbstractModule {
    private final RotationController rotationController;
    private final MovementController movementController;
    private Optional<Vector2f> auraRotation = Optional.empty();

    public Aura(final ClientContext ctx) {
        super("aura", "автоматически наводиться и бьёт сущность", Category.Combat);

        this.rotationController = ctx.getRotationController();
        this.movementController = ctx.getMovementController();

        setKey(GLFW.GLFW_KEY_R);
    }

    @Subscribe
    public void onPlayerTick(final PlayerTickEvent event) {
        PlayerEntity target = StreamSupport.stream(mc.world.getEntities().spliterator(), false)
            .filter(e -> e instanceof PlayerEntity)
            .map(e -> (PlayerEntity) e)
            .filter(player -> player != mc.player && !player.isDead())
            .filter(player -> mc.player.distanceTo(player) <= 3)
            .min((p1, p2) -> Float.compare(mc.player.distanceTo(p1), mc.player.distanceTo(p2)))
            .orElse(null);

        if (target == null) {
            rotationController.reset();
            auraRotation = Optional.empty();
            return;
        }

        Vec3d hitPoint = getClosestHitboxPoint(target);

        double dx = hitPoint.x - mc.player.getX();
        double dz = hitPoint.z - mc.player.getZ();
        double dy = hitPoint.y - mc.player.getEyeY();

        int randomValue = ThreadLocalRandom.current().nextInt(-5, 6);

        auraRotation = Optional.of(new Vector2f(
            (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90f) + randomValue,
            (float) (-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)))) + randomValue
        ));

        float tickProgress = mc.getRenderTickCounter().getTickProgress(true);

        auraRotation.ifPresent(
            rotation -> rotationController.set(new Vector2f(rotation.x(), rotation.y()))
        );

        this.attackTarget(target, tickProgress);
    }

    private Vec3d getClosestHitboxPoint(final PlayerEntity target) {
        Vec3d eyes = mc.player.getEyePos();
        Box box = target.getBoundingBox();

        double x = MathHelper.clamp(eyes.x, box.minX, box.maxX);
        double y = MathHelper.clamp(eyes.y, box.minY, box.maxY);
        double z = MathHelper.clamp(eyes.z, box.minZ, box.maxZ);

        return new Vec3d(x, y, z);
    }

    public void attackTarget(final LivingEntity target, final float tickProgress) {
        if (mc.player.getAttackCooldownProgress(tickProgress) >= 0.9f &&
            target.isAlive() &&
            !target.isDead() &&
            target.deathTime == 0
        ) {
            if (mc.options.jumpKey.isPressed() && mc.player.fallDistance <= 0.0f) return;

            if (mc.options.jumpKey.isPressed() &&
                !mc.player.isOnGround() &&
                mc.player.fallDistance > 0.0f
            ) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
                return;
            }

            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    @Subscribe
    public void onUpdateVelocity(final UpdateVelocityEvent event) {
        movementController.fixUpdateVelocity(event);
    }

    @Subscribe
    public void onKeyboardInput(final KeyboardInputEvent event) {
        movementController.fixKeyboardInput(event);
    }

    @Override
    public void onDisable() {
        rotationController.reset();
    }
}
