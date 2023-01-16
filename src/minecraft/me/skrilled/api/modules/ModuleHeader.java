
/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.modules;

import com.darkmagician6.eventapi.EventManager;
import me.skrilled.SenseHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.IMC;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleHeader implements IMC {
    public float[] modulePosInfo = new float[]{0, 0, 0, 0};
    public boolean menuFlag;
    String moduleName;
    String suffix = "";
    boolean isOpen;
    boolean canView = true;
    ArrayList<ValueHeader> valueList;
    int key;
    ModuleType moduleType;
    Animation arrayWidth = new Animation(1000f, false, Easing.BACK_OUT);

    public ModuleHeader(String moduleName, boolean isOpen, ModuleType moduleType) {
        this.valueList = new ArrayList<>();
        this.moduleName = moduleName;
        this.isOpen = isOpen;
        this.moduleType = moduleType;
    }

    public List<ValueHeader> getValueListByValueType(ValueHeader.ValueType valueType) {

        return valueList.stream().filter(value -> value.getValueType() == valueType).collect(Collectors.toList());

    }

    public Object getValue(ValueHeader valueHeader) {
        if (valueHeader.getValueType() == ValueHeader.ValueType.BOOLEAN) return valueHeader.isOptionOpen();
        else if (valueHeader.getValueType() == ValueHeader.ValueType.ENUM_TYPE) {
            return valueHeader.getCurrentEnumType();
        } else if (valueHeader.getValueType() == ValueHeader.ValueType.DOUBLE) {
            return valueHeader.getDoubles()[1];
        } else if (valueHeader.getValueType() == ValueHeader.ValueType.COLOR) {
            return valueHeader.getColorValue();
        } else if (valueHeader.getValueType() == ValueHeader.ValueType.STRING) {
            return valueHeader.getStrValue();
        } else return "getERROR";
    }

    public void addValueList(ValueHeader... valueHeader) {
        this.valueList.addAll(Arrays.asList(valueHeader));
    }

    public void addEnumTypes(ArrayList<String> enumTypes, String... values) {
        enumTypes.addAll(Arrays.asList(values));
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModuleDisplay() {
        if (!suffix.isEmpty()) return moduleName + " " + suffix;
        else return moduleName;
    }

    public boolean isIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
        arrayWidth.setState(isOpen);
        if (isOpen) this.onOpen();
        else this.isNotOpen();
        if (!moduleName.equals("SettingMenu"))
            SenseHeader.getSense.printINFO(getModuleName() + (this.isOpen ? " Was Open!" : " Was not Open!"));
    }


    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = EnumChatFormatting.GRAY + suffix + EnumChatFormatting.RESET;
    }

    public void toggle() {
        setIsOpen(!isOpen);
    }

    public Animation getArrayWidth() {
        return arrayWidth;
    }

    public ArrayList<ValueHeader> getValueList() {
        return valueList;
    }


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }


    public void onOpen() {
        EventManager.register(this);
    }

    public void isNotOpen() {
        EventManager.unregister(this);
    }

    public enum ModuleType {
        COMBAT, MISC, MOVE, PLAYER, RENDER
    }

}
