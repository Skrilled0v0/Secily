package me.cubex2.ttfr;

import me.skrilled.utils.IMC;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontBuffer implements IMC {
    public CFontRenderer CN16 = this.getFont("CN", 16, true);
    public CFontRenderer CN18 = this.getFont("CN", 18, true);
    public CFontRenderer CN24 = this.getFont("CN", 24, true);
    public CFontRenderer EN10 = this.getFont("EN", 10, true);
    public CFontRenderer EN12 = this.getFont("EN", 12, true);
    public CFontRenderer EN16 = this.getFont("EN", 16, true);
    public CFontRenderer EN18 = this.getFont("EN", 18, true);
    public CFontRenderer EN20 = this.getFont("EN", 20, true);
    public CFontRenderer EN24 = this.getFont("EN", 24, true);
    public CFontRenderer EN36 = this.getFont("EN", 36, true);
    public CFontRenderer EN48 = this.getFont("EN", 48, true);
    public CFontRenderer EN64 = this.getFont("EN", 64, true);
    public CFontRenderer EN72 = this.getFont("EN", 72, true);
    public CFontRenderer EN128 = this.getFont("EN", 128, true);

    public CFontRenderer getFont(String fontName, int size) {
        Font font;
        try {
            InputStream inputStream = mc.getResourceManager().getResource(new ResourceLocation("skrilled/fonts/" + fontName + ".ttf")).getInputStream();
            font = Font.createFont(0, inputStream);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new CFontRenderer(font, size, true);
    }

    public CFontRenderer getFont(String fontName, int size, boolean antiAlias) {
        Font font;
        try {
            InputStream inputStream = mc.getResourceManager().getResource(new ResourceLocation("skrilled/fonts/" + fontName + ".ttf")).getInputStream();
            font = Font.createFont(0, inputStream);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new CFontRenderer(font, size, antiAlias);
    }


}