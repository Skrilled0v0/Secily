/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230111
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventRender3D;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

@ModuleInitialize(name = "ESP", type = ModuleType.RENDER, key = Keyboard.KEY_J)
public class ESP extends ModuleHeader {
    static ArrayList<String> espModeList = new ArrayList<>();
    public static ValueHeader espMode = new ValueHeader("Mode", "3DBox", espModeList);
    Color lineColors = new Color(-1);
    Color boxColors = new Color(0, 0, 0, 0);
    ValueHeader renderMobs = new ValueHeader("Mobs", true);
    ValueHeader renderAnimals = new ValueHeader("Animals", true);
    ValueHeader renderPlayers = new ValueHeader("Players", true);

    ValueHeader blackoutline = new ValueHeader("BlackOutline", false);
    ValueHeader lineColor = new ValueHeader("LineColor", lineColors);
    ValueHeader boxColor = new ValueHeader("BoxColor", boxColors);
    Animation anim = new Animation(1000f, false, Easing.LINEAR);
    public ESP() {
        this.addEnumTypes(espModeList, "3DBox", "2DBox", "Circular");

    }

    @EventTarget
    public void onEvent3D(EventRender3D eventRender3D) {
        this.setSuffix(espMode.getCurrentEnumType());
        Color box = boxColor.getColorValue();
        Color line = lineColor.getColorValue();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (canDrawESP(entity)) {
                switch (espMode.getCurrentEnumType()) {
                    case "3DBox":
                        RenderUtil.drawEntityBoxESP(entity, box, line, blackoutline.isOptionOpen());
                        break;
                    case "Circular":
                        RenderUtil.drawEntityCircularESP((EntityLivingBase) entity, line, true);
                        break;
                }

            }
        }
    }

    private boolean canDrawESP(Entity entity) {
        if (entity == mc.thePlayer) return false;
        if (entity instanceof EntityPlayer && (Boolean) this.getValue(renderPlayers)) return true;
        if (entity instanceof EntityMob && (Boolean) this.getValue(renderMobs)) return true;
        return entity instanceof EntityAnimal && (Boolean) this.getValue(renderAnimals);
    }


}
