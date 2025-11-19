package com.client.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.client.Client;
import com.client.event.player.UpdateVelocityEvent;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static com.client.util.IMinecraft.mc;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
	private void onUpdateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
		if ((Object) this == mc.player) {
			ci.cancel();
			
			UpdateVelocityEvent updateVelocityEvent = new UpdateVelocityEvent(
				movementInput,
				speed,
				mc.player.getYaw(),
				movementInputToVelocityC(movementInput, speed, mc.player.getYaw())
			);

			Client.getEventBus().post(updateVelocityEvent);

			mc.player.setVelocity(mc.player.getVelocity().add(updateVelocityEvent.getVelocity()));
		}
	}

    @Unique
    private static Vec3d movementInputToVelocityC(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
        float f = MathHelper.sin(yaw * ((float) Math.PI / 180));
        float g = MathHelper.cos(yaw * ((float) Math.PI / 180));
        return new Vec3d(vec3d.x * (double) g - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) g + vec3d.x * (double) f);
    }
}
