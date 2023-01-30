package me.kl0udy92.apart.screen.clickgui.style.imgui

import com.google.common.util.concurrent.AtomicDouble
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.implementations.visual.ClickGUIModule
import me.kl0udy92.apart.screen.clickgui.style.imgui.frames.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Palette
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import net.minecraft.client.gui.GuiScreen
import oh.yalan.NativeClass
import java.util.*

@NativeClass
class ImGui(
    var x: Double = 0.0,
    var y: Double = 0.0,
    private var dragX: Int = 0,
    private var dragY: Int = 0,
    private var dragging: Boolean = false,
    var currentCategory: Category = Category.ATTACK,
    val frames: MutableList<CategoryFrame> = mutableListOf()
) : GuiScreen() {

    init {
        this.x = 5.0
        this.y = 150.0
        Arrays.stream(Category.values()).forEach {
            val categoryFrame = CategoryFrame(it, this, 0.0, 0.0)
            this.frames.add(categoryFrame)
        }
    }

    override fun initGui() {
        this.frames.forEach { it.init() }
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawDefaultBackground()

        if (this.dragging) {
            this.x = (mouseX - this.dragX).toDouble()
            this.y = (mouseY - this.dragY).toDouble()
        }
        RenderUtil.drawRoundedRect(
            this.x,
            this.y + 25,
            this.x + 50.0 + 150.0 + 245.0,
            this.y + 300.0 - 50.0,
            8.0,
            Palette.BACKGROUND.color.rgb
        )
        RenderUtil.drawRoundedRect(
            this.x,
            this.y + 25,
            this.x + 40.0,
            this.y + 300.0 - 50.0,
            8.0,
            Palette.DARK.color.rgb
        )
        RenderUtil.drawRect(
            this.x + 15, this.y + 25,
            this.x + 40.5, this.y + 250.0,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.x + 15, this.y + 25,
            this.x + 40.0, this.y + 250.0,
            Palette.DARK.color.rgb
        )
        RenderUtil.drawRoundedRect(
            this.x,
            this.y + 25,
            this.x + 50.0 + 150.0 + 245.0,
            this.y + 25 + 25,
            8.0,
            Palette.DARK.color.rgb
        )
        RenderUtil.drawRect(
            this.x,
            this.y + 25 + 10,
            this.x + 50.0 + 150.0 + 245,
            this.y + 25 + 25.5,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.x,
            this.y + 25 + 10,
            this.x + 50.0 + 150.0 + 245.0,
            this.y + 25 + 25,
            Palette.DARK.color.rgb
        )
        this.frames.forEach {
            this.updateAxis()
            it.update(mouseX, mouseY)
            it.render(mouseX, mouseY)
        }
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val hoveringTopbar = MouseUtil.isHovered(this.x,
            this.y + 25.0,
            50.0 + 150.0 + 245.0,
            25.0, mouseX, mouseY)
        if (mouseButton == MouseUtil.ButtonType.LEFT.identifier)
            if (hoveringTopbar) {
                this.dragging = true
                this.dragX = (mouseX - this.x).toInt()
                this.dragY = (mouseY - this.y).toInt()
            }
        this.frames.forEach { it.mouseClicked(mouseX, mouseY, mouseButton) }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        this.dragging = false
        this.frames.forEach { it.mouseReleased(mouseX, mouseY, state) }
        super.mouseReleased(mouseX, mouseY, state)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        this.frames.forEach { it.keyTyped(typedChar, keyCode) }
        super.keyTyped(typedChar, keyCode)
    }

    fun updateAxis() {
        val yAxis = AtomicDouble(70.0)
        this.frames.forEach {
            it.x = this.x + 8 + 5 + 2
            it.y = this.y + yAxis.get()
            yAxis.addAndGet(it.height + 5 + 10 + 10)
        }
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    override fun onGuiClosed() {
        this.frames.forEach { it.closed() }
        Main.moduleManager.getModuleByClass(ClickGUIModule().javaClass)?.state = false
        super.onGuiClosed()
    }

}