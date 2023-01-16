package me.cubex2.ttfr;

import me.skrilled.utils.IMC;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontBuffer implements IMC {
    public CFontRenderer font10 = this.getFont("font", 10, true);
    public CFontRenderer font16 = this.getFont("font", 16, true);
    public CFontRenderer font18 = this.getFont("font", 18, true);
    public CFontRenderer font24 = this.getFont("font", 24, true);
    public CFontRenderer font36 = this.getFont("font", 36, true);

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