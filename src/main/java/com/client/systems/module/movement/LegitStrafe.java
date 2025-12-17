package com.client.systems.module.movement;

import static com.client.util.IMinecraft.mc;

import org.lwjgl.glfw.GLFW;

import com.client.Client;
import com.client.event.player.KeyboardInputEvent;
import com.client.event.player.UpdateVelocityEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.client.systems.module.ModuleInfo;
import com.client.util.player.MovementCorrection;
import com.client.util.rotation.SilentRotation;
import com.google.common.eventbus.Subscribe;

import net.minecraft.util.math.MathHelper;

@ModuleInfo(
	name = "legit strafe",
	description = "легитные стрейфы",
	category = Category.Movement
)
public class LegitStrafe extends AbstractModule {
	private final SilentRotation silentRotation;
	private float yaw = Float.NaN;

	public LegitStrafe() {
		silentRotation = Client.getContext().getSilentRotation();

		setKey(GLFW.GLFW_KEY_U);
	}

	@Override
	public void onDisable() {
		silentRotation.reset();
	}

	@Subscribe
	public void onUpdateVelocity(UpdateVelocityEvent event) {
		MovementCorrection.fixUpdateVelocity(event, yaw);
	}

	@Subscribe
	public void onKeyboardInput(KeyboardInputEvent event) {
		float forward = event.getMovementForward();
		float strafe  = event.getMovementStrafe();

		float yawOffset = calculateYawOffset(forward, strafe);

		if (Float.isNaN(yawOffset)) {
			yaw = Float.NaN;
			return;
		}

		yaw = MathHelper.wrapDegrees(mc.player.getYaw() + yawOffset);

		silentRotation.set(yaw, mc.player.getPitch());

		MovementCorrection.fixKeyboardInput(event, yaw);
	}

	private float calculateYawOffset(float forward, float strafe) {
		if (forward == 0.0f && strafe == 0.0f) return Float.NaN;

		return (float) Math.toDegrees(Math.atan2(-strafe, forward));
	}
}
