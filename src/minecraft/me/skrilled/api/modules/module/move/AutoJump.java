/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230113
 */
package me.skrilled.api.modules.module.move;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import org.lwjgl.input.Keyboard;

@ModuleInitialize(name = "AutoJump", type = ModuleType.MOVE, key = Keyboard.KEY_K)
public class AutoJump extends ModuleHeader {
    @EventTarget
    private void onUpdate(EventUpdate eventUpdate) {
        if (mc.thePlayer.onGround) mc.thePlayer.jump();
    }
}
