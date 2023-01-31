/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230124
 */
package me.fontloader;

import me.skrilled.utils.IMC;

import java.awt.*;
import java.io.InputStream;

public class FontLoader implements IMC {
    public FontDrawer EN24 = this.getFont("EN", 24);
    public FontDrawer EN10 = this.getFont("EN", 10);
    public FontDrawer EN12 = this.getFont("EN", 12);
    public FontDrawer EN16 = this.getFont("EN", 16);
    public FontDrawer EN18 = this.getFont("EN", 18);
    public FontDrawer EN22 = this.getFont("EN", 22);
    public FontDrawer EN36 = this.getFont("EN", 36);
    public FontDrawer EN48 = this.getFont("EN", 48);
    public FontDrawer EN64 = this.getFont("EN", 64);
    public FontDrawer EN72 = this.getFont("EN", 72);
    public FontDrawer EN128 = this.getFont("EN", 128);
    public FontDrawer ICON64 = this.getFont("ICON", 64);
    public FontDrawer ICON128 = this.getFont("ICON", 128);
    public FontDrawer ICON32 = this.getFont("ICON", 32);
    public FontDrawer ICON36 = this.getFont("ICON", 36);
    public FontDrawer ICON38 = this.getFont("ICON", 38);
    public FontDrawer ICON42 = this.getFont("ICON", 42);
    public FontDrawer ICON48 = this.getFont("ICON", 48);
    public FontDrawer ICON47 = this.getFont("ICON", 47);
    public FontDrawer ICON46 = this.getFont("ICON", 46);
    public FontDrawer ICON50 = this.getFont("ICON", 50);

    public FontDrawer getFont(String fontName, int size) {
        Font font;
        System.out.println("Loading-" + fontName);
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(fontName + ".ttf");
            assert inputStream != null;
            font = Font.createFont(0, inputStream);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new FontDrawer(font, true, true, true);
    }
}
