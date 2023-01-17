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
    Animation loading = new Animation(4000f, false, Easing.CUBIC_OUT);
    ColourAnimation color = new ColourAnimation(new Color(255, 89, 22, 170), new Color(104, 169, 255, 255), 3000f, false, Easing.CUBIC_OUT);


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float w = RenderUtil.width();
        float h = RenderUtil.height();
        color.setState(true);
        loading.setState(true);
        System.out.println(loading.getAnimationFactor());
        RenderUtil.drawImage(bg, 0, 0, RenderUtil.width(), RenderUtil.height());
        CFontRenderer fontRenderer = SenseHeader.getSense.getFontBuffer().font36;
        fontRenderer.drawCenteredStringWithShadow("Final Status Loading", w / 2, h / 2, -1);
        RenderUtil.drawRound(w / 8f, h / 2f + h / 8f, w - w / 8f, h / 2f + h / 8f + 30f, new Color(0x393939).getRGB(), new Color(0x393939).getRGB());
        RenderUtil.drawRound(w / 8f, h / 2f + h / 8f, (float) ((w - w / 8f) * loading.getAnimationFactor()), h / 2f + h / 8f + 30f, color.getColour().getRGB(), color.getColour().getRGB());
        if (loading.getAnimationFactor() == 1) mc.displayGuiScreen(new GuiMainMenu());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


}
