/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230206
 */
package me.skrilled.ui.menu.ui;

import me.ashyx.blur.util.Blur;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class KeyBindingGui extends GuiScreen {
    ModuleHeader module;

    public KeyBindingGui(ModuleHeader module) {
        this.module = module;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Blur.renderBlur(20);
        RenderUtil.drawRect(0, 0, width, height, new Color(0, 0, 0, 100).getRGB());
        Main.fontLoader.EN36.drawCenteredString("Press any key to bind the module shortcut key, and type Delete to delete the current module shortcut key", width / 2f, height / 2f, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_DELETE) {
            module.setKey(0);
            module.setOnBinding(false);
            mc.displayGuiScreen(SettingMenu.secilyUI);
        } else {
            module.setKey(keyCode);
            module.setOnBinding(false);
            mc.displayGuiScreen(SettingMenu.secilyUI);
        }
        super.keyTyped(typedChar, keyCode);
    }

}
