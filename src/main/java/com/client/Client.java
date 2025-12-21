package com.client;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.client.core.ClientContext;

import lombok.Getter;

public final class Client implements ModInitializer {
	@Getter
	private static final String MOD_ID = "client";
	@Getter
	private static ClientContext context;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		context = new ClientContext();

		LOGGER.info("Modules loaded: {}", context.getModuleStorage().getModules().size());
		LOGGER.info("Client initialized");
	}
}