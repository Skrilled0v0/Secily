package me.kl0udy92.apart.features.module.implementations.visual.components

import me.kl0udy92.apart.features.module.implementations.visual.HUDModule
import me.kl0udy92.apart.features.module.implementations.visual.components.interfaces.IComponent
import me.kl0udy92.apart.utils.MinecraftInstance

abstract class AbstractComponent(val hudModule: HUDModule) : IComponent, MinecraftInstance()