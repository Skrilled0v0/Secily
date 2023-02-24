/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230201
 */
package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.ModuleInitialize;
import me.skrilled.api.value.ValueHeader;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;

import java.util.ArrayList;

@ModuleInitialize(name = "RenderEditor", type = ModuleType.RENDER)
public class WorldRenderEditor extends ModuleHeader {
    public static ValueHeader weatherEditor = new ValueHeader("Weather", true);
    public static ValueHeader timeEditor = new ValueHeader("Timer", true);
    static double[] times = {0.0f, 0.0f, 18000.0f, 1.0f};
    public static ValueHeader time = new ValueHeader("Time", times);
    static ArrayList<String> weather = new ArrayList<>();
    public static ValueHeader weathers = new ValueHeader("WeatherType", "Snow", weather);
    ValueHeader doDaylightCycle = new ValueHeader("Alternation", false);
    BoundedAnimation timeChange = new BoundedAnimation(0, 18000, 20000, false, Easing.LINEAR);

    public WorldRenderEditor() {
        addEnumTypes(weather, "None", "Snow", "Rain");
    }

    @EventTarget
    private void onUpdate(EventUpdate eventUpdate) {
        if (doDaylightCycle.isOptionOpen()) {
            if (timeChange.getAnimationValue() == 0) timeChange.setState(true);
            if (timeChange.getAnimationValue() == 18000) timeChange.setState(false);
            time.setDoubleCurrentValue(timeChange.getAnimationValue());
        }
        if (weatherEditor.isOptionOpen()) {
            if (weathers.getCurrentEnumType().equalsIgnoreCase("Snow") || weathers.getCurrentEnumType().equalsIgnoreCase("Rain")) {
                mc.theWorld.setRainStrength(1);
            } else if (weathers.getCurrentEnumType().equalsIgnoreCase("None")) mc.theWorld.setRainStrength(0);
        }
    }


}
