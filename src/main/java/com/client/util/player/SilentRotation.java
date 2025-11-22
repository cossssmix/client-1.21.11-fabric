package com.client.util.player;

import static com.client.util.IMinecraft.mc;

import com.client.Client;
import com.client.event.EventStage;
import com.client.event.player.PlayerJumpEvent;
import com.client.event.player.SendMovementEvent;
import com.client.event.render.UpdateRenderStateEvent;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.MathHelper;

public class SilentRotation {
	@Getter @Setter
	private float serverYaw, serverPitch;
	@Getter @Setter
    private float originalYaw, originalPitch;
	@Getter
	private float prevPitch;

	public SilentRotation() {
		this.serverYaw = Float.NaN;
		this.serverPitch = Float.NaN;

		this.originalYaw = Float.NaN;
		this.originalPitch = Float.NaN;

		this.prevPitch = Float.NaN;

		Client.getEventBus().register(this);
	}

    public void set(float yaw, float pitch) {
        this.serverYaw = yaw;
		this.serverPitch = pitch;
    }

	@Subscribe
	public void onPlayerJump(PlayerJumpEvent event) {
		if (event.getStage() == EventStage.PRE) {
			Client.getRotation().setOriginalYaw(mc.player.getYaw());
			Client.getRotation().setOriginalPitch(mc.player.getPitch());

			mc.player.setYaw(this.serverYaw);
			mc.player.setPitch(this.serverPitch);
		}

		if (event.getStage() == EventStage.POST) {
			mc.player.setYaw(this.originalYaw);
			mc.player.setPitch(this.originalPitch);

			this.originalYaw = Float.NaN;
			this.originalPitch = Float.NaN;
		}
	}

	@Subscribe
	public void onUpdateRenderState(UpdateRenderStateEvent event) {
		if (!Float.isNaN(this.serverPitch) && event.getLivingEntity() == mc.player) {
			if (Float.isNaN(this.prevPitch)) {
				this.prevPitch = this.getServerPitch();
			}

			float pitch = event.getTickProgress() == 1.0F
				? this.getServerPitch()
				: MathHelper.lerp(event.getTickProgress(), this.prevPitch, this.getServerPitch());

			event.getLivingEntityRenderState().pitch = pitch;

			this.prevPitch = this.getServerPitch();
		}
	}

	@Subscribe
    public void onSendMovement(SendMovementEvent event) {
		if (event.getStage() == EventStage.PRE) {
			if (mc.getCameraEntity() != mc.player) return;

			if (!Float.isNaN(this.serverYaw) && !Float.isNaN(this.serverPitch)) {
				this.originalYaw = mc.player.getYaw();
				this.originalPitch = mc.player.getPitch();

				mc.player.setYaw(serverYaw);
				mc.player.setPitch(serverPitch);

				mc.player.setHeadYaw(serverYaw);
				mc.player.setBodyYaw(serverYaw);
			}
		}

		if (event.getStage() == EventStage.POST) {
			if (!Float.isNaN(this.originalYaw) && !Float.isNaN(this.originalPitch)) {
				mc.player.setYaw(this.originalYaw);
				mc.player.setPitch(this.originalPitch);

				this.originalYaw = Float.NaN;
				this.originalPitch = Float.NaN;
			}
		}
    }

	public void reset() {
		this.serverYaw = Float.NaN;
		this.serverPitch = Float.NaN;
	}
}