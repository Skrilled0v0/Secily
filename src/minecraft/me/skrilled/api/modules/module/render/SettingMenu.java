/*
 *Client
 *Code by SkrilledSense
 *20230107
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.ui.clickui.Menu;
import me.skrilled.ui.clickui.MenuMotion;
import org.lwjgl.input.Keyboard;

public class SettingMenu extends ModuleHeader {
    public static Menu menu = new Menu();

    public SettingMenu() {
        super("SettingMenu", false, ModuleType.RENDER);
        this.setKey(Keyboard.KEY_RSHIFT);
        this.setCanView(false);
    }


    @Override
    public void onOpen() {
        MenuMotion.setMenuMotion();
        super.onOpen();
    }


    @Override
    public void isNotOpen() {
        MenuMotion.setMenuMotion();
        super.isNotOpen();
    }
}
