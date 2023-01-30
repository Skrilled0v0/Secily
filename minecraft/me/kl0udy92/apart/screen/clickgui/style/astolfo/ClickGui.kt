package me.kl0udy92.apart.screen.clickgui.style.astolfo

import me.kl0udy92.apart.config.implementations.ClickGuiConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.implementations.visual.ClickGUIModule
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import net.minecraft.client.gui.GuiScreen
import oh.yalan.NativeClass
import org.lwjgl.input.Keyboard
import java.util.*

@NativeClass
class ClickGui : GuiScreen() {

    val frames = mutableListOf<AbstractFrame>()

    init {
        var initX = 5.0
        Arrays.stream(Category.values()).forEach {
            val categoryFrame = CategoryFrame(it)
            categoryFrame.x = initX.toDouble()
            this.frames.add(categoryFrame)
            initX += categoryFrame.width + 5
        }
    }

    override fun initGui() {
        this.frames.forEach { it.init() }
        Main.configManager.read(ClickGuiConfig())
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawDefaultBackground()
        this.frames.forEach {
            it.update(mouseX, mouseY)
            it.render(mouseX, mouseY)
        }
        Main.configManager.write(ClickGuiConfig())
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        this.frames.forEach { it.mouseClicked(mouseX, mouseY, mouseButton) }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        this.frames.forEach { it.mouseReleased(mouseX, mouseY, state) }
        super.mouseReleased(mouseX, mouseY, state)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        this.frames.forEach { it.keyTyped(typedChar, keyCode) }
        if (keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(null)
        super.keyTyped(typedChar, keyCode)
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    override fun onGuiClosed() {
        this.frames.forEach { it.closed() }
        Main.moduleManager.getModuleByClass(ClickGUIModule().javaClass)?.state = false
        Main.configManager.write(ClickGuiConfig())
        super.onGuiClosed()
    }

    fun findFrame(name: String): AbstractFrame? {
        this.frames.filter { it.name.equals(name, true) }.forEach { return it }
        return null
    }

}