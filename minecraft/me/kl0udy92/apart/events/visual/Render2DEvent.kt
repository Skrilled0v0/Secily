package me.kl0udy92.apart.events.visual

import net.ayataka.eventapi.Event
import net.minecraft.client.gui.ScaledResolution

class Render2DEvent(var scaledResolution: ScaledResolution, val partialTicks: Float) : Event()