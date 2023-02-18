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
import me.skrilled.ui.menu.assembly.NumberAssembly;

import java.math.BigDecimal;

@ModuleInitialize(name = "Reach", type = ModuleType.COMBAT)
public class Reach extends ModuleHeader {
    double[] buildValues = {4.5f, 5.0f, 10f, 0.1f};
    public ValueHeader build = new ValueHeader("Build", buildValues);
    double[] combatValues = {3, 3.5f, 10f, 0.1f};
    public ValueHeader combat = new ValueHeader("Combat", combatValues);

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        BigDecimal combatValue = new BigDecimal(combat.getDoubleCurrentValue());
        BigDecimal buildValue = new BigDecimal(build.getDoubleCurrentValue());
        this.setSuffix("C:" + combatValue.setScale(NumberAssembly.scaleNum, NumberAssembly.roundingMode) + " B:" + buildValue.setScale(NumberAssembly.scaleNum, NumberAssembly.roundingMode));
    }
}
