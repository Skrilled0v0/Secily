/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230116
 */
package me.skrilled.ui.clickui;

import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.utils.IMC;
import me.surge.animation.Animation;
import me.surge.animation.Easing;

public class MenuMotion implements IMC {
    public static Animation MenuMotion = new Animation(800f, false, Easing.ELASTIC_IN_OUT);

    public static Animation getMenuMotion() {
        return MenuMotion;
    }

    public static void setMenuMotion() {
        if (sense.moduleManager.getModuleByClass(SettingMenu.class).isIsOpen()) {
            mc.displayGuiScreen(SettingMenu.menu);
            MenuMotion.setState(true);
        }
        if (!sense.moduleManager.getModuleByClass(SettingMenu.class).isIsOpen())
            MenuMotion.setState(false);
    }
}
