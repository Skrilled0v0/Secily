/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230202
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.fontloader.FontDrawer;
import me.skrilled.api.event.EventRender2D;
import me.skrilled.api.event.EventRender3D;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.BlurUtil;
import me.skrilled.utils.render.Colors;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.awt.*;

@ModuleInitialize(name = "OverlyRender", type = ModuleType.RENDER)
public class MouseOverlyRender extends ModuleHeader {
    public static ValueHeader blkEdit = new ValueHeader("BlockOverlyEdit", true);
    //计时器
    TimerUtil healthTimer = new TimerUtil();
    ValueHeader entRender = new ValueHeader("EntitiesRender", true);
    ValueHeader blkRender = new ValueHeader("BlocksRender", true);
    Color lineColor = new Color(-1);
    ValueHeader blklineColor = new ValueHeader("OverlyLineColor", lineColor);
    Color boxColor = new Color(0, 0, 0, 200);
    ValueHeader blkboxColor = new ValueHeader("OverlyBoxColor", boxColor);
    Entity mouseEnt;
    Entity lastMouseEnt;
    BlockPos mouseBlockPos;
    //记录最后的生命值
    float lastEntityHealth = 0;
    float currentHP = 0;
    float maxHP = 0;
    Animation healthMotion = new Animation(1000f, false, Easing.LINEAR);
    Color hpColor;
    float hp = 0;

    @EventTarget
    private void onUpdate(EventUpdate eventUpdate) {

        if (blkRender.isOptionOpen() && mc.objectMouseOver.getBlockPos() != null)
            mouseBlockPos = mc.objectMouseOver.getBlockPos();
        else mouseBlockPos = null;
        if (blkRender.isOptionOpen() && mc.objectMouseOver.entityHit != null) mouseEnt = mc.objectMouseOver.entityHit;
        else mouseEnt = null;

    }

    @EventTarget
    private void onEventRender2D(EventRender2D eventRender2D) {
        float w = RenderUtil.width();
        float h = RenderUtil.height();
        FontDrawer font = Main.fontLoader.EN16;
        String overlyName = "Wait For Searching";

        if (mouseEnt != null) {
            if (lastMouseEnt == mouseEnt) {
                if (((EntityLivingBase) mouseEnt).getHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount() == currentHP) {
                    if (healthMotion.getAnimationFactor() == 1D) {
                        lastEntityHealth = currentHP;
                        healthMotion = new Animation(1000, false, Easing.LINEAR);
                    }
                } else {
                    lastEntityHealth = (float) (lastEntityHealth + (currentHP - lastEntityHealth) * healthMotion.getAnimationFactor());
                    currentHP = ((EntityLivingBase) mouseEnt).getHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount();
                    healthMotion = new Animation(1000, true, Easing.LINEAR);
                }
                hpColor = Colors.getHealthColor(currentHP, maxHP);
            } else {
                currentHP = ((EntityLivingBase) mouseEnt).getHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount();
                lastEntityHealth = currentHP;
                maxHP = ((EntityLivingBase) mouseEnt).getMaxHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount();
                healthMotion = new Animation(1000, false, Easing.LINEAR);
            }


            overlyName = mouseEnt.getName() + " HP:" + Math.round(((EntityLivingBase) mouseEnt).getHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount());
            lastMouseEnt = mouseEnt;
        }


        if (mouseBlockPos != null && mc.theWorld.getBlockState(mouseBlockPos).getBlock() != Blocks.air) {
            overlyName = mc.theWorld.getBlockState(mouseBlockPos).getBlock().getLocalizedName();
        }
        float[] rectPos = {w / 2, h / 2, w / 2 + font.getStringWidth(overlyName) + 10 + (mouseEnt != null ? 10 : 0), h / 2 + font.getHeight() + 10};
        if (mc.objectMouseOver != null && !overlyName.equalsIgnoreCase("Wait For Searching")) {
            RenderUtil.drawRoundRect(rectPos[0], rectPos[1], rectPos[2], rectPos[3], 3, new Color(0, 0, 0, 40).getRGB());
            BlurUtil.blurAreaRounded(rectPos[0], rectPos[1], rectPos[2], rectPos[3], 3, 5);
            font.drawString(overlyName, w / 2 + 5, h / 2 + 5, -1);
            if (mouseEnt != null) {
                RenderUtil.drawAngleCirque(rectPos[2] + 5, rectPos[1], 10, 0, 360 * ((float) (lastEntityHealth + (currentHP - lastEntityHealth) * healthMotion.getAnimationFactor()) / maxHP), 2, -1);
                RenderUtil.drawAngleCirque(rectPos[2] + 5, rectPos[1], 10, 0, 360 * currentHP / maxHP, 2, hpColor.getRGB());
            }
        }

    }

    private boolean canDrawOverlyInfo() {
        return false;
    }

    @EventTarget
    private void onEventRender3D(EventRender3D eventRender3D) {

    }


}
