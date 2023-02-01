/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230118
 */
package me.skrilled.ui;


import me.fontloader.FontDrawer;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class LoadingGui extends GuiScreen {
    ResourceLocation bg = new ResourceLocation("skrilled/bgNoBlur.png");
    ResourceLocation clientIcon = new ResourceLocation("skrilled/launcher.png");
    BoundedAnimation loading = new BoundedAnimation(RenderUtil.width() / 6f, RenderUtil.width() - RenderUtil.width() / 6f, 4000f, false, Easing.QUART_OUT);
    ColourAnimation color = new ColourAnimation(new Color(116, 78, 170, 150), new Color(104, 169, 255, 200), 4000f, false, Easing.LINEAR);
    ColourAnimation black = new ColourAnimation(new Color(0, 0, 0, 255), new Color(0, 0, 0, 0), 2500f, false, Easing.LINEAR);
    Animation iconMotion=new Animation(3500f,false,Easing.BACK_OUT);
    TimerUtil timerUtil=new TimerUtil();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int w = RenderUtil.width();
        int h = RenderUtil.height();
        float size = (float) (128*iconMotion.getAnimationFactor());
        FontDrawer fontRenderer = Main.fontLoader.EN64;
        black.setState(true);
        color.setState(true);
        loading.setState(true);
        iconMotion.setState(true);
        RenderUtil.drawImage(bg, 0, 0, w, h);
        RenderUtil.drawIcon(w / 2f - size / 2f, h / 2f - size, (int)size, (int)size, clientIcon);
        fontRenderer.drawCenteredStringWithShadow("Final Status Loading", w / 2f, h / 2f, 1.2f, -1);
        RenderUtil.drawRect(w / 6f - 5, h / 2f + h / 6f - 5, w - w / 6f + 5, h / 2f + h / 6f + 35f, new Color(62, 62, 62).getRGB());
        RenderUtil.drawRect(w / 6f, h / 2f + h / 6f, (float) loading.getAnimationValue(), h / 2f + h / 6f + 30f, color.getColour().getRGB());
        RenderUtil.drawRect(0,0,w,h,black.getColour().getRGB());
        GL11.glColor4f(1,1,1,1);
        if(timerUtil.hasReached(6000))
           mc.displayGuiScreen(new GuiMainMenu());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


}
