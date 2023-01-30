package me.kl0udy92.apart.screen.clickgui.style.imgui.frames

import com.google.common.util.concurrent.AtomicDouble
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.IFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.ImGui
import me.kl0udy92.apart.screen.clickgui.style.imgui.components.ModuleComponent
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.arthimo.Translate
import me.kl0udy92.apart.utils.render.gl.BlurUtil
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

@NativeClass
class CategoryFrame(
    val category: Category, val gui: ImGui,
    var x: Double, var y: Double,
    val width: Double = 12.0, val height: Double = 12.0,
    val components: MutableList<ModuleComponent> = mutableListOf(), var scrollY: Double = 0.0,
    val scrollTranslate: Translate = Translate(0f, 0f),
    private var opacity: Int = 95
): IFrame {

    init {
        val leftYAxis = AtomicDouble(55.0)
        val rightYAxis = AtomicDouble(55.0)
        val count = AtomicInteger(0)
        Main.moduleManager.getModulesInCategory(this.category).forEach {
            var xAxis =
                if (count.get() % 2 == 0) 45.0
                else 45.0 + 190.0 + 5.0
            var yAxis =
                if (count.get() % 2 == 0) leftYAxis.get()
                else rightYAxis.get()

            val moduleComponent = ModuleComponent(
                it,
                this,
                xAxis,
                yAxis,
                190.0,
                35.0
            )

            if (count.get() % 2 == 0) {
                leftYAxis.addAndGet(moduleComponent.offset + 5)
            }else rightYAxis.addAndGet(moduleComponent.offset + 5)

            this.components.add(moduleComponent)
            count.addAndGet(1)
        }
    }

    override fun init() {
        this.components.forEach { it.init() }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        RenderUtil.drawImage(
            this.category.iconLocation2,
            this.x.toFloat(), this.y.toFloat(),
            this.width.toFloat(), this.height.toFloat(),
            Color(this.opacity, this.opacity, this.opacity).rgb
        )
        BlurUtil.blurArea(
            this.x.toFloat() - 2f, this.y.toFloat() - 2f,
            this.x.toFloat() + this.width.toFloat() + 2f, this.y.toFloat() + this.height.toFloat() + 2f,
            5f
        )
        RenderUtil.drawImage(
            this.category.iconLocation2,
            this.x.toFloat(), this.y.toFloat(),
            this.width.toFloat(), this.height.toFloat(),
            Color(this.opacity, this.opacity, this.opacity).rgb
        )
        if (this.category == this.gui.currentCategory) {
            GlStateManager.pushMatrix()
            GL11.glEnable(GL11.GL_SCISSOR_TEST)
            RenderUtil.doScissor(
                (this.gui.x + 41).roundToInt(),
                (this.gui.y + 25 + 25.5).roundToInt(),
                (this.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
                (this.gui.y + 250.0).roundToInt()
            )
            this.components.forEach { it.render(mouseX, mouseY) }
            GL11.glDisable(GL11.GL_SCISSOR_TEST)
            GlStateManager.popMatrix()
        }

    }

    override fun update(mouseX: Int, mouseY: Int) {
        this.updateOffset()
        this.handleScrollY(mouseX, mouseY)
        if (this.gui.currentCategory == this.category) {
            if (this.opacity != 255) this.opacity += 5
        }else if (this.isHovering(mouseX, mouseY)) {
            if (this.opacity < 155) {
                this.opacity += 5
            }
        } else if (this.opacity > 95) {
            this.opacity -= 5
        }
        if (this.category == this.gui.currentCategory) this.components.forEach { it.update(mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == MouseUtil.ButtonType.LEFT.identifier) if (this.isHovering(mouseX, mouseY)) {
            this.gui.currentCategory = this.category
        }
        if (this.category == this.gui.currentCategory) this.components.forEach { it.mouseClicked(mouseX, mouseY, button) }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        if (this.category == this.gui.currentCategory) this.components.forEach { it.mouseReleased(mouseX, mouseY, state) }
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        if (this.category == this.gui.currentCategory) this.components.forEach { it.keyTyped(typedChar, key) }
    }

    override fun closed() {
        this.components.forEach { it.closed() }
    }

    fun isHovering(mouseX: Int, mouseY: Int): Boolean {
        return MouseUtil.isHovered(this.x, this.y, this.width, this.height, mouseX, mouseY)
    }

    fun updateOffset() {
        val leftYAxis = AtomicDouble(55.0)
        val rightYAxis = AtomicDouble(55.0)
        val count = AtomicInteger()
        if (this.category == this.gui.currentCategory)
            this.components.forEach {
                var yAxis =
                    if (count.get() % 2 == 0) leftYAxis.get()
                    else rightYAxis.get()

                it.y = yAxis

                if (count.get() % 2 == 0) {
                    leftYAxis.addAndGet(it.offset + 5)
                }else rightYAxis.addAndGet(it.offset + 5)

                count.addAndGet(1)
            }
    }

    private fun handleScrollY(mouseX: Int, mouseY: Int) {
        if (this.category != this.gui.currentCategory) return
        val leftYAxis = AtomicDouble(55.0)
        val rightYAxis = AtomicDouble(55.0)
        val count = AtomicInteger()
        this.components.forEach {
            if (count.get() % 2 == 0) {
                leftYAxis.addAndGet(it.offset + 5)
            }else rightYAxis.addAndGet(it.offset + 5)

            count.addAndGet(1)
        }

        val offset = leftYAxis.get().coerceAtLeast(rightYAxis.get()) - 100

        if (MouseUtil.isHovering(
                (this.gui.x + 41).roundToInt(),
                (this.gui.y + 25).roundToInt(),
                (this.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
                (this.gui.y + 250.0).roundToInt(), mouseX, mouseY
        )) {
            if (Mouse.hasWheel()) {
                val wheel = Mouse.getDWheel()
                if (wheel > 0) {
                    this.scrollY -= 25.0
                } else if (wheel < 0) {
                    this.scrollY += 25.0
                }

                if (this.scrollY < 0.0) this.scrollY = 0.0
                if (this.scrollY > offset) this.scrollY = offset
            }
        }

        this.scrollTranslate.interpolate(0f, this.scrollY.toFloat(), 5.0)
    }

}