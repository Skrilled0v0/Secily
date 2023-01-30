package me.kl0udy92.apart.utils.system

class Stopwatch(private var ms: Long = System.currentTimeMillis()) {

    fun getElapsedTime(): Long {
        return System.currentTimeMillis() - this.ms
    }

    fun elapsed(milliseconds: Long): Boolean {
        return System.currentTimeMillis() - this.ms > milliseconds
    }

    fun elapsedWithReset(milliseconds: Long, reset: () -> Boolean): Boolean {
        return if (System.currentTimeMillis() - this.ms > milliseconds) {
            if (reset.invoke()) this.reset()
            true
        } else false
    }

    fun elapsedWithReset(milliseconds: Long): Boolean {
        return this.elapsedWithReset(milliseconds) { true }
    }

    fun reset() {
        this.ms = System.currentTimeMillis()
    }

}