package me.kl0udy92.apart.screen.menu.account.menu

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.screen.CGuiTextField
import me.kl0udy92.apart.screen.menu.account.AccountLoginThread
import me.kl0udy92.apart.screen.menu.button.GuiSimpleButton
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class GuiDirectLogin(val preScreen: GuiAccountManager) : GuiScreen() {

    lateinit var nameTextField: CGuiTextField
    lateinit var passwordField: CGuiTextField
    lateinit var combinedTextField: CGuiTextField
    lateinit var loginButton: GuiSimpleButton
    var loginThread: AccountLoginThread? = null
    var status = "\u00a7eStatic"

    override fun initGui() {
        this.simpleButtonList.clear()
        this.loginButton = GuiSimpleButton(0, this.width / 2 - 100, this.height / 2 + 9, "Login")
        this.simpleButtonList.add(this.loginButton)
        this.simpleButtonList.add(GuiSimpleButton(1, this.width / 2 - 100, this.height / 2 + 30, "Cancel"))
        this.nameTextField = CGuiTextField(0, Main.fontBuffer.SF19, this.width / 2 - 100, this.height / 2 - 81, 200, 20)
        this.passwordField =
            CGuiTextField(1, Main.fontBuffer.SF19, this.width / 2 - 100, this.height / 2 - 80 + 20, 200, 20)
        this.combinedTextField =
            CGuiTextField(2, Main.fontBuffer.SF19, this.width / 2 - 100, this.height / 2 - 59 + 20, 200, 20)
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
            "Direct Login",
            this.width / 2.0 - Main.fontBuffer.SF19.getStringWidth("Direct Login") / 2,
            10.0,
            -1
        )

        this.nameTextField.drawTextBox()
        if (this.nameTextField.getText()!!.isEmpty()) {
            Main.fontBuffer.SF19.drawString(
                "Username / Email",
                this.width / 2 - 100 + 3,
                this.height / 2 - 81 + 7,
                Color.GRAY.rgb
            )
        }
        this.passwordField.drawTextBox()
        if (this.passwordField.getText()!!.isEmpty()) {
            Main.fontBuffer.SF19.drawString(
                "Password",
                this.width / 2 - 100 + 3,
                this.height / 2 - 80 + 28,
                Color.GRAY.rgb
            )
        }
        this.combinedTextField.drawTextBox()
        if (this.combinedTextField.getText()!!.isEmpty()) {
            Main.fontBuffer.SF19.drawString(
                "Email:Password",
                this.width / 2 - 100 + 3,
                this.height / 2 - 59 + 28,
                Color.GRAY.rgb
            )
        }
        if (this.loginThread != null) this.status = this.loginThread!!.status
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        this.nameTextField.textboxKeyTyped(typedChar, keyCode)
        this.passwordField.textboxKeyTyped(typedChar, keyCode)
        this.combinedTextField.textboxKeyTyped(typedChar, keyCode)
        super.keyTyped(typedChar, keyCode)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        this.nameTextField.mouseClicked(mouseX, mouseY, mouseButton)
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton)
        this.combinedTextField.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                if (this.combinedTextField.getText()!!.isEmpty()) {
                    this.loginThread =
                        AccountLoginThread(this.nameTextField.getText()!!, this.passwordField.getText()!!)
                    this.status = this.loginThread!!.status
                } else if (this.combinedTextField.getText()!!.isNotEmpty() && this.combinedTextField.getText()!!
                        .contains(":")
                ) {
                    val name = this.combinedTextField.getText()!!.split(":")[0]
                    val password = this.combinedTextField.getText()!!.split(":")[1]
                    this.loginThread = AccountLoginThread(name, password)
                    this.status = this.loginThread!!.status
                } else {
                    this.loginThread =
                        AccountLoginThread(this.nameTextField.getText()!!, this.passwordField.getText()!!)
                    this.status = this.loginThread!!.status
                }
                this.loginThread?.start()
            }
            1 -> mc.displayGuiScreen(this.preScreen)
        }
        super.actionPerformed(button)
    }

}