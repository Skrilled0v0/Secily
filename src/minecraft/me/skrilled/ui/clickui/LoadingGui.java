/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230118
 */
package me.skrilled.ui.clickui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.SenseHeader;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class LoadingGui extends GuiScreen {
    ResourceLocation bg = new ResourceLocation("skrilled/bgNoBlur.png");
    ResourceLocation genshin = new ResourceLocation("skrilled/launcher.png");
    Animation loading = new Animation(4000f, false, Easing.CUBIC_OUT);
    ColourAnimation color = new ColourAnimation(new Color(255, 89, 22, 170), new Color(104, 169, 255, 255), 4000f, false, Easing.LINEAR);


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int w = RenderUtil.width();
        int h = RenderUtil.height();
        int genshinSize = 128;
        CFontRenderer fontRenderer = SenseHeader.getSense.getFontBuffer().EN64;
        color.setState(true);
        loading.setState(true);
        RenderUtil.drawImage(bg, 0, 0, w, h);
        RenderUtil.drawIcon(w / 2f - genshinSize / 2f, h / 2f - genshinSize, genshinSize, genshinSize, genshin);
        fontRenderer.drawCenteredStringWithShadow("Final Status Loading", w / 2f, h / 2f + h / 10f, -1);
        RenderUtil.drawRound(w / 6f - 5, h / 2f + h / 6f - 5, w - w / 6f + 5, h / 2f + h / 6f + 35f, new Color(62, 62, 62).getRGB(), new Color(62, 62, 62).getRGB());
        RenderUtil.drawRound(w / 6f, h / 2f + h / 6f, (float) ((w - w / 6f) * loading.getAnimationFactor()), h / 2f + h / 6f + 30f, color.getColour().getRGB(), color.getColour().getRGB());
        if (loading.getAnimationFactor() == 1) mc.displayGuiScreen(new GuiMainMenu());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


}
