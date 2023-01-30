package me.kl0udy92.apart.screen.menu.button

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.utils.render.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class GuiSimpleButton(buttonId: Int, x: Int, y: Int, width: Int, height: Int, buttonText: String) :
    GuiButton(buttonId, x, y, width, height, buttonText) {

    constructor(buttonId: Int, x: Int, y: Int, buttonText: String) : this(buttonId, x, y, 200, 20, buttonText)

    fun drawSimpleButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

        val hovered =
            mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height

        RenderUtil.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Color.BLACK.rgb)

        var j = 14737632
        if (!enabled) {
            j = 10526880
        } else if (hovered) {
            j = 16777120
        }

        Main.fontBuffer.SF20.drawCenteredString(
            this.displayString,
            (this.xPosition + this.width / 2).toDouble(), (this.yPosition + (this.height - 8) / 2).toDouble(), j
        );
    }

}