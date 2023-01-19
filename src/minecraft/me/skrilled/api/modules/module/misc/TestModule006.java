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

public class TestModule006 extends ModuleHeader {
    private final ValueHeader testBoolean = new ValueHeader("TestBoolean", true);
    private final ArrayList<String> typeList = new ArrayList<>();
    private final ValueHeader testType = new ValueHeader("TestType", "Test3", typeList);
    private final double[] value = {0.1, 0.7, 5.0, 0.1};
    private final ValueHeader testDouble = new ValueHeader("TestDouble", value);
    private final String testStr = "";
    private final Color testC = new Color(0);
    private final ValueHeader testString = new ValueHeader("TestString", testStr);
    private final ValueHeader testColor = new ValueHeader("TestColor", testC);

    public TestModule006() {
        super("TestModule006", true, ModuleType.MISC);
        this.addEnumTypes(typeList, "Test112", "Test2222", "Test3");
        this.addValueList(testType, testBoolean, testDouble, testString, testColor);
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
