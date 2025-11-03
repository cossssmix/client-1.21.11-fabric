package com.client.mixin.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.client.Client;
import com.client.event.player.KeyboardEvent;
import static com.client.util.IMinecraft.mc;

import net.minecraft.client.Keyboard;


@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
	@Inject(method = "onKey", at = @At("HEAD"))
	private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		if (window == mc.getWindow().getHandle()) {
			Client.getEventBus().post(new KeyboardEvent(window, key, scancode, action, modifiers));
		}
	}
}
