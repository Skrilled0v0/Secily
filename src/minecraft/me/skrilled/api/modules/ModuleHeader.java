
/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.modules;

import com.darkmagician6.eventapi.EventManager;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.Notification;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleHeader implements IMC {
    public float[] modulePosInfo = new float[]{0, 0, 0, 0};
    public ColourAnimation moduleMotionColor = new ColourAnimation(new Color(74, 74, 74, 150), new Color(0, 255, 169, 150), 1000f, false, Easing.LINEAR);
    public boolean menuFlag;
    public int key, anim, clickAnim, valueWheelY = 0;
    public String moduleName;
    public boolean Enabled;
    public Animation arrayWidth = new Animation(1000f, false, Easing.BACK_OUT);
    String suffix = "";
    boolean canView = true;
    boolean onBinding = false;
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
        this.Enabled = isEnabled;
        arrayWidth.setState(isEnabled);
        if (isEnabled) this.onEnabled();
        else this.onDisabled();
        if (!moduleName.equals("SettingMenu") && mc.theWorld != null) {
            Notification.sendNotification(getModuleName() + (this.Enabled ? " Was Open!" : " Was not Open!"), 1500, (this.Enabled ? Notification.Type.SUCCESS : Notification.Type.WARNING));
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

    public void setKeyWidthGui() {
        //Key Binding提示
        if (isOnBinding()) {
            int bgColor = new Color(25, 25, 25, 200).getRGB();
            String editingStr = "Press a key to set the shortcut, or use delete to delete the shortcut";
            RenderUtil.drawRect(0, 0, RenderUtil.width(), RenderUtil.height(), bgColor);
            Main.fontLoader.EN36.drawCenteredString(editingStr, RenderUtil.width() / 2f, RenderUtil.height() / 2f, -1);
            new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        SenseHeader.getSense.printINFO("DEL|e.getKeyCode()=" + e.getKeyCode());
                        setKey(0);
                        setOnBinding(false);
                    } else {
                        SenseHeader.getSense.printINFO("BIND|e.getKeyCode()=" + e.getKeyCode());
                        setKey(e.getKeyCode());
                        setOnBinding(false);
                    }

                }

                @Override
                public void keyPressed(KeyEvent e) {

                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            };

        }
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
