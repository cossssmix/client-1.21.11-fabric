package com.client.gui.screen;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.Color;

import com.client.systems.module.ModuleRepository;
import com.client.systems.module.visuals.ClickGui;

public class ClickGuiScreen extends Screen {
	private final ModuleRepository moduleRepository;
	final int width = 100, height = 200; 

	public ClickGuiScreen(final String title, final ModuleRepository moduleRepository) {
		super(Text.of(title));

		this.moduleRepository = moduleRepository;
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		return true;
	}

	@Override
	public void close() {
		final ClickGui clickGuiModule = this.moduleRepository.getModule(ClickGui.class);

		if (clickGuiModule.isEnabled()) {
			this.moduleRepository.toggle(clickGuiModule);
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
