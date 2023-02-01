package net.minecraft.client.gui;


import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.ui.Notification;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.BlurUtil;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.main.Main;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiButton extends Gui implements IMC {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    private final Animation motion = new Animation(1000f, false, Easing.LINEAR);
    //    ColourAnimation upBgMotion = new ColourAnimation(new Color(255, 255, 255, 50), new Color(108, 108, 255, 75), 1000f, false, Easing.LINEAR);
//    ColourAnimation downBgMotion = new ColourAnimation(new Color(108, 108, 255, 50), new Color(255, 255, 255, 75), 1000f, false, Easing.LINEAR);
    private final ColourAnimation rectMotion = new ColourAnimation(new Color(10, 10, 10, 35), new Color(10, 10, 10, 100), 1000f, false, Easing.LINEAR);
    private final ColourAnimation strMotion = new ColourAnimation(new Color(255, 255, 255), new Color(255, 140, 0), 1000f, false, Easing.LINEAR);
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected int width;
    protected int height;
    public boolean hovered;
    public Animation widthMotion;
    public Animation heightMotion;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    public GuiButton(int id,int xPosition, int yPosition,  int width, int height,String displayString,  Animation widthMotion, Animation heightMotion) {
        this.enabled = true;
        this.visible = true;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.displayString = displayString;
        this.id = id;
        this.width = width;
        this.height = height;
        this.widthMotion = widthMotion;
        this.heightMotion = heightMotion;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
/*
             四周border逐渐补全绘制方法
            //up line
            RenderUtil.drawRect(this.xPosition, this.yPosition, this.xPosition + width * motion.getAnimationFactor(), this.yPosition + lineWidth, rectMotion.getColour().getRGB());
            //right line
            RenderUtil.drawRect(this.xPosition + width - lineWidth, this.yPosition, this.xPosition + width, this.yPosition + height * motion.getAnimationFactor(), rectMotion.getColour().getRGB());
            //down line
            RenderUtil.drawRect(this.xPosition + width - width * motion.getAnimationFactor(), this.yPosition + height, this.xPosition + width, this.yPosition + height + lineWidth, rectMotion.getColour().getRGB());
            //left line
            RenderUtil.drawRect(this.xPosition, this.yPosition + height - height * motion.getAnimationFactor(), this.xPosition + lineWidth, this.yPosition + height, rectMotion.getColour().getRGB());
*/
        FontDrawer font = Main.fontLoader.EN16;
        float xMotion = 0f;
        float yMotion = 0f;
        if (widthMotion != null) {
            xMotion =(1-(float) widthMotion.getAnimationFactor())* -(float) (this.xPosition + width + 4f * motion.getAnimationFactor());
        }
        if (heightMotion != null) {
            yMotion = (1 - (float) heightMotion.getAnimationFactor())*-(float) (this.yPosition + height + 2f * motion.getAnimationFactor());
        }

        if (this.visible) {

            startMotion(hovered);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            RenderUtil.drawRoundRect((float) (this.xPosition - 4f * motion.getAnimationFactor()) + xMotion, (float) (this.yPosition - 2f * motion.getAnimationFactor()) + yMotion, (float) (this.xPosition + width + 4f * motion.getAnimationFactor()) + xMotion, (float) (this.yPosition + height + 2f * motion.getAnimationFactor()) + yMotion, 5, rectMotion.getColour().getRGB());
            BlurUtil.blurAreaRounded((float) (this.xPosition - 4f * motion.getAnimationFactor()) + xMotion, (float) (this.yPosition - 2f * motion.getAnimationFactor()) + yMotion, (float) (this.xPosition + width + 4f * motion.getAnimationFactor()) + xMotion, (float) (this.yPosition + height + 2f * motion.getAnimationFactor()) + yMotion, 5, 10);

            this.mouseDragged(mc, mouseX, mouseY);
            font.drawCenteredString(this.displayString, this.xPosition + this.width / 2f + xMotion, this.yPosition + yMotion + (this.height - 8) / 2f, strMotion.getColour().getRGB());
        }
    }

    public void startMotion(boolean flag) {
        rectMotion.setState(flag);
        motion.setState(flag);
        strMotion.setState(flag);
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
