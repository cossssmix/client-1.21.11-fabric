package com.client.systems.module;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AbstractModule {
	@Getter
	private final String name, description;
	@Setter
	private int key;
	private Category category;
	@Getter @Setter
	private boolean enabled;

	public AbstractModule() {
		ModuleInfo moduleInfoAnnotation = this.getClass().getAnnotation(ModuleInfo.class);

		name = moduleInfoAnnotation.name();
		description = moduleInfoAnnotation.description();
		category = moduleInfoAnnotation.category();
	}

	public void onEnable() {}
	public void onDisable() {}
}
