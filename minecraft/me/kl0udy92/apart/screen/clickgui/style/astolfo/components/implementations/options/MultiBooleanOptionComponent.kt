package me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.options

import com.google.common.util.concurrent.AtomicDouble
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.MultiBooleanOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.ModuleComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import java.awt.Color

@NativeClass
class MultiBooleanOptionComponent(
    val multiBooleanOption: MultiBooleanOption,
    val moduleComponet: ModuleComponent,
    abstractFrame: AbstractFrame, x: Double, y: Double, width: Double, height: Double,
    private val categoryFrame: CategoryFrame = abstractFrame as CategoryFrame, var extended: Boolean = false,
    private val animation: Animation = Animation(), private val collapseAnimations: MutableList<Animation> = mutableListOf()
) : AbstractComponent(abstractFrame, x, y, width, height, 15.0) {

    init {
        repeat(this.multiBooleanOption.value.count { it.dependency.invoke() }) {
            this.collapseAnimations.add(Animation())
        }
    }

    override fun init() {
        //Nothing todo.
    }

    private fun drawOptionDescription(mouseX: Int, mouseY: Int) {
        GlStateManager.pushMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered) {
            val sr = ScaledResolution(mc)
            val descriptionWidth =
                (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(this.multiBooleanOption.description)
            val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
            GlStateManager.scale(0.5, 0.5, 0.5)
            mc.fontRendererObj.drawStringWithOutline(
                this.multiBooleanOption.description,
                descriptionWidth,
                descriptionHeight,
                -1
            )
        }
        if (this.extended) {
            val yAxis = AtomicDouble(15.0)
            this.multiBooleanOption.value.filter { it.dependency.invoke() }.forEach {
                val hoveredSubOption = MouseUtil.isHovered(
                    this.categoryFrame.x + this.x + 1.5,
                    (this.categoryFrame.y + this.y + yAxis.get()),
                    this.width - 1.5,
                    this.height - 0.1, mouseX, mouseY
                )
                if (hoveredSubOption) {
                    val sr = ScaledResolution(mc)
                    val descriptionWidth = (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(it.description)
                    val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
                    GlStateManager.scale(0.5, 0.5, 0.5)
                    mc.fontRendererObj.drawStringWithOutline(it.description, descriptionWidth, descriptionHeight, -1)
                }
                yAxis.addAndGet(15.0)
            }
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
    }

    override fun render(mouseX: Int, mouseY: Int) {
        RenderUtil.drawRect(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y),
            this.categoryFrame.x + this.x + this.width - 1.5,
            this.categoryFrame.y + this.y + this.height + 0.1, Color.black.rgb
        )

        Main.fontBuffer.SF18.drawStringWithOutline(
            this.multiBooleanOption.name,
            this.categoryFrame.x + this.x + this.width / 2 - Main.fontBuffer.SF18.getStringWidth(this.multiBooleanOption.name) / 2 - 5,
            this.categoryFrame.y + this.y + 4,
            -1
        )

        if (this.animation.value >= 15.0) {
            val yAxis = AtomicDouble(0.0)
            this.multiBooleanOption.value.filter { it.dependency.invoke() }.forEach {
                this.collapseAnimations.forEach { collapseAnimation ->
                    RenderUtil.drawRect(
                        this.categoryFrame.x + this.x + 1.5,
                        (this.categoryFrame.y + this.y + 15 + yAxis.get()),
                        this.categoryFrame.x + this.x + this.width - 1.5,
                        this.categoryFrame.y + this.y + 15 + this.height + yAxis.get() + 0.1, Color.black.rgb
                    )
                    RenderUtil.drawCircle(
                        (this.categoryFrame.x + this.x + 8).toFloat(),
                        (this.categoryFrame.y + this.y + 15 + 7 + (yAxis.get() * collapseAnimation.value)).toFloat(), 2f,
                        if (!it.value)
                            Color(115, 115, 115).rgb
                        else
                            Color(185, 185, 185).rgb
                    )
                    Main.fontBuffer.SF18.drawStringWithOutline(
                        it.name,
                        this.categoryFrame.x + this.x + 14,
                        this.categoryFrame.y + this.y + 15 + (yAxis.get() * collapseAnimation.value) + 3,
                        if (!it.value)
                            Color(115, 115, 115).rgb
                        else
                            Color(185, 185, 185).rgb
                    )

                }
                yAxis.addAndGet(15.0)
            }
        }

        this.drawOptionDescription(mouseX, mouseY)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        val yAxis = AtomicDouble(15.0)
        this.collapseAnimations.forEach {
            it.update()
        }
        if (this.extended) {
            repeat(this.multiBooleanOption.value.count { it.dependency.invoke() }) {
                yAxis.addAndGet(15.0)
            }
            this.collapseAnimations.forEach { it.animate(1.0, 0.4, Easings().EXPO_OUT, true) }
        }else {
            yAxis.set(15.0)
            this.collapseAnimations.forEach { it.animate(0.0, 0.4, Easings().EXPO_OUT, true) }
        }
        this.animation.update()
        this.animation.animate(yAxis.get(), 0.5, Easings().EXPO_OUT, true)
        this.offset = this.animation.value
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hoveredButton = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        when (button) {
            MouseUtil.ButtonType.LEFT.identifier -> {
                if (this.extended) {
                    val yAxis = AtomicDouble(15.0)
                    this.multiBooleanOption.value.filter { it.dependency.invoke() }.forEach {
                        val hoveredSubOption = MouseUtil.isHovered(
                            this.categoryFrame.x + this.x + 1.5,
                            (this.categoryFrame.y + this.y + yAxis.get()),
                            this.width - 1.5,
                            this.height - 0.1, mouseX, mouseY
                        )
                        if (hoveredSubOption) {
                            it.value = !it.value
                        }
                        yAxis.addAndGet(15.0)
                    }
                }
            }
            MouseUtil.ButtonType.RIGHT.identifier -> {
                if (hoveredButton) this.extended = !this.extended
            }
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        //Nothing todo.
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        //Nothing todo.
    }

    override fun closed() {
        //Nothing todo.
    }

    override fun isVisible(): Boolean {
        return this.multiBooleanOption.dependency.invoke()
    }

}