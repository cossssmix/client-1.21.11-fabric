package com.client.module.combat;

import org.lwjgl.glfw.GLFW;

import com.client.Client;
import com.client.event.player.KeyboardInputEvent;
import com.client.event.player.PlayerTickEvent;
import com.client.event.player.UpdateVelocityEvent;
import com.client.mixin.accessor.EntityAccessor;
import com.client.module.AbstractModule;
import com.client.module.EnumCategory;
import com.client.module.ModuleInfo;
import com.client.util.player.MovementCorrection;

import static com.client.util.IMinecraft.mc;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(
	name = "test",
	description = "",
	category = EnumCategory.Combat
)
public class Test extends AbstractModule {
	@Getter
	private static float fakeYaw, fakePitch;

	public Test() {
		setKey(GLFW.GLFW_KEY_R);
	}

	@Subscribe
	public void onPlayerTick(PlayerTickEvent event) {
		PlayerEntity target = null;
		double best = 9;

		for (Entity entity : mc.world.getEntities()) {
			if (entity == mc.player) continue;
			if (!(entity instanceof PlayerEntity)) continue;

			double d = mc.player.squaredDistanceTo(entity);

			if (d <= best) {
				best = d;
				target = (PlayerEntity) entity;
			} else {
				target = null;
			}
		}

		if (target == null) {
			Client.getRotation().reset();
			return;
		};

		double dx = target.getX() - mc.player.getX();
		double dz = target.getZ() - mc.player.getZ();
		double dy = target.getBodyY(0.5) - mc.player.getEyeY();

		fakeYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90f);
		fakePitch = (float) (-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));

		Client.getRotation().set(fakeYaw, fakePitch);
	}
	
	@Override
	public void onDisable() {
		Client.getRotation().reset();
	}

	@Subscribe
	public void onUpdateVelocity(UpdateVelocityEvent event) {
		if (Float.isNaN(fakeYaw))
			return;

		event.setVelocity(EntityAccessor.movementInputToVelocity(
			event.getMovementInput(),
			event.getSpeed(),
			fakeYaw
		));
	}

	@Subscribe
	public void onKeyboardInput(KeyboardInputEvent event) {
		if (Float.isNaN(fakeYaw))
			return;
		
		MovementCorrection.fixMovement(event, fakeYaw);
	}
}
