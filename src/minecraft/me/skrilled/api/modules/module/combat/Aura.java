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
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

@ModuleInitialize(name = "KillAura", type = ModuleType.COMBAT, key = Keyboard.KEY_R)
public class Aura extends ModuleHeader implements IMC {
    public boolean doBlock = false;
    public boolean unBlock = false;
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
    ValueHeader ab = new ValueHeader("AutoBlock", true);
    ValueHeader atkPlayers = new ValueHeader("atkPlayers", true);
    ValueHeader atkMobs = new ValueHeader("atkMobs", true);
    ValueHeader atkAnimals = new ValueHeader("atkAnimals", true);
    double cps;
    ArrayList<Entity> targets = new ArrayList<>();
    Entity curTarget;
    int swIndex = 0;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        cps = (double) this.getValue(mi_cps) + Math.random() * ((double) this.getValue(ma_cps) - (double) this.getValue(mi_cps));
        for (Entity entity : mc.theWorld.loadedEntityList) {
//            SenseHeader.getSense.printINFO(entity.toString());
            if (canAddTarget(entity)&&!canRemoveTarget(entity)) {
                SenseHeader.getSense.printINFO("Add:" + entity.getName());
                targets.add(entity);
            }
            if (canRemoveTarget(entity)&&!canAddTarget(entity)) {
                SenseHeader.getSense.printINFO("Remove:" + entity.getName());
                targets.remove(entity);

            }

            if (switchDelay.hasReached((int) (switchTick.getDoubleCurrentValue() * 1000)) && targets.size() != 0) {
                if (swIndex + 1 <= targets.size() - 1) swIndex++;
                else swIndex = 0;
                curTarget = targets.get(swIndex);
                switchDelay.reset();
                SenseHeader.getSense.printINFO("Targets:" + targets.size() + " Index:" + swIndex);
                SenseHeader.getSense.printINFO("SWED-TO-" + curTarget.getName());
            }
            targets.clear();
        }
        if (curTarget != null) attack();
    }

    public boolean canAddTarget(Entity entity) {
        if (entity.getDistanceToEntity(mc.thePlayer) <= reach.getDoubleCurrentValue() && entity != mc.thePlayer && curTarget != entity) {
            if (entity instanceof EntityPlayer) return (atkPlayers.isOptionOpen());
            if (entity instanceof EntityMob || entity instanceof EntitySlime) return (atkMobs.isOptionOpen());
            if (entity instanceof EntityAnimal) return (atkAnimals.isOptionOpen());
        }
        return false;
    }

    private void attack() {
        if (atkDelay.hasReached((int) (1000L / cps))) {
            mc.thePlayer.swingItem();
            if (ab.isOptionOpen() && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(),10);
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(curTarget, C02PacketUseEntity.Action.ATTACK));
//            SenseHeader.getSense.printINFO("atked:" + curTarget.getName() + " |timer:" + (int) (1000L / cps));
        }
    }

    public boolean canRemoveTarget(Entity entity) {
        return (entity.getDistanceToEntity(mc.thePlayer) > reach.getDoubleCurrentValue() || entity.isDead) && targets.contains(entity);
    }

    @EventTarget
    public void onScreen3D(EventRender3D eventRender3D) {
        if (curTarget != null) {
            RenderUtil.drawEntityBoxESP(curTarget, new Color(255, 0, 0, 100), new Color(74, 38, 255, 175), false, false);
            RenderUtil.reColor();
        }
    }
}
