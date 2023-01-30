package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.visual.Render2DEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.features.module.implementations.attack.KillAuraModule
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventPriority
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import java.awt.Color

@ModuleData(
    "TargetHUD",
    "目标面板",
    ["TargetOverlay", "Target"],
    "Target HUD",
    Category.VISUAL,
    "HUD is displayed when killaura has a target."
)
class TargetHUDModule : Module() {

    var scaledResolution = ScaledResolution(mc)
    var x = this.scaledResolution.scaledWidth / 2
    var y = this.scaledResolution.scaledHeight / 2
    private val xAnimation: Animation = Animation()
    var variable = false

    @EventListener(EventPriority.HIGHEST)
    fun onRender2D(event: Render2DEvent) {
        val target = ((Main.moduleManager.getModuleByClass(KillAuraModule().javaClass)) as KillAuraModule).target
        if (target != null) {
            this.draw(target)
        } else if (mc.currentScreen is GuiChat) {
            this.draw(mc.thePlayer)
        }
    }

    fun draw(target: EntityLivingBase) {
        val health = target.health
        var hpProgress = health / target.maxHealth
        hpProgress = MathHelper.clamp_float(hpProgress, 0f, 1f)
        this.xAnimation.update()
        this.xAnimation.animate(138 * hpProgress.toDouble(), 0.3, Easings().EXPO_OUT, true)
        if (target is EntityPlayer) {
            this.variable = true
            RenderUtil.drawRect(this.x - 2, this.y - 2, this.x + 120 + 20, this.y + 26 + 15 + 3, Color(20, 20, 20).rgb)
            RenderUtil.drawFace(target, this.x, this.y)
            mc.fontRendererObj.drawStringWithShadow(target.name, this.x + 25 + 3, this.y, -1)
            RenderUtil.drawRect(
                this.x.toDouble(),
                this.y + 25.0 + 5,
                this.x + this.xAnimation.value,
                this.y + 25.0 + 15,
                ColorUtil.getHealthColor(target.health, target.maxHealth).rgb
            )
            mc.fontRendererObj.drawStringWithShadow(
                String.format("%.1f", target.health),
                this.x + 138 / 2 - mc.fontRendererObj.getStringWidth(String.format("%.1f", target.health)) / 2.0,
                this.y + 25.0 + 5.5,
                -1
            )
        } else {
            this.variable = false
            RenderUtil.drawRect(this.x - 2, this.y - 2, this.x + 120 + 20, this.y + 26, Color(20, 20, 20).rgb)
            mc.fontRendererObj.drawStringWithShadow(target.name, this.x, this.y, -1)
            RenderUtil.drawRect(
                this.x.toDouble(),
                this.y + 9 + 3.0,
                this.x + this.xAnimation.value,
                this.y + 9 + 13.0,
                ColorUtil.getHealthColor(target.health, target.maxHealth).rgb
            )
            mc.fontRendererObj.drawStringWithShadow(
                String.format("%.1f", target.health),
                this.x + 138 / 2 - mc.fontRendererObj.getStringWidth(String.format("%.1f", target.health)) / 2.0,
                this.y + 9 + 3.5,
                -1
            )
        }
    }

}