/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230201
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;

@ModuleInitialize(name = "WorldRenderEditor",type = ModuleType.RENDER)
public class WorldRenderEditor extends ModuleHeader {

    public static ValueHeader rain=new ValueHeader("Rain",true);

    public static ValueHeader snow=new ValueHeader("Snow",true);
    public static ValueHeader timeEditor=new ValueHeader("TimeModifier",true);
//
//    public static ValueHeader time=new ValueHeader("Time",)




}
