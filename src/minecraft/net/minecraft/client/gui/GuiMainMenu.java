package net.minecraft.client.gui;


import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.ui.alt.GuiAltLogin;
import me.skrilled.utils.IMC;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.io.IOException;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback, IMC {
    private final Minecraft mc = IMC.mc;

    int bAlpha = 75;

    public void initGui() {
        int interval = 30;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int j = this.height / 2 - buttonHeight / 2;
        this.buttonList.add(new GuiButton(1, interval, j + interval * 2, buttonWidth, buttonHeight, I18n.format("menu.quit")));
        this.buttonList.add(new GuiButton(2, interval, j + interval, buttonWidth, buttonHeight, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(3, interval, j, buttonWidth, buttonHeight, "Alt:" + mc.session.getUsername()));
        this.buttonList.add(new GuiButton(4, interval, j - interval, buttonWidth, buttonHeight, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(5, interval, j - interval * 2, buttonWidth, buttonHeight, I18n.format("menu.singleplayer")));
    }


    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.id == 5) mc.displayGuiScreen(new GuiSelectWorld(this));

        if (button.id == 4) mc.displayGuiScreen(new GuiMultiplayer(this));

        if (button.id == 3) mc.displayGuiScreen(new GuiAltLogin(this));

        if (button.id == 2) mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));

        if (button.id == 1) SenseHeader.getSense.stopClient();

    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();
        int j = this.height / 2;
        int sideBarColor = new Color(10, 10, 10, bAlpha).getRGB();
        FontDrawer font = Main.fontLoader.EN48;

        GlStateManager.pushMatrix();
        RenderUtil.drawRect(0, 0, 160, height, sideBarColor);
        font.drawRainbowString(SenseHeader.getSense.getClientName(), 30, j - 90);
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


}
