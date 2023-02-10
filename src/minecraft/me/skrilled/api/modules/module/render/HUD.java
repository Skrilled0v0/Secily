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
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;

@ModuleInitialize(name = "HUD", type = ModuleType.RENDER, key = Keyboard.KEY_H)
public class HUD extends ModuleHeader {
    public static ValueHeader fontReplace = new ValueHeader("FontReplace", true);
    public static ValueHeader not = new ValueHeader("Notifications", true);
    static ArrayList<String> notType = new ArrayList<>();
    public static ValueHeader moduleNotType = new ValueHeader("ModuleSwitchPrompt", "LEFT", notType);
    public static ValueHeader moduleSoundType = new ValueHeader("ModuleSwitchSound", "Basic", notType);
    static ArrayList<String> soundType = new ArrayList<>();
    ArrayList<String> markType = new ArrayList<>();
    double[] size = {32, 128, 512, 0.1};
    String markStr = SenseHeader.getSense.getClientName();
    ArrayList<String> didis = new ArrayList<>();
    ValueHeader clientMark = new ValueHeader("ClientMark", "Flower", markType);
    ValueHeader info = new ValueHeader("information", true);
    ValueHeader malist = new ValueHeader("ArrayList", true);
    ValueHeader didi = new ValueHeader("DrawDIDI", true);
    ValueHeader diType = new ValueHeader("DiDiType", "Blue", didis);
    ValueHeader markText = new ValueHeader("MarkText", markStr);
    ValueHeader iconSize = new ValueHeader("FlowerSize", size);
    BoundedAnimation angle=new BoundedAnimation(0,360,5000f,false, Easing.LINEAR);

    public HUD() {
        addEnumTypes(didis, "Red", "Blue", "Green", "Tellow", "GRAY", "ORANGE", ")V)");
        addEnumTypes(notType, "LEFT", "UP", "None");
        addEnumTypes(soundType, "Basic", "Ding", "New", "None");
        addEnumTypes(markType, "Text", "Flower", "Info", "None");
    }

    @EventTarget
    public void onEvent2D(EventRender2D render2D) {
        int w = RenderUtil.width();
        int h = RenderUtil.height();
        double[] pos = SenseHeader.getSense.getPlayerPos();
        FontDrawer font = Main.fontLoader.EN16;
        FontDrawer arrayFont = Main.fontLoader.EN24;
        FontDrawer markFont = Main.fontLoader.EN36;
        float icon = (float) iconSize.getDoubleCurrentValue();
        //Mark
        if (!clientMark.getCurrentEnumType().equalsIgnoreCase("None")) {
            switch (clientMark.getCurrentEnumType()) {
                case "Flower":
                    if (angle.getAnimationValue() == 0) angle.setState(true);
                    if (angle.getAnimationValue() == 360) angle.setState(false);
                    GL11.glPushMatrix();
//                    GlStateManager.rotate((float) angle.getAnimationValue(),0,0,1);
                    RenderUtil.drawIcon(0, 0 ,(int) icon, (int) icon, new ResourceLocation("skrilled/launcher.png"));
                    GL11.glPopMatrix();
                    break;
                case "Text":
                    markFont.drawStringWithShadow(markText.getStrValue(), 5, 5, 0.2f, -1);
                    break;
            }
        }
        //ArrayList
        if ((Boolean) this.getValue(malist)) {
            ArrayList<ModuleHeader> sortedList = new ArrayList<>();
            for (ModuleHeader moduleHeader : ModuleManager.mList)
                if (moduleHeader.isCanView()) sortedList.add(moduleHeader);
            sortedList.sort(Comparator.comparingInt(module -> -arrayFont.getStringWidth(module.getModuleDisplay())));
            int yAxis = 5;
            for (ModuleHeader moduleHeader : sortedList) {
                arrayFont.drawStringWithShadow(moduleHeader.getModuleDisplay(), w - moduleHeader.getArrayWidth().getAnimationFactor() * (arrayFont.getStringWidth(moduleHeader.getModuleDisplay()) + 5), yAxis, 1.2f, -1);
                yAxis += arrayFont.getHeight() * moduleHeader.getArrayWidth().getAnimationFactor();
            }
        }
        //drawDIDI
        if ((Boolean) getValue(didi)) {
            RenderUtil.drawSikadi(w / 2f - 300, h / 2f, diType.getCurrentEnumType().equalsIgnoreCase("Red"));
        }

        //Information
        if ((Boolean) this.getValue(info)) {
            font.drawStringWithOutline(SenseHeader.getSense.getClientName() + " LastUpdate:" + SenseHeader.getSense.getClientUpdate(), 5, h - font.getHeight()-5, -1);
            font.drawStringWithOutline(SenseHeader.getSense.getPlayerName() + " X:" + (int) pos[0] + " Y:" + (int) pos[1] + " Z:" + (int) pos[2] + " FPS:" + SenseHeader.getSense.getClientFPS(), 5, h - font.getHeight() * 2-5, -1);
        }
        if (not.isOptionOpen()) Notification.drawNotifications();


//        EnumAssembly = new EnumAssembly(new float[]{50, 50, 100, 100}, null, Main.fontLoader.EN18, new Color(82, 82, 89), Color.white, strings, "11", new Animation(500f, false, Easing.LINEAR));
//        enumAssembly.draw();
    }
}
