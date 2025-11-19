package com.client.renderer.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.ColorHelper;

import java.awt.Color;

@AllArgsConstructor
public final class ColorProperty {
	@Getter @Setter
	private Color color;

	public int getArgb() {
		return ColorHelper.getArgb(color.getRed(), color.getGreen(), color.getBlue());
	}
}