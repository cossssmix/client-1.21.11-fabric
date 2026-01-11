package com.client.systems.module;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AbstractModule {
	private final String name, description;
	@Setter
	private int key;
	private final Category category;
	@Setter
	private boolean enabled;
	@Setter
	private int scancode;

	public AbstractModule(String name, String description, Category category) {
		this.name = name;
		this.description = description;
		this.category = category;
	}

	public void onEnable() {}
	public void onDisable() {}
}
