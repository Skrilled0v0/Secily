package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.ui.menu.ui.SecilyUserInterface;

import java.awt.*;

public class KeyTypeStringAssembly extends StringAssembly {
    Color bgColorIn;
    Color bgColorOut;

    public KeyTypeStringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean centered, Color bgColorOut, Color bgColorIn, Color fontColor, boolean border, FontDrawer font) {
        super(pos, fatherWindow, value, centered, bgColorOut, fontColor, border, font);
        this.bgColorIn = bgColorIn;
        this.bgColorOut = bgColorOut;
    }

    @Override
    public float draw() {
        if (SecilyUserInterface.keyTypeStringAssembly == this) {
            this.bgColor = bgColorIn;
        } else {
            this.bgColor = bgColorOut;
        }
        return super.draw();
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 14) {
            if (value.length() > 0) value = value.substring(0, value.length() - 1);
            return;
        }
        this.value += typedChar;
        String[] valueInfo = this.assemblyName.split("\\.");
        SenseHeader.getSense.getModuleManager().getModuleByName(valueInfo[0]).getValueByName(valueInfo[1]).setStrValue(value);
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        SecilyUserInterface.keyTypeStringAssembly = this;
    }
}
