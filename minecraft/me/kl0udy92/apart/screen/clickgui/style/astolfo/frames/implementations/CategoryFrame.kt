package me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations

import com.google.common.util.concurrent.AtomicDouble
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.ModuleComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.utils.AnimationState
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.roundToInt

@NativeClass
class CategoryFrame(
    var category: Category, var offset: AtomicDouble = AtomicDouble(), val animation: Animation = Animation(),
    var calculatedHeight: Double = 0.0, var animationState: AnimationState = AnimationState.STATIC
) : AbstractFrame(category.identifier, 5.0, 5.0, 110.0, 15.0, false, false, 0.0, 0.0) {

    init {
        val yAxis = AtomicDouble(this.height.toDouble())
        Main.moduleManager.getModulesInCategory(category).forEach {
            val moduleComponent = ModuleComponent(
                it, this, 0.0, yAxis.get(), this.width.toDouble(),
                this.height.toDouble()
            )
            this.components.add(moduleComponent)
            yAxis.addAndGet(this.height + moduleComponent.offset)
        }
        this.offset.set(yAxis.get() - 15)
    }

    override fun init() {
        this.components.forEach {
            it.init()
        }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        GlStateManager.pushMatrix()
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        RenderUtil.doScissor(
            (this.x - 1).roundToInt(),
            (this.y - 1).roundToInt(),
            (this.x + this.width).roundToInt(),
            (this.calculatedHeight + 3).roundToInt()
        )
        RenderUtil.drawRect(
            (this.x - 0.5),
            (this.y - 0.5),
            (this.x + this.width + 0.5),
            this.calculatedHeight + 1.5,
            this.category.color.rgb
        )
        if (this.animation.value > 0.0) {
            RenderUtil.drawRect(
                this.x,
                (this.y + this.height + 2),
                (this.x + this.width),
                this.y + this.height + this.offset.get() + 1,
                Color(30, 30, 30).rgb
            )

            if (this.components.isNotEmpty()) {
                this.components.forEach {
                    it.render(mouseX, mouseY)
                    RenderUtil.doScissor(
                        (this.x - 1).roundToInt(),
                        (this.y - 1).roundToInt(),
                        (this.x + this.width).roundToInt(),
                        (this.calculatedHeight + 3).roundToInt()
                    )
                }
            }
        }
        RenderUtil.drawRect(
            this.x,
            this.y,
            (this.x + this.width),
            (if (this.animation.value > 0.0) this.y + this.height + 3 else this.y + this.height + 1).toDouble(),
            Color(25, 25, 25).rgb
        )
        RenderUtil.drawImage(
            this.category.iconLocation,
            (this.x + this.width - 11).toFloat(),
            (this.y + 3).toFloat(),
            8F,
            8F,
            this.category.color.rgb
        )
        RenderUtil.drawImage(
            if (this.open) ResourceLocation("${Main.name.lowercase()}/icons/other/eye_open.png")
            else ResourceLocation("${Main.name.lowercase()}/icons/other/eye_close.png"),
            (this.x + this.width - 22).toFloat(),
            (this.y + 3).toFloat(),
            8F, 8F, this.category.color.rgb
        )
        Main.fontBuffer.SF17.drawString(this.name.lowercase(), this.x + 2, this.y + 4, -1)
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
    }

    private fun updateOffset() {
        val yAxis = AtomicDouble(this.height.toDouble())
        this.components.forEach {
            it.y = yAxis.get()
            yAxis.addAndGet(this.height + it.offset)
        }
        this.offset.set(yAxis.get() - 15)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        this.updateOffset()
        this.updateAnimationState()
        if (this.open) AnimationState.DOWNWARD else AnimationState.UPWARD
        this.animation.update()
        this.animation.animate(
            if (this.open) this.offset.get().toDouble() else 0.0,
            0.2,
            Easings().EXPO_OUT,
            true
        )
        this.calculatedHeight = this.y + this.height + this.animation.value
        super.update(mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hovered = MouseUtil.isHovered(
            this.x.toFloat(),
            this.y.toFloat(),
            this.width.toFloat(),
            this.height.toFloat(),
            mouseX,
            mouseY
        )
        if (hovered) {
            if (button == MouseUtil.ButtonType.RIGHT.identifier) {
                if (this.animation.value > 0.0 && (this.animationState == AnimationState.DOWNWARD || this.animationState == AnimationState.STATIC)) {
                    this.animationState = AnimationState.UPWARD
                } else if (this.animation.value < this.offset.get()
                        .toDouble() && (this.animationState == AnimationState.DOWNWARD || this.animationState == AnimationState.STATIC)
                ) {
                    this.animationState = AnimationState.DOWNWARD
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, button)
    }

    override fun closed() {
        this.components.forEach {
            it.closed()
        }
    }

    private fun updateAnimationState() {
        when (this.animationState) {
            AnimationState.UPWARD ->
                if (this.animation.value == 0.0) {
                    this.animationState = AnimationState.STATIC
                }
            AnimationState.DOWNWARD ->
                if (this.animation.value == this.offset.get().toDouble()) {
                    this.animationState = AnimationState.STATIC
                }
            AnimationState.STATIC -> { /*Nothing here.*/
            }
        }
    }

}