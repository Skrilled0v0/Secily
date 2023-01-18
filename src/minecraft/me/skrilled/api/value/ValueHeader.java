/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.value;

import me.skrilled.ui.clickui.value.BooleanSetting;

import java.awt.*;
import java.util.ArrayList;

public class ValueHeader {
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
        BooleanSetting.motion.setState(optionOpen);
        this.optionOpen = optionOpen;
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
