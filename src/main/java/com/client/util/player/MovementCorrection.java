package com.client.util.player;

import com.client.event.player.KeyboardInputEvent;

import net.minecraft.util.math.MathHelper;
import static com.client.util.IMinecraft.mc;

public class MovementCorrection {
    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;
        float forward = 1F;
        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;
        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;
        return Math.toRadians(rotationYaw);
    }

    public static void fixMovement(KeyboardInputEvent event, float yaw) {
        final float forward = event.getMovementForward();
        final float strafe = event.getMovementStrafe();

        final double angle = MathHelper.wrapDegrees(Math.toDegrees(direction(
				mc.player.isGliding() ? yaw : mc.player.getYaw(),
				forward,
				strafe
		)));

        if (forward == 0 && strafe == 0) return;

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setMovementForward(closestForward);
        event.setMovementStrafe(closestStrafe);
    }
}
