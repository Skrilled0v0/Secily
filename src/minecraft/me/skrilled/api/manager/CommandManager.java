/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230122
 */
package me.skrilled.api.manager;

import me.skrilled.SenseHeader;
import me.skrilled.api.commands.CommandBind;
import me.skrilled.api.commands.CommandHeader;
import me.skrilled.api.commands.CommandSetPrefix;
import me.skrilled.utils.IMC;

import java.util.ArrayList;

public class CommandManager implements IMC {
    public static ArrayList<CommandHeader> cmdList = new ArrayList<>();

    public static void loadCommands() {
        cmdList.add(new CommandBind());
        cmdList.add(new CommandSetPrefix());
    }

    public static void sendClientCommand(String cmd) {
        boolean ok = true;
        for (CommandHeader commandHeader : cmdList) {
            for (int i = 0; i < commandHeader.getExtraName().length; i++)
                if (commandHeader.getExtraName()[i].equals(cmd.substring(1).split(" ")[0])) {
                    commandHeader.executed(cmd.substring(1));
                    ok = true;
                    break;
                } else {
                    ok = false;
                }
            if (!ok) SenseHeader.getSense.printINFO("Unknown command,Please enter §a.help§r to get help commands :D");
        }
    }

}
