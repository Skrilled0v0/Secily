/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230122
 */
package me.skrilled.api.commands;

public class CommandSetPrefix extends CommandHeader {
    public CommandSetPrefix() {
        super("SetPrefix", new String[]{"prefix", "setpf", "pf"}, false);
    }

    @Override
    public void executed(String commandBody) {
        if (!commandBody.isEmpty())
            sense.setClientPrefix(commandBody);
        else
            sense.printINFO("Please enter string that match the rule. :D");
        super.executed(commandBody);
    }
}
