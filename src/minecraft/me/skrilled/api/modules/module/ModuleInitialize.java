/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230120
 */
package me.skrilled.api.modules.module;

import me.skrilled.api.modules.ModuleType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInitialize {
    //Module名字
    String name();

    //infoType
    ModuleType type();

    //按键
    int key() default 0;


}
