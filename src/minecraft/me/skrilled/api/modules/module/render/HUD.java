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

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

@ModuleInitialize(name = "HUD", type = ModuleType.RENDER, key = Keyboard.KEY_H)
public class HUD extends ModuleHeader {
    ArrayList<String> didis = new ArrayList<>();
    ValueHeader info = new ValueHeader("information", true);
    ValueHeader malist = new ValueHeader("ArrayList", true);
    ValueHeader not = new ValueHeader("Notifications", true);
    ValueHeader didi = new ValueHeader("drawDIDI", true);
    ValueHeader diType = new ValueHeader("didiType", "Red", didis);


    public HUD() {
        addEnumTypes(didis, "Red", "Blue", "Green", "Tellow", "GRAY", "ORANGE", ")V)");
    }

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
        //drawDIDI
        if ((Boolean) getValue(didi)) {
            RenderUtil.drawSikadi(w / 2f - 300, h / 2f, diType.getCurrentEnumType().equalsIgnoreCase("Red"));
        }

        //Test
        float[] cAPos = new float[]{100,100,200,213};
        ColorAssembly colorAssembly = new ColorAssembly(cAPos, null, 360, 1f, 1f, 1f);
        colorAssembly.draw();

        //Information
        if ((Boolean) this.getValue(info)) {
            font.drawStringWithOutline(SenseHeader.getSense.getClientName() + " LastUpdate:" + SenseHeader.getSense.getClientUpdate(), 0, h - font.getHeight(), -1);
            font.drawStringWithOutline(SenseHeader.getSense.getPlayerName() + " X:" + (int) pos[0] + " Y:" + (int) pos[1] + " Z:" + (int) pos[2] + " FPS:" + SenseHeader.getSense.getClientFPS(), 0, h - font.getHeight() * 2, -1);
        }
        if (not.isOptionOpen()) Notification.drawNotifications();
    }
}
