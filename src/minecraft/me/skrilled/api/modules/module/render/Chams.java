/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230201
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
@ModuleInitialize(name = "Chams",type = ModuleType.RENDER)
public class Chams extends ModuleHeader {
    public static ValueHeader colored = new ValueHeader("Colored", false);
    static Color color = new Color(255,255,255,150);
    public static ValueHeader chamColor = new ValueHeader("Color", color);
    static ValueHeader renderMobs = new ValueHeader("Mobs", true);
    static ValueHeader renderAnimals = new ValueHeader("Animals", true);
    static ValueHeader renderPlayers = new ValueHeader("Players", true);

    public static boolean camChams(EntityLivingBase livingBase) {
        if (SenseHeader.getSense.moduleManager.getModuleByClass(Chams.class).isEnabled()) {
            if (renderMobs.isOptionOpen() && livingBase instanceof EntityMob) {
                return true;
            } else if (renderAnimals.isOptionOpen() && livingBase instanceof EntityAnimal) {
                return true;
            } else return renderPlayers.isOptionOpen() && livingBase instanceof EntityPlayer;
        } else return false;
    }
}
