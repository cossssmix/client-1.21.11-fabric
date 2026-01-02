package com.client.systems.module.visuals;

import static com.client.util.MinecraftVariables.mc;

import com.client.Client;
import com.client.event.render.HudRenderEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.client.systems.module.ModuleStorage;
import com.google.common.eventbus.Subscribe;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.glfw.GLFW;

public final class Hud extends AbstractModule {
	private final ModuleStorage moduleStorage;

	public Hud(final ModuleStorage moduleStorage) {
		super(
			"hud",
			"",
			Category.Visuals
		);

		this.moduleStorage = moduleStorage;
		this.moduleStorage.toggle(this);

		setKey(GLFW.GLFW_KEY_B);
	}

	@Subscribe
	public void onHudRender(final HudRenderEvent event) {
		final DrawContext context = event.getContext();

		this.drawWaterMark(context);
		this.drawArrayList(context);
	}

	private void drawWaterMark(final DrawContext context) {
		context.drawText(
			mc.textRenderer,
			Client.MOD_ID + " " + SharedConstants.getGameVersion().name(),
			3, 2,
			Color.WHITE.getRGB(),
			false
		);
	}

	private void drawArrayList(final DrawContext context) {
		List<AbstractModule> modules = new ArrayList<>(this.moduleStorage.getModules());

		modules.sort(Comparator.comparingInt((AbstractModule m) -> m.getName().length()).reversed());

		int yOffset = 15;

		for (final AbstractModule module : modules) {
			if (module.isEnabled()) {
				context.drawText(
					mc.textRenderer,
					module.getName(),
					3,
					yOffset,
					Color.WHITE.getRGB(),
					false
				);

				yOffset += 8;
			}
		};
	}
}
