/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230122
 */
package me.skrilled.api.commands;

import me.skrilled.api.modules.ModuleHeader;
import org.lwjgl.input.Keyboard;

public class CommandBind extends CommandHeader {
    public CommandBind() {
        super("Bind", new String[]{"bind", "bd"}, false);
    }

    @Override
    public void executed(String commandBody) {
        try {
            boolean ok = false;
            if (commandBody.split(" ").length <= 2) sense.printINFO("Usage: .bind <module> <key> :)");
            String moduleName = commandBody.split(" ")[1];
            String key = "0";
            if (commandBody.split(" ")[2].length() == 1) key = commandBody.split(" ")[2].toUpperCase();
            else sense.printINFO("Usage: .bind <module> <key> :)");
            for (ModuleHeader moduleHeader : sense.moduleManager.mList) {
                if (moduleHeader.getModuleName().equalsIgnoreCase(moduleName)) {
                    moduleHeader.setKey(Keyboard.getKeyIndex(key));
                    sense.printINFO("§a" + moduleHeader.getModuleName() + "§r Bind the shortcut key to §b" + Keyboard.getKeyName(Keyboard.getKeyIndex(key)) + "§r :D");
                    ok = true;
                }
            }
            if (!ok)
                sense.printINFO("Unknown module name §c" + moduleName + "§r. Please enter §4.modules§r to view the list of modules. :)");

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.executed(commandBody);
    }
}
