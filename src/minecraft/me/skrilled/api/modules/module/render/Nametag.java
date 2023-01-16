/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230112
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventRender3D;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class Nametag extends ModuleHeader {
    ValueHeader renderMobs = new ValueHeader("Mobs", true);
    ValueHeader renderAnimals = new ValueHeader("Animals", true);
    ValueHeader renderPlayers = new ValueHeader("Players", true);

    public Nametag() {
        super("NameTag", false, ModuleType.RENDER);
        this.addValueList(renderMobs, renderAnimals, renderPlayers);
        this.setKey(Keyboard.KEY_N);
    }

    @EventTarget
    private void onEvent3D(EventRender3D eventRender3D) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (canDrawNameTag(entity)) {
                RenderUtil.drawNameTag((EntityLivingBase) entity);
//                theFuckOpGLFix();
            }
        }
    }

    private boolean canDrawNameTag(Entity entity) {
        if(entity==mc.thePlayer)return false;
        if (entity instanceof EntityPlayer && (Boolean) this.getValue(renderPlayers)) return true;
        if (entity instanceof EntityMob && (Boolean) this.getValue(renderMobs)) return true;
        return entity instanceof EntityAnimal && (Boolean) this.getValue(renderAnimals);

    }
}

 