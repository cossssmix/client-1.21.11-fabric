package com.client.systems.module.combat;

import static com.client.util.MinecraftVariables.mc;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.SendMovementEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;

public final class Criticals extends AbstractModule {
	public Criticals() {
		super(
			"criticals",
			"криты под мета хвх",
			Category.Combat
		);

		setKey(GLFW.GLFW_KEY_N);
	}

	private int lowHopTickCounter = 0;

	public void onPlayerTick(final SendMovementEvent.Pre event) {
		final float tickProgress = mc.getRenderTickCounter().getTickProgress(true);

		if (mc.player.isOnGround() && mc.player.getAttackCooldownProgress(tickProgress) >= 0.7f) {
			++this.lowHopTickCounter;
		} else {
			this.lowHopTickCounter = 0;
		}

		switch (this.lowHopTickCounter) {
			case 1:
				mc.player.setOnGround(false);
				mc.player.setPosition(mc.player.getX(), mc.player.getY() + 1.0E-4F, mc.player.getZ());
				break;
			case 2:
				mc.player.setOnGround(false);
				mc.player.setPosition(mc.player.getX(), mc.player.getY() + 1.0E-5F, mc.player.getZ());
				break;
			case 3:
				mc.player.setOnGround(false);
				mc.player.fallDistance += 0.1F;
				this.lowHopTickCounter = 0;
				break;
		}
    }
	
	// @Subscribe
	// public void onSendPacket(final PacketEvent.Send event) {
	// 	final Packet<?> packet = event.getPacket();

	// 	if (packet instanceof PlayerInteractEntityC2SPacket) {
	// 		final PlayerInteractEntityC2SPacket interactPacket = (PlayerInteractEntityC2SPacket) packet;

	// 		interactPacket.handle(new PlayerInteractEntityC2SPacket.Handler() {

	// 			@Override
	// 			public void attack() {
	// 				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
	// 					mc.player.getX(),
	// 					mc.player.getY() + 1.0E-4F,
	// 					mc.player.getZ(),
	// 					false,
	// 					mc.player.horizontalCollision
	// 				));

	// 				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
	// 					mc.player.getX(),
	// 					mc.player.getY(),
	// 					mc.player.getZ(),
	// 					false,
	// 					mc.player.horizontalCollision
	// 				));

	// 				mc.player.sendMessage(Text.literal("attack trigger"), false);
	// 			}

	// 			@Override
	// 			public void interact(Hand hand) {}

	// 			@Override
	// 			public void interactAt(Hand hand, Vec3d pos) {}
	// 		});
	// 	}
	// }
}
