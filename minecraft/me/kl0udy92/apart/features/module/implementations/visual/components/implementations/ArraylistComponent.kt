package me.kl0udy92.apart.features.module.implementations.visual.components.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.visual.Render2DEvent
import me.kl0udy92.apart.features.module.implementations.visual.HUDModule
import me.kl0udy92.apart.features.module.implementations.visual.components.AbstractComponent
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import me.kl0udy92.apart.utils.render.animations.easing.util.V2
import net.minecraft.util.EnumChatFormatting
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

class ArraylistComponent(hudModule: HUDModule) : AbstractComponent(hudModule) {

    override fun draw(event: Render2DEvent) {
        val width = event.scaledResolution.scaledWidth
        val yAxis = AtomicInteger()
        val index = AtomicInteger()
        val my16 = Main.fontBuffer.microsoftYahei16
        val sf18 = Main.fontBuffer.SF18

        if (this.hudModule.elementsOption.find("Arraylist")!!.value) {
            Main.moduleManager.modules.stream().sorted(Comparator.comparing {
                if (this.hudModule.textCaseOption.value == "Default")
                    if (this.hudModule.suffixOption.value)
                        if (this.hudModule.fontOption.value)
                            if (this.hudModule.chineseOption.value)
                                -my16.getStringWidth(it.chineseName + this.getSuffix(it.suffix.toString()))
                            else if (this.hudModule.nameBreakOption.value)
                                -sf18.getStringWidth(it.namebreak + this.getSuffix(it.suffix.toString()))
                            else -sf18.getStringWidth(it.name + this.getSuffix(it.suffix.toString()))
                        else if (this.hudModule.chineseOption.value)
                            -mc.fontRendererObj.getStringWidth(it.chineseName + this.getSuffix(it.suffix.toString()))
                        else if (this.hudModule.nameBreakOption.value)
                            -mc.fontRendererObj.getStringWidth(it.namebreak + this.getSuffix(it.suffix.toString()))
                        else -mc.fontRendererObj.getStringWidth(it.name + this.getSuffix(it.suffix.toString()))
                    else if (this.hudModule.fontOption.value)
                        if (this.hudModule.chineseOption.value)
                            -my16.getStringWidth(it.chineseName)
                        else if (this.hudModule.nameBreakOption.value)
                            -sf18.getStringWidth(it.namebreak)
                        else -sf18.getStringWidth(it.name)
                    else if (this.hudModule.chineseOption.value)
                        -mc.fontRendererObj.getStringWidth(it.chineseName)
                    else if (this.hudModule.nameBreakOption.value)
                        -mc.fontRendererObj.getStringWidth(it.namebreak)
                    else -mc.fontRendererObj.getStringWidth(it.name)
                else
                    if (this.hudModule.suffixOption.value)
                        if (this.hudModule.fontOption.value)
                            if (this.hudModule.chineseOption.value)
                                -my16.getStringWidth(it.chineseName + this.getSuffix(it.suffix.toString()))
                            else if (this.hudModule.nameBreakOption.value)
                                -sf18.getStringWidth((it.namebreak + this.getSuffix(it.suffix.toString())).lowercase())
                            else -sf18.getStringWidth((it.name + this.getSuffix(it.suffix.toString())).lowercase())
                        else if (this.hudModule.chineseOption.value)
                            -mc.fontRendererObj.getStringWidth(it.chineseName + this.getSuffix(it.suffix.toString()))
                        else if (this.hudModule.nameBreakOption.value)
                            -mc.fontRendererObj.getStringWidth((it.namebreak + this.getSuffix(it.suffix.toString())).lowercase())
                        else -mc.fontRendererObj.getStringWidth((it.name + this.getSuffix(it.suffix.toString())).lowercase())
                    else if (this.hudModule.fontOption.value)
                        if (this.hudModule.chineseOption.value)
                            -my16.getStringWidth(it.chineseName)
                        else if (this.hudModule.nameBreakOption.value)
                            -sf18.getStringWidth(it.namebreak.lowercase())
                        else -sf18.getStringWidth(it.name.lowercase())
                    else if (this.hudModule.chineseOption.value)
                        -mc.fontRendererObj.getStringWidth(it.chineseName)
                    else if (this.hudModule.nameBreakOption.value)
                        -mc.fontRendererObj.getStringWidth(it.namebreak.lowercase())
                    else -mc.fontRendererObj.getStringWidth(it.name.lowercase())
            }).filter { it.visible }.forEach {
                val name = if (this.hudModule.chineseOption.value) it.chineseName
                else if (this.hudModule.textCaseOption.value == "Default")
                    if (this.hudModule.nameBreakOption.value) it.namebreak
                    else it.name
                else if (this.hudModule.nameBreakOption.value) it.namebreak.lowercase()
                else it.name.lowercase()
                val combinedName = if (this.hudModule.suffixOption.value && this.getSuffix(it.suffix.toString()) != null)
                    "$name${
                        if (this.hudModule.textCaseOption.value == "Default") this.getSuffix(it.suffix.toString()) else this.getSuffix(it.suffix.toString()).toString()
                            .lowercase()
                    }"
                else name
                it.posAnimation.update()
                it.posAnimation.animate(
                    V2(
                        if (it.state)
                            if (this.hudModule.fontOption.value)
                                if (this.hudModule.chineseOption.value) my16.getStringWidth(combinedName)
                                else sf18.getStringWidth(combinedName)
                            else mc.fontRendererObj.getStringWidth(combinedName)
                        else 0,
                        if (it.state || this.hudModule.animationMode.value == "Slide")
                            yAxis.get()
                        else
                            if (this.hudModule.fontOption.value)
                                if (this.hudModule.chineseOption.value)
                                    -my16.getHeight(true)
                                else -sf18.getHeight(false)
                            else
                                -mc.fontRendererObj.FONT_HEIGHT
                    ),
                    0.4,
                    if (this.hudModule.animationMode.value == "Throw") Easings().EXPO_OUT else Easings().BACK_BOTH,
                    true
                )
                if (it.posAnimation.x.value > 0) {
                    if (this.hudModule.fontOption.value) {
                        if (this.hudModule.chineseOption.value) {
                            when (this.hudModule.textTypeOption.value) {
                                "Normal" -> {
                                    my16.drawString(
                                        combinedName,
                                        width - it.posAnimation.x.value,
                                        it.posAnimation.y.value,
                                        ColorUtil.getHUDColor(index.get())
                                    )
                                }
                                "Shadow" -> {
                                    my16.drawStringWithShadow(
                                        combinedName,
                                        width - it.posAnimation.x.value,
                                        it.posAnimation.y.value,
                                        ColorUtil.getHUDColor(index.get())
                                    )
                                }
                                "Outline" -> {
                                    my16.drawStringWithOutlineWithoutColorcode(
                                        combinedName,
                                        width - it.posAnimation.x.value,
                                        it.posAnimation.y.value,
                                        ColorUtil.getHUDColor(index.get())
                                    )
                                }
                            }
                            index.getAndIncrement()
                        } else {
                            when (this.hudModule.textTypeOption.value) {
                                "Normal" -> {
                                    sf18.drawString(
                                        combinedName,
                                        width - it.posAnimation.x.value,
                                        it.posAnimation.y.value,
                                        ColorUtil.getHUDColor(index.get())
                                    )
                                }
                                "Shadow" -> {
                                    sf18.drawStringWithShadow(
                                        combinedName,
                                        width - it.posAnimation.x.value,
                                        it.posAnimation.y.value,
                                        ColorUtil.getHUDColor(index.get())
                                    )
                                }
                                "Outline" -> {
                                    sf18.drawStringWithOutlineWithoutColorcode(
                                        combinedName,
                                        width - it.posAnimation.x.value,
                                        it.posAnimation.y.value,
                                        ColorUtil.getHUDColor(index.get())
                                    )
                                }
                            }
                            index.getAndIncrement()
                        }
                    } else {
                        when (this.hudModule.textTypeOption.value) {
                            "Normal" -> {
                                mc.fontRendererObj.drawString(
                                    combinedName,
                                    width - it.posAnimation.x.value,
                                    it.posAnimation.y.value,
                                    ColorUtil.getHUDColor(index.get())
                                )
                            }
                            "Shadow" -> {
                                mc.fontRendererObj.drawStringWithShadow(
                                    combinedName,
                                    width - it.posAnimation.x.value,
                                    it.posAnimation.y.value,
                                    ColorUtil.getHUDColor(index.get())
                                )
                            }
                            "Outline" -> {
                                mc.fontRendererObj.drawStringWithOutlineWithoutColorcode(
                                    combinedName,
                                    width - it.posAnimation.x.value,
                                    it.posAnimation.y.value,
                                    ColorUtil.getHUDColor(index.get())
                                )
                            }
                        }
                        index.getAndIncrement()
                    }
                    yAxis.addAndGet(
                        if (this.hudModule.fontOption.value)
                            if (this.hudModule.chineseOption.value) my16.getHeight(this.hudModule.chineseOption.value) + 2 + this.hudModule.offsetOption.value.roundToInt()
                            else sf18.getHeight(true) + this.hudModule.offsetOption.value.roundToInt()
                        else mc.fontRendererObj.FONT_HEIGHT + this.hudModule.offsetOption.value.roundToInt()
                    )
                }
            }
        }
    }

    private fun getSuffix(string: String): String {
        return if (string.isNotEmpty()) {
            when (this.hudModule.suffixStyleOption.value) {
                "()" -> " ${EnumChatFormatting.GRAY}(¡ìr$string¡ìr${EnumChatFormatting.GRAY})"
                "[]" -> " ${EnumChatFormatting.GRAY}[¡ìr$string¡ìr${EnumChatFormatting.GRAY}]"
                "<>" -> " ${EnumChatFormatting.GRAY}<¡ìr$string¡ìr${EnumChatFormatting.GRAY}>"
                "-" -> " ${EnumChatFormatting.GRAY}- ¡ìr$string"
                else -> " $string"
            }
        }else ""
    }

}