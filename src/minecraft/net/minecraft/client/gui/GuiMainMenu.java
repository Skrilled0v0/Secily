package net.minecraft.client.gui;


import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.ui.alt.GuiAltLogin;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.io.IOException;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    Animation animation = new Animation(1500f, false, Easing.CUBIC_OUT);
    Animation newA=new Animation(1000f,false,Easing.CUBIC_OUT);
    ColourAnimation colourAnimation = new ColourAnimation(new Color(0, 0, 0, 30), new Color(0, 0, 0, 100), 1500f, false, Easing.LINEAR);

    public void initGui() {
        int interval = 30;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int j = this.height / 2 - buttonHeight / 2;
        this.buttonList.add(new GuiButton(1, interval, j + interval * 2, buttonWidth, buttonHeight, I18n.format("menu.quit"), animation, animation));
        this.buttonList.add(new GuiButton(2, interval, j + interval, buttonWidth, buttonHeight, I18n.format("menu.options"), animation, animation));
        this.buttonList.add(new GuiButton(3, interval, j, buttonWidth, buttonHeight, "Alt:" + mc.session.getUsername(), animation, animation));
        this.buttonList.add(new GuiButton(4, interval, j - interval, buttonWidth, buttonHeight, I18n.format("menu.multiplayer"), animation, animation));
        this.buttonList.add(new GuiButton(5, interval, j - interval * 2, buttonWidth, buttonHeight, I18n.format("menu.singleplayer"), animation, animation));
    }


    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.id == 5) mc.displayGuiScreen(new GuiSelectWorld(this));

        if (button.id == 4) mc.displayGuiScreen(new GuiMultiplayer(this));

        if (button.id == 3) mc.displayGuiScreen(new GuiAltLogin(this));

        if (button.id == 2) mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));

        if (button.id == 1) SenseHeader.getSense.stopClient();

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animation.setState(isHovering(mouseX, mouseY, 0, 0, width / 4f, height));
        colourAnimation.setState(isHovering(mouseX, mouseY, 0, 0, width / 4f, height));        newA.setState(isHovering(mouseX, mouseY, 0, 0, width / 4f, height));

        this.drawBackground();
        int j = this.height / 2;
        FontDrawer font = Main.fontLoader.EN48;

        GlStateManager.pushMatrix();


        int interval = 30;
        int buttonWidth = 100;
        RenderUtil.drawRect(0, 0, (buttonWidth+interval*2) * animation.getAnimationFactor(), height*animation.getAnimationFactor(), colourAnimation.getColour().getRGB());
        font.drawRainbowString(SenseHeader.getSense.getClientName(), 30, j - 90);
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private boolean isHovering(int mouseX, int mouseY, float xLeft, float yUp, float xRight, float yBottom) {
        return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


}
