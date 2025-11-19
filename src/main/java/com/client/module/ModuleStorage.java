package com.client.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.client.Client;
import com.client.event.player.KeyboardEvent;
import com.client.module.combat.*;
import com.client.module.movement.*;
import com.client.module.visuals.*;
import com.client.ui.screen.ClickGuiScreen;
import com.google.common.eventbus.Subscribe;
import static com.client.util.IMinecraft.mc;

public final class ModuleStorage {
	private List<AbstractModule> modules = new ArrayList<>();

	public ModuleStorage() {
		Client.getEventBus().register(this);

		modules.addAll(List.of(
			new AutoSprint(),
			new ClickGui(),
			new Test()
		));
	}

	@Subscribe
	public void onKeyboard(KeyboardEvent event) {
		boolean isValidScreen = mc.currentScreen == null || mc.currentScreen instanceof ClickGuiScreen;

		if (event.getAction() == GLFW.GLFW_PRESS && isValidScreen) {
			for (AbstractModule module : modules) {
				if (module.getKey() == event.getKey()) {
					module.toggle();
				}
			}
		}
	}

	public <T extends AbstractModule> T getModule(Class<T> clazz) {
		for (AbstractModule module : modules) {
			if (clazz.isInstance(module)) {
				return clazz.cast(module);
			}
		}
		return null;
	}
}
