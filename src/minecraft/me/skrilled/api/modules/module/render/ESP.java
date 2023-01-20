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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

@ModuleInitialize(name = "ESP", type = ModuleType.RENDER, key = Keyboard.KEY_J)
public class ESP extends ModuleHeader {
    ValueHeader renderMobs = new ValueHeader("Mobs", true);
    ValueHeader renderAnimals = new ValueHeader("Animals", true);
    ValueHeader renderPlayers = new ValueHeader("Players", true);
    ArrayList<String> espModeList = new ArrayList<>();
    ValueHeader espMode = new ValueHeader("Mode", "3DBox", espModeList);

    public ESP() {
        this.addEnumTypes(espModeList, "3DBox", "2DBox", "Outline");
        this.setSuffix(espMode.getCurrentEnumType());
    }

    @EventTarget
    public void onEvent3D(EventRender3D eventRender3D) {
        Color boxColor = new Color(255, 255, 255, 50);
        Color lineColor = new Color(255, 255, 255, 255);
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (canDrawESP(entity)) RenderUtil.drawEntityBoxESP(entity, boxColor, lineColor, true);
        }
    }

    private boolean canDrawESP(Entity entity) {
        if (entity == mc.thePlayer) return false;
        if (entity instanceof EntityPlayer && (Boolean) this.getValue(renderPlayers)) return true;
        if (entity instanceof EntityMob && (Boolean) this.getValue(renderMobs)) return true;
        return entity instanceof EntityAnimal && (Boolean) this.getValue(renderAnimals);
    }


}
