package me.kl0udy92.apart.events.entities

import net.ayataka.eventapi.Event
import net.minecraft.entity.EntityLivingBase

class LivingUpdateEvent(val entity: EntityLivingBase) : Event()