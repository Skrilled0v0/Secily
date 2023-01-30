package me.kl0udy92.apart.screen.notification

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.implementations.visual.HUDModule
import me.kl0udy92.apart.utils.MinecraftInstance
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import me.kl0udy92.apart.utils.render.gl.BlurUtil
import net.minecraft.client.gui.ScaledResolution
import oh.yalan.NativeClass
import java.awt.Color
import java.util.concurrent.atomic.AtomicInteger

@NativeClass
class NotificationManager : MinecraftInstance() {

    val notifications = mutableListOf<Notification>()

    init {
        this.notifications.clear()
    }

    fun draw() {
        val sr = ScaledResolution(mc)
        val yAxis = AtomicInteger(0)
        val SF20 = Main.fontBuffer.SF20
        val SF18 = Main.fontBuffer.SF18
        val width = sr.scaledWidth
        val height = sr.scaledHeight

        if (!(Main.moduleManager.getModuleByClass(HUDModule().javaClass) as HUDModule).elementsOption.find("Notifications")!!.value) return;

        kotlin.runCatching {
            this.notifications.forEach {
                it.animationX.update()
                it.animationY.update()

                val calculatedWidth =
                    SF20.getStringWidth(it.title).coerceAtLeast(SF18.getStringWidth(it.subTitle)).toDouble()

                if (!it.finished) it.animationX.animate(calculatedWidth, 0.3, Easings().BACK_OUT, true)
                it.animationY.animate(yAxis.get().toDouble(), 0.3, Easings().BACK_OUT, true)

                if (it.animationX.isDone()) {
                    if (it.backStopwatch.elapsed(it.duration)) {
                        it.animationX.animate(0.0, 0.3, Easings().BACK_IN, true)
                        it.animationY.animate(0.0, 0.4, Easings().BACK_OUT, true)
                    }
                }

                if (it.animationX.value == 0.0) it.finished = true

                if (it.animationX.value > 0) {
                    RenderUtil.drawImage(
                        it.type.location,
                        (width - it.animationX.value - 28).toFloat(),
                        (height - 30 - 23 - it.animationY.value - 2).toFloat(),
                        23f,
                        23f,
                        it.type.color.rgb
                    )
                    SF20.drawStringWithOutlineWithoutColorcode(
                        it.title,
                        width - it.animationX.value,
                        height - 30 - 22 - it.animationY.value,
                        -1
                    )
                    SF18.drawStringWithOutlineWithoutColorcode(
                        it.subTitle,
                        width - it.animationX.value,
                        height - 30 - 12 - it.animationY.value,
                        -1
                    )
                    BlurUtil.blurArea(
                        (width - it.animationX.value - 28).toFloat(),
                        (height - 30 - 23 - it.animationY.value - 2).toFloat(),
                        (width - it.animationX.value + calculatedWidth).toFloat(),
                        (height - 30 - 2 - it.animationY.value).toFloat(),
                        12.5f
                    )
                    RenderUtil.drawRect(
                        width - it.animationX.value - 28,
                        height - 30 - 23 - it.animationY.value - 2,
                        width - it.animationX.value + calculatedWidth,
                        height - 30 - 2 - it.animationY.value,
                        Color(0, 0, 0, 155).rgb
                    )
                    RenderUtil.drawImage(
                        it.type.location,
                        (width - it.animationX.value - 28).toFloat(),
                        (height - 30 - 23 - it.animationY.value - 2).toFloat(),
                        23f,
                        23f,
                        it.type.color.rgb
                    )
                    SF20.drawStringWithOutlineWithoutColorcode(
                        it.title,
                        width - it.animationX.value,
                        height - 30 - 22 - it.animationY.value,
                        -1
                    )
                    SF18.drawStringWithOutlineWithoutColorcode(
                        it.subTitle,
                        width - it.animationX.value,
                        height - 30 - 12 - it.animationY.value,
                        -1
                    )
                    yAxis.addAndGet(25)
                }

                this.notifications.removeIf { notification -> notification.removeStopwatch.elapsed(notification.duration + 1000L) && notification.finished }
            }
        }.onFailure {
            /*Nothing todo.*/
        }

    }

}