package me.skrilled.api.value;

import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;

public class SubEnumValueHeader {
    public String name;
    public ValueHeader valueHeader;
    public float x1, y1, x2, y2;
    public BoundedAnimation enumValueAlpha = new BoundedAnimation(165, 250, 550f, false, Easing.LINEAR);

    public SubEnumValueHeader(String name, ValueHeader valueHeader) {
        this.name = name;
        this.valueHeader = valueHeader;
    }
}
