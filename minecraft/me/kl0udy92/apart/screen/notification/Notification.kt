package me.kl0udy92.apart.screen.notification

import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.system.Stopwatch

class Notification(
    val title: String, val subTitle: String,
    val type: NotificationType,
    val animationX: Animation = Animation(), val animationY: Animation = Animation(),
    val removeStopwatch: Stopwatch = Stopwatch(), val backStopwatch: Stopwatch = Stopwatch(),
    var finished: Boolean = false, var duration: Long = 2000L
) {

    constructor(title: String, subTitle: String, type: NotificationType, duration: Long) : this(title, subTitle, type) {
        this.duration = duration
    }

}