/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230107
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.api.event.EventRender2D;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.Notification;
import me.skrilled.ui.menu.assembly.ColorAssembly;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.main.Main;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;

@ModuleInitialize(name = "HUD", type = ModuleType.RENDER, key = Keyboard.KEY_H)
public class HUD extends ModuleHeader {

    ValueHeader info = new ValueHeader("information", true);
    ValueHeader malist = new ValueHeader("ArrayList", true);
    ValueHeader not = new ValueHeader("Notifications", true);


    @EventTarget
    public void onEvent2D(EventRender2D render2D) {

        int w = RenderUtil.width();
        int h = RenderUtil.height();
        double[] pos = SenseHeader.getSense.getPlayerPos();
        FontDrawer font = Main.fontLoader.EN16;
        FontDrawer arrayFont = Main.fontLoader.EN24;
        //ArrayList
        if ((Boolean) this.getValue(malist)) {
            ArrayList<ModuleHeader> sortedList = new ArrayList<>();
            for (ModuleHeader moduleHeader : ModuleManager.mList)
                if (moduleHeader.isCanView()) sortedList.add(moduleHeader);
            sortedList.sort(Comparator.comparingInt(module -> -arrayFont.getStringWidth(module.getModuleDisplay())));
            int yAxis = 5;
            for (ModuleHeader moduleHeader : sortedList) {
                arrayFont.drawStringWithShadow(moduleHeader.getModuleDisplay(), w - moduleHeader.getArrayWidth().getAnimationFactor() * (arrayFont.getStringWidth(moduleHeader.getModuleDisplay()) + 5), yAxis, -1);
                yAxis += arrayFont.getHeight() * moduleHeader.getArrayWidth().getAnimationFactor();
            }
        }
        RenderUtil.drawPoint(50, 50, -1);
        ColorAssembly colorAssembly = new ColorAssembly(new float[]{w / 2f, h / 2f, w / 2f + 100, h / 2f + 100f}, null);
        colorAssembly.draw();
//        RenderUtil.drawBooleanButtton(w / 2f, h / 2f, 45, 15, 0f, Color.DARK_GRAY.getRGB(), Color.BLUE.getRGB(), Color.GRAY.getRGB());
        //Information
        if ((Boolean) this.getValue(info)) {
            font.drawString(SenseHeader.getSense.getClientName() + " LastUpdate:" + SenseHeader.getSense.getClientUpdate(), 0, h - font.getHeight(), -1);
            font.drawString(SenseHeader.getSense.getPlayerName() + " X:" + (int) pos[0] + " Y:" + (int) pos[1] + " Z:" + (int) pos[2] + " FPS:" + SenseHeader.getSense.getClientFPS(), 0, h - font.getHeight() * 2, -1);
        }
        if (not.isOptionOpen()) Notification.drawNotifications();
    }
}
