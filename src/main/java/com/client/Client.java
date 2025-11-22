package com.client;

import com.client.module.ModuleStorage;
import com.client.util.player.SilentRotation;
import com.google.common.eventbus.EventBus;

import net.fabricmc.api.ModInitializer;
import lombok.Getter;

public final class Client implements ModInitializer {
	@Getter
	private static EventBus eventBus;
	@Getter
	private static SilentRotation rotation;
	@Getter
	private static ModuleStorage moduleStorage;

	@Override
	public void onInitialize() {
		eventBus = new EventBus();
		rotation = new SilentRotation();
		moduleStorage = new ModuleStorage();
	}
}