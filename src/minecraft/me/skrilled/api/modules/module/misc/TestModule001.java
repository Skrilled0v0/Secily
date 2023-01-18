/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230115
 */
package me.skrilled.api.modules.module.misc;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;

import java.awt.*;
import java.util.ArrayList;

public class TestModule001 extends ModuleHeader {
    private final ValueHeader testBoolean = new ValueHeader("TesdastBoolean", true);
    private final ValueHeader test1Boolean = new ValueHeader("Test2Boodsalean", true);
    private final ValueHeader test2Boolean = new ValueHeader("TesadtBo3olean", true);
    private final ValueHeader test3Boolean = new ValueHeader("TestBo4olean", true);
    private final ValueHeader tes4tBoolean = new ValueHeader("TestB5oolean", true);
    private final ValueHeader testB5oolean = new ValueHeader("Tes6tBoolean", true);
    private final ValueHeader test6Boolean = new ValueHeader("TestBdoolean", true);
    private final ValueHeader te7tBoolean = new ValueHeader("TestdBoolean", true);
    private final ValueHeader test8Boolean = new ValueHeader("TestBsaoolean", true);
    private final ArrayList<String> typeList = new ArrayList<>();
    private final ValueHeader testType = new ValueHeader("TestType", "Skrilled", typeList);
    private final double[] value = {0.1, 0.7, 5.0, 0.1};
    private final ValueHeader testDouble = new ValueHeader("TestDouble", value);
    private final String testStr = "";
    private final Color testC = new Color(0);
    private final ValueHeader testString = new ValueHeader("TestString", testStr);
    private final ValueHeader testColor = new ValueHeader("TestColor", testC);

    public TestModule001() {
        super("TestModule001", true, ModuleType.MISC);
        this.addEnumTypes(typeList, "Test1", "Test2", "Test3");
        this.addValueList(testType, testBoolean, testDouble, testString, testColor, test1Boolean, test2Boolean, test3Boolean, tes4tBoolean, tes4tBoolean, testB5oolean, test6Boolean, te7tBoolean, test8Boolean);
    }

    @Override
    public void onOpen() {
        for (ValueHeader valueHeader : this.getValueList()) {
            sense.printINFO("TestModule is Open!  ->");
            sense.printINFO(this.getValue(valueHeader));
        }
        super.onOpen();
    }
}
