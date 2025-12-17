package com.client.systems.module;

import static com.client.util.IMinecraft.mc;

import com.client.Client;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Getter
public abstract class AbstractModule {
	private final String name, description;
	@Setter
	private int key;
	private Category category;
	private boolean enabled;

	public AbstractModule() {
		ModuleInfo moduleInfoAnnotation = this.getClass().getAnnotation(ModuleInfo.class);

		name = moduleInfoAnnotation.name();
		description = moduleInfoAnnotation.description();
		category = moduleInfoAnnotation.category();
	}

	public void onEnable() {}
	public void onDisable() {}

	public void toggle() {
		enabled = !enabled;

		if (enabled) {
			onEnable();
			Client.getContext().getEventBus().register(this);
		} else {
			onDisable();
			Client.getContext().getEventBus().unregister(this);
		}

		mc.player.sendMessage(
			Text.empty()
				.append(Text.literal(name))
				.append(Text.literal(enabled ? " вкл" : " выкл")
					.formatted(enabled ? Formatting.GREEN : Formatting.RED)),
			false
		);
	}
}
