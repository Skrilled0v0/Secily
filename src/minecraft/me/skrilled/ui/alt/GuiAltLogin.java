package me.skrilled.ui.alt;


import me.fontloader.FontDrawer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.main.Main;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiAltLogin extends GuiScreen {
    private final GuiScreen previousScreen;
    private GuiTextField password;
    private GuiTextField username;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                AltLoginThread thread = new AltLoginThread(this.username.getText(), this.password.getText());
                thread.start();
            }
        }
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        FontDrawer font = Main.fontLoader.EN18;
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        font.drawCenteredString("Alt Login", width / 2f, 20, -1);
        if (this.username.getText().isEmpty()) {
            font.drawStringWithShadow("Username / E-Mail", width / 2f - 96, 66.0f, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            font.drawStringWithShadow("Password", width / 2f - 96, 106.0f, -7829368);
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.username = new GuiTextField(1, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        this.username.setMaxStringLength(200);
        this.password.setMaxStringLength(200);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}
