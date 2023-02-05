/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230128
 */
package me.skrilled.api.modules.module.combat;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.SenseHeader;
import me.skrilled.api.event.EventRender3D;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.IMC;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

@ModuleInitialize(name = "KillAura", type = ModuleType.COMBAT, key = Keyboard.KEY_R)
public class Aura extends ModuleHeader implements IMC {
    double[] ip = {1.0, 6.0, 20.0, 0.1};
    double[] ap = {1.0, 6.0, 20.0, 0.1};
    double[] range = {1.0, 3.0, 10.0, 0.1};
    double[] swdely = {0.5, 1.0, 5.0, 0.1};
    double[] swSize = {1, 2, 5, 1};
    TimerUtil atkDelay = new TimerUtil();
    TimerUtil switchDelay = new TimerUtil();
    ValueHeader ma_cps = new ValueHeader("MaxCPS", ap);
    ValueHeader mi_cps = new ValueHeader("MinCPS", ip);
    ValueHeader reach = new ValueHeader("Range", range);
    ValueHeader switchTick = new ValueHeader("SwitchDelay", swdely);
    ValueHeader switchSize = new ValueHeader("SwitchSize", swSize);
    ValueHeader atkPlayers = new ValueHeader("atkPlayers", true);
    ValueHeader atkMobs = new ValueHeader("atkMobs", true);
    ValueHeader atkAnimals = new ValueHeader("atkAnimals", true);


    ArrayList<Entity> targets = new ArrayList<>();
    Entity curTarget;
    int swIndex = 0;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (canAddTarget(entity)) {
                SenseHeader.getSense.printINFO("Add:" + entity.getName());
                targets.add(entity);
            }
            if (canRemoveTarget(entity)) {
                SenseHeader.getSense.printINFO("Remove:" + entity.getName());
                targets.remove(entity);

            }
            if (switchDelay.hasReached((int) (switchTick.getDoubleCurrentValue() * 1000)) && targets.size() != 0) {
                curTarget = targets.get(swIndex);
                if(swIndex+1<= targets.size())
                    swIndex++;
                else
                    swIndex=0;
                switchDelay.reset();
                SenseHeader.getSense.printINFO("Targets:" + targets.size());
                SenseHeader.getSense.printINFO("SWED-TO-" + curTarget.getName());
            }

        }
    }

    public boolean canAddTarget(Entity entity) {
        if (entity.getDistanceToEntity(mc.thePlayer) <= reach.getDoubleCurrentValue() && entity != mc.thePlayer) {

            if (atkPlayers.isOptionOpen()) return (entity instanceof EntityPlayer);
            if (atkMobs.isOptionOpen()) return (entity instanceof EntityMob || entity instanceof EntitySlime);
            if (atkAnimals.isOptionOpen()) return (entity instanceof EntityAnimal);

        }
        return false;
    }

    public boolean canRemoveTarget(Entity entity) {
        return (entity.getDistanceToEntity(mc.thePlayer) > reach.getDoubleCurrentValue() || entity.isDead) && targets.contains(entity);
    }

    @EventTarget
    public void onScreen3D(EventRender3D eventRender3D) {
        if (curTarget != null)
            RenderUtil.drawEntityBoxESP(curTarget, new Color(255, 0, 0, 100), new Color(74, 38, 255, 175), false);
    }
}
