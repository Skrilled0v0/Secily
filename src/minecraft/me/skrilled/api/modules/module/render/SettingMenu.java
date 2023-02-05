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
import me.skrilled.ui.menu.ui.EclipseMenu;
import me.skrilled.ui.menu.MenuMotion;
import me.skrilled.ui.menu.ui.SecilyUserInterface;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@ModuleInitialize(name = "SettingMenu", type = ModuleType.RENDER, key = Keyboard.KEY_RSHIFT)
public class SettingMenu extends ModuleHeader {
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
    public static SecilyUserInterface secilyUI = new SecilyUserInterface();

    public SettingMenu() {
        this.setCanView(false);
        this.addEnumTypes(cmode, "Dark", "White");
        this.addEnumTypes(mode, "Eclipse", "Secily");
    }

    public static float[] getGuiPos() {
        return new float[]{(float) posX1.getDoubleCurrentValue(), (float) posY1.getDoubleCurrentValue(), (float) posX2.getDoubleCurrentValue(), (float) posY2.getDoubleCurrentValue()};
    }

    @Override
    public void onEnabled() {
        if (sideMode.getCurrentEnumType().equalsIgnoreCase("Eclipse") && mc.theWorld != null) {
            mc.displayGuiScreen(new EclipseMenu());
            this.toggle();
        } else MenuMotion.setMenuMotion();
        super.onEnabled();
    }


    @Override
    public void onDisabled() {
        if (!sideMode.getCurrentEnumType().equalsIgnoreCase("Eclipse") && mc.theWorld != null)
            MenuMotion.setMenuMotion();
        SenseHeader.getSense.configManager.saveAll();
        super.onDisabled();
    }
}
