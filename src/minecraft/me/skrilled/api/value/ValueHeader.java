/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.value;


import me.surge.animation.Animation;
import me.surge.animation.Easing;

import java.awt.*;
import java.util.ArrayList;

public class ValueHeader {

    public boolean visible = false;
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
        setDoubleCurrentValue(doubles[1]);
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

    public void setConfigColorValue(int colorValue) {
        this.colorValue = new Color(colorValue, true);
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
