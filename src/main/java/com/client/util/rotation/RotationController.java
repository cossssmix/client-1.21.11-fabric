package com.client.util.rotation;

import static com.client.util.MinecraftVariables.mc;

import java.util.Optional;

import org.joml.Vector2f;

import com.client.core.ClientContext;
import com.client.event.player.PlayerJumpEvent;
import com.client.event.player.SendMovementEvent;
import com.client.event.render.UpdateRenderStateEvent;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import net.minecraft.util.math.MathHelper;

public class RotationController {
	@Getter
	private Optional<Vector2f> serverRotation;
	@Getter
	private Optional<Vector2f> originalRotation;
	@Getter
	private Optional<Vector2f> prevRotation;

	public RotationController(final ClientContext ctx) {
		serverRotation = Optional.empty();
		originalRotation = Optional.empty();
		prevRotation = Optional.empty();

		ctx.getEventBus().register(this);
	}

    public void set(Vector2f rotation) {
		serverRotation = Optional.of(rotation);
    }

	@Subscribe
	public void onPlayerJumpPre(final PlayerJumpEvent.Pre event) {
		serverRotation.ifPresent(rotation -> {
			originalRotation = Optional.of(new Vector2f(
				mc.player.getYaw(),
				mc.player.getPitch()
			));

			mc.player.setYaw(rotation.x());
			mc.player.setPitch(rotation.y());
		});
	}

	@Subscribe
	public void onPlayerJumpPost(final PlayerJumpEvent.Post event) {
		originalRotation.ifPresent(rotation -> {
			mc.player.setYaw(rotation.x());
			mc.player.setPitch(rotation.y());

			originalRotation = Optional.empty();
		});
	}

	@Subscribe
	public void onUpdateRenderState(final UpdateRenderStateEvent event) {
		if (event.getLivingEntity() != mc.player) return;
		
		serverRotation.ifPresent(rotation -> {
			prevRotation.ifPresentOrElse(prevRotation -> {
				float pitch = event.getTickProgress() == 1.0F
					? rotation.y()
					: MathHelper.lerp(event.getTickProgress(), prevRotation.y(), rotation.y());
				
				event.getLivingEntityRenderState().pitch = pitch;

				prevRotation.set(mc.player.getYaw(), pitch);
			}, () -> {
				prevRotation = Optional.of(new Vector2f(
					rotation.x(), rotation.y()
				));
			});
		});
	}

	@Subscribe
    public void onSendMovementPre(final SendMovementEvent.Pre event) {
		if (mc.getCameraEntity() != mc.player) return;

		serverRotation.ifPresent(rotation -> {
			originalRotation = Optional.of(new Vector2f(
				mc.player.getYaw(),
				mc.player.getPitch()
			));

			mc.player.setYaw(rotation.x());
			mc.player.setPitch(rotation.y());

			mc.player.setHeadYaw(rotation.x());
			mc.player.setBodyYaw(rotation.x());
		});
    }

	@Subscribe
	public void onSendMovementPost(final SendMovementEvent.Post event) {
		if (mc.getCameraEntity() != mc.player) return;

		originalRotation.ifPresent(rotation -> {
			mc.player.setYaw(rotation.x());
			mc.player.setPitch(rotation.y());

			originalRotation = Optional.empty();
		});
	}

	public void reset() {
		serverRotation = Optional.empty();
	}
}