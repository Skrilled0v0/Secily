package me.kl0udy92.apart.utils.system

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

object MathUtil {

    fun clamp(number: Float, min: Float, max: Float): Float {
        return if (number < min) min else number.coerceAtMost(max)
    }

    fun randomDouble(min: Double, max: Double): Double {
        return if (min > max) min else Random().nextDouble() * (max - min) + min
    }

    fun randomFloat(min: Float, max: Float): Float {
        return if (min > max) min else Random().nextFloat() * (max - min) + min
    }

    fun randomLong(min: Long, max: Long): Long {
        return if (min > max) min else Random().nextLong() * (max - min) + min
    }

    fun randomInt(min: Int, max: Int): Int {
        return if (min > max) min else Random().nextInt(max) + min
    }

    fun randomByte(min: Byte, max: Byte): Byte {
        return if (min > max) min else (Random().nextInt(max.toInt()) + min).toByte()
    }

    fun roundToPlace(value: Double, places: Int): Double {
        require(places >= 0)
        val bd = BigDecimal(value).setScale(places, RoundingMode.HALF_UP)
        return bd.toDouble()
    }

    fun interpolate(current: Double, old: Double, scale: Double): Double {
        return old + (current - old) * scale
    }

}