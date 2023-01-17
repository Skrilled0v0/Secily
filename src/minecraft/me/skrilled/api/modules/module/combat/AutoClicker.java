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
import me.skrilled.utils.math.TimerUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class AutoClicker extends ModuleHeader {
    TimerUtil timerUtil = new TimerUtil();
    double[] ip = {1.0, 6.0, 20.0, 1.0};
    double[] ap = {1.0, 6.0, 20.0, 1.0};
    ValueHeader ma_cps = new ValueHeader("MaxCPS", ap);
    ValueHeader mi_cps = new ValueHeader("MinCPS", ip);

    int key = mc.gameSettings.keyBindAttack.getKeyCode();

    public AutoClicker() {
        super("AutoClicker", false, ModuleType.COMBAT);
        this.setKey(Keyboard.KEY_R);
        this.addValueList(ma_cps, mi_cps);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        double cps = ThreadLocalRandom.current().nextDouble((double) this.getValue(ma_cps));
        if (timerUtil.hasReached((int) (cps / 1000L)) && Mouse.isButtonDown(key)) {
            mc.clickMouse();
        }
    }

}
