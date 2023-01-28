package me.fontloader;

import net.minecraft.client.renderer.GlStateManager;

public final class GLUtils {
    public static void color(int r, int g, int b) {
        color(r, g, b, 255);
    }

    public static void color(int r, int g, int b, int a) {
        GlStateManager.color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static void color(int hex) {
        GlStateManager.color((hex >> 16 & 0xFF) / 255.0f, (hex >> 8 & 0xFF) / 255.0f, (hex & 0xFF) / 255.0f, (hex >> 24 & 0xFF) / 255.0f);
    }
}
