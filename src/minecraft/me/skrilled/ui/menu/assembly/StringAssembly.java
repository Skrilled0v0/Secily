/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.ui.menu.ui.KeyBindingGui;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class StringAssembly extends Assembly implements IMC {
    String value;
    boolean[] centered;
    Color bgColor;
    Color onSelectedBGColor;
    Color fontColor;
    boolean border;
    FontDrawer font;
    float radius;

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean[] centered, Color bgColor, Color fontColor, FontDrawer font, float radius) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.font = font;
        this.radius = radius;
    }

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean[] centered, Color bgColor, Color onSelectedBGColor, Color fontColor, FontDrawer font, float radius) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.onSelectedBGColor = onSelectedBGColor;
        this.fontColor = fontColor;
        this.font = font;
        this.radius = radius;
    }

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean[] centered, Color bgColor, Color fontColor, boolean border, FontDrawer font) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.border = border;
        this.font = font;
    }

    public static void drawRoundRect(float startX, float startY, float width, float height, float radius, int rgba) {
        float z;
        if (startX > width) {
            z = startX;
            startX = width;
            width = z;
        }

        if (startY > height) {
            z = startY;
            startY = height;
            height = z;
        }
        double x1 = startX + radius;
        double y1 = startY + radius;
        double x2 = width - radius;
        double y2 = height - radius;
        Color color = new Color(rgba, true);
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        glPushMatrix();
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(2848);

        glBegin(GL_POLYGON);
        glColor4f(r, g, b, a);
        double degree = Math.PI / 180;
        for (double i = 0; i <= 90; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        }

        for (double i = 90; i <= 180; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        }

        for (double i = 180; i <= 270; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        }

        for (double i = 270; i <= 360; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        }

        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    @Override
    public float draw() {
        return RenderUtil.drawCenteredStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB(), radius, centered);
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        if (assemblyName.startsWith("bindAssembly")) {
            String moduleName = assemblyName.split("\\.")[1];
            ModuleHeader module = ModuleManager.getModuleByName(moduleName);
            mc.displayGuiScreen(new KeyBindingGui(module));
        } else if (assemblyName.startsWith("renderedInArrayListAssembly")) {
            String moduleName = assemblyName.split("\\.")[1];
            ModuleHeader module = ModuleManager.getModuleByName(moduleName);
            module.setCanView(this.value.equals("K"));
            if (this.value.equals("K")) {
                value = "J";
            } else {
                value = "K";
            }
        }
    }
}
