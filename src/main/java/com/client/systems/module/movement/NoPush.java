package com.client.systems.module.movement;

import static com.client.util.MinecraftVariables.mc;

import org.lwjgl.glfw.GLFW;

import com.client.event.entity.PushAwayEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.google.common.eventbus.Subscribe;

import net.minecraft.util.math.Vec3d;

public final class NoPush extends AbstractModule {

	public NoPush() {
		super(
			"no push",
			"убирает отталкивание от сущностей",
			Category.Movement
		);

		setKey(GLFW.GLFW_KEY_N);
	}

	@Subscribe
	public void onPushAway(final PushAwayEvent event) {
		if (event.getEntity() == mc.player) {
			Vec3d velocity = event.getVelocity();

			event.setVelocity(new Vec3d(
				0.0,
				velocity.getY(),
				0.0
			));
		}
	}
}
