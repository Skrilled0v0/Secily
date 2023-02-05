/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.value;


import me.fontloader.FontDrawer;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.util.ArrayList;

public class ValueHeader {
    //hsb.SB
    public Color[][] colorPoints = new Color[][]{};
    //hsb.H
    public Color[] colorBar = new Color[360];
    public int x1 = 0;
    public int y1 = 0;
    public int x2 = 0;
    public int y2 = 0;
    public boolean visible = false;
    public String strInFieldBox = null;
    /**
     * 构造的valueHeader属于enum时存储子enum对象
     */
    public ArrayList<SubEnumValueHeader> subEnumValueHeaders = new ArrayList<>();
    /**
     * enum类value框的x1动画
     */
    public BoundedAnimation selectedEnumBGAnim = null;
    public SubEnumValueHeader lastSelectedSubEnumValueHeader = null;
    /**
     * 用于Gui中拖动double条
     */
    public boolean onClicking = false;
    String valueName;
    boolean optionOpen;
    ValueType valueType;
    double[] doubles;
    double maxValue;
    double minValue;
    double doubleCurrentValue;
    double incValue;
    ArrayList<String> enumTypes;
    String currentEnumType;
    Color colorValue;
    String strValue;
    int bAnim = 55;
    int eAnim = 55;
    int dAnim = 55;
    Animation motion = new Animation(600f, false, Easing.LINEAR);

    public ValueHeader(String valueName, double[] doubles) {
        this.valueType = ValueType.DOUBLE;
        this.valueName = valueName;
        this.minValue = doubles[0];
        this.doubleCurrentValue = doubles[1];
        this.maxValue = doubles[2];
        this.incValue = doubles[3];
        this.doubles = doubles;
    }

    public ValueHeader(String valueName, String currentEnumType, ArrayList<String> enumType) {
        this.valueType = ValueType.ENUM_TYPE;
        this.valueName = valueName;
        this.enumTypes = enumType;
        this.currentEnumType = currentEnumType;
    }

    public ValueHeader(String valueName, Color colorValue) {
        this.valueType = ValueType.COLOR;
        this.valueName = valueName;
        this.colorValue = colorValue;

    }

    public ValueHeader(String valueName, String strValue) {
        this.valueType = ValueType.STRING;
        this.valueName = valueName;
        this.strValue = strValue;
    }

    public ValueHeader(String valueName, boolean optionOpen) {
        this.valueType = ValueType.BOOLEAN;
        this.valueName = valueName;
        this.optionOpen = optionOpen;
    }

    public void draw() {
        FontDrawer font = Main.fontLoader.EN12;
        switch (valueType) {
            case DOUBLE:

                int buttonColor = new Color(0, 136, 255).getRGB();
                int bgColor = new Color(25, 25, 25, 150).getRGB();

                y2 = y1 + 10;

                RenderUtil.drawRoundRect(x1, 0.75f * y1 + 0.25f * y2, x2, 0.25f * y1 + 0.75f * y2, 5, bgColor);
                double[] ds = this.getDoubles();
                RenderUtil.drawCircle((float) (x1 + (x2 - x1) * ((ds[1] - ds[0]) / (ds[2] - ds[0]))), (y1 + y2) / 2f, 5, buttonColor);
                String value = Double.toString((Math.floor(ds[1] * 10) / 10));
                font.drawCenteredString(value, (float) (x1 + (x2 - x1) * ((ds[1] - ds[0]) / (ds[2] - ds[0]))), 0.1f * y1 + 0.9f * y2, -1);

                break;
            case COLOR:

                int colorBoardBGWidth = 380;
                int colorBoardWidth = 360;
                int colorBoardBGHeight = 380;
                int colorBoardHeight = 360;

                colorBar = new Color[colorBoardWidth];
                for (int h = 0; h < colorBoardWidth; h++) {
                    colorBar[h] = Color.getHSBColor(h, 1f, 1f);
                    for (int s = 0; s < colorBoardWidth; s++) {
                        for (int b = 0; b < colorBoardHeight; b++) {
                            colorPoints[s][b] = Color.getHSBColor(h, s / colorBoardWidth, b / colorBoardHeight);
                        }
                    }
                }


                break;
            case STRING:

                int fieldBGColor = new Color(25, 25, 25, 150).getRGB();
                y2 = y1 + 10;

                RenderUtil.drawRect(x1, y1, x2, y2, fieldBGColor);
                if (strInFieldBox == null) {
                    strInFieldBox = strValue;
                }
                font.drawString(strInFieldBox, x1, y1, -1);
                break;
            case BOOLEAN:

                int valueButtonBooleanColor = new Color(0, 136, 255).getRGB();
                int valueBooleanOPColor = new Color(74, 74, 74).getRGB();
                int valueBooleanDisColor = new Color(25, 25, 25).getRGB();
                int BGColor = this.isOptionOpen() ? valueBooleanOPColor : valueBooleanDisColor;
                x2 = x1 + 35;
                y2 = y1 + 10;
                RenderUtil.drawRoundRect(x1, y1, x2, y2, 5, BGColor);
                RenderUtil.drawRoundRect((float) ((x1 - 7) + (35 * motion.getAnimationFactor())), (float) (y1 - 2), (float) ((x1 + 7) + (35 * motion.getAnimationFactor())), y1 + 12, 5, valueButtonBooleanColor);
                break;
            case ENUM_TYPE:

                int enumBGColor = new Color(63, 63, 63).getRGB();
                int selectedEnumColor = new Color(0, 136, 255).getRGB();
                //x2+16=x1=右边界
                y2 = y1 + 10;
                String newStr = this.getEnumTypes().toString().replace(",", " ").replace("[", "").replace("]", "");
                //x：右边界-18-字宽;x1：右边界-14
//                RenderUtil.drawRound(x2 - font.getStringWidth(newStr) - 2f, y1, x2 + 2f, y2, enumBGColor, enumBGColor);
                //x:右边界-16-字宽,color:-1
//                font.drawString(newStr, x2 - font.getStringWidth(newStr), y1, -1);
                for (int i = 0; i < subEnumValueHeaders.size(); i++) {
                    SubEnumValueHeader subEnumValueHeader = subEnumValueHeaders.get(i);
                    //坐标计算
                    if (i == 0) {
                        subEnumValueHeader.x1 = x2 - font.getStringWidth(newStr) - 2f;
                    } else {
                        SubEnumValueHeader lastSubEnumValueHeader = subEnumValueHeaders.get(i - 1);
                        subEnumValueHeader.x1 = lastSubEnumValueHeader.x2 - 4f + 2f * font.getStringWidth("  ");
                    }
                    subEnumValueHeader.y1 = y1;
                    subEnumValueHeader.x2 = subEnumValueHeader.x1 + font.getStringWidth(subEnumValueHeader.name) + 4f;
                    subEnumValueHeader.y2 = y2;
                    //绘制背景框
                    RenderUtil.drawRoundRect(subEnumValueHeader.x1, subEnumValueHeader.y1, subEnumValueHeader.x2, subEnumValueHeader.y2, 5, enumBGColor);
                }
                //初始化动画
                if (selectedEnumBGAnim == null) {
                    for (SubEnumValueHeader subEnumValueHeader : this.subEnumValueHeaders) {
                        if (this.getCurrentEnumType().equalsIgnoreCase(subEnumValueHeader.name)) {
                            this.selectedEnumBGAnim = new BoundedAnimation(subEnumValueHeader.x1, subEnumValueHeader.x1, 0, false, Easing.LINEAR);

                            this.lastSelectedSubEnumValueHeader = subEnumValueHeader;
                        }
                    }
                }

                //绘制选中enum背景框
                assert selectedEnumBGAnim != null;
                RenderUtil.drawRoundRect((float) selectedEnumBGAnim.getAnimationValue(), y1, (float) (selectedEnumBGAnim.getAnimationValue() + font.getStringWidth(this.getCurrentEnumType()) + 4f), y2, 5, selectedEnumColor);
                //绘制文字
                for (SubEnumValueHeader subEnumValueHeader : subEnumValueHeaders) {
                    font.drawString(subEnumValueHeader.name, subEnumValueHeader.x1 + 2f, y1, -1);
                }
                break;
        }
    }

    public void initSubEnumValueHeaders() {
        for (String enumType : this.getEnumTypes()) {
            subEnumValueHeaders.add(new SubEnumValueHeader(enumType, this));
        }
    }

    public void posDel() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public String getCurrentEnumType() {
        return currentEnumType;
    }

    public void setCurrentEnumType(String currentEnumType) {
        this.currentEnumType = currentEnumType;
    }

    public SubEnumValueHeader getCurrentSubEnumHeader() {
        if (this.subEnumValueHeaders.size() == 0) this.initSubEnumValueHeaders();
        for (SubEnumValueHeader subEnumValueHeader : this.subEnumValueHeaders) {
            if (subEnumValueHeader.name.equalsIgnoreCase(currentEnumType)) return subEnumValueHeader;
        }
        return this.subEnumValueHeaders.get(0);
    }

    public double getIncValue() {
        return incValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getDoubleCurrentValue() {
        return doubleCurrentValue;
    }

    public void setDoubleCurrentValue(double settingValue) {
        this.doubleCurrentValue = settingValue;
    }

    public String getValueName() {
        return valueName;
    }

    public boolean isOptionOpen() {
        return optionOpen;
    }

    public void setOptionOpen(boolean optionOpen) {
        this.optionOpen = optionOpen;
        motion.setState(optionOpen);
    }

    public double[] getDoubles() {
        return doubles;
    }

    public void setDoubles(double[] doubles) {
        this.doubles = doubles;
    }

    public ArrayList<String> getEnumTypes() {
        return enumTypes;
    }

    public void setEnumTypes(ArrayList<String> enumTypes) {
        this.enumTypes = enumTypes;
    }

    public Color getColorValue() {
        return colorValue;
    }

    public void setColorValue(Color colorValue) {
        this.colorValue = colorValue;
    }    public void setConfigColorValue(int colorValue) {
        this.colorValue = new Color(colorValue);
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public int getAnim(ValueType valueType) {
        if (valueType == ValueType.BOOLEAN) return this.bAnim;
        if (valueType == ValueType.DOUBLE) return this.dAnim;
        if (valueType == ValueType.ENUM_TYPE) return this.eAnim;
        return 1134;
    }

    public void setAnim(ValueType valueType, int var) {
        if (valueType == ValueType.BOOLEAN) this.bAnim = var;
        if (valueType == ValueType.DOUBLE) this.dAnim = var;
        if (valueType == ValueType.ENUM_TYPE) this.eAnim = var;

    }

    public enum ValueType {
        DOUBLE, ENUM_TYPE, COLOR, STRING, BOOLEAN
    }
}
