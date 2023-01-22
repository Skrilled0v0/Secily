/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230122
 */
package me.skrilled.api.commands;

import me.skrilled.utils.IMC;

public class CommandHeader implements IMC {
    String cmdName;
    String[] extraName;
    boolean isModuleValueCmd;

    public CommandHeader() {
    }

    public CommandHeader(String cmdName, String[] extraName, boolean isModuleValueCmd) {
        this.cmdName = cmdName;
        this.extraName = extraName;
        this.isModuleValueCmd = isModuleValueCmd;
    }


    public String getCmdName() {
        return cmdName;
    }


    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }


    public String[] getExtraName() {
        return extraName;
    }


    public void setExtraName(String[] extraName) {
        this.extraName = extraName;
    }

    public boolean isIsModuleValueCmd() {
        return isModuleValueCmd;
    }

    public void executed(String commandBody) {

    }
}
