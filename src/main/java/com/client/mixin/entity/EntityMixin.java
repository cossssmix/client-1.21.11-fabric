package com.client.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.client.Client;
import com.client.event.player.UpdateVelocityEvent;
import com.client.mixin.accessor.EntityAccessor;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import static com.client.util.IMinecraft.mc;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
	private void onUpdateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
		if ((Object) this == mc.player) {
			ci.cancel();
			
			UpdateVelocityEvent updateVelocityEvent = new UpdateVelocityEvent(
				movementInput,
				speed,
				mc.player.getYaw(),
				EntityAccessor.movementInputToVelocity(movementInput, speed, mc.player.getYaw())
			);

			Client.getEventBus().post(updateVelocityEvent);

			mc.player.setVelocity(mc.player.getVelocity().add(updateVelocityEvent.getVelocity()));
		}
	}
}
