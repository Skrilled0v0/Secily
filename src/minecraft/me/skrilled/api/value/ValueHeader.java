/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.value;

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
    ArrayList<String> enumTypes;
    String currentEnumType;
    Color colorValue;
    String strValue;


    public ValueHeader(String valueName, double[] doubles) {
        this.valueType = ValueType.DOUBLE;
        this.valueName = valueName;
        this.minValue = doubles[0];
        this.settingValue = doubles[1];
        this.maxValue = doubles[2];
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


    public enum ValueType {
        DOUBLE, ENUM_TYPE, COLOR, STRING, BOOLEAN
    }
}
