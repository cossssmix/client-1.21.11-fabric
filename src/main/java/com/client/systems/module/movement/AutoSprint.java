package com.client.systems.module.movement;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.PlayerTickEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;

import static com.client.util.MinecraftVariables.mc;

import com.google.common.eventbus.Subscribe;

public class AutoSprint extends AbstractModule {
	public AutoSprint() {
		super("auto sprint", "автоматически включает режим бега", Category.Movement);
				
		setKey(GLFW.GLFW_KEY_G);
	}

	@Subscribe
	public void onPlayerTick(PlayerTickEvent event) {
		mc.options.sprintKey.setPressed(true);
	}

	@Override
	public void onDisable() {
		mc.options.sprintKey.setPressed(false);
	}
}
