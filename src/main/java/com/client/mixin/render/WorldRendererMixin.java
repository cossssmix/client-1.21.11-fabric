package com.client.mixin.render;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.client.Client;
import com.client.event.render.WorldRendererEvent;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.ObjectAllocator;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Inject(method = "render", at = @At("HEAD"))
	private void onRender(
		ObjectAllocator allocator,
		RenderTickCounter tickCounter,
		boolean renderBlockOutline,
		Camera camera,
		GameRenderer gameRenderer,
		Matrix4f positionMatrix,
		Matrix4f projectionMatrix,
		CallbackInfo ci
	) {
		Client.getEventBus().post(new WorldRendererEvent(allocator, tickCounter, renderBlockOutline, camera, gameRenderer, positionMatrix, projectionMatrix));
	}
}
