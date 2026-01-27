package com.client.systems.module.movement;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.UpdateVelocityEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.google.common.eventbus.Subscribe;

public final class Speed extends AbstractModule {

	public Speed() {
		super(
			"speed",
			"ускоряют игрока",
			Category.Movement
		);

		setKey(GLFW.GLFW_KEY_G);
	}
	
	@Subscribe
	public void onUpdateVelocity(final UpdateVelocityEvent event) {
		event.setVelocity(event.getVelocity().multiply(1.1));
	}
}
