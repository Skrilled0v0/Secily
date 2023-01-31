/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230122
 */
package me.skrilled.api.modules.module.combat;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;

@ModuleInitialize(name = "HitBox", type = ModuleType.COMBAT)
public class HitBox extends ModuleHeader {

    public double[] sizeValues = {0.1, 0.2, 6.0, 0.1};
    public ValueHeader size = new ValueHeader("Size", sizeValues);

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.setSuffix("Size:" + Math.round(size.getDoubleCurrentValue()));
    }
}
