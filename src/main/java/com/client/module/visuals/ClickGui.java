package com.client.module.visuals;

import org.lwjgl.glfw.GLFW;

import com.client.event.render.HudRendererEvent;
import com.client.module.AbstractModule;
import com.client.module.EnumCategory;
import com.client.module.ModuleInfo;
import com.google.common.eventbus.Subscribe;

@ModuleInfo(
	name = "click gui",
	category = EnumCategory.Visuals
)
public class ClickGui extends AbstractModule {

	public ClickGui() {
		setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	@Subscribe
	public void onHudRenderer(HudRendererEvent event) {
	}
}
