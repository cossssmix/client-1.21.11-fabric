package com.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Tessellator;

public class IMinecraft {
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static Tessellator tessellator = Tessellator.getInstance();
}
