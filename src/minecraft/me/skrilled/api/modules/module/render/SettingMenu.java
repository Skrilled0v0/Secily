/*
 *Client
 *Code by SkrilledSense
 *20230107
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.ui.clickui.Menu;
import org.lwjgl.input.Keyboard;

public class SettingMenu extends ModuleHeader {
    Menu menu = new Menu();

    public SettingMenu() {
        super("SettingMenu", false, ModuleType.RENDER);
        this.setKey(Keyboard.KEY_RSHIFT);
        this.setCanView(false);
    }


    @Override
    public void onOpen() {
        mc.displayGuiScreen(menu);
        this.toggle();
        super.onOpen();
    }
}
