package me.skrilled.ui.menu.assembly.enums;

import me.skrilled.ui.menu.assembly.LineAssembly;
import me.skrilled.utils.math.Vec3f;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

//内部类可以直接访问私有属性...
public enum FillingMode {

    /**
     * 绘制矩形
     */
    SIMPLE {
        @Override
        public float draw(LineAssembly lineAssembly) {
            Vec3f[] points = calcPoints(lineAssembly);
            drawPolygon(points[0], points[1], points[2], points[3], lineAssembly.color);
            return lineAssembly.deltaY();
        }

        @Override
        public Vec3f[] calcPoints(LineAssembly lineAssembly) {
            double theta = PI / 4d - Math.atan2(lineAssembly.deltaY(), lineAssembly.deltaX());
            float[] p = lineAssembly.calcAbsPos();
            Vec3f pointLU = new Vec3f(p[0] - pow(2, -0.5) * lineAssembly.width * sin(theta), p[1] - pow(2, -0.5) * lineAssembly.width * cos(theta));
            Vec3f pointLD = new Vec3f(p[0] - pow(2, -0.5) * lineAssembly.width * cos(theta), p[1] + pow(2, -0.5) * lineAssembly.width * sin(theta));
            Vec3f pointRD = new Vec3f(p[2] + pow(2, -0.5) * lineAssembly.width * sin(theta), p[3] + pow(2, -0.5) * lineAssembly.width * cos(theta));
            Vec3f pointRU = new Vec3f(p[2] + pow(2, -0.5) * lineAssembly.width * cos(theta), p[3] - pow(2, -0.5) * lineAssembly.width * sin(theta));
            return new Vec3f[]{pointLU, pointLD, pointRD, pointRU};
        }
    },
    /**
     * 该模式由于画半圆在角度存在负数或大于360的树出现bug，不支持透明度
     */
    ROUNDED_SIDE {
        @Override
        public float draw(LineAssembly lineAssembly) {
            float[] p = lineAssembly.calcAbsPos();
            Vec3f[] points = calcPoints(lineAssembly);
            Color color = new Color(lineAssembly.color.getRed(), lineAssembly.color.getGreen(), lineAssembly.color.getBlue());
            drawPolygon(points[0], points[1], points[2], points[3], lineAssembly.color);
            RenderUtil.drawCircle(p[0], p[1], lineAssembly.width / 2, color.getRGB());
            RenderUtil.drawCircle(p[2], p[3], lineAssembly.width / 2, color.getRGB());
            return lineAssembly.deltaY();
        }

        @Override
        public Vec3f[] calcPoints(LineAssembly lineAssembly) {
            double theta = Math.atan2(lineAssembly.deltaY(), lineAssembly.deltaX());
            float[] p = lineAssembly.calcAbsPos();
            Vec3f pointLU = new Vec3f(p[0] + 0.5 * lineAssembly.width * sin(theta), p[1] - 0.5 * lineAssembly.width * cos(theta));
            Vec3f pointLD = new Vec3f(p[0] - 0.5 * lineAssembly.width * sin(theta), p[1] + 0.5 * lineAssembly.width * cos(theta));
            Vec3f pointRD = new Vec3f(p[2] - 0.5 * lineAssembly.width * sin(theta), p[3] + 0.5 * lineAssembly.width * cos(theta));
            Vec3f pointRU = new Vec3f(p[2] + 0.5 * lineAssembly.width * sin(theta), p[3] - 0.5 * lineAssembly.width * cos(theta));
            return new Vec3f[]{pointLU, pointLD, pointRD, pointRU};
        }
    };

    /**
     * 画凸多边形
     * lu -> left up, etc.
     */
    static void drawPolygon(Vec3f lu, Vec3f ld, Vec3f rd, Vec3f ru, Color color) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glBegin(GL_POLYGON);
        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        glVertex2d(lu.getX(), lu.getY());
        glVertex2d(ld.getX(), ld.getY());
        glVertex2d(rd.getX(), rd.getY());
        glVertex2d(ru.getX(), ru.getY());
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    public abstract float draw(LineAssembly lineAssembly);

    public abstract Vec3f[] calcPoints(LineAssembly lineAssembly);
}
