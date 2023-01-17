/*
 *Client
 *Code by SkrilledSense
 *20230107
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.clickui.Menu;
import me.skrilled.ui.clickui.MenuMotion;
import me.skrilled.ui.clickui.SkrilledGui;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class SettingMenu extends ModuleHeader {
    public static Menu menu = new Menu();
    static ArrayList<String> cmode = new ArrayList<>();
    public static ValueHeader colorMode = new ValueHeader("ColorMode", "Dark", cmode);

    ArrayList<String> mode = new ArrayList<>();
    ValueHeader sideMode = new ValueHeader("Mode", "Eclipse", mode);

    public SettingMenu() {
        super("SettingMenu", false, ModuleType.RENDER);
        this.setKey(Keyboard.KEY_RSHIFT);
        this.setCanView(false);
        this.addEnumTypes(cmode, "Dark", "White");
        this.addEnumTypes(mode, "Secily", "Eclipse");
        this.addValueList(colorMode, sideMode);
    }


    @Override
    public void onOpen() {
        if (sideMode.getCurrentEnumType().equalsIgnoreCase("Eclipse")) {
            mc.displayGuiScreen(new SkrilledGui());
            this.toggle();
        }
        MenuMotion.setMenuMotion();
        super.onOpen();
    }


    @Override
    public void isNotOpen() {
        if (!sideMode.getCurrentEnumType().equalsIgnoreCase("Eclipse")) MenuMotion.setMenuMotion();
        super.isNotOpen();
    }
}
