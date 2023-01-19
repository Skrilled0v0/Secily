/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.value;


import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.ui.clickui.SecilyMenu;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;

import java.awt.*;
import java.util.ArrayList;

public class ValueHeader {
    public int x1 = 0;
    public int y1 = 0;
    public int x2 = 0;
    public int y2 = 0;
    public boolean visible = true;
    /**
     * 构造的valueHeader属于enum时存储子enum对象
     */
    public ArrayList<SubEnumValueHeader> subEnumValueHeaders = new ArrayList<>();
    /**
     * enum类value框的x1动画
     */
    public BoundedAnimation selectedEnumBGAnim = null;
    public SubEnumValueHeader lastSelectedSubEnumValueHeader = null;
    String valueName;
    boolean optionOpen;
    ValueType valueType;
    double[] doubles;
    double maxValue;
    double minValue;
    double settingValue;
    double incValue;
    ArrayList<String> enumTypes;
    String currentEnumType;
    Color colorValue;
    String strValue;
    int Banim = 55;
    int Eanim = 55;
    int Nanim = 55;
    Animation motion = new Animation(600f, false, Easing.LINEAR);

    public ValueHeader(String valueName, double[] doubles) {
        this.valueType = ValueType.DOUBLE;
        this.valueName = valueName;
        this.minValue = doubles[0];
        this.settingValue = doubles[1];
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
        CFontRenderer font = SenseHeader.getSense.getFontBuffer().EN12;
        switch (valueType) {
            case DOUBLE:
            case COLOR:
            case STRING:
            case BOOLEAN:
                int valueButtonBooleanColor = new Color(0, 136, 255).getRGB();
                int valueBooleanOPColor = new Color(74, 74, 74).getRGB();
                int valueBooleanDisColor = new Color(25, 25, 25).getRGB();
                int BGColor = this.isOptionOpen() ? valueBooleanOPColor : valueBooleanDisColor;
                x2 = x1 + 35;
                y2 = y1 + 10;
                RenderUtil.drawRound(x1, y1, x2, y2, BGColor, BGColor);
                RenderUtil.drawRound((float) ((x1 - 7) + (35 * motion.getAnimationFactor())), (float) (y1 - 2), (float) ((x1 + 7) + (35 * motion.getAnimationFactor())), y1 + 12, valueButtonBooleanColor, valueButtonBooleanColor);
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
                    RenderUtil.drawRound(subEnumValueHeader.x1, subEnumValueHeader.y1, subEnumValueHeader.x2, subEnumValueHeader.y2, enumBGColor, enumBGColor);
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
                RenderUtil.drawRound((float) selectedEnumBGAnim.getAnimationValue(), y1, (float) (selectedEnumBGAnim.getAnimationValue() + font.getStringWidth(this.getCurrentEnumType()) + 4f), y2, selectedEnumColor, selectedEnumColor);
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

    public void setIncValue(double incValue) {
        this.incValue = incValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(double settingValue) {
        this.settingValue = settingValue;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
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
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public int getAinm(ValueType valueType) {
        if (valueType == ValueType.BOOLEAN) return this.Banim;
        if (valueType == ValueType.DOUBLE) return this.Nanim;
        if (valueType == ValueType.ENUM_TYPE) return this.Eanim;
        return 1134;
    }

    public void setAinm(ValueType valueType, int var) {
        if (valueType == ValueType.BOOLEAN) this.Banim = var;
        if (valueType == ValueType.DOUBLE) this.Nanim = var;
        if (valueType == ValueType.ENUM_TYPE) this.Eanim = var;

    }

    public enum ValueType {
        DOUBLE, ENUM_TYPE, COLOR, STRING, BOOLEAN
    }
}
