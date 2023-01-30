package me.kl0udy92.apart.screen.clickgui.style.imgui.components

import com.google.common.util.concurrent.AtomicDouble
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.implementations.visual.ClickGUIModule
import me.kl0udy92.apart.option.implementations.*
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.components.options.*
import me.kl0udy92.apart.screen.clickgui.style.imgui.frames.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Palette
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.minecraft.util.ResourceLocation
import oh.yalan.NativeClass
import org.lwjgl.input.Keyboard
import java.awt.Color
import kotlin.math.roundToInt

@NativeClass
class ModuleComponent(
    val module: Module,
    categoryFrame: CategoryFrame,
    x: Double, y: Double, width: Double, height: Double, offset: Double = 35.0,
    val daddy: CategoryFrame = categoryFrame,
    val alphaAnimaion: Animation = Animation(), var visibleOpacity: Int = 115, var bindOpacity: Int = 115,
    val subComponents: MutableList<AbstractComponent> = mutableListOf(), var binding: Boolean = false
) : AbstractComponent(categoryFrame, x, y, width, height, offset) {

    init {
        val yAxis = AtomicDouble(28.5 + 8.0)
        this.module.options.forEach {
            if (it is ArrayOption) {
                val comboBoxComponent = ComboBoxComponenet(
                    it,
                    this.daddy,
                    this,
                    0.0,
                    yAxis.get(),
                    this.width,
                    25.0
                )
                this.subComponents.add(
                    comboBoxComponent
                )
                yAxis.addAndGet(comboBoxComponent.offset)
            }else if (it is BooleanOption) {
                val checkBoxComponent = CheckBoxComponent(
                    it,
                    this.daddy,
                    this,
                    0.0,
                    yAxis.get(),
                    this.width,
                    15.0
                )
                this.subComponents.add(
                    checkBoxComponent
                )
                yAxis.addAndGet(checkBoxComponent.offset)
            }else if (it is DoubleOption) {
                val sliderComponent = SliderComponent(
                    it,
                    this.daddy,
                    this,
                    0.0,
                    yAxis.get(),
                    this.width,
                    25.0
                )
                this.subComponents.add(
                    sliderComponent
                )
                yAxis.addAndGet(sliderComponent.offset)
            }else if (it is StringOption) {
                val textInputComponent = TextInputComponent(
                    it,
                    this.daddy,
                    this,
                    0.0,
                    yAxis.get(),
                    this.width,
                    25.0
                )
                this.subComponents.add(
                    textInputComponent
                )
                yAxis.addAndGet(textInputComponent.offset)
            }else if (it is MultiBooleanOption) {
                val multiableComboBoxComponent = MultiableComboBoxComponent(
                    it,
                    this.daddy,
                    this,
                    0.0,
                    yAxis.get(),
                    this.width,
                    25.0
                )
                this.subComponents.add(
                    multiableComboBoxComponent
                )
                yAxis.addAndGet(multiableComboBoxComponent.offset)
            }else if (it is ColourOption) {
                val colourPickerComponent = ColourPickerComponent(
                    it,
                    this.daddy,
                    this,
                    0.0,
                    yAxis.get(),
                    this.width,
                    25.0
                )
                this.subComponents.add(
                    colourPickerComponent
                )
                yAxis.addAndGet(colourPickerComponent.offset)
            }
        }
        this.offset += yAxis.get() - (28.5 + 8.0)
    }

    override fun init() {
        this.subComponents.forEach { it.init() }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        RenderUtil.drawRect(
            this.daddy.gui.x + this.x - 0.5,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y - 0.5,
            this.daddy.gui.x + this.x + this.width + 0.5,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + this.offset + 0.5,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.daddy.gui.x + this.x,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y,
            this.daddy.gui.x + this.x + this.width,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + this.offset,
            Palette.DARK.color.rgb
        )
        RenderUtil.drawRect(
            this.daddy.gui.x + this.x,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 15,
            this.daddy.gui.x + this.x + this.width,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 15 + 0.5,
            Palette.LINE_GRAY.color.rgb
        )
        Main.fontBuffer.SF18.drawString(
            this.module.namebreak,
            this.daddy.gui.x + this.x + 3,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 4,
            -1
        )
        Main.fontBuffer.SF17.drawString(
            "Toggle",
            this.daddy.gui.x + this.x + 3,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 19 + 3,
            -1
        )
        RenderUtil.drawRect(
            this.daddy.gui.x + this.x + this.width - 12.5,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 19.5,
            this.daddy.gui.x + this.x + this.width - 3.5,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 20 + 8.5,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.daddy.gui.x + this.x + this.width - 12,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 20,
            this.daddy.gui.x + this.x + this.width - 4,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y+ 20 + 8,
            ColorUtil.reAlpha((Main.moduleManager.getModuleByClass(ClickGUIModule().javaClass) as ClickGUIModule).colourOption.value, this.alphaAnimaion.value).rgb
        )
        RenderUtil.drawImage(
            if (this.module.visible) ResourceLocation("${Main.name.lowercase()}/icons/other/eye_open.png")
            else ResourceLocation("${Main.name.lowercase()}/icons/other/eye_close.png"),
            (this.daddy.gui.x + this.x + this.width - 15 - Main.fontBuffer.SF16.getStringWidth(if (this.binding) "Press a key..." else "[${Keyboard.getKeyName(this.module.keyCode)}]")).toFloat(),
            (this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 3.5).toFloat(),
            8f, 8f, Color(this.visibleOpacity, this.visibleOpacity, this.visibleOpacity).rgb
        )
        Main.fontBuffer.SF16.drawString(
            if (this.binding) "Press a key..." else "[${Keyboard.getKeyName(this.module.keyCode)}]",
            this.daddy.gui.x + this.x + this.width - 3 - Main.fontBuffer.SF16.getStringWidth(if (this.binding) "Press a key..." else "[${Keyboard.getKeyName(this.module.keyCode)}]"),
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 3,
            Color(this.bindOpacity, this.bindOpacity, this.bindOpacity).rgb
        )
        this.subComponents.filter { it.isVisible() }.forEach { it.render(mouseX, mouseY) }
    }

    override fun update(mouseX: Int, mouseY: Int) {
        this.updateAxis()
        this.alphaAnimaion.update()
        this.alphaAnimaion.animate(
            if (this.module.state) 255.0
            else 0.0,
            0.5,
            Easings().EXPO_BOTH,
            true
        )

        if (this.module.visible) {
            if (this.visibleOpacity != 200) this.visibleOpacity += 5
        }else if (this.isHoverVisibleButton(mouseX, mouseY)) {
            if (this.visibleOpacity < 155) {
                this.visibleOpacity += 5
            }
        } else if (this.visibleOpacity > 95) {
            this.visibleOpacity -= 5
        }

        if (this.binding) {
            if (this.bindOpacity != 200) this.bindOpacity += 5
        }else if (this.isHoverKeyBindButton(mouseX, mouseY)) {
            if (this.bindOpacity < 155) {
                this.bindOpacity += 5
            }
        } else if (this.bindOpacity > 95) {
            this.bindOpacity -= 5
        }

        this.subComponents.filter { it.isVisible() }.forEach { it.update(mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == MouseUtil.ButtonType.LEFT.identifier) {
            if (this.isHoverToggleButton(mouseX, mouseY))
                this.module.state = !this.module.state
            if (this.isHoverVisibleButton(mouseX, mouseY))
                this.module.visible = !this.module.visible
            if (this.isHoverKeyBindButton(mouseX, mouseY))
                this.binding = !this.binding
        }
        this.subComponents.filter { it.isVisible() }.forEach { it.mouseClicked(mouseX, mouseY, button) }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        this.subComponents.filter { it.isVisible() }.forEach { it.mouseReleased(mouseX, mouseY, state) }
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        if (this.binding) {
            this.module.keyCode =
                if (key != Keyboard.KEY_SPACE) key else Keyboard.KEY_NONE
            this.binding = false
        }
        this.subComponents.filter { it.isVisible() }.forEach { it.keyTyped(typedChar, key) }
    }

    override fun closed() {
        this.subComponents.forEach { it.closed() }
    }

    fun isHoverToggleButton(mouseX: Int, mouseY: Int): Boolean {
        return MouseUtil.isHovering(
            this.daddy.gui.x + this.x + this.width - 12.5,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 19.5,
            this.daddy.gui.x + this.x + this.width - 3.5,
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 20 + 8.5, mouseX, mouseY)
        && MouseUtil.isHovering(
            (this.daddy.gui.x + 41).roundToInt(),
            (this.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY)
    }

    fun isHoverVisibleButton(mouseX: Int, mouseY: Int): Boolean {
        return MouseUtil.isHovered(
            (this.daddy.gui.x + this.x + this.width - 15 - Main.fontBuffer.SF16.getStringWidth(if (this.binding) "Press a key..." else "[${Keyboard.getKeyName(this.module.keyCode)}]")).toFloat(),
            (this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 3.5).toFloat(),
            8f, 8f, mouseX, mouseY)
        && MouseUtil.isHovering(
            (this.daddy.gui.x + 41).roundToInt(),
            (this.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY)
    }

    fun isHoverKeyBindButton(mouseX: Int, mouseY: Int): Boolean {
        return MouseUtil.isHovered(
            this.daddy.gui.x + this.x + this.width - 3 - Main.fontBuffer.SF16.getStringWidth(if (this.binding) "Press a key..." else "[${Keyboard.getKeyName(this.module.keyCode)}]"),
            this.daddy.gui.y + this.y - this.daddy.scrollTranslate.y + 3,
            Main.fontBuffer.SF16.getStringWidth(if (this.binding) "Press a key..." else "[${Keyboard.getKeyName(this.module.keyCode)}]").toDouble(),
            Main.fontBuffer.SF16.getHeight(false).toDouble(), mouseX, mouseY)
        && MouseUtil.isHovering(
            (this.daddy.gui.x + 41).roundToInt(),
            (this.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY)
    }

    fun updateAxis() {
        val yAxis = AtomicDouble(28.5 + 8.0)
        this.subComponents.filter { it.isVisible() }.forEach {
            it.x = this.daddy.gui.x + this.x
            it.y = yAxis.get()
            yAxis.addAndGet(it.offset)
        }
        this.offset = 35.0 + yAxis.get() - (28.5 + 8.0)
    }

}