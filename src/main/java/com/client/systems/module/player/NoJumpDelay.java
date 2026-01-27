package com.client.systems.module.player;

import static com.client.util.MinecraftVariables.mc;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.PlayerTickEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.google.common.eventbus.Subscribe;

public final class NoJumpDelay extends AbstractModule {

	public NoJumpDelay() {
		super(
			"speed",
			"ускоряют игрока",
			Category.Movement
		);

		setKey(GLFW.GLFW_KEY_G);
	}
	
	@Subscribe
	public void onPlayerTick(final PlayerTickEvent event) {
		mc.player.setLastJump
	}
}
