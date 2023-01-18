/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230107
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.api.event.EventRender2D;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.render.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;

public class HUD extends ModuleHeader {

    ValueHeader info = new ValueHeader("information", true);
    ValueHeader malist = new ValueHeader("ArrayList", true);


    public HUD() {
        super("HUD", true, ModuleType.RENDER);
        this.addValueList(info, malist);
        this.setKey(Keyboard.KEY_H);
    }

    @EventTarget
    public void onEvent2D(EventRender2D render2D) {
        int w = RenderUtil.width();
        int h = RenderUtil.height();
        double[] pos = sense.getPlayerPos();
        CFontRenderer font = sense.getFontBuffer().EN16;
        CFontRenderer arrayFont = sense.getFontBuffer().EN24;
        //ArrayList
        if ((Boolean) this.getValue(malist)) {
            ArrayList<ModuleHeader> sortedList = new ArrayList<>();
            for (ModuleHeader moduleHeader : sense.getModuleManager().mList)
                if (moduleHeader.isCanView()) sortedList.add(moduleHeader);


            sortedList.sort(Comparator.comparingInt(module -> -arrayFont.getStringWidth(module.getModuleDisplay())));
            int yAxis = 5;
            for (ModuleHeader moduleHeader : sortedList) {
                arrayFont.drawStringWithShadow(moduleHeader.getModuleDisplay(), w - moduleHeader.getArrayWidth().getAnimationFactor() * (arrayFont.getStringWidth(moduleHeader.getModuleDisplay()) + 5), yAxis, -1);
                yAxis += arrayFont.getHeight(false) * moduleHeader.getArrayWidth().getAnimationFactor();
            }
        }

        //Information
        if ((Boolean) this.getValue(info)) {
            font.drawString(sense.getClientName() + " " + sense.getClientUpdate() + " " + sense.skrilledSense(), 0, h - font.FONT_HEIGHT, -1);
            font.drawString(sense.getPlayerName() + " X:" + (int) pos[0] + " Y:" + (int) pos[1] + " Z:" + (int) pos[2] + " FPS:" + sense.getClientFPS(), 0, h - font.FONT_HEIGHT * 2, -1);
        }

    }
}
