package me.fontloader;

public final class ColorUtils {
    public static final int RED = getRGB(255, 0, 0);
    public static final int GREED = getRGB(0, 255, 0);
    public static final int BLUE = getRGB(0, 0, 255);
    public static final int WHITE = getRGB(255, 255, 255);
    public static final int BLACK = getRGB(0, 0, 0);

    public static final int NO_COLOR = getRGB(0, 0, 0, 0);

    public static int getRGB(int r, int g, int b) {
        return getRGB(r, g, b, 255);
    }

    public static int getRGB(float r, float g, float b) {
        return getRGB((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), 255);
    }

    public static int getRGB(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    public static int getRGB(float r, float g, float b, float a) {
        return getRGB((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
    }

    public static int[] splitRGB(int rgb) {
        final int[] ints = new int[3];

        ints[0] = (rgb >> 16) & 0xFF;
        ints[1] = (rgb >> 8) & 0xFF;
        ints[2] = rgb & 0xFF;

        return ints;
    }

    public static int getRGB(int rgb) {
        return 0xff000000 | rgb;
    }

    public static int reAlpha(int rgb, int alpha) {
        return getRGB(getRed(rgb), getGreen(rgb), getBlue(rgb), alpha);
    }

    public static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    public static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    public static int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    public static int getAlpha(int rgb) {
        return (rgb >> 24) & 0xff;
    }

    public static int blend(float r1, float g1, float b1, float r2, float g2, float b2, float ratio) {
        final float f = 1.0f - ratio;

        return getRGB(r1 * ratio + r2 * f, g1 * ratio + g2 * f, b1 * ratio + b2 * f);
    }
}
