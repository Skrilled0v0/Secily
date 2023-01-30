package me.kl0udy92.apart.utils.render

import kotlin.math.sqrt

object MouseUtil {

    fun isHovered(x: Float, y: Float, width: Float, height: Float, mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= x && (mouseX <= x + width) && mouseY >= y && mouseY <= y + height
    }

    fun isHovered(x: Double, y: Double, width: Double, height: Double, mouseX: Int, mouseY: Int): Boolean {
        return this.isHovered(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), mouseX, mouseY)
    }

    fun isHovered(x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int): Boolean {
        return this.isHovered(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(), mouseX, mouseY)
    }

    fun isHovering(x: Float, y: Float, width: Float, height: Float, mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= x && (mouseX <= width) && mouseY >= y && mouseY <= height
    }

    fun isHovering(x: Double, y: Double, width: Double, height: Double, mouseX: Int, mouseY: Int): Boolean {
        return this.isHovering(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), mouseX, mouseY)
    }

    fun isHovering(x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int): Boolean {
        return this.isHovering(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(), mouseX, mouseY)
    }

    fun isMouseInsideCircle(x: Float, y: Float, radius: Float, mouseX: Int, mouseY: Int): Boolean {
        return sqrt((mouseX - x) * (mouseX - x) + (mouseY - y) * (mouseY - y)) <= radius;
    }

    fun isMouseInsideCircle(circleX: Double, circleY: Double, circleRadius: Double, mouseX: Int, mouseY: Int): Boolean {
        return this.isMouseInsideCircle(circleX.toFloat(), circleY.toFloat(), circleRadius.toFloat(), mouseX, mouseY)
    }

    fun isMouseInsideCircle(circleX: Int, circleY: Int, circleRadius: Int, mouseX: Int, mouseY: Int): Boolean {
        return this.isMouseInsideCircle(circleX.toDouble(), circleY.toDouble(), circleRadius.toDouble(), mouseX, mouseY)
    }

    enum class ButtonType(val identifier: Int) {
        LEFT(0),
        RIGHT(1),
        MIDDLE(2)
    }

}