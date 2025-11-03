package com.client.module.movement;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.PlayerTickEvent;
import com.client.module.AbstractModule;
import com.client.module.EnumCategory;
import com.client.module.ModuleInfo;
import static com.client.util.IMinecraft.mc;
import com.google.common.eventbus.Subscribe;

@ModuleInfo(
	name = "auto sprint",
	description = "автоматически включает режим бега",
	category = EnumCategory.Movement
)
public class AutoSprint extends AbstractModule {
	public AutoSprint() {
		setKey(GLFW.GLFW_KEY_G);
	}

	@Subscribe
	public void onPlayerTick(PlayerTickEvent event) {
		mc.options.sprintKey.setPressed(true);
	}

	@Override
	public void onDisable() {
		mc.options.sprintKey.setPressed(false);
	}
}
