package me.kl0udy92.apart.screen.menu.account.menu

import me.kl0udy92.apart.config.implementations.AccountsConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.screen.menu.GuiMainMenu
import me.kl0udy92.apart.screen.menu.account.Account
import me.kl0udy92.apart.screen.menu.account.AccountLoginThread
import me.kl0udy92.apart.screen.menu.button.GuiSimpleButton
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.arthimo.Translate
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt
import kotlin.random.Random

class GuiAccountManager(
    val preScreen: GuiMainMenu
) : GuiScreen() {

    lateinit var loginButton: GuiSimpleButton
    lateinit var randomButton: GuiSimpleButton
    lateinit var removeButton: GuiSimpleButton
    lateinit var editButton: GuiSimpleButton
    var loginThread: AccountLoginThread? = null
    var selectedAccount: Account? = null
    var status = "\u00a7eStatic"
    var offset = 0.0
    val offsetTranslate = Translate(0f, 0f)

    override fun initGui() {
        this.simpleButtonList.clear()
        this.simpleButtonList.add(GuiSimpleButton(0, this.width / 2 + 100, this.height / 2 + 100, 100, 20, "Add"))
        this.simpleButtonList.add(
            GuiSimpleButton(
                4,
                this.width / 2 + 100 - 50,
                this.height / 2 + 121,
                100 + 50,
                20,
                "Cancel"
            )
        )
        this.randomButton = GuiSimpleButton(1, this.width / 2 - 1, this.height / 2 + 100, 100, 20, "Random")
        this.simpleButtonList.add(this.randomButton)
        this.editButton = GuiSimpleButton(5, this.width / 2 - 203 + 100 + 52, this.height / 2 + 121, 100, 20, "Edit")
        this.simpleButtonList.add(this.editButton)
        this.simpleButtonList.add(
            GuiSimpleButton(
                2,
                this.width / 2 - 102,
                this.height / 2 + 100,
                100,
                20,
                "Direct Login"
            )
        )
        this.removeButton = GuiSimpleButton(6, this.width / 2 - 203, this.height / 2 + 121, 100 + 51, 20, "Remove")
        this.simpleButtonList.add(this.removeButton)
        this.loginButton = GuiSimpleButton(3, this.width / 2 - 203, this.height / 2 + 100, 100, 20, "Login")
        this.simpleButtonList.add(this.loginButton)
        this.loginButton.enabled = false
        this.randomButton.enabled = false
        this.removeButton.enabled = false
        this.editButton.enabled = false
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val sf19 = Main.fontBuffer.SF19
        mc.fontRendererObj.drawString(
            this.status,
            mc.displayWidth - mc.fontRendererObj.getStringWidth(this.status),
            mc.displayHeight - mc.fontRendererObj.FONT_HEIGHT,
            -1
        )
        this.handleOfffset()
        this.updateButtonsState()
        this.preScreen.drawBackground()
        sf19.drawStringWithOutlineWithoutColorcode(mc.session.username, 10.0, 10.0, Color.GRAY.rgb)
        sf19.drawStringWithOutline(
            "Account Manager",
            this.width / 2.0 - sf19.getStringWidth("Account Manager") / 2,
            10.0,
            -1
        )
        GlStateManager.pushMatrix()
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        RenderUtil.doScissor(this.width / 2 - 203 + 17, 35, this.width / 2 - 203 + 17 + 371, 29 * 9 + 3)
        this.drawAccountsBox(mouseX, mouseY)
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    fun drawAccountsBox(mouseX: Int, mouseY: Int) {
        val yAxis = AtomicInteger(35)
        Main.accountManager.accounts.forEach {
            val password = if (it.password.isEmpty()) "\u00a7cCracked" else it.password.replace(".".toRegex(), "*")
            val hovered = MouseUtil.isHovering(
                this.width / 2 - 203 + 17.0,
                yAxis.get() - this.offsetTranslate.y.toDouble(),
                this.width / 2 - 203 + 17.0 + 371,
                yAxis.get() - this.offsetTranslate.y.toDouble() + 29,
                mouseX,
                mouseY
            )
            if (hovered && Mouse.isButtonDown(MouseUtil.ButtonType.LEFT.identifier)) {
                this.selectedAccount = it
            }
            RenderUtil.drawRect(
                this.width / 2 - 203 + 17.0,
                yAxis.get() - this.offsetTranslate.y.toDouble(),
                this.width / 2 - 203 + 17.0 + 371,
                yAxis.get() - this.offsetTranslate.y.toDouble() + 29,
                if (hovered) Color(0, 0, 0, 200).rgb else Color(0, 0, 0, 120).rgb
            )
            RenderUtil.drawFace(
                it.name,
                this.width / 2 - 203 + 20,
                (yAxis.get() - this.offsetTranslate.y + 1).roundToInt()
            )
            Main.fontBuffer.SF24.drawStringWithOutlineWithoutColorcode(
                if (this.selectedAccount == it) "${it.name} \u00a7a(Selected)" else it.name,
                this.width / 2 - 203 + 20.0 + 25,
                yAxis.get() - this.offsetTranslate.y.toDouble() + 3,
                -1
            )
            Main.fontBuffer.SF24.drawStringWithOutlineWithoutColorcode(
                password,
                this.width / 2 - 203 + 20.0 + 25,
                yAxis.get() - this.offsetTranslate.y.toDouble() + Main.fontBuffer.SF24.getHeight(false),
                -1
            )
            yAxis.addAndGet(29)
        }
    }

    private fun handleOfffset() {
        val yAxis = AtomicInteger(35)
        repeat(Main.accountManager.accounts.size) {
            yAxis.addAndGet(29)
        }
        if (Mouse.hasWheel()) {
            val wheel = Mouse.getDWheel()
            if (wheel > 0) {
                this.offset -= 29.0
            } else if (wheel < 0) {
                this.offset += 29.0
            }
            if (this.offset < 0.0) this.offset = 0.0
            val afuckingval = yAxis.get().toDouble() - 29 * 2 - 5
            if (this.offset > yAxis.get() - 29 * 2 - 5) this.offset = afuckingval
        }

        this.offsetTranslate.interpolate(0f, this.offset.toFloat(), 5.0)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiAddAccount(this))
            1 -> {
                if (Main.accountManager.accounts.size > 0) {
                    val randomAccount = Main.accountManager.accounts[Random.nextInt(Main.accountManager.accounts.size)]
                    this.loginThread = AccountLoginThread(randomAccount.name, randomAccount.password)
                    this.loginThread!!.start()
                }
            }
            2 -> mc.displayGuiScreen(GuiDirectLogin(this))
            3 -> {
                if (this.selectedAccount != null) {
                    this.loginThread = AccountLoginThread(this.selectedAccount!!.name, this.selectedAccount!!.password)
                    this.loginThread!!.start()
                }
            }
            4 -> {
                if (this.loginThread != null) {
                    if (this.loginThread!!.status == "Logging in..." && this.loginThread!!.status == "Don't close this interface! Logging in...") {
                        this.loginThread!!.status = "Don't close this interface! Logging in..."
                    } else mc.displayGuiScreen(this.preScreen)
                } else {
                    mc.displayGuiScreen(this.preScreen)
                }
            }
            5 -> {
                if (this.selectedAccount != null) {
                    mc.displayGuiScreen(GuiAccountEditor(this))
                }
            }
            6 -> {
                if (this.loginThread != null) {
                    this.loginThread = null
                }
                Main.accountManager.removeAccount(this.selectedAccount!!.name)
                this.status = "\u00a7cRemoved."
                this.selectedAccount = null
            }
        }
        super.actionPerformed(button)
    }

    fun updateButtonsState() {
        if (Main.accountManager.accounts.size > 0) this.randomButton.enabled = true
        if (this.selectedAccount != null) {
            this.loginButton.enabled = true
            this.removeButton.enabled = true
            this.editButton.enabled = true
        }
    }

}