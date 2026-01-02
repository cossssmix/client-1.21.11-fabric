package com.client.core;

import com.client.systems.command.CommandManager;
import com.client.systems.module.ModuleStorage;
import com.client.util.player.MovementController;
import com.client.util.rotation.RotationController;
import com.google.common.eventbus.EventBus;

import lombok.Getter;

@Getter
public final class ClientContext {
	private final EventBus eventBus;
	private final RotationController rotationController;
	private final MovementController movementController;
	private final CommandManager commandManager;
	private final ModuleStorage moduleStorage;

	public ClientContext() {
		this.eventBus = new EventBus();
		this.rotationController = new RotationController(this);
		this.movementController = new MovementController(this);
		this.commandManager = new CommandManager();
		this.moduleStorage = new ModuleStorage(this);
	}
}
