package me.skrilled.ui.clickui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleHeader.ModuleType;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

import static me.skrilled.utils.IMC.sense;

public class EclipseMenu extends GuiScreen {
    public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static ModuleType currentModuleType = ModuleType.COMBAT;
    public static ModuleHeader currentModule = SenseHeader.sense.getModuleManager().getModuleListByModuleType(currentModuleType).get(0);
    public static float startX = sr.getScaledWidth() / 2 - 370 / 2, startY = sr.getScaledHeight() / 2 - 325 / 2;
    public static int moduleStart = 0;
    public static int valueStart = 0;
    public float moveX = 0, moveY = 0;
    boolean previousmouse = true;
    boolean mouse;
    int sidey;
    boolean bind = false;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(mc);
        int f1c = 1, bc = 1, f3c = 1, f2c = 1, f4c = 1, va = 1, bcc = 1, rc = 1, rc2 = 1, color = 1;
        if (SettingMenu.colorMode.getCurrentEnumType().equalsIgnoreCase("Dark")) {
            f1c = new Color(175, 175, 175).getRGB();
            f2c = new Color(13, 13, 13).getRGB();
            f4c = new Color(40, 40, 40).getRGB();
            f3c = new Color(22, 22, 22).getRGB();
            bcc = new Color(67, 67, 67).getRGB();
            rc = new Color(42, 42, 42).getRGB();
            rc2 = new Color(70, 70, 70).getRGB();
            va = new Color(21, 21, 21).getRGB();
            bc = new Color(27, 27, 27).getRGB();
            color = new Color(46, 115, 252).getRGB();
        } else {
            f1c = new Color(10, 10, 10).getRGB();
            va = new Color(150, 150, 150).getRGB();
            f4c = new Color(150, 150, 150).getRGB();
            bc = new Color(241, 239, 241).getRGB();
            rc = new Color(200, 200, 200).getRGB();
            rc2 = new Color(170, 170, 170).getRGB();
            bcc = new Color(177, 177, 177).getRGB();
            f2c = new Color(214, 214, 214).getRGB();
            f3c = new Color(238, 238, 238).getRGB();
            color = new Color(41, 158, 255).getRGB();
        }

        if (startX > sr.getScaledWidth_double() || startY > sr.getScaledHeight_double() || startX < 161 || startX < 0) {
            startX = sr.getScaledWidth() / 2 - 370 / 2;
            startY = sr.getScaledHeight() / 2 - 325 / 2;
        }
        RenderUtil.drawBorderedRect(startX - 20, startY + 435, startX + 380, startY + 35, 2, color, bc);
        RenderUtil.drawRect(startX + 360, startY + 415, startX, startY + 70, f2c);
        int m = Mouse.getDWheel();
        if (this.isCategoryHovered(startX, startY + 50, startX + 150, startY + 375, mouseX, mouseY)) {
            if (m < 0 && moduleStart < sense.moduleManager.getModuleListByModuleType(currentModuleType).size() - 1) {
                moduleStart++;
            }
            if (m > 0 && moduleStart > 0) {
                moduleStart--;
            }
        }
        if (this.isCategoryHovered(startX + 150, startY + 50, startX + 370, startY + 375, mouseX, mouseY)) {
            if (m < 0 && valueStart < currentModule.getValueList().size() - 1) {
                valueStart++;
            }
            if (m > 0 && valueStart > 0) {
                valueStart--;
            }
        }
        // wheel
        sense.getFontBuffer().EN24.drawCenteredString(currentModuleType.toString() + "/" + currentModule.getModuleName(), startX + 175, startY + 45, color);
        float mY = startY + 30;
        for (int i = 0; i < sense.moduleManager.getModuleListByModuleType(currentModuleType).size(); i++) {
            ModuleHeader module = sense.moduleManager.getModuleListByModuleType(currentModuleType).get(i);
            if (mY > startY + 350) break;
            if (i < moduleStart) {
                continue;
            }
            RenderUtil.drawRect(startX + 10, mY + 48, startX + 140, mY + 68, bc);
            RenderUtil.drawFilledCircle(startX + 12, mY + 58, 10, bc, 10);
            RenderUtil.drawFilledCircle(startX + 142, mY + 58, 10, bc, 10);
            if (!module.isIsOpen()) {
                if (module.clickAnim > 110) {
                    module.clickAnim--;
                }

                RenderUtil.drawRect(startX + 110, mY + 54, startX + 125, mY + 62, f4c);
                RenderUtil.drawFilledCircle(startX + 125, mY + 58, 4, f4c, 5);
                RenderUtil.drawFilledCircle(startX + 110, mY + 58, 4, f4c, 5);
                RenderUtil.drawFilledCircle(startX + module.clickAnim, mY + 58, 5, bcc, 5);
            } else {
                if (module.clickAnim < 125) {
                    module.clickAnim++;
                }
                RenderUtil.drawRect(startX + 110, mY + 54, startX + 125, mY + 62, f4c);
                RenderUtil.drawFilledCircle(startX + 125, mY + 58, 4, f4c, 5);
                RenderUtil.drawFilledCircle(startX + 110, mY + 58, 4, f4c, 5);
                RenderUtil.drawFilledCircle(startX + module.clickAnim, mY + 58, 5, color, 5);
            }
            sense.getFontBuffer().EN18.drawString(module.getModuleName(), startX + 20, mY + 53, f1c);
            if (isSettingsButtonHovered(startX + 110, mY + 54, startX + 125, mY + 62, mouseX, mouseY)) {
                if (!this.previousmouse && Mouse.isButtonDown(0)) {
                    module.setIsOpen(!module.isIsOpen());
                    previousmouse = true;
                }
                if (!this.previousmouse && Mouse.isButtonDown(1)) {
                    previousmouse = true;
                }
            }

            if (!Mouse.isButtonDown(0)) {
                this.previousmouse = false;
            }
            if (isSettingsButtonHovered(startX, mY + 45, startX + 125, mY + 70, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                for (int j = 0; j < currentModule.getValueList().size(); j++) {
                    ValueHeader value = currentModule.getValueList().get(j);
                    if (value.getValueType() == ValueHeader.ValueType.BOOLEAN) {
                        value.setAinm(ValueHeader.ValueType.BOOLEAN, 55);
                    }
                }
                for (ModuleHeader mod : sense.moduleManager.getModuleListByModuleType(currentModuleType)) {
                    mod.clickAnim = 115;
                }
                currentModule = module;
                valueStart = 0;
            }
            mY += 25;
        }

        // modules
        mY = startY + 30;
        CFontRenderer font = sense.getFontBuffer().EN18;
        for (int i = 0; i < currentModule.getValueList().size(); i++) {
            if (mY > startY + 350) break;
            if (i < valueStart) {
                continue;
            }
            ValueHeader value = currentModule.getValueList().get(i);
            if (value.getValueType() == ValueHeader.ValueType.DOUBLE) {
                float x = startX + 270;
                double render = 68.0F * (value.getSettingValue() - value.getMinValue()) / (value.getMaxValue() - value.getMinValue());
                RenderUtil.drawRect(x + 2, mY + 52, (float) ((double) x + 75), mY + 53, f1c);
                RenderUtil.drawRect(x + 2, mY + 52, (float) ((double) x + render + 6.5D), mY + 53, color);
                RenderUtil.drawFilledCircle((float) ((double) x + render + 2D) + 3, mY + 52.25, 3.7, color, 5);
                RenderUtil.drawRound(0, 0, 0, 0, new Color(0, 0, 0, 0).getRGB(), 1);
                font.drawString(value.getValueName() + ": " + value.getSettingValue(), startX + 170, mY + 48, f1c);
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(x, mY + 52, x + 100, mY + 57, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        render = value.getMinValue();
                        double max = value.getMaxValue();
                        double inc = value.getIncValue();
                        double valAbs = (double) mouseX - ((double) x + 1.0D);
                        double perc = valAbs / 68.0D;
                        perc = Math.min(Math.max(0.0D, perc), 1.0D);
                        double valRel = (max - render) * perc;
                        double val = render + valRel;
                        val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
                        value.setSettingValue(val);
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 20;
            }
            if (value.getValueType() == ValueHeader.ValueType.BOOLEAN) {
                float x = startX + 245;
                int xx = 50;
                int x2x = 65;
                font.drawString(value.getValueName(), startX + 170, mY + 48, f1c);
                RenderUtil.drawBorderedRect(1, 1, 1, 1, 1, new Color(0, 0, 0, 0).getRGB(), 77);
                if (value.isOptionOpen()) {

                    if (value.getAinm(ValueHeader.ValueType.BOOLEAN) < x2x) {
                        value.setAinm(ValueHeader.ValueType.BOOLEAN, value.getAinm(ValueHeader.ValueType.BOOLEAN) + 1);
                    }
                    RenderUtil.drawRect(x + xx, mY + 50, x + x2x, mY + 59, va);
                    RenderUtil.drawFilledCircle(x + xx, mY + 54.5, 4.5, va, 10);
                    RenderUtil.drawFilledCircle(x + x2x, mY + 54.5, 4.5, va, 10);
                    RenderUtil.drawFilledCircle(x + value.getAinm(ValueHeader.ValueType.BOOLEAN), mY + 54.5, 5, color, 10);
                } else {
                    if (value.getAinm(ValueHeader.ValueType.BOOLEAN) > xx) {
                        value.setAinm(ValueHeader.ValueType.BOOLEAN, value.getAinm(ValueHeader.ValueType.BOOLEAN) - 1);
                    }
                    RenderUtil.drawRect(x + xx, mY + 50, x + x2x, mY + 59, va);
                    RenderUtil.drawFilledCircle(x + xx, mY + 54.5, 4.5, va, 10);
                    RenderUtil.drawFilledCircle(x + x2x, mY + 54.5, 4.5, va, 10);
                    RenderUtil.drawFilledCircle(x + value.getAinm(ValueHeader.ValueType.BOOLEAN), mY + 54.5, 5, new Color(177, 177, 177).getRGB(), 10);
                }
                if (this.isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        this.previousmouse = true;
                        this.mouse = true;
                    }

                    if (this.mouse) {
                        value.setOptionOpen(!(boolean) value.isOptionOpen());
                        this.mouse = false;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                mY += 20;
            }
            if (value.getValueType() == ValueHeader.ValueType.ENUM_TYPE) {
                float x = startX + 260;
                font.drawString(value.getValueName(), startX + 170, mY + 48, f1c);
                RenderUtil.drawRect(x + 5, mY + 45, x + 75, mY + 65, va);
                RenderUtil.drawFilledCircle(x + 5, mY + 55, 10, va, 5);
                RenderUtil.drawFilledCircle(x + 75, mY + 55, 10, va, 5);
                font.drawString(value.getCurrentEnumType(), x + 40 - font.getStringWidth(value.getCurrentEnumType()) / 2, mY + 51, f1c);
                if (this.isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)) {
                    if (Mouse.isButtonDown(0) && !this.previousmouse) {
                        int index = 0;
                        for (String enumType : value.getEnumTypes()) {
                            if (enumType == value.getCurrentEnumType()) break;
                            index++;
                        }
                        if (value.getEnumTypes().size() > (index + 1))
                            value.setCurrentEnumType(value.getEnumTypes().get(index + 1));
                        else value.setCurrentEnumType(value.getEnumTypes().get(0));
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }

                }
                mY += 25;
            }
        }
        if (mY < startY + 350) {
            float x = startX + 260;
            font.drawString("Bind", startX + 170, mY + 48, f1c);
            RenderUtil.drawRect(x + 5, mY + 45, x + 75, mY + 65, va);
            RenderUtil.drawFilledCircle(x + 5, mY + 55, 10, va, 5);
            RenderUtil.drawFilledCircle(x + 75, mY + 55, 10, va, 5);
            font.drawString("" + Keyboard.getKeyName(currentModule.getKey()), x + 40 - font.getStringWidth(Keyboard.getKeyName(currentModule.getKey())) / 2, mY + 51, f1c);
        }

        // value

        if (isHovered(startX, startY + 30, startX + 400, startY + 70, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (moveX == 0 && moveY == 0) {
                moveX = mouseX - startX;
                moveY = mouseY - startY;
            } else {
                startX = mouseX - moveX;
                startY = mouseY - moveY;
            }
            this.previousmouse = true;
        } else if (moveX != 0 || moveY != 0) {
            moveX = 0;
            moveY = 0;
        }

        RenderUtil.drawRect(0, 0, 160, sr.getScaledHeight_double(), f3c);

        RenderUtil.drawRect(160, 0, 161, sr.getScaledHeight_double(), color);
        sense.getFontBuffer().EN36.drawCenteredString(sense.skrilledSense(), 75, 87, color);
        int j = 60;
        int l = 45;
        int k = 100;
        int xx = 42;
        for (int i = 0; i < ModuleType.values().length; i++) {
            ModuleType[] iterator = ModuleType.values();
            RenderUtil.drawFilledCircle(+35, +k + j + i * l, 15, f2c, 5);
            RenderUtil.drawFilledCircle(+120, +k + j + i * l, 15, f2c, 5);
            RenderUtil.drawRect(35, k - 15 + j + i * l, 120, k + 15 + j + i * l, f2c);
            RenderUtil.drawRound(0, 0, 0, 0, new Color(0, 0, 0, 0).getRGB(), 1);
            if (iterator[i] == currentModuleType) {
                if (sidey < i * l) {
                    sidey = sidey + 10 > i * l ? i * l : sidey + 10;
                }
                if (sidey > i * l) {
                    sidey = sidey - 10 < i * l ? i * l : sidey - 10;
                }

                RenderUtil.drawFilledCircle(+35, k + j + sidey, 15, color, 5);
                RenderUtil.drawFilledCircle(+120, k + j + sidey, 15, color, 5);
                RenderUtil.drawRect(35, k - 15 + j + sidey, 120, k + 15 + j + sidey, color);

            }
            sense.getFontBuffer().EN24.drawCenteredString(iterator[i].toString(), xx + 40, k + 53 + l * i, new Color(255, 255, 255).getRGB());
            RenderUtil.drawImageWithColor(new ResourceLocation("skrilled/MenuICON/" + iterator[i].toString() + ".png"), xx - 10, k + 48 + l * i, 25, 25, -1);
            try {
                if (this.isCategoryHovered(+15, +k - 10 + j + i * l, +120, +k + 20 + j + i * l, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    currentModuleType = iterator[i];
                    currentModule = sense.moduleManager.getModuleListByModuleType(currentModuleType).get(0);
                    moduleStart = 0;
                    valueStart = 0;
                    for (int x = 0; x < currentModule.getValueList().size(); x++) {
                        ValueHeader value = currentModule.getValueList().get(x);
                        if (value.getValueType() == ValueHeader.ValueType.BOOLEAN) {
                            value.setAinm(ValueHeader.ValueType.BOOLEAN, 55);
                        }
                    }
                    for (ModuleHeader mod : sense.moduleManager.getModuleListByModuleType(currentModuleType)) {
                        mod.clickAnim = 115;
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        // MoudleType
    }

    public void initGui() {
        for (int i = 0; i < currentModule.getValueList().size(); i++) {
            ValueHeader value = currentModule.getValueList().get(i);
            if (value.getValueType() == ValueHeader.ValueType.BOOLEAN) {
                value.setAinm(ValueHeader.ValueType.BOOLEAN, 55);
            }
        }
        for (ModuleHeader mod : sense.moduleManager.getModuleListByModuleType(currentModuleType)) {
            mod.clickAnim = 115;
        }
        int l = 45;
        for (int i = 0; i < ModuleType.values().length; i++) {
            ModuleType[] iterator = ModuleType.values();
            if (iterator[i] == currentModuleType) {
                sidey = i * l;
            }
        }
        if (this.mc.theWorld != null) {
            this.mc.entityRenderer.loadShader(new ResourceLocation("skrilled/blur.json"));
        }

        super.initGui();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.bind) {
            currentModule.setKey(keyCode);
            if (keyCode == 1) {
                currentModule.setKey(0);
            }
            this.bind = false;
        } else if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            sense.moduleManager.getModuleByClass(SettingMenu.class).setIsOpen(false);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        float x = startX + 220;
        float mY = startY + 30;
        for (int i = 0; i < currentModule.getValueList().size(); i++) {
            if (mY > startY + 350) break;
            if (i < valueStart) {
                continue;
            }
            ValueHeader value = currentModule.getValueList().get(i);
            if (value.getValueType() == ValueHeader.ValueType.DOUBLE) {
                mY += 20;
            }
            if (value.getValueType() == ValueHeader.ValueType.BOOLEAN) {

                mY += 20;
            }
            if (value.getValueType() == ValueHeader.ValueType.ENUM_TYPE) {

                mY += 25;
            }
        }
        if (mY < startY + 350) {
            if (mouseX > x - 5 && mouseX < x + 90 && mouseY > mY + 45 && mouseY < mY + 65 && button == 0) {
                this.bind = !this.bind;
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed() {
        mc.entityRenderer.switchUseShader();
    }
}
