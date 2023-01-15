/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230113
 */
package me.skrilled.api.modules.module.move;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import org.lwjgl.input.Keyboard;

public class AutoJump extends ModuleHeader {
    public AutoJump() {
        super("AutoJump", false, ModuleType.MOVE);
        this.setKey(Keyboard.KEY_K);
    }

    @EventTarget
    private void onUpdate(EventUpdate eventUpdate) {
        if (mc.thePlayer.onGround) mc.thePlayer.jump();
    }
}
