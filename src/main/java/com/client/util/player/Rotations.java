package com.client.util.player;

import static com.client.util.IMinecraft.mc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rotations {
	private static float serverYaw = Float.NaN, serverPitch = Float.NaN;
    private static float prevYaw = Float.NaN, prevPitch = Float.NaN;

    public static void rotate(float yaw, float pitch) {
        serverYaw = yaw;
		serverPitch = pitch;
    }

    public static void onSendMovementPacketsPre() {
        if (mc.getCameraEntity() != mc.player) return;

        if (!Float.isNaN(serverYaw) && !Float.isNaN(serverPitch)) {
            prevYaw = mc.player.getYaw();
            prevPitch = mc.player.getPitch();

            mc.player.setYaw(serverYaw);
            mc.player.setPitch(serverPitch);
        }
    }

	public static void reset() {
		serverYaw = Float.NaN;
		serverPitch = Float.NaN;
	}

    public static void onSendMovementPacketsPost() {
        if (!Float.isNaN(prevYaw) && !Float.isNaN(prevPitch)) {
            mc.player.setYaw(prevYaw);
            mc.player.setPitch(prevPitch);
            prevYaw = Float.NaN;
            prevPitch = Float.NaN;
        }
    }
}