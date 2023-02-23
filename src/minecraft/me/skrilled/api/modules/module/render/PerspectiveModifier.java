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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

@ModuleInitialize(name = "Perspective", type = ModuleType.RENDER, key = Keyboard.KEY_J)
public class PerspectiveModifier extends ModuleHeader {
    public static ValueHeader nameTag = new ValueHeader("NameTags", true);
    static ArrayList<String> espModeList = new ArrayList<>();
    public static ValueHeader espMode = new ValueHeader("Mode", "3DBox", espModeList);
    Color lineColors = new Color(-1);
    Color boxColors = new Color(0, 0, 0, 0);
    Color nameTagBG = new Color(0, 0, 0, 100);
    ValueHeader renderMobs = new ValueHeader("Mobs", true);
    ValueHeader renderAnimals = new ValueHeader("Animals", true);
    ValueHeader renderPlayers = new ValueHeader("Players", true);
    ValueHeader blackoutline = new ValueHeader("ESPBlackOutline", false);
    ValueHeader espHP = new ValueHeader("ESPHPRender", true);
    ValueHeader tagHP = new ValueHeader("NameTagsHP", true);
    ValueHeader arr = new ValueHeader("ArmorTags", true);
    ValueHeader lineColor = new ValueHeader("ESPLineColor", lineColors);
    ValueHeader boxColor = new ValueHeader("ESPBoxColor", boxColors);
    ValueHeader tagBgColor = new ValueHeader("NameTagsBGColor", nameTagBG);

    public PerspectiveModifier() {
        this.addEnumTypes(espModeList, "3DBox", "2DBox", "Circular", "None");

    }

    @EventTarget
    public void onEvent3D(EventRender3D eventRender3D) {
        this.setSuffix(espMode.getCurrentEnumType());
        Color box = boxColor.getColorValue();
        Color line = lineColor.getColorValue();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (canDraw(entity)) {
                if (nameTag.isOptionOpen())
                    RenderUtil.drawEntityNameTag((EntityLivingBase) entity, tagBgColor.getColorValue(), arr.isOptionOpen(), tagHP.isOptionOpen());
                switch (espMode.getCurrentEnumType()) {
                    case "3DBox":
                        RenderUtil.drawEntityBoxESP(entity, box, line, espHP.isOptionOpen(), blackoutline.isOptionOpen());
                        break;
                    case "Circular":
                        RenderUtil.drawEntityCircularESP((EntityLivingBase) entity, line, espHP.isOptionOpen(), blackoutline.isOptionOpen());
                        break;
                    case "2DBox":
                        RenderUtil.drawEntity2DESP((EntityLivingBase) entity, line, espHP.isOptionOpen(), blackoutline.isOptionOpen());
                        break;
                }

            }
        }
        GL11.glColor4f(1,1,1,1);
    }

    private boolean canDraw(Entity entity) {
        if (entity == mc.thePlayer) return false;
        if (entity instanceof EntityPlayer && (Boolean) this.getValue(renderPlayers)) return true;
        if (entity instanceof EntityMob && (Boolean) this.getValue(renderMobs)) return true;
        return entity instanceof EntityAnimal && (Boolean) this.getValue(renderAnimals);
    }


}
