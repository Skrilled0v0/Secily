package me.kl0udy92.apart.events.entities.player

import net.ayataka.eventapi.Event
import net.ayataka.eventapi.enumeration.EventState

class MotionEvent(
    var posX: Double, var posY: Double, var posZ: Double,
    var yaw: Float, var pitch: Float,
    var ground: Boolean, var state: EventState
) : Event()