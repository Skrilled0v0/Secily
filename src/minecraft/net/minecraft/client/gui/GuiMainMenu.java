package net.minecraft.client.gui;


import me.skrilled.utils.IMC;
import me.skrilled.utils.render.BlurUtil;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback, IMC {

    Animation sideBarMotion = new Animation(1200f, false, Easing.QUAD_OUT);
    Animation singleplayer = new Animation(1200f, false, Easing.QUAD_OUT);
    Animation multiplayer = new Animation(1200f, false, Easing.QUAD_OUT);
    Animation alt = new Animation(1200f, false, Easing.QUAD_OUT);
    Animation settings = new Animation(1200f, false, Easing.QUAD_OUT);
    Animation exit = new Animation(1200f, false, Easing.QUAD_OUT);
    ArrayList<Animation> aniList = new ArrayList<>();

    ColourAnimation sideBarColor = new ColourAnimation(new Color(0, 0, 0, 30), new Color(0, 0, 0, 80), 1500f, false, Easing.LINEAR);

    @Override
    public void initGui() {
        Collections.addAll(aniList, singleplayer, multiplayer, alt, settings, exit);
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();
        GlStateManager.pushMatrix();

        RenderUtil.drawRect(0, 0, width * 0.16f * sideBarMotion.getAnimationFactor(), height, sideBarColor.getColour().getRGB());

        float onceHeight = height*0.8f/5f;
        float lMargin = width * 0.035f;
        float spacing = height * 0.065f;
        float boxWidth = width * 0.2f;
        for (int j = 0; j < aniList.size(); j++) {
            BlurUtil.blurAreaRounded((float) (lMargin * aniList.get(j).getAnimationFactor()), height / 2f - (2.5f - j) * onceHeight - (2 - j) * spacing, (float) (lMargin + boxWidth * aniList.get(j).getAnimationFactor()), height / 2f - (2.5f - j) * onceHeight - (2 - j) * spacing + onceHeight, Main.fontLoader.EN16.getHeight() / 2f, 10);
            RenderUtil.drawCenteredStringBoxWith4PosWithOutAutoNextLine(new float[]{},
                    Main.fontLoader.EN16, "aniList.get(j).toString()", new Color(0, 0, 0, 35).getRGB(), -1);
            if (isHovering(mouseX, mouseY, lMargin, height / 2f - (2.5f - j) * onceHeight - (2 - j) * spacing, lMargin + boxWidth, height / 2f - (2.5f - j) * onceHeight - (2 - j) * spacing + onceHeight)) {
                System.out.println(aniList.get(j) + "   | T");
                aniList.get(j).setState(true);

            } else {
//                System.out.println(aniList.get(j)+"   | F");
                aniList.get(j).setState(false);
            }

        }
        GlStateManager.popMatrix();
        setMotion(isHovering(mouseX, mouseY, 0, 0, width / 6f, height));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void setMotion(boolean flag) {
        sideBarColor.setState(flag);
        sideBarMotion.setState(flag);
    }

    private void setListMotion(int index, boolean flag) {
        for (int z = 0; z < aniList.size(); z++) {
            if (z == index) aniList.get(z).setState(flag);
            else aniList.get(z).setState(!flag);
        }
    }

    private boolean isHovering(int mouseX, int mouseY, float xLeft, float yUp, float xRight, float yBottom) {
        return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


}
