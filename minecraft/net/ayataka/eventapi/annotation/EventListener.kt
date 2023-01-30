package net.ayataka.eventapi.annotation

import net.ayataka.eventapi.enumeration.EventPriority

@Target(AnnotationTarget.FUNCTION)
@SuppressWarnings("unused")
annotation class EventListener(val priority: EventPriority = EventPriority.NORMAL)
