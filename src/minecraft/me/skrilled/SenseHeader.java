/*
 *eclipse
 *Code by SkrilledSense
 *20230103
 */
package me.skrilled;

import me.skrilled.api.manager.CommandManager;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.ui.menu.ui.SecilyUserInterface;
import me.skrilled.utils.IMC;
import me.skrilled.utils.config.ConfigManager;
import me.skrilled.utils.friend.FriendManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SenseHeader implements IMC {
    public static String[] clientInfo = {"SecilyTeam", "230122", "Secily"};
    public static SenseHeader getSense = new SenseHeader();
    public final Path directory = Paths.get(mc.mcDataDir.getAbsolutePath(), getClientName());
    public ModuleManager moduleManager;
    public FriendManager friendManager;
    public ConfigManager configManager;

    public String getClientUpdate() {
        return clientInfo[1];
    }

    public String getClientName() {
        return clientInfo[2];
    }

    public String getAuthor() {
        return clientInfo[0];
    }

    public void clientStart() {
        configManager = new ConfigManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        moduleManager.load();
        configManager.loadAll();
        SettingMenu.secilyUI = new SecilyUserInterface();
        Display.setTitle(getClientName());
        CommandManager.loadCommands();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
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
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(getClientPrefix() + str));

    }

    public void printINFO(int str) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(getClientPrefix() + str));

    }


    public void printINFO(Object str) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(getClientPrefix() + str));

    }

    public String getClientPrefix() {
        return "\u00A76[\u00A7d" + clientInfo[2] + "\u00A76]\u00A7r";
    }

    public void setClientPrefix(String str) {
        clientInfo[2] = str;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }


    public void stopClient() {
        configManager.saveAll();
        mc.shutdown();
    }

    public Path getDirectory() {
        return directory;
    }
}
