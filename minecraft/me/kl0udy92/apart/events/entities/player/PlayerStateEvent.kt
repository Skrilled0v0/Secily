package me.kl0udy92.apart.events.entities.player

import net.ayataka.eventapi.Event

/**
 * @author DeadAngels.
 * @since 2023/1/28
 **/
class PlayerStateEvent(var sprinting: Boolean, var sneaking: Boolean): Event()