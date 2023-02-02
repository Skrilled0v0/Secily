/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230202
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
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
import me.surge.animation.BoundedAnimation;
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
    BlockPos mouseBlockPos;
    //记录最后的生命值
    float lastEntityHealth = 0;
    float currentHP = 0;
    float maxHP = 0;
    BoundedAnimation healthMotion = new BoundedAnimation(0, lastEntityHealth - currentHP, 1000f, false, Easing.LINEAR);
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
            currentHP = ((EntityLivingBase) mouseEnt).getHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount();
            maxHP = ((EntityLivingBase) mouseEnt).getMaxHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount();
            hpColor = Colors.getHealthColor(currentHP, maxHP);
            //赋值给最后的生命值

            //每0.5s更新检查最后的生命值与当前的生命值并且计算出差值
                SenseHeader.getSense.printINFO((lastEntityHealth > currentHP)+" |Last:"+lastEntityHealth+" |current:"+currentHP);
                //如果检测到生命减少则开始 new Motion
                if (lastEntityHealth > currentHP) {
                    healthMotion = new BoundedAnimation(0, lastEntityHealth - currentHP, 1000f, false, Easing.LINEAR);
                    healthMotion.setState(true);
                }

            overlyName = mouseEnt.getName() + " HP:" + Math.round(((EntityLivingBase) mouseEnt).getHealth() + ((EntityLivingBase) mouseEnt).getAbsorptionAmount());
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
                RenderUtil.drawAngleCirque(rectPos[2] + 5, rectPos[1], 10, 0,360 * ((float) (lastEntityHealth - healthMotion.getAnimationValue()) / maxHP),  2, hpColor.getRGB());
                lastEntityHealth = currentHP;
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
