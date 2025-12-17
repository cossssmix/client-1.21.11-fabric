package com.client.core;

import com.client.systems.module.ModuleStorage;
import com.client.util.player.MovementCorrection;
import com.client.util.rotation.SilentRotation;
import com.google.common.eventbus.EventBus;

import lombok.Getter;

@Getter
public final class ClientContext {
	private final EventBus eventBus;
	private final SilentRotation silentRotation;
	private final MovementCorrection movementCorrection;
	private final ModuleStorage moduleStorage;

	public ClientContext() {
		this.eventBus = new EventBus();

		this.silentRotation = new SilentRotation();
		this.movementCorrection = new MovementCorrection(silentRotation);
		this.moduleStorage = new ModuleStorage();
	}
}
