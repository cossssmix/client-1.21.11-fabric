package com.client.renderer.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public final class RadiusProperty {
	@Getter @Setter
	private float radius;

	public static final RadiusProperty NONE = new RadiusProperty(0.0f);
}