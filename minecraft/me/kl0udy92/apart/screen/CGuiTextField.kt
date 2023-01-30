package me.kl0udy92.apart.screen

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import me.cubex2.ttfr.CFontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ChatAllowedCharacters
import net.minecraft.util.MathHelper

class CGuiTextField(componentId: Int, fontrendererObj: CFontRenderer, x: Int, y: Int, par5Width: Int, par6Height: Int) {

    private var id = componentId
    private var fontRendererInstance: CFontRenderer = fontrendererObj
    var xPosition = x
    var yPosition = y
    var width = par5Width
    var height = par6Height
    private var text = ""
    private var maxStringLength = 32
    private var cursorCounter = 0
    private var enableBackgroundDrawing = true
    private var canLoseFocus = true
    private var isFocused = false
    private var isEnabled = true
    private var lineScrollOffset = 0
    private var cursorPosition = 0
    private var selectionEnd = 0
    private var enabledColor = 14737632
    private var disabledColor = 7368816
    private var visible = true
    private var field_175210_x: GuiResponder? = null
    private var validator = Predicates.alwaysTrue<String>()

    fun func_175207_a(p_175207_1_: GuiResponder?) {
        field_175210_x = p_175207_1_
    }

    fun updateCursorCounter() {
        ++cursorCounter
    }

    fun setText(p_146180_1_: String) {
        if (validator.apply(p_146180_1_)) {
            if (p_146180_1_.length > maxStringLength) {
                text = p_146180_1_.substring(0, maxStringLength)
            } else {
                text = p_146180_1_
            }
            setCursorPositionEnd()
        }
    }

    fun getText(): String? {
        return text
    }

    fun getSelectedText(): String? {
        val i = if (cursorPosition < selectionEnd) cursorPosition else selectionEnd
        val j = if (cursorPosition < selectionEnd) selectionEnd else cursorPosition
        return text.substring(i, j)
    }

    fun setValidator(theValidator: Predicate<String>) {
        validator = theValidator
    }

    fun writeText(p_146191_1_: String?) {
        var s = ""
        val s1 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_)
        val i = if (cursorPosition < selectionEnd) cursorPosition else selectionEnd
        val j = if (cursorPosition < selectionEnd) selectionEnd else cursorPosition
        val k = maxStringLength - text.length - (i - j)
        var l = 0
        if (text.length > 0) {
            s = s + text.substring(0, i)
        }
        if (k < s1.length) {
            s = s + s1.substring(0, k)
            l = k
        } else {
            s = s + s1
            l = s1.length
        }
        if (text.length > 0 && j < text.length) {
            s = s + text.substring(j)
        }
        if (validator.apply(s)) {
            text = s
            moveCursorBy(i - selectionEnd + l)
            if (field_175210_x != null) {
                field_175210_x!!.func_175319_a(id, text)
            }
        }
    }

    fun deleteWords(p_146177_1_: Int) {
        if (text.length != 0) {
            if (selectionEnd != cursorPosition) {
                this.writeText("")
            } else {
                deleteFromCursor(getNthWordFromCursor(p_146177_1_) - cursorPosition)
            }
        }
    }

    fun deleteFromCursor(p_146175_1_: Int) {
        if (text.length != 0) {
            if (selectionEnd != cursorPosition) {
                this.writeText("")
            } else {
                val flag = p_146175_1_ < 0
                val i = if (flag) cursorPosition + p_146175_1_ else cursorPosition
                val j = if (flag) cursorPosition else cursorPosition + p_146175_1_
                var s = ""
                if (i >= 0) {
                    s = text.substring(0, i)
                }
                if (j < text.length) {
                    s = s + text.substring(j)
                }
                if (validator.apply(s)) {
                    text = s
                    if (flag) {
                        moveCursorBy(p_146175_1_)
                    }
                    if (field_175210_x != null) {
                        field_175210_x!!.func_175319_a(id, text)
                    }
                }
            }
        }
    }

    fun getId(): Int {
        return id
    }

    fun getNthWordFromCursor(p_146187_1_: Int): Int {
        return getNthWordFromPos(p_146187_1_, getCursorPosition())
    }

    fun getNthWordFromPos(p_146183_1_: Int, p_146183_2_: Int): Int {
        return func_146197_a(p_146183_1_, p_146183_2_, true)
    }

    fun func_146197_a(p_146197_1_: Int, p_146197_2_: Int, p_146197_3_: Boolean): Int {
        var i = p_146197_2_
        val flag = p_146197_1_ < 0
        val j = Math.abs(p_146197_1_)
        for (k in 0 until j) {
            if (!flag) {
                val l = text.length
                i = text.indexOf(32.toChar(), i)
                if (i == -1) {
                    i = l
                } else {
                    while (p_146197_3_ && i < l && text[i].code == 32) {
                        ++i
                    }
                }
            } else {
                while (p_146197_3_ && i > 0 && text[i - 1].code == 32) {
                    --i
                }
                while (i > 0 && text[i - 1].code != 32) {
                    --i
                }
            }
        }
        return i
    }

    fun moveCursorBy(p_146182_1_: Int) {
        setCursorPosition(selectionEnd + p_146182_1_)
    }

    fun setCursorPosition(p_146190_1_: Int) {
        cursorPosition = p_146190_1_
        val i = text.length
        cursorPosition = MathHelper.clamp_int(cursorPosition, 0, i)
        setSelectionPos(cursorPosition)
    }

    fun setCursorPositionZero() {
        setCursorPosition(0)
    }

    fun setCursorPositionEnd() {
        setCursorPosition(text.length)
    }

    fun textboxKeyTyped(p_146201_1_: Char, p_146201_2_: Int): Boolean {
        return if (!isFocused) {
            false
        } else if (GuiScreen.isKeyComboCtrlA(p_146201_2_)) {
            setCursorPositionEnd()
            setSelectionPos(0)
            true
        } else if (GuiScreen.isKeyComboCtrlC(p_146201_2_)) {
            GuiScreen.setClipboardString(getSelectedText())
            true
        } else if (GuiScreen.isKeyComboCtrlV(p_146201_2_)) {
            if (isEnabled) {
                this.writeText(GuiScreen.getClipboardString())
            }
            true
        } else if (GuiScreen.isKeyComboCtrlX(p_146201_2_)) {
            GuiScreen.setClipboardString(getSelectedText())
            if (isEnabled) {
                this.writeText("")
            }
            true
        } else {
            when (p_146201_2_) {
                14 -> {
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (isEnabled) {
                            deleteWords(-1)
                        }
                    } else if (isEnabled) {
                        deleteFromCursor(-1)
                    }
                    true
                }
                199 -> {
                    if (GuiScreen.isShiftKeyDown()) {
                        setSelectionPos(0)
                    } else {
                        setCursorPositionZero()
                    }
                    true
                }
                203 -> {
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()))
                        } else {
                            setSelectionPos(getSelectionEnd() - 1)
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        setCursorPosition(getNthWordFromCursor(-1))
                    } else {
                        moveCursorBy(-1)
                    }
                    true
                }
                205 -> {
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            setSelectionPos(getNthWordFromPos(1, getSelectionEnd()))
                        } else {
                            setSelectionPos(getSelectionEnd() + 1)
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        setCursorPosition(getNthWordFromCursor(1))
                    } else {
                        moveCursorBy(1)
                    }
                    true
                }
                207 -> {
                    if (GuiScreen.isShiftKeyDown()) {
                        setSelectionPos(text.length)
                    } else {
                        setCursorPositionEnd()
                    }
                    true
                }
                211 -> {
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (isEnabled) {
                            deleteWords(1)
                        }
                    } else if (isEnabled) {
                        deleteFromCursor(1)
                    }
                    true
                }
                else -> if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
                    if (isEnabled) {
                        this.writeText(Character.toString(p_146201_1_))
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

    fun mouseClicked(p_146192_1_: Int, p_146192_2_: Int, p_146192_3_: Int) {
        val flag =
            p_146192_1_ >= xPosition && p_146192_1_ < xPosition + width && p_146192_2_ >= yPosition && p_146192_2_ < yPosition + height
        if (canLoseFocus) {
            setFocused(flag)
        }
        if (isFocused && flag && p_146192_3_ == 0) {
            var i = p_146192_1_ - xPosition
            if (enableBackgroundDrawing) {
                i -= 4
            }
            val s: String =
                fontRendererInstance.trimStringToWidth(text.substring(lineScrollOffset), getCalculatedWidth())
            setCursorPosition(fontRendererInstance.trimStringToWidth(s, i).length + lineScrollOffset)
        }
    }

    fun drawTextBox() {
        if (getVisible()) {
            if (getEnableBackgroundDrawing()) {
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, -16777216)
            }
            val i = if (isEnabled) enabledColor else disabledColor
            val j = cursorPosition - lineScrollOffset
            var k = selectionEnd - lineScrollOffset
            val s: String =
                fontRendererInstance.trimStringToWidth(text.substring(lineScrollOffset), getCalculatedWidth())
            val flag = j >= 0 && j <= s.length
            val flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag
            val l = if (enableBackgroundDrawing) xPosition + 4 else xPosition
            val i1 = if (enableBackgroundDrawing) yPosition + (height - 8) / 2 else yPosition
            var j1 = l
            if (k > s.length) {
                k = s.length
            }
            if (s.isNotEmpty()) {
                val s1 = if (flag) s.substring(0, j) else s
                j1 = fontRendererInstance.drawString(s1, l.toFloat(), i1.toFloat() + 1.5f, i)
            }
            val flag2 = cursorPosition < text.length || text.length >= getMaxStringLength()
            var k1 = j1
            if (!flag) {
                k1 = if (j > 0) l + width else l
            } else if (flag2) {
                k1 = j1 - 1
                --j1
            }
            if (s.isNotEmpty() && flag && j < s.length) {
                j1 = fontRendererInstance.drawString(s.substring(j), j1.toFloat(), i1.toFloat() + 1.5f, i)
            }
            if (flag1) {
                if (flag2) {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + 9, -3092272)
                } else {
                    fontRendererInstance.drawString("_", k1.toFloat(), i1.toFloat() + 1.5f, i)
                }
            }
            if (k != j) {
                val l1: Int = l + fontRendererInstance.getStringWidth(s.substring(0, k))
                drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + 9)
            }
        }
    }

    private fun drawCursorVertical(p_146188_1_: Int, p_146188_2_: Int, p_146188_3_: Int, p_146188_4_: Int) {
        var p_146188_1_ = p_146188_1_
        var p_146188_2_ = p_146188_2_
        var p_146188_3_ = p_146188_3_
        var p_146188_4_ = p_146188_4_
        if (p_146188_1_ < p_146188_3_) {
            val i = p_146188_1_
            p_146188_1_ = p_146188_3_
            p_146188_3_ = i
        }
        if (p_146188_2_ < p_146188_4_) {
            val j = p_146188_2_
            p_146188_2_ = p_146188_4_
            p_146188_4_ = j
        }
        if (p_146188_3_ > xPosition + width) {
            p_146188_3_ = xPosition + width
        }
        if (p_146188_1_ > xPosition + width) {
            p_146188_1_ = xPosition + width
        }
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f)
        GlStateManager.disableTexture2D()
        GlStateManager.enableColorLogic()
        GlStateManager.colorLogicOp(5387)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(p_146188_1_.toDouble(), p_146188_4_.toDouble(), 0.0).endVertex()
        worldrenderer.pos(p_146188_3_.toDouble(), p_146188_4_.toDouble(), 0.0).endVertex()
        worldrenderer.pos(p_146188_3_.toDouble(), p_146188_2_.toDouble(), 0.0).endVertex()
        worldrenderer.pos(p_146188_1_.toDouble(), p_146188_2_.toDouble(), 0.0).endVertex()
        tessellator.draw()
        GlStateManager.disableColorLogic()
        GlStateManager.enableTexture2D()
    }

    fun setMaxStringLength(p_146203_1_: Int) {
        maxStringLength = p_146203_1_
        if (text.length > p_146203_1_) {
            text = text.substring(0, p_146203_1_)
        }
    }

    fun getMaxStringLength(): Int {
        return maxStringLength
    }

    fun getCursorPosition(): Int {
        return cursorPosition
    }

    fun getEnableBackgroundDrawing(): Boolean {
        return enableBackgroundDrawing
    }

    fun setEnableBackgroundDrawing(p_146185_1_: Boolean) {
        enableBackgroundDrawing = p_146185_1_
    }

    fun setTextColor(p_146193_1_: Int) {
        enabledColor = p_146193_1_
    }

    fun setDisabledTextColour(p_146204_1_: Int) {
        disabledColor = p_146204_1_
    }

    fun setFocused(p_146195_1_: Boolean) {
        if (p_146195_1_ && !isFocused) {
            cursorCounter = 0
        }
        isFocused = p_146195_1_
    }

    fun isFocused(): Boolean {
        return isFocused
    }

    fun setEnabled(p_146184_1_: Boolean) {
        isEnabled = p_146184_1_
    }

    fun getSelectionEnd(): Int {
        return selectionEnd
    }

    fun getCalculatedWidth(): Int {
        return if (getEnableBackgroundDrawing()) width - 8 else width
    }

    fun setSelectionPos(p_146199_1_: Int) {
        var p_146199_1_ = p_146199_1_
        val i = text.length
        if (p_146199_1_ > i) {
            p_146199_1_ = i
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0
        }
        selectionEnd = p_146199_1_
        if (lineScrollOffset > i) {
            lineScrollOffset = i
        }
        val j = getCalculatedWidth()
        val s: String = fontRendererInstance.trimStringToWidth(text.substring(lineScrollOffset), j)
        val k = s.length + lineScrollOffset
        if (p_146199_1_ == lineScrollOffset) {
            lineScrollOffset -= fontRendererInstance.trimStringToWidth(text, j, true).length
        }
        if (p_146199_1_ > k) {
            lineScrollOffset += p_146199_1_ - k
        } else if (p_146199_1_ <= lineScrollOffset) {
            lineScrollOffset -= lineScrollOffset - p_146199_1_
        }
        lineScrollOffset = MathHelper.clamp_int(lineScrollOffset, 0, i)
    }

    fun setCanLoseFocus(p_146205_1_: Boolean) {
        canLoseFocus = p_146205_1_
    }

    fun getVisible(): Boolean {
        return visible
    }

    fun setVisible(p_146189_1_: Boolean) {
        visible = p_146189_1_
    }


}