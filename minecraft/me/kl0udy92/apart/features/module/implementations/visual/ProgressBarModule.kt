package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.visual.Render2DEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.system.Stopwatch
import net.ayataka.eventapi.annotation.EventListener
import java.awt.Color

/**
 * @author DeadAngels.
 * @since 2023/1/10
 **/
@ModuleData("ProgressBar", "½ø¶ÈÌõ", [""], "Progress Bar", Category.VISUAL, "Progress Bar Demo.")
class ProgressBarModule: Module() {

    val delayOption = DoubleOption("Millisecond", "Millisecond.", 0.0, 100.0, 10000.0, 100.0)

    val stopWatch = Stopwatch()

    @EventListener
    fun onRender2D(event: Render2DEvent) {

        val width = event.scaledResolution.scaledWidth_double
        val height = event.scaledResolution.scaledHeight_double

        val rectangle_width = 100

        this.stopWatch.elapsedWithReset(this.delayOption.value.toLong())

        val elapsedTime = this.stopWatch.getElapsedTime().toDouble()
        val percentage = elapsedTime / this.delayOption.value

        RenderUtil.drawRect(
            width / 2 - rectangle_width / 2 - 1,
            height / 2 + 100.0 - 1,
            (width / 2 - rectangle_width / 2) + rectangle_width + 1,
            height / 2 + 100 + 15 + 1,
            Color.BLACK.rgb
        )

        RenderUtil.drawRect(
            width / 2 - rectangle_width / 2,
            height / 2 + 100.0,
            (width / 2 - rectangle_width / 2) + percentage * rectangle_width,
            height / 2 + 100 + 15,
            -1
        )

    }

}