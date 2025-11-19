package com.client.renderer.primitives;

import org.joml.Matrix4f;

import com.client.renderer.property.ColorProperty;
import com.client.renderer.property.PositionProperty;
import com.client.renderer.property.RadiusProperty;
import com.client.renderer.property.SizeProperty;
import com.mojang.blaze3d.systems.RenderSystem;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

@Setter
@Getter
public class Rectangle {
	private ColorProperty color;
	private RadiusProperty radius;
	private SizeProperty size;
	private float smoothness;
	private PositionProperty position;

    public Rectangle size(SizeProperty size) {
        this.size = size;
        return this;
    }

    public Rectangle radius(RadiusProperty radius) {
        this.radius = radius;
        return this;
    }

    public Rectangle color(ColorProperty color) {
        this.color = color;
        return this;
    }

    public Rectangle smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }

	public Rectangle position(PositionProperty position) {
		this.position = position;
		return this;
	}

	public Rectangle render(Matrix4f matrices) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
		RenderSystem.disableDepthTest();

        float width = this.size.getWidth(), height = this.size.getHeight();

        ShaderProgram shader = RenderSystem.setShader(
			new ShaderProgramKey(
				Identifier.of("client", "core/rectangle"),
        		VertexFormats.POSITION_COLOR,
				Defines.EMPTY
			)
		);

        shader.getUniform("Size").set(width, height);
        shader.getUniform("Radius").set(this.radius.getRadius(), this.radius.getRadius(), 
            this.radius.getRadius(), this.radius.getRadius());
        shader.getUniform("Smoothness").set(this.smoothness);

        BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		
        builder.vertex(matrices, position.getX(), position.getY(), position.getZ()).color(this.color.getArgb());
        builder.vertex(matrices, position.getX(), position.getY() + height, position.getZ()).color(this.color.getArgb());
        builder.vertex(matrices, position.getX() + width, position.getY() + height, position.getZ()).color(this.color.getArgb());
        builder.vertex(matrices, position.getX() + width, position.getY(), position.getZ()).color(this.color.getArgb());

        BufferRenderer.drawWithGlobalProgram(builder.end());

		RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

		return this;
	}
}
