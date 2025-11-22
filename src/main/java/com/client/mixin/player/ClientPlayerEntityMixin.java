package com.client.mixin.player;

import static com.client.util.IMinecraft.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.client.Client;
import com.client.event.EventStage;
import com.client.event.player.PlayerTickEvent;
import com.client.event.player.SendMovementEvent;
import com.client.util.player.Rotations;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		Client.getEventBus().post(new PlayerTickEvent());
	}

	@Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
	private void onPreSendMovementPackets(CallbackInfo ci) {
		Rotations.onSendMovementPacketsPre();

		SendMovementEvent sendMovementEvent = new SendMovementEvent(
			mc.player.getX(),
			mc.player.getY(),
			mc.player.getZ(),
			mc.player.getYaw(),
			mc.player.getPitch(),
			mc.player.isOnGround(),
			EventStage.PRE
		);

		Client.getEventBus().post(sendMovementEvent);

		if (sendMovementEvent.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "sendMovementPackets", at = @At("TAIL"))
	private void onPostSendMovementPackets(CallbackInfo ci) {
		Rotations.onSendMovementPacketsPost();
		Client.getEventBus().post(new SendMovementEvent(EventStage.POST));
	}
}