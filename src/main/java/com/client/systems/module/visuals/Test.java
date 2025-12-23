package com.client.systems.module.visuals;

import org.lwjgl.glfw.GLFW;

import com.client.event.player.PlayerTickEvent;
import com.client.event.render.HudRendererEvent;
import com.client.systems.module.AbstractModule;
import com.client.systems.module.Category;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Test extends AbstractModule {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    private double lastX = Double.NaN;
    private double lastZ = Double.NaN;
    private double bps = 0.0;

    public Test() {
		super("test", "", Category.Visuals);

        setKey(GLFW.GLFW_KEY_J);
    }

    @Subscribe
    public void onPlayerTick(PlayerTickEvent event) {
        if (mc.player == null) return;

        double x = mc.player.getX();
        double z = mc.player.getZ();

        if (!Double.isNaN(lastX) && !Double.isNaN(lastZ)) {
            double dx = x - lastX;
            double dz = z - lastZ;
            double dist = Math.sqrt(dx * dx + dz * dz);
            bps = dist * 20.0;
        }

        lastX = x;
        lastZ = z;
    }

    @Subscribe
    public void onHudRenderer(HudRendererEvent event) {
        if (mc.player == null) return;

        DrawContext ctx = event.getContext();

        String text = String.format("BPS: %.2f", bps);

        int x = 5;
        int y = 5;

        ctx.drawText(
            mc.textRenderer,
            text,
            x,
            y,
            0xFFFFFFFF,
            true
        );
    }
}
