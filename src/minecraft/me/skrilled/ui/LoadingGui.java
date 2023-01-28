/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230118
 */
package me.skrilled.ui;


import me.fontloader.FontDrawer;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class LoadingGui extends GuiScreen {
    ResourceLocation bg = new ResourceLocation("skrilled/bgNoBlur.png");
    ResourceLocation genshin = new ResourceLocation("skrilled/launcher.png");
    BoundedAnimation loading = new BoundedAnimation(RenderUtil.width() / 6f, RenderUtil.width() - RenderUtil.width() / 6f, 4000f, false, Easing.QUART_OUT);
    ColourAnimation color = new ColourAnimation(new Color(255, 89, 22, 170), new Color(104, 169, 255, 255), 4000f, false, Easing.LINEAR);


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int w = RenderUtil.width();
        int h = RenderUtil.height();
        int genshinSize = 128;
        FontDrawer fontRenderer = Main.fontLoader.EN64;
        color.setState(true);
        loading.setState(true);
        RenderUtil.drawImage(bg, 0, 0, w, h);
        RenderUtil.drawIcon(w / 2f - genshinSize / 2f, h / 2f - genshinSize, genshinSize, genshinSize, genshin);
        fontRenderer.drawCenteredStringWithShadow("Final Status Loading", w / 2f, h / 2f + h / 10f, 1.2f, -1);
        RenderUtil.drawRoundRect(w / 6f - 5, h / 2f + h / 6f - 5, w - w / 6f + 5, h / 2f + h / 6f + 35f, 10, new Color(62, 62, 62).getRGB());
        RenderUtil.drawRoundRect(w / 6f, h / 2f + h / 6f, (float) loading.getAnimationValue(), h / 2f + h / 6f + 30f, 10, color.getColour().getRGB());
        if (loading.getAnimationFactor() == 1) mc.displayGuiScreen(new GuiMainMenu());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


}
