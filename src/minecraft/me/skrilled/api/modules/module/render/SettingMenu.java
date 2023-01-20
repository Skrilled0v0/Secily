/*
 *Client
 *Code by SkrilledSense
 *20230107
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.clickui.EclipseMenu;
import me.skrilled.ui.clickui.MenuMotion;
import me.skrilled.ui.clickui.SecilyMenu;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@ModuleInitialize(name = "SettingMenu", type = ModuleType.RENDER, key = Keyboard.KEY_RSHIFT)
public class SettingMenu extends ModuleHeader {
    public static SecilyMenu menu = new SecilyMenu();
    static ArrayList<String> cmode = new ArrayList<>();
    public static ValueHeader colorMode = new ValueHeader("ColorMode", "Dark", cmode);

    ArrayList<String> mode = new ArrayList<>();
    ValueHeader sideMode = new ValueHeader("Mode", "Secily", mode);

    public SettingMenu() {
        this.setCanView(false);
        this.addEnumTypes(cmode, "Dark", "White");
        this.addEnumTypes(mode, "Secily", "Eclipse");
    }


    @Override
    public void onOpen() {
        if (sideMode.getCurrentEnumType().equalsIgnoreCase("Eclipse")) {
            mc.displayGuiScreen(new EclipseMenu());
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
