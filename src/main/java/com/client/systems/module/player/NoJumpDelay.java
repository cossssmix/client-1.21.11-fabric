package com.client.systems.module.player;

import static com.client.util.MinecraftVariables.mc;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.SendMovementEvent;
import com.client.mixin.accessor.ClientPlayerEntityAccessor;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.google.common.eventbus.Subscribe;

public final class NoJumpDelay extends AbstractModule {

	public NoJumpDelay() {
		super(
			"no jump delay",
			"",
			Category.Movement
		);

		setKey(GLFW.GLFW_KEY_H);
	}
	
	@Subscribe
	public void onSendMovement(final SendMovementEvent event) {
		final ClientPlayerEntityAccessor clientPlayerEntityAccessor = (ClientPlayerEntityAccessor) mc.player;
		
		clientPlayerEntityAccessor.setTicksToNextAutoJump(0);
	}
}
