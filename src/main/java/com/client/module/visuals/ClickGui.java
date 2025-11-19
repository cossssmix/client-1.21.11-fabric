package com.client.module.visuals;

import org.lwjgl.glfw.GLFW;

import com.client.module.AbstractModule;
import com.client.module.EnumCategory;
import com.client.module.ModuleInfo;
import com.client.ui.screen.ClickGuiScreen;

import static com.client.util.IMinecraft.mc;

@ModuleInfo(
	name = "click gui",
	category = EnumCategory.Visuals
)
public class ClickGui extends AbstractModule {
	private ClickGuiScreen clickGuiScreen = new ClickGuiScreen();

	public ClickGui() {
		setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	@Override
	public void onEnable() {
		mc.setScreen(clickGuiScreen);
	}

	@Override
	public void onDisable() {
		clickGuiScreen.close();
	}
}
