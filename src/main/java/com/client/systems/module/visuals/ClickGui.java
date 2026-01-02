package com.client.systems.module.visuals;

import static com.client.util.MinecraftVariables.mc;

import org.lwjgl.glfw.GLFW;

import com.client.core.ClientContext;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.client.ui.screen.ClickGuiScreen;

public final class ClickGui extends AbstractModule {
	private final ClientContext ctx;
	private ClickGuiScreen clickGuiScreen;

    public ClickGui(final ClientContext ctx) {
		super(
			"click gui",
			"",
			Category.Visuals
		);

		this.ctx = ctx;

        setKey(GLFW.GLFW_KEY_J);
    }

	@Override
	public void onEnable() {
		clickGuiScreen = new ClickGuiScreen("click gui", this.ctx);

		mc.setScreen(clickGuiScreen);
	}

	@Override
	public void onDisable() {
		clickGuiScreen.close();
	}
}
