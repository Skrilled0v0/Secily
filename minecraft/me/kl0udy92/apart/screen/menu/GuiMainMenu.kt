package me.kl0udy92.apart.screen.menu

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.screen.menu.account.menu.GuiAccountManager
import me.kl0udy92.apart.screen.menu.button.GuiSimpleButton
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.shaders.implementations.MainMenuShader
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.awt.Color

class GuiMainMenu(
    private val sandboxShader: MainMenuShader = MainMenuShader(0),
    private val languagesLocation: ResourceLocation = ResourceLocation("${Main.name.lowercase()}/icons/menu/languages.png"),
    private val settingsLocation: ResourceLocation = ResourceLocation("${Main.name.lowercase()}/icons/menu/settings.png")
) : GuiScreen() {

    override fun initGui() {
        this.simpleButtonList.add(GuiSimpleButton(0, this.width / 2 - 100, this.height / 2 - 42, "Singleplayer"))
        this.simpleButtonList.add(GuiSimpleButton(1, this.width / 2 - 100, this.height / 2 - 21, "Multiplayer"))
        this.simpleButtonList.add(GuiSimpleButton(2, this.width / 2 - 100, this.height / 2, "Shut down"))
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        /*mc.textureManager.bindTexture(ResourceLocation("apart/background.jpg"))
        drawModalRectWithCustomSizedTexture(
            -mouseX,
            -mouseY,
            0f,
            0f,
            width * 2, height * 2,
            width * 2F, height * 2.toFloat()
        )*/
        this.drawBackground()
        this.renderIconButtons(mouseX, mouseY)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiSelectWorld(this))
            1 -> mc.displayGuiScreen(GuiMultiplayer(this))
            2 -> mc.shutdown()
        }
        super.actionPerformed(button)
    }

    fun drawBackground() {
        GlStateManager.enableAlpha()
        GlStateManager.disableCull()
        this.sandboxShader.render(width.toFloat(), height.toFloat())
        GL11.glBegin(GL11.GL_QUADS)

        GL11.glVertex2f(-1f, -1f)
        GL11.glVertex2f(-1f, 1f)
        GL11.glVertex2f(1f, 1f)
        GL11.glVertex2f(1f, -1f)

        GL11.glEnd()

        GL20.glUseProgram(0)
    }

    private fun renderIconButtons(mouseX: Int, mouseY: Int) {
        val isHoveringAltsLocation = MouseUtil.isHovered(5, 5, 24, 24, mouseX, mouseY)
        val isHoveringLanguagesLocation = MouseUtil.isHovered(width - 24, height - 24, 24, 24, mouseX, mouseY)
        val isHoveringSettingsLocation = MouseUtil.isHovered(width - 24, 0, 24, 24, mouseX, mouseY)
        if (isHoveringAltsLocation) {
            RenderUtil.drawRect(4, 4, 5 + 24 + 1, 5 + 24 + 1, Color.BLACK.rgb)
            if (Mouse.isButtonDown(0)) mc.displayGuiScreen(GuiAccountManager(this))
        }
        RenderUtil.drawFace(this.mc.session.profile, 5, 5)
        RenderUtil.drawImage(
            this.languagesLocation,
            width - 24f,
            height - 24f,
            24f,
            24f,
            if (isHoveringLanguagesLocation) Color(243, 233, 224).rgb else Color(200, 200, 200).rgb
        )
        if (isHoveringLanguagesLocation && Mouse.isButtonDown(0))
            mc.displayGuiScreen(GuiLanguage(this, mc.gameSettings, mc.languageManager))
        RenderUtil.drawImage(
            settingsLocation,
            (width - 24).toFloat(),
            0f,
            24f,
            24f,
            if (isHoveringSettingsLocation) Color(243, 233, 224).rgb else Color(200, 200, 200).rgb
        )
        if (isHoveringSettingsLocation && Mouse.isButtonDown(0))
            mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
    }

}