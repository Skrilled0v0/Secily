package me.kl0udy92.apart.features.module.implementations.player.disablers

import me.kl0udy92.apart.features.module.implementations.player.DisablerModule
import me.kl0udy92.apart.features.module.implementations.player.disablers.interfaces.IDisabler
import me.kl0udy92.apart.utils.MinecraftInstance

/**
 * @author DeadAngels.
 * @since 2023/1/28
 **/
abstract class AbstractDisablerMode(val key: String, val daddy: DisablerModule): IDisabler, MinecraftInstance()