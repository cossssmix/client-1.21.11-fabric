package com.client.systems.module.movement;

import static com.client.util.MinecraftVariables.mc;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.client.core.ClientContext;
import com.client.event.player.KeyboardInputEvent;
import com.client.event.player.UpdateVelocityEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.client.util.player.MovementController;
import com.client.util.rotation.RotationController;
import com.google.common.eventbus.Subscribe;

import net.minecraft.util.math.MathHelper;

public class LegitStrafe extends AbstractModule {
	private final RotationController rotationController;
	private final MovementController movementController;
	private float yaw = Float.NaN;

	public LegitStrafe(ClientContext ctx) {
		super("legit strafe", "легитные стрейфы", Category.Movement);

		this.rotationController = ctx.getRotationController();
		this.movementController = ctx.getMovementController();

		setKey(GLFW.GLFW_KEY_U);
	}

	@Override
	public void onDisable() {
		rotationController.reset();
	}

	@Subscribe
	public void onKeyboardInput(KeyboardInputEvent event) {
		movementController.fixKeyboardInput(event);

		float forward = event.getMovementForward();
		float strafe  = event.getMovementStrafe();

		float yawOffset = calculateYawOffset(forward, strafe);

		if (Float.isNaN(yawOffset)) {
			yaw = Float.NaN;
			return;
		}

		yaw = MathHelper.wrapDegrees(mc.player.getYaw() + yawOffset);

		rotationController.set(new Vector2f(yaw, mc.player.getPitch()));
	}

	private float calculateYawOffset(float forward, float strafe) {
		if (forward == 0.0f && strafe == 0.0f) return Float.NaN;

		return (float) Math.toDegrees(Math.atan2(-strafe, forward));
	}

	@Subscribe
    public void onUpdateVelocity(UpdateVelocityEvent event) {
       	movementController.fixUpdateVelocity(event);
    }
}
