package com.client;

import net.fabricmc.api.ModInitializer;

import com.client.core.ClientContext;

import lombok.Getter;

public final class Client implements ModInitializer {
	@Getter
	private static final String MOD_ID = "client";
	@Getter
	private static ClientContext context;

	@Override
	public void onInitialize() {
		context = new ClientContext();
	}
}