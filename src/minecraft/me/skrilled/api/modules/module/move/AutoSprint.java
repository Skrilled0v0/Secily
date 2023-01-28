/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.modules.module.move;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import org.lwjgl.input.Keyboard;

@ModuleInitialize(name = "AutoSprint", type = ModuleType.MOVE, key = Keyboard.KEY_V)
public class AutoSprint extends ModuleHeader {


    @EventTarget
    public void onOpen(EventUpdate eventUpdate) {
        if (mc.thePlayer.moveForward != 0) mc.thePlayer.setSprinting(true);
    }
}
