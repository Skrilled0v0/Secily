package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.visual.Render2DEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.features.module.implementations.visual.components.AbstractComponent
import me.kl0udy92.apart.features.module.implementations.visual.components.implementations.ArraylistComponent
import me.kl0udy92.apart.features.module.implementations.visual.components.implementations.WatermarkComponent
import me.kl0udy92.apart.option.implementations.*
import net.ayataka.eventapi.annotation.EventListener
import java.awt.Color

@ModuleData("HUD", "面板", ["Overlay"], "HUD", Category.VISUAL, "Enable or disable the visibility of HUD.")
class HUDModule : Module() {

    val components = mutableListOf<AbstractComponent>()

    init {
        this.components.clear()
        this.components.add(WatermarkComponent(this))
        this.components.add(ArraylistComponent(this))
    }

    val elementsOption = MultiBooleanOption("Elements", "It is up to you to decide which components to display on the HUD.", arrayOf(
        BooleanOption("Watermark", "Draw watermark.", true),
        BooleanOption("Arraylist", "Draw arraylist.", true),
        BooleanOption("Notifications", "Draw notifications.", true),
        BooleanOption("Chat Animation", "Smooth chat bar animation.", true),
        BooleanOption("Fast Chat", "Remove the nigga in the chat bar.", true),
        BooleanOption("Cape", "Show cape.", true)
    ))

    val notificationsFilter = MultiBooleanOption("Notifications Filter", "Notifications filter.", arrayOf(
        BooleanOption("Toggle", "Draw toggle notifications.", false)
    ))

    val markTextOption = StringOption("Mark Text", "Watermark custom text.", Main.name) { this.elementsOption.find("Watermark")!!.value }

    val nameBreakOption = BooleanOption("Name Break", "Break name.", true) {
        this.elementsOption.find("Arraylist")!!.value
    }

    val suffixOption = BooleanOption("Suffix", "Module suffix.", true) {
        this.elementsOption.find("Arraylist")!!.value
    }
    val suffixStyleOption = ArrayOption("Suffix Style", "Switch suffix style.", "None", arrayOf("None", "()", "[]", "<>", "-")) {
        this.suffixOption.value
    }

    val fontOption = BooleanOption("Font", "Switch custom font.", true) {
        this.elementsOption.find("Arraylist")!!.value || this.elementsOption.find("Watermark")!!.value
    }
    val animationMode = ArrayOption("Animation Mode", "Switch arraylist animation mode.", "Throw", arrayOf("Throw", "Slide")) {
        this.elementsOption.find("Arraylist")!!.value
    }
    val chineseOption = BooleanOption("Chinese", "Switch language.", false) {
        this.elementsOption.find("Arraylist")!!.value
    }
    val textTypeOption =
        ArrayOption("Text Type", "Switch text type on ur HUD.", "Normal", arrayOf("Normal", "Shadow", "Outline")) {
            this.elementsOption.find("Arraylist")!!.value || this.elementsOption.find("Watermark")!!.value
        }
    val textCaseOption = ArrayOption("Text Case", "Switch text case.", "Default", arrayOf("Default", "Lower")) {
        this.elementsOption.find("Arraylist")!!.value
    }

    val offsetOption = DoubleOption("Offset", "Arraylist's offset.", 0.0, 0.0, 20.0, 1.0) {
        this.elementsOption.find("Arraylist")!!.value
    }
    val colorModeOption =
        ArrayOption("Color", "HUD Color", "Astolfo", arrayOf("Astolfo", "Rainbow", "Fade", "Custom")) {
            this.elementsOption.find("Arraylist")!!.value
        }
    val changeSpeedOption = DoubleOption(
        "Change Speed",
        "Color change speed.",
        10.0,
        5.0,
        25.0,
        1.0
    ) { (this.colorModeOption.value == "Astolfo" || this.colorModeOption.value == "Rainbow") && this.elementsOption.find("Arraylist")!!.value }
    val delayOption = DoubleOption("Delay", "Color delay.", 100.0, 100.0, 200.0, 1.0) {
        this.colorModeOption.value != "Custom" && this.elementsOption.find("Arraylist")!!.value
    }
    val colourOption = ColourOption("Colour", "Custom colour.", Color.WHITE) {
        (this.colorModeOption.value == "Fade" || this.colorModeOption.value == "Custom") && this.elementsOption.find("Arraylist")!!.value
    }
    val fadeColourOption = ColourOption("Fade Colour", "Custom fade colour.", Color.WHITE) {
        this.colorModeOption.value == "Fade" && this.elementsOption.find("Arraylist")!!.value
    }
    val saturationOption = DoubleOption("Saturation", "Color saturation.", 100.0, 1.0, 100.0, 1.0) {
        !(this.colorModeOption.value == "Fade" || this.colorModeOption.value == "Custom") && this.elementsOption.find("Arraylist")!!.value
    }
    val brightnessOption = DoubleOption("Brightness", "Color brightness.", 100.0, 1.0, 100.0, 1.0) {
        !(this.colorModeOption.value == "Fade" || this.colorModeOption.value == "Custom") && this.elementsOption.find("Arraylist")!!.value
    }

    val chatAnimationSpeedOption = DoubleOption(
        "Chat Animation-Speed",
        "Chat bar animation speed.",
        0.1,
        0.01,
        0.1,
        0.01
    ) { this.elementsOption.find("Chat Animation")!!.value }

    @EventListener
    fun onRender2D(event: Render2DEvent) {
        if (!mc.gameSettings.showDebugInfo) {
            this.components.forEach {
                it.draw(event)
            }
        }
    }

}