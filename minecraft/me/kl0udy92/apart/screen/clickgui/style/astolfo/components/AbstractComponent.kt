package me.kl0udy92.apart.screen.clickgui.style.astolfo.components

import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.IFrame
import me.kl0udy92.apart.utils.MinecraftInstance
import oh.yalan.NativeClass

@NativeClass
abstract class AbstractComponent(
    var frame: IFrame,
    var x: Double,
    var y: Double,
    var width: Double,
    var height: Double,
    var offset: Double
) : IComponent, MinecraftInstance() {

    open fun isVisible(): Boolean {
        return true
    }

}