/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.value;


import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.SenseHeader;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;

import java.awt.*;
import java.util.ArrayList;

public class ValueHeader {
    public int x1 = 0;
    public int y1 = 0;
    public int x2 = 0;
    public int y2 = 0;
    public boolean visible = true;
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
                y2 = y1 + 10;
                String newStr = this.getEnumTypes().toString().replace(",", " ").replace("[", "").replace("]", "");
                RenderUtil.drawRound(x2 - font.getStringWidth(newStr) - 2f, y1, x2 + 2f, y2, enumBGColor, enumBGColor);
                font.drawString(newStr, x2 - font.getStringWidth(newStr), y1, -1);
                break;


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
