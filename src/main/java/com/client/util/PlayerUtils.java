package com.client.util;

import net.minecraft.util.math.Vec2f;
import static com.client.util.IMinecraft.mc;

public final class PlayerUtils {
	public static boolean isMoving() {
		Vec2f movementInput = mc.player.input.getMovementInput();

		return movementInput.x > 1.0E-5F && movementInput.y > 1.0E-5F; 
	}
}
