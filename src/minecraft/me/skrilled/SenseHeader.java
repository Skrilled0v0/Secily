/*
 *eclipse
 *Code by SkrilledSense
 *20230103
 */
package me.skrilled;

import me.cubex2.ttfr.FontBuffer;
import me.skrilled.api.manager.FileManager;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.IMC;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.util.List;

public class SenseHeader implements IMC {
    public static String[] clientInfo = {"SkrilledSense", "230117", "Genshin Impact"};
    public static SenseHeader getSense = new SenseHeader();
    public FontBuffer fontBuffer;
    public ModuleManager moduleManager;
    public String getClientUpdate() {
        return clientInfo[1];
    }

    public String getClientName() {
        return clientInfo[2];
    }

    public String skrilledSense() {
        return clientInfo[0];
    }

    public FontBuffer getFontBuffer() {
        return fontBuffer;
    }

    public void clientStart() {
        Display.setTitle(getClientName());
        FileManager.init();
        moduleManager = new ModuleManager();
        moduleManager.load();
        readSettings();
        fontBuffer = new FontBuffer();

    }

    public String getPlayerName() {
        return mc.thePlayer.getName();
    }

    public double[] getPlayerPos() {
        return new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};
    }

    public int getClientFPS() {
        return Minecraft.getDebugFPS();
    }

    //输出信息
    public void printINFO(String str) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("\u00a77[\u00a7c" + getClientName() + "\u00a77]\u00a7r " + str));

    }

    public void printINFO(double str) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("\u00a77[\u00a7c" + getClientName() + "\u00a77]\u00a7r " + str));

    }

    public void printINFO(int str) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("\u00a77[\u00a7c" + getClientName() + "\u00a77]\u00a7r " + str));

    }

    public void printINFO(float str) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("\u00a77[\u00a7c" + getClientName() + "\u00a77]\u00a7r " + str));

    }

    public void printINFO(Object str) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("\u00a77[\u00a7c" + getClientName() + "\u00a77]\u00a7r " + str));

    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    //存档
    public void saveSettings() {
        StringBuilder values = new StringBuilder();
        for (ModuleHeader m : this.getModuleManager().mList) {
            values.append(String.format("M-%s %s %s%s", m.getModuleName(), m.getKey(), m.isIsOpen(), System.lineSeparator()));
            for (ValueHeader v : m.getValueList()) {
                values.append(String.format("V-%s %s %s%s", m.getModuleName(), v.getValueName(), m.getValue(v), System.lineSeparator()));
            }
        }
        FileManager.save("Config.txt", values.toString());
    }

    //读档
    public void readSettings() {
        try {
            List<String> cfg = FileManager.read("Config.txt");
            String moduleName, moduleKey, moduleIsOpen;
            String valueName, values;
            for (String s : cfg) {
                if (s.startsWith("M-")) {
                    moduleName = s.split(" ")[0].substring(2);
                    moduleKey = s.split(" ")[1];
                    moduleIsOpen = s.split(" ")[2];
                    ModuleHeader module = this.getModuleManager().getModuleByName(moduleName);
                    module.setKey(Integer.parseInt(moduleKey));
                    module.setIsOpenWithOutNotification(Boolean.parseBoolean(moduleIsOpen));
                }
                if (s.startsWith("V-")) {
                    moduleName = s.split(" ")[0].substring(2);
                    valueName = s.split(" ")[1];
                    values = s.split(" ")[2];
                    ModuleHeader module = this.getModuleManager().getModuleByName(moduleName);
                    for (ValueHeader value : module.getValueList()) {
                        if (valueName.equals(value.getValueName())) {
                            if (value.getValueType() == ValueHeader.ValueType.BOOLEAN)
                                value.setOptionOpen(Boolean.parseBoolean(values));
                            if (value.getValueType() == ValueHeader.ValueType.DOUBLE)
                                value.setSettingValue(Double.parseDouble(values));
                            if (value.getValueType() == ValueHeader.ValueType.ENUM_TYPE)
                                value.setCurrentEnumType(values);
                            if (value.getValueType() == ValueHeader.ValueType.COLOR)
                                value.setColorValue(Color.decode(values));
                            if (value.getValueType() == ValueHeader.ValueType.STRING) value.setStrValue(values);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭
    public void stopClient() {
        saveSettings();
        mc.shutdown();
    }

}
