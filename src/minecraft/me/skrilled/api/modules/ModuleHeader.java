
/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.modules;

import com.darkmagician6.eventapi.EventManager;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.modules.module.render.HUD;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.Notification;
import me.skrilled.utils.IMC;
import me.skrilled.utils.SoundPlayer;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.util.EnumChatFormatting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleHeader implements IMC {
    public int key, anim, clickAnim;
    public String moduleName;
    public boolean Enabled;
    public Animation arrayWidth = new Animation(1000f, false, Easing.BACK_OUT);
    String suffix = "";
    boolean canView = true;
    boolean onBinding = false;
    String toggle = "/assets/minecraft/skrilled/music/toggle/boo.wav";
    SoundPlayer toggleSound;

    ArrayList<ValueHeader> valueList;
    ModuleType moduleType;

    public ModuleHeader() {
        this.valueList = new ArrayList<>();
        this.moduleName = this.getClass().getAnnotation(ModuleInitialize.class).name();
        this.moduleType = this.getClass().getAnnotation(ModuleInitialize.class).type();
        this.key = this.getClass().getAnnotation(ModuleInitialize.class).key();

    }

    public List<ValueHeader> getValueListByValueType(ValueHeader.ValueType valueType) {

        return valueList.stream().filter(value -> value.getValueType() == valueType).collect(Collectors.toList());

    }

    public void loadValueLists() {
        try {
            for (Field declaredField : this.getClass().getDeclaredFields()) {
                if (declaredField.getType().isAssignableFrom(ValueHeader.class)) {
                    if (!declaredField.isAccessible()) declaredField.setAccessible(true);
                    this.getValueList().add((ValueHeader) declaredField.get(this));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOnBinding() {
        return onBinding;
    }

    public void setOnBinding(boolean onBinding) {
        this.onBinding = onBinding;
    }

    public Object getValue(ValueHeader valueHeader) {
        if (valueHeader.getValueType() == ValueHeader.ValueType.BOOLEAN) return valueHeader.isOptionOpen();
        else if (valueHeader.getValueType() == ValueHeader.ValueType.ENUM_TYPE) {
            return valueHeader.getCurrentEnumType();
        } else if (valueHeader.getValueType() == ValueHeader.ValueType.DOUBLE) {
            return valueHeader.getDoubleCurrentValue();
        } else if (valueHeader.getValueType() == ValueHeader.ValueType.COLOR) {
            return valueHeader.getColorValue().getRGB();
        } else if (valueHeader.getValueType() == ValueHeader.ValueType.STRING) {
            return valueHeader.getStrValue();
        } else return "getERROR";
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

    public boolean isEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean isEnabled) {
        Notification.posType posType = Notification.posType.NONE;
        if (HUD.moduleNotType.getCurrentEnumType().equalsIgnoreCase("UP")) {
            posType = Notification.posType.UP;
        } else if (HUD.moduleNotType.getCurrentEnumType().equalsIgnoreCase("LEFT")) {
            posType = Notification.posType.LEFT;
        }
        this.Enabled = isEnabled;
        arrayWidth.setState(isEnabled);
        if (isEnabled) this.onEnabled();
        else this.onDisabled();
        if (!moduleName.equals("SettingMenu") && mc.theWorld != null) {
            Notification.sendNotification(getModuleName() + (this.Enabled ? " Was Enabled!" : " Was Disabled!"), 1500, (this.Enabled ? Notification.infoType.SUCCESS : Notification.infoType.WARNING), posType);
        }
    }


    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = EnumChatFormatting.GRAY + suffix + EnumChatFormatting.RESET;
    }

    public void toggle() {
        setEnabled(!Enabled);
    }

    public Animation getArrayWidth() {
        return arrayWidth;
    }

    public ArrayList<ValueHeader> getValueList() {
        return valueList;
    }

    public ValueHeader getValueByName(String valueName) {
        for (ValueHeader valueHeader : valueList) {
            if (valueHeader.getValueName().equalsIgnoreCase(valueName)) return valueHeader;
        }
        return null;
    }

    public ValueHeader getValues(String name) {
        return valueList.stream().filter(option -> option.getValueName().equalsIgnoreCase(name)).findFirst().orElse(null);
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


    public void onEnabled() {
        EventManager.register(this);
    }

    public void onDisabled() {
        EventManager.unregister(this);
    }


    @Override
    public String toString() {
        return moduleName;
    }
}
