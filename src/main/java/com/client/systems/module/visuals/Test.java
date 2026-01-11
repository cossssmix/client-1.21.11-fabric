package com.client.systems.module.visuals;

import com.client.event.render.HudRenderEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.client.systems.render.PipelinesRepository;
import com.client.systems.render.uniforms.TriangleUniform;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.ProjectionMatrix2;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.Window;
// import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexFormats;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.VertexFormat;

import static com.client.util.MinecraftVariables.mc;

import java.awt.Color;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Test extends AbstractModule {
    private final BufferAllocator allocator = new BufferAllocator(12);
    // private final MatrixStack stack = new MatrixStack();

    private ProjectionMatrix2 guiProjectionMatrix;

    private RenderData renderData;

    public Test() {
        super("test", "", Category.Visuals);
        setKey(GLFW.GLFW_KEY_U);
    }

    @Subscribe
    public void onHudRender(final HudRenderEvent event) {
        this.triangle(10, 10, 100, 100);
        this.drawBuffer();
    }

    private void triangle(final float x, final float y, final float width, final float height) {
        final int indices = 3;

        try (BuiltBuffer builtBuffer = this.buildTriangleBuffer(x, y, width, height).end()) {
            final GpuBufferSlice triangleUniforms = new TriangleUniform(new Vector2f(width, height)).uniforms();

            this.renderData = new RenderData(
                    RenderSystem.getDevice().createBuffer(() -> "triangle render", 1, builtBuffer.getBuffer()),
                    triangleUniforms,
                    indices
            );
        }
    }

    private void drawBuffer() {
        if (renderData == null) return;

        final Window window = mc.getWindow();

        if (guiProjectionMatrix == null) {
            guiProjectionMatrix = new ProjectionMatrix2(
                    "triangle renderer",
                    1000.0F,
                    11000.0F,
                    true
            );
        }

        RenderSystem.setProjectionMatrix(
                guiProjectionMatrix.set(
                        (float) window.getFramebufferWidth() / (float) window.getScaleFactor(),
                        (float) window.getFramebufferHeight() / (float) window.getScaleFactor()
                ),
                ProjectionType.ORTHOGRAPHIC
        );

        final Framebuffer framebuffer = mc.getFramebuffer();

        final GpuBufferSlice[] dynamicUniforms = new GpuBufferSlice[] { renderData.customUniforms };

        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.TRIANGLES);
        GpuBuffer gpuBuffer = shapeIndexBuffer.getIndexBuffer(1);
        VertexFormat.IndexType indexType = shapeIndexBuffer.getIndexType();

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "triangle renderer",
                        framebuffer.getColorAttachmentView(),
                        OptionalInt.empty(),
                        framebuffer.useDepthAttachment ? framebuffer.getDepthAttachmentView() : null,
                        OptionalDouble.empty()
                )) {

            RenderSystem.bindDefaultUniforms(renderPass);

            renderPass.setUniform("DynamicTransforms", dynamicUniforms[0]);
            renderPass.setPipeline(PipelinesRepository.RECTANGLE);
            renderPass.setVertexBuffer(0, renderData.vertexBuffer);
            renderPass.disableScissor();
            renderPass.setIndexBuffer(gpuBuffer, indexType);
            renderPass.drawIndexed(0, 0, renderData.indices, 1);
        }
    }

    private BufferBuilder buildTriangleBuffer(final float x, final float y, final float width, final float height) {
        final BufferBuilder bufferBuilder = new BufferBuilder(
                allocator,
                VertexFormat.DrawMode.TRIANGLES,
                VertexFormats.POSITION_COLOR
        );

        final int color = Color.WHITE.getRGB();

        bufferBuilder.vertex(x, y + height, 0).color(color);
        bufferBuilder.vertex((x + width) / 2, y, 0).color(color);
        bufferBuilder.vertex(x + width, y + height, 0).color(color);

        return bufferBuilder;
    }

    private static class RenderData {
        private final GpuBuffer vertexBuffer;
        private final GpuBufferSlice customUniforms;
        private final int indices;

        public RenderData(final GpuBuffer vertexBuffer, final GpuBufferSlice customUniforms, final int indices) {
            this.vertexBuffer = vertexBuffer;
            this.customUniforms = customUniforms;
            this.indices = indices;
        }
    }
}
