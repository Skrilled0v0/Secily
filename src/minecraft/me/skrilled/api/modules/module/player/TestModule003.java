/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230202
 */
package me.skrilled.api.modules.module.player;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;

import java.awt.*;
import java.util.ArrayList;

@ModuleInitialize(name="TestModule003",type = ModuleType.PLAYER)
public class TestModule003 extends ModuleHeader {
    private final ValueHeader testBoolean = new ValueHeader("TestBoolean", true);
    private final ArrayList<String> typeList = new ArrayList<>();
    private final ValueHeader testType = new ValueHeader("TestType", "Test1", typeList);
    private final double[] value = {0.1, 0.7, 5.0, 0.1};
    private final ValueHeader testDouble = new ValueHeader("TestDouble", value);
    private final String testStr = "1122339";
    private final Color testC = new Color(0);
    private final ValueHeader testString = new ValueHeader("TestString", testStr);
    private final ValueHeader testColor = new ValueHeader("TestColor", testC);
}
