package com.client.ui.screen;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.Color;

import com.client.core.ClientContext;
import com.client.systems.module.ModuleStorage;
import com.client.systems.module.visuals.ClickGui;

public class ClickGuiScreen extends Screen {
	private final ModuleStorage moduleStorage;
	final int width = 100, height = 200; 

	public ClickGuiScreen(final String title, final ClientContext ctx) {
		super(Text.of(title));

		this.moduleStorage = ctx.getModuleStorage();
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		return true;
	}

	@Override
	public void close() {
		ClickGui clickGuiModule = this.moduleStorage.getModule(ClickGui.class);

		if (clickGuiModule.isEnabled()) {
			this.moduleStorage.toggle(clickGuiModule);
		}

		super.close();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		int centerX = context.getScaledWindowWidth() / 2;
		int centerY = context.getScaledWindowHeight() / 2;

		context.fill(
			centerX - width / 2,
			centerY - height / 2,
			centerX + width / 2,
			centerY + height / 2,
			new Color(255, 255, 255, 180).getRGB()
		);
	}
}
