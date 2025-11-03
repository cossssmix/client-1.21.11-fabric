package com.client.module;

import com.client.Client;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import static com.client.util.IMinecraft.mc;

public abstract class AbstractModule {
	@Getter
	private final String name, description;

	@Getter @Setter
	private int key;

	@Getter
	private EnumCategory category;

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

	public void toggle() {
		setEnabled(!isEnabled());

		if (isEnabled()) {
			onEnable();
			Client.getEventBus().register(this);
		} else {
			onDisable();
			Client.getEventBus().unregister(this);
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
