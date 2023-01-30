package me.kl0udy92.apart.features.module.implementations.visual.components.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.visual.Render2DEvent
import me.kl0udy92.apart.features.module.implementations.visual.HUDModule
import me.kl0udy92.apart.features.module.implementations.visual.components.AbstractComponent
import me.kl0udy92.apart.utils.render.RenderUtil
import net.minecraft.util.MathHelper
import java.awt.Color

class WatermarkComponent(hudModule: HUDModule) : AbstractComponent(hudModule) {

    override fun draw(event: Render2DEvent) {
        val sf20 = Main.fontBuffer.SF20
        if (this.hudModule.elementsOption.find("Watermark")!!.value) {
            if (this.hudModule.markTextOption.value.isNotEmpty()) {
                if (this.hudModule.fontOption.value) {
                    when (this.hudModule.textTypeOption.value) {
                        "Normal" -> {
                            sf20.drawWaveString(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Shadow" -> {
                            sf20.drawWaveStringWithShadow(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Outline" -> {
                            sf20.drawWaveStringWithOutline(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                    }
                } else {
                    when (this.hudModule.textTypeOption.value) {
                        "Normal" -> {
                            mc.fontRendererObj.drawWaveString(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Shadow" -> {
                            mc.fontRendererObj.drawWaveStringWithShadow(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Outline" -> {
                            mc.fontRendererObj.drawWaveStringWithOutline(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                    }
                }
            } else {
                if (this.hudModule.fontOption.value) {
                    when (this.hudModule.textTypeOption.value) {
                        "Normal" -> {
                            sf20.drawWaveString(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Shadow" -> {
                            sf20.drawWaveStringWithShadow(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Outline" -> {
                            sf20.drawWaveStringWithOutline(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                    }
                } else {
                    when (this.hudModule.textTypeOption.value) {
                        "Normal" -> {
                            mc.fontRendererObj.drawWaveString(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Shadow" -> {
                            mc.fontRendererObj.drawWaveStringWithShadow(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                        "Outline" -> {
                            mc.fontRendererObj.drawWaveStringWithOutline(
                                this.hudModule.markTextOption.value,
                                0.8,
                                0.8
                            )
                        }
                    }
                }
            }
        }
    }

}