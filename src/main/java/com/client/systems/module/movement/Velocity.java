package com.client.systems.module.movement;

import static com.client.util.MinecraftVariables.mc;

import org.lwjgl.glfw.GLFW;

import com.client.event.client.PacketEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.google.common.eventbus.Subscribe;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public final class Velocity extends AbstractModule {
	public Velocity() {
		super(
			"velocity",
			"",
			Category.Movement
		);

		setKey(GLFW.GLFW_KEY_Y);
	}

	@Subscribe
	public void onPacketReceive(final PacketEvent.Receive event) {
		if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {
			if (mc.player != null
				|| !mc.player.isTouchingWater()
				|| !mc.player.isSubmergedInWater()
				|| !mc.player.isInLava()
			) {
				event.cancel();
			}
		}
	}
}
