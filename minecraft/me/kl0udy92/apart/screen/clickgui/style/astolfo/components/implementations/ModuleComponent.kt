package me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations

import com.google.common.util.concurrent.AtomicDouble
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.option.implementations.*
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.options.*
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.utils.AnimationState
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import kotlin.math.roundToInt

@NativeClass
class ModuleComponent(
    val module: Module,
    abstractFrame: AbstractFrame,
    x: Double, y: Double, width: Double, height: Double, offset: Double = 15.0,
    var open: Boolean = false, private val categoryFrame: CategoryFrame = abstractFrame as CategoryFrame,
    private val alphaAnimation: Animation = Animation(),
    val offsetAnimation: Animation = Animation(),
    private var calculatedX: Double = 0.0, private var calculatedY: Double = 0.0,
    val subcomponents: MutableList<AbstractComponent> = mutableListOf(),
    var animationState: AnimationState = AnimationState.STATIC
) : AbstractComponent(abstractFrame, x, y, width, height, offset) {

    init {
        val yAxis = AtomicDouble()
        if (this.module.options.isNotEmpty()) {
            this.module.options.forEach {
                if (it is BooleanOption) {
                    this.subcomponents.add(
                        BooleanOptionComponent(
                            it,
                            this.categoryFrame,
                            this.x,
                            this.y + yAxis.get(),
                            this.width,
                            this.height
                        )
                    )
                } else if (it is DoubleOption) {
                    this.subcomponents.add(
                        DoubleOptionComponent(
                            it,
                            this.categoryFrame,
                            this.x,
                            this.y + yAxis.get(),
                            this.width,
                            this.height
                        )
                    )
                } else if (it is ArrayOption) {
                    this.subcomponents.add(
                        ArrayOptionComponent(
                            it,
                            this.categoryFrame,
                            this.x,
                            this.y + yAxis.get(),
                            this.width,
                            this.height
                        )
                    )
                } else if (it is StringOption) {
                    this.subcomponents.add(
                        StringOptionComponent(
                            it,
                            this.categoryFrame,
                            this.x,
                            this.y + yAxis.get(),
                            this.width,
                            this.height
                        )
                    )
                } else if (it is MultiBooleanOption) {
                    this.subcomponents.add(
                        MultiBooleanOptionComponent(
                            it,
                            this,
                            this.categoryFrame,
                            this.x,
                            this.y + yAxis.get(),
                            this.width,
                            this.height
                        )
                    )
                } else if (it is ColourOption) {
                    this.subcomponents.add(
                        ColourOptionComponent(
                            it,
                            this.categoryFrame,
                            this.x,
                            this.y + yAxis.get(),
                            this.width,
                            this.height
                        )
                    )
                }
                yAxis.addAndGet(15.0)
            }
        }
    }

    override fun init() {
        this.subcomponents.forEach { it.init() }
    }

    private fun drawModuleDescription(mouseX: Int, mouseY: Int) {
        GlStateManager.pushMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        val hovered = MouseUtil.isHovered(
            this.calculatedX + 1.5,
            this.calculatedY,
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered) {
            val sr = ScaledResolution(mc)
            val descriptionWidth = (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(this.module.description)
            val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
            GlStateManager.scale(0.5, 0.5, 0.5)
            mc.fontRendererObj.drawStringWithOutline(this.module.description, descriptionWidth, descriptionHeight, -1)
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
    }

    private fun drawSubcomponents(mouseX: Int, mouseY: Int) {
        val yAxis = AtomicDouble(this.height)
        if (this.subcomponents.isNotEmpty()) this.subcomponents.filter { it.isVisible() }.forEach {
            it.y = this.y + yAxis.get()
            it.render(mouseX, mouseY)
            yAxis.addAndGet(it.offset)
            //println("(${it.x} ${it.y} ${it.width} ${it.height})")
            //println(this.calculatedY)
        }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        val SF18 = Main.fontBuffer.SF18
        RenderUtil.drawRect(
            this.calculatedX + 1.5,
            this.calculatedY.toDouble(),
            this.calculatedX + this.width - 1.5,
            this.calculatedY + this.height + 0.1,
            ColorUtil.reAlpha(this.categoryFrame.category.color, this.alphaAnimation.value).rgb
        )
        SF18.drawString(
            this.module.name.lowercase(),
            this.calculatedX + 4,
            this.calculatedY + 4, -1
        )

        this.drawModuleDescription(mouseX, mouseY)

        if (this.offsetAnimation.value > 0.0) {
            if (this.categoryFrame.animationState == AnimationState.STATIC) {
                RenderUtil.doScissor(
                    this.calculatedX.roundToInt(),
                    this.calculatedY.roundToInt(),
                    (this.calculatedX + this.width).roundToInt(),
                    ((this.calculatedY + this.offsetAnimation.value.coerceAtMost(this.categoryFrame.animation.value)).roundToInt() + this.height).roundToInt()
                )
            }
            this.drawSubcomponents(mouseX, mouseY)
        }
    }

    private fun updateOffset() {
        val yAxis = AtomicDouble()
        if (this.open && this.subcomponents.isNotEmpty()) {
            this.subcomponents.filter { it.isVisible() }.forEach {
                yAxis.addAndGet(it.offset)
            }
        }
        this.offsetAnimation.animate(
            if (this.open) yAxis.get() else 0.0,
            0.3,
            Easings().EXPO_OUT,
            true
        )
        this.offset = this.offsetAnimation.value
    }

    override fun update(mouseX: Int, mouseY: Int) {
        this.offsetAnimation.update()
        this.updateOffset()
        this.updateAnimationState()
        if (this.open) AnimationState.DOWNWARD else AnimationState.UPWARD
        this.calculatedX = this.categoryFrame.x + this.x
        this.calculatedY = this.categoryFrame.y + this.y
        this.alphaAnimation.update()
        this.alphaAnimation.animate(
            if (this.module.state) 255.0 else 0.0,
            .5,
            Easings().EXPO_BOTH,
            true
        )
        if (this.offsetAnimation.value > 0.0 && this.subcomponents.isNotEmpty()) this.subcomponents.filter { it.isVisible() }
            .forEach { it.update(mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hovered = MouseUtil.isHovered(
            this.calculatedX + 1.5,
            this.calculatedY.toDouble() + 1.0,
            this.width - 1.5,
            (this.height - 0.1), mouseX, mouseY
        )
        if (hovered) {
            if (button == MouseUtil.ButtonType.LEFT.identifier) this.module.state = !this.module.state
            if (button == MouseUtil.ButtonType.RIGHT.identifier) {
                this.open = !this.open
                if (this.offsetAnimation.value > 0.0 && (this.animationState == AnimationState.DOWNWARD || this.animationState == AnimationState.STATIC)) {
                    this.animationState = AnimationState.UPWARD
                } else if (this.offsetAnimation.value < this.offset && (this.animationState == AnimationState.DOWNWARD || this.animationState == AnimationState.STATIC)
                ) {
                    this.animationState = AnimationState.DOWNWARD
                }
            }
        }
        if (this.offsetAnimation.value > 0.0 && this.subcomponents.isNotEmpty()) this.subcomponents.filter { it.isVisible() }
            .forEach { it.mouseClicked(mouseX, mouseY, button) }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        if (this.offsetAnimation.value > 0.0 && this.subcomponents.isNotEmpty()) this.subcomponents.filter { it.isVisible() }
            .forEach { it.mouseReleased(mouseX, mouseY, state) }
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        if (this.offsetAnimation.value > 0.0 && this.subcomponents.isNotEmpty()) this.subcomponents.filter { it.isVisible() }
            .forEach { it.keyTyped(typedChar, key) }
    }

    override fun closed() {
        this.subcomponents.forEach { it.closed() }
    }

    private fun updateAnimationState() {
        when (this.animationState) {
            AnimationState.UPWARD ->
                if (this.offsetAnimation.value == 0.0) {
                    this.animationState = AnimationState.STATIC
                }
            AnimationState.DOWNWARD ->
                if (this.offsetAnimation.value == this.offset) {
                    this.animationState = AnimationState.STATIC
                }
            AnimationState.STATIC -> { /*Nothing here.*/ }
        }
    }

}