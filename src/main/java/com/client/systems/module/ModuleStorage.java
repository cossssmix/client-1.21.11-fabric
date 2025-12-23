package com.client.systems.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.client.core.ClientContext;
import com.client.event.player.KeyboardEvent;
import com.client.systems.module.combat.*;
import com.client.systems.module.movement.*;
import com.client.systems.module.visuals.*;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.client.util.MinecraftVariables.mc;

public final class ModuleStorage {
	private ClientContext ctx;
	@Getter
	private List<AbstractModule> modules;

	public ModuleStorage(ClientContext ctx) {
		this.ctx = ctx;

		modules = new ArrayList<>();

		ctx.getEventBus().register(this);

		modules.addAll(List.of(
			new AutoSprint(),
			new LegitStrafe(ctx),
			new Aura(ctx),
			new Test()
		));
	}

	public void toggle(AbstractModule module) {
		module.setEnabled(!module.isEnabled());

		if (module.isEnabled()) {
			module.onEnable();
			this.ctx.getEventBus().register(module);
		} else {
			module.onDisable();
			this.ctx.getEventBus().unregister(module);
		}

		mc.player.sendMessage(
			Text.empty()
				.append(Text.literal(module.getName()))
				.append(Text.literal(module.isEnabled() ? " вкл" : " выкл")
					.formatted(module.isEnabled() ? Formatting.GREEN : Formatting.RED)),
			false
		);
	}

	@Subscribe
	public void onKeyboard(KeyboardEvent event) {
		boolean isValidScreen = mc.currentScreen == null;

		if (event.getAction() == GLFW.GLFW_PRESS && isValidScreen) {
			modules.stream()
				.filter(module -> module.getKey() == event.getInput().getKeycode())
				.forEach(this::toggle);
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
