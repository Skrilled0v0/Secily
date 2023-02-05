/*
 *Client
 *Code by SkrilledSense
 *20230107
 */
package me.skrilled.api.modules.module.render;

import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.clickui.EclipseMenu;
import me.skrilled.ui.clickui.MenuMotion;
import me.skrilled.ui.clickui.SecilyMenu;
import me.skrilled.ui.menu.ui.SecilyUserInterface;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@ModuleInitialize(name = "SettingMenu", type = ModuleType.RENDER, key = Keyboard.KEY_RSHIFT)
public class SettingMenu extends ModuleHeader {
    public static SecilyMenu menu = new SecilyMenu();
    static ArrayList<String> cmode = new ArrayList<>();
    public static ValueHeader colorMode = new ValueHeader("EclipseColorMode", "Dark", cmode);
    static double[] x2 = {0.0, 659.5, 199999, 0.5f};
    public static ValueHeader posX2 = new ValueHeader("X2", x2);
    static double[] y2 = {0.0, 344, 199999, 0.5f};
    public static ValueHeader posY2 = new ValueHeader("Y2", y2);
    static double[] x1 = {0.0, 50, 199999, 0.5f};
    public static ValueHeader posX1 = new ValueHeader("X1", x1);
    static double[] y1 = {0.0, 50, 199999, 0.5f};
    public static ValueHeader posY1 = new ValueHeader("Y1", y1);
    ArrayList<String> mode = new ArrayList<>();
    ValueHeader sideMode = new ValueHeader("Mode", "Secily", mode);

    public SettingMenu() {
        this.setCanView(false);
        this.addEnumTypes(cmode, "Dark", "White");
        this.addEnumTypes(mode, "AbandonedSecily", "Eclipse", "Secily");
    }

    public static float[] getGuiPos() {
        return new float[]{(float) posX1.getDoubleCurrentValue(), (float) posY1.getDoubleCurrentValue(), (float) posX2.getDoubleCurrentValue(), (float) posY2.getDoubleCurrentValue()};
    }

    @Override
    public void onEnabled() {
        SenseHeader.getSense.configManager.saveAll();
        if (sideMode.getCurrentEnumType().equalsIgnoreCase("Eclipse")) {
            mc.displayGuiScreen(new EclipseMenu());
            this.toggle();
        }
        if (sideMode.getCurrentEnumType().equalsIgnoreCase("Test")) {
            mc.displayGuiScreen(new SecilyUserInterface());
            this.toggle();
        }
        MenuMotion.setMenuMotion();
        super.onEnabled();
    }


    @Override
    public void onDisabled() {
        SenseHeader.getSense.configManager.saveAll();
        if (!sideMode.getCurrentEnumType().equalsIgnoreCase("Eclipse")) MenuMotion.setMenuMotion();
        super.onDisabled();
    }
}
