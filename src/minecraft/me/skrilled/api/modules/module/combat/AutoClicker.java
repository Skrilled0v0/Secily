/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230109
 */
package me.skrilled.api.modules.module.combat;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.Notification;
import me.skrilled.utils.math.TimerUtil;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

public class AutoClicker extends ModuleHeader {
    public static boolean pressed = false;
    TimerUtil timerUtil = new TimerUtil();
    double[] ip = {1.0, 6.0, 20.0, 1.0};
    double[] ap = {1.0, 6.0, 20.0, 1.0};
    ValueHeader ma_cps = new ValueHeader("MaxCPS", ap);
    ValueHeader mi_cps = new ValueHeader("MinCPS", ip);

    public AutoClicker() {
        super("AutoClicker", false, ModuleType.COMBAT);
        this.setKey(Keyboard.KEY_R);
        this.addValueList(ma_cps, mi_cps);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        pressed = mc.gameSettings.keyBindAttack.pressed;
        double cps = (double) this.getValue(mi_cps)+Math.random()*((double) this.getValue(ma_cps)-(double) this.getValue(mi_cps));
        if (timerUtil.hasReached((int) (1000L / cps)) && pressed) {
//            sense.printINFO("Clicked");
            mc.clickMouse();
            timerUtil.reset();
        }
    }
    @Override
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;

        arrayWidth.setState(isOpen);
        if (isOpen) this.onOpen();
        else this.isNotOpen();
        if (!moduleName.equals("SettingMenu") && mc.theWorld != null) {
            Notification.sendNotification(getModuleName() + (this.isOpen ? " Was Open!" : " Was not Open!"), 1500, (this.isOpen ? Notification.Type.SUCCESS : Notification.Type.WARNING));
        }
    }

}
