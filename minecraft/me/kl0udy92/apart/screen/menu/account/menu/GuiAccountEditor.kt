package me.kl0udy92.apart.screen.menu.account.menu

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.screen.CGuiTextField
import me.kl0udy92.apart.screen.menu.account.AccountLoginThread
import me.kl0udy92.apart.screen.menu.button.GuiSimpleButton
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class GuiAccountEditor(val preScreen: GuiAccountManager) : GuiScreen() {

    lateinit var nameTextField: CGuiTextField
    lateinit var passwordField: CGuiTextField
    var loginThread: AccountLoginThread? = null
    var status = "\u00a7eStatic"

    override fun initGui() {
        this.simpleButtonList.clear()

        this.simpleButtonList.add(GuiSimpleButton(0, this.width / 2 - 100, this.height / 2 + 9 - 21, "Edit"))
        this.simpleButtonList.add(GuiSimpleButton(1, this.width / 2 - 100, this.height / 2 + 9, "Edit and login"))
        this.simpleButtonList.add(GuiSimpleButton(2, this.width / 2 - 100, this.height / 2 + 30, "Cancel"))

        this.nameTextField = CGuiTextField(0, Main.fontBuffer.SF19, this.width / 2 - 100, this.height / 2 - 81, 200, 20)
        this.passwordField =
            CGuiTextField(1, Main.fontBuffer.SF19, this.width / 2 - 100, this.height / 2 - 80 + 20, 200, 20)
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.preScreen.preScreen.drawBackground()

        GlStateManager.pushMatrix()
        GlStateManager.scale(0.5, 0.5, 1.0)
        mc.fontRendererObj.drawString(
            this.status,
            mc.displayWidth - mc.fontRendererObj.getStringWidth(this.status),
            mc.displayHeight - mc.fontRendererObj.FONT_HEIGHT,
            -1
        )
        GlStateManager.popMatrix()

        Main.fontBuffer.SF19.drawStringWithOutline(
            "Account Editor",
            this.width / 2.0 - Main.fontBuffer.SF19.getStringWidth("Account Editor") / 2,
            10.0,
            -1
        )

        this.nameTextField.drawTextBox()
        if (this.nameTextField.getText()!!.isEmpty()) {
            Main.fontBuffer.SF19.drawString(
                "New Username / New Email",
                this.width / 2 - 100 + 3,
                this.height / 2 - 81 + 7,
                Color.GRAY.rgb
            )
        }
        this.passwordField.drawTextBox()
        if (this.passwordField.getText()!!.isEmpty()) {
            Main.fontBuffer.SF19.drawString(
                "New Password",
                this.width / 2 - 100 + 3,
                this.height / 2 - 80 + 28,
                Color.GRAY.rgb
            )
        }
        if (this.loginThread != null) this.status = this.loginThread!!.status
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        this.nameTextField.textboxKeyTyped(typedChar, keyCode)
        this.passwordField.textboxKeyTyped(typedChar, keyCode)
        super.keyTyped(typedChar, keyCode)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        this.nameTextField.mouseClicked(mouseX, mouseY, mouseButton)
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                if (this.nameTextField.getText()!!.isNotEmpty()) {
                    this.preScreen.selectedAccount!!.name = this.nameTextField.getText()!!
                }
                if (this.passwordField.getText()!!.isNotEmpty()) {
                    this.preScreen.selectedAccount!!.password = this.passwordField.getText()!!
                }
            }
            1 -> {
                if (this.nameTextField.getText()!!.isNotEmpty()) {
                    this.preScreen.selectedAccount!!.name = this.nameTextField.getText()!!
                }
                if (this.passwordField.getText()!!.isNotEmpty()) {
                    this.preScreen.selectedAccount!!.password = this.passwordField.getText()!!
                }
                this.loginThread =
                    AccountLoginThread(this.preScreen.selectedAccount!!.name, this.preScreen.selectedAccount!!.password)
                this.loginThread!!.start()
            }
            2 -> mc.displayGuiScreen(this.preScreen)
        }
        super.actionPerformed(button)
    }

}