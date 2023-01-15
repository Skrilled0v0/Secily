/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.modules.module.move;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.SenseHeader;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.player.MovementUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AutoSprint extends ModuleHeader {

    public AutoSprint() {
        super("AutoSprint", true,  ModuleType.MOVE);
        this.setKey(Keyboard.KEY_V);
    }

    @EventTarget
    public void onOpen(EventUpdate eventUpdate) {
        if (mc.thePlayer.moveForward != 0)
            mc.thePlayer.setSprinting(true);
    }
}
