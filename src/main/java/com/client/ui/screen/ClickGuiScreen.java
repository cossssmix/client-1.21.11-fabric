package com.client.ui.screen;

import org.joml.Matrix4f;

import com.client.Client;
import com.client.module.visuals.ClickGui;
import com.client.renderer.primitives.Rectangle;
import com.client.renderer.property.ColorProperty;
import com.client.renderer.property.PositionProperty;
import com.client.renderer.property.RadiusProperty;
import com.client.renderer.property.SizeProperty;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static com.client.util.IMinecraft.mc;

import java.awt.Color;

public final class ClickGuiScreen extends Screen {
	public ClickGuiScreen() {
		super(Text.literal("click gui"));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.renderBackground(context, mouseX, mouseY, delta);

		Matrix4f matrices = context.getMatrices().peek().getPositionMatrix();

		float width = 225f, height = 160.0f;

		float x = (mc.getWindow().getScaledWidth() / 2) - (width / 2);
		float y = (mc.getWindow().getScaledHeight() / 2) - (height / 2);

		Rectangle rect = new Rectangle()
			.position(new PositionProperty(x, y, 0.0f))
			.size(new SizeProperty(width, height))
			.smoothness(1.0f)
			.radius(new RadiusProperty(6.0f))
			.color(new ColorProperty(new Color(11, 14, 17)));

		rect.render(matrices);
	}

	@Override
	public void close() {
		super.close();

		ClickGui clickGui = Client.getModuleStorage().getModule(ClickGui.class);
		
		if (clickGui.isEnabled()) {
			clickGui.toggle();
		}
	}
}
