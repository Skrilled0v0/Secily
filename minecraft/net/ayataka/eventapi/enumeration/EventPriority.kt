package net.ayataka.eventapi.enumeration

/**
 * Created by John on 2018/02/21.
 */
enum class EventPriority(val order: Int) {
    HIGHEST(0),
    HIGH(1),
    NORMAL(2),
    LOW(3),
    LOWEST(4),
}