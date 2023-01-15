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

public class AutoClicker extends ModuleHeader {
    TimerUtil timerUtil = new TimerUtil();
    double[] ap = {1.0, 6.0, 20.0};
    ValueHeader aps = new ValueHeader("APS", ap);
    int key = mc.gameSettings.keyBindAttack.getKeyCode();

    public AutoClicker() {
        super("AutoClicker", false, ModuleType.COMBAT);
        this.setKey(Keyboard.KEY_R);
        this.addValueList(aps);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        double theAPS = (Double) this.getValue(aps);
        if (timerUtil.hasReached((int) (theAPS * 1000)))
            mc.thePlayer.swingItem();
    }


}
