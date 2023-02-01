/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230201
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.SenseHeader;
import me.skrilled.api.event.EventRender2D;
import me.skrilled.api.event.EventTick;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInitialize(name = "RenderModifications", type = ModuleType.RENDER)
public class RenderModifications extends ModuleHeader {
    public static double[] fov = {10.0f, 70.0f, 360.0f, 0.1f};
    public static ValueHeader noHurtCam = new ValueHeader("NoHurtCam", true);
    public static ValueHeader noBobbing = new ValueHeader("NoVisualWobble", true);
    public static ValueHeader viewClip = new ValueHeader("ViewClip", true);
    public static ValueHeader fovEdit = new ValueHeader("FovEditor", false);
    public static ValueHeader fovDouble = new ValueHeader("Fov", fov);
    ValueHeader highlight = new ValueHeader("NightVision", false);
    private float old;

    public RenderModifications() {
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
//            SenseHeader.getSense.printINFO("onEventUpdate");
            System.out.println(1111);

    }

    @Override
    public void onEnabled() {
        if (highlight.isOptionOpen())
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 4000, 1));
    }

    @Override
    public void onDisabled() {
        if (highlight.isOptionOpen()) this.mc.gameSettings.gammaSetting = this.old;
    }
}
