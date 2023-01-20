/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230115
 */
package me.skrilled.api.modules.module.misc;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;

import java.awt.*;
import java.util.ArrayList;

@ModuleInitialize(name = "TestModule01", type = ModuleType.MISC)
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
    private final ValueHeader testType = new ValueHeader("TestType", "Test1", typeList);
    private final double[] value = {0.1, 0.7, 5.0, 0.1};
    private final ValueHeader testDouble = new ValueHeader("TestDouble", value);
    private final String testStr = "1122339";
    private final Color testC = new Color(0);
    private final ValueHeader testString = new ValueHeader("TestString", testStr);
    private final ValueHeader testColor = new ValueHeader("TestColor", testC);

    public TestModule001() {
        this.addEnumTypes(typeList, "Test1", "Test22", "Test3333");
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
