package com.client.systems.module.combat;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.client.Client;
import com.client.event.player.PlayerTickEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.client.systems.module.ModuleInfo;
import com.client.util.rotation.SilentRotation;

import static com.client.util.IMinecraft.mc;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

@ModuleInfo(
    name = "aura",
    description = "",
    category = Category.Combat
)
public class Aura extends AbstractModule {
	private final SilentRotation silentRotation;
    private Optional<Vector2f> auraRotation = Optional.empty();
	// private Optional<Vector2f> prevAuraRotation = Optional.empty();

    public Aura() {
		silentRotation = Client.getContext().getSilentRotation();

        setKey(GLFW.GLFW_KEY_R);
    }

    @Subscribe
    public void onPlayerTick(PlayerTickEvent event) {
        PlayerEntity target = null;
        double best = 9.0;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player) continue;
            if (!(entity instanceof PlayerEntity targetPlayer)) continue;
            if (targetPlayer.isDead()) continue;

            double d = mc.player.squaredDistanceTo(targetPlayer);
            if (d < best) {
                best = d;
                target = targetPlayer;
            }
        }

        if (target == null) {
            silentRotation.reset();

			auraRotation = Optional.empty();
			// prevAuraRotation = Optional.empty();

            return;
        }

        double dx = target.getX() - mc.player.getX();
        double dz = target.getZ() - mc.player.getZ();
        double dy = target.getBodyY(0.5) - mc.player.getEyeY();

        int randomValue = ThreadLocalRandom.current().nextInt(-5, 6);

        auraRotation = Optional.of(new Vector2f(
			(float) (Math.toDegrees(Math.atan2(dz, dx)) - 90f) + randomValue,
         	(float) (-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)))) + randomValue
		));

        float tickProgress = mc.getRenderTickCounter().getTickProgress(true);

		auraRotation.ifPresent(
			rotation -> silentRotation.set(rotation.x(), rotation.y())
		);

        this.attackTarget(target, tickProgress);
    }

    public void attackTarget(LivingEntity target, float tickProgress) {
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

    @Override
    public void onDisable() {
        silentRotation.reset();
    }
}
