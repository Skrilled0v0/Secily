package me.kl0udy92.apart.events.network

import net.ayataka.eventapi.Event

class ConnectingEvent(val ip: String, val port: Int): Event()