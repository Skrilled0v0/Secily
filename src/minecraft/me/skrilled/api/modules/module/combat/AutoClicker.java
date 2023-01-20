/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230109
 */
package me.skrilled.api.modules.module.combat;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.math.TimerUtil;
import org.lwjgl.input.Keyboard;

@ModuleInitialize(name = "AutoClicker", key = Keyboard.KEY_R, type = ModuleType.COMBAT)
public class AutoClicker extends ModuleHeader {
    public static boolean pressed = false;
    TimerUtil timerUtil = new TimerUtil();
    double[] ip = {1.0, 6.0, 20.0, 1.0};
    double[] ap = {1.0, 6.0, 20.0, 1.0};
    ValueHeader ma_cps = new ValueHeader("MaxCPS", ap);
    ValueHeader mi_cps = new ValueHeader("MinCPS", ip);

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        pressed = mc.gameSettings.keyBindAttack.pressed;
        double cps = (double) this.getValue(mi_cps) + Math.random() * ((double) this.getValue(ma_cps) - (double) this.getValue(mi_cps));
        if (timerUtil.hasReached((int) (1000L / cps)) && pressed) {
//            sense.printINFO("Clicked");
            mc.clickMouse();
            timerUtil.reset();
        }
    }


}
