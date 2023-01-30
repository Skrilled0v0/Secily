package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.ColourOption
import org.lwjgl.input.Keyboard
import java.awt.Color

@ModuleData(
    "ClickGui",
    "调试菜单",
    ["ClickUi", "Ui"],
    "Click Gui",
    Category.VISUAL,
    "Used to debug your modules, values.",
    Keyboard.KEY_RSHIFT
)
class ClickGUIModule : Module() {

    val modeOption = ArrayOption("Mode", "Switch click-gui style.", "Dropdown", arrayOf("Dropdown", "ImGui"))
    val colourOption = ColourOption("Colour", "ImGui main colour.", Color(0,46,166)) { mc.currentScreen == Main.imgui }

    override fun onEnable() {
        if (mc.currentScreen == null) mc.displayGuiScreen(
            when (this.modeOption.value) {
                "Dropdown" -> Main.clickGui
                "ImGui" -> Main.imgui
                else -> null
            }
        )
        super.onEnable()
    }

}