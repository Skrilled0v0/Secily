package me.skrilled.api.value;

public class SubEnumValueHeader {
    public String name;
    public ValueHeader valueHeader;
    public float x1, y1, x2, y2;

    public SubEnumValueHeader(String name, ValueHeader valueHeader) {
        this.name = name;
        this.valueHeader = valueHeader;
    }
}
