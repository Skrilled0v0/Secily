/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230201
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;

@ModuleInitialize(name = "RenderModifications", type = ModuleType.RENDER)
public class RenderModifications extends ModuleHeader {
    public static double[] fov = {10.0f, 70.0f, 360.0f, 0.1f};
    public static ValueHeader noHurtCam = new ValueHeader("NoHurtCam", true);
    public static ValueHeader noBobbing = new ValueHeader("NoVisualWobble", true);
    public static ValueHeader viewClip = new ValueHeader("ViewClip", true);
    public static ValueHeader fovEdit = new ValueHeader("FovEditor", false);
    public static ValueHeader fovDouble = new ValueHeader("Fov", fov);
    private final float old = mc.gameSettings.gammaSetting;
    ValueHeader highlight = new ValueHeader("NightVision", false);

    @Override
    public void onEnabled() {
        if (highlight.isOptionOpen()) mc.gameSettings.gammaSetting = 114514;
    }

    @Override
    public void onDisabled() {
        if (highlight.isOptionOpen()) mc.gameSettings.gammaSetting = this.old;
    }
}
