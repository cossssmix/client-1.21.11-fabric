package com.client.systems.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.client.Client;
import com.client.event.player.KeyboardEvent;
import com.client.systems.module.combat.*;
import com.client.systems.module.movement.*;
import com.client.systems.module.visuals.*;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;

import static com.client.util.IMinecraft.mc;

public final class ModuleStorage {
	@Getter
	private List<AbstractModule> modules;

	public ModuleStorage() {
		modules = new ArrayList<>();

		Client.getContext().getEventBus().register(this);

		modules.addAll(List.of(
			new AutoSprint(),
			new Aura(),
			new Test(),
			new LegitStrafe()
		));
	}

	@Subscribe
	public void onKeyboard(KeyboardEvent event) {
		boolean isValidScreen = mc.currentScreen == null;

		if (event.getAction() == GLFW.GLFW_PRESS && isValidScreen) {
			modules.stream()
				.filter(module -> module.getKey() == event.getInput().getKeycode())
				.forEach(AbstractModule::toggle);
		}
	}

	public <T extends AbstractModule> T getModule(Class<T> clazz) {
		return modules.stream()
					.filter(clazz::isInstance)
					.map(clazz::cast)
					.findFirst()
					.orElse(null);
	}
}
