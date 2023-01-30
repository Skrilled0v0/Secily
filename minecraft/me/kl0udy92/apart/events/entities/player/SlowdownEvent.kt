package me.kl0udy92.apart.events.entities.player

import net.ayataka.eventapi.Event
import net.ayataka.eventapi.interfaces.Cancellable

class SlowdownEvent(var strafe: Float, var forward: Float) : Event(), Cancellable {

    override var canceled: Boolean = false

}