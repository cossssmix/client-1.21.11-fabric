package com.client.event.render;

import com.client.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;

@Getter
@AllArgsConstructor
public class UpdateRenderStateEvent<T extends LivingEntity, S extends LivingEntityRenderState> extends Event {
	private T livingEntity;
	private S livingEntityRenderState;
	private float tickProgress;
}