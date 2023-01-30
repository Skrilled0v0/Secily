package me.kl0udy92.apart.features.lua

import me.kl0udy92.apart.events.visual.*
import me.kl0udy92.apart.events.system.*
import me.kl0udy92.apart.events.network.*
import me.kl0udy92.apart.events.entities.*
import me.kl0udy92.apart.events.entities.player.*
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.ayataka.eventapi.enumeration.EventState
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue

class LuaScript(
    val globals: Globals,
    name: String,
    chineseName: String,
    namebreak: String,
    description: String,
    category: Category
) : Module(name, chineseName, namebreak, description, category) {

    //Initialization hook.
    init {
        this.call("init")
    }

    //Enable/disable hook.
    override fun onEnable() {
        this.call("on_enable")
        super.onEnable()
    }

    override fun onDisable() {
        this.call("on_disable")
        super.onDisable()
    }

    // FIXME: 2023/1/8 Lua内的事件函数无法与Java事件传参
    //Events hook.
    @EventListener
    fun onMotion(event: MotionEvent) {
        when (event.state) {
            EventState.PRE -> this.call("on_pre_motion")
            EventState.POST -> this.call("on_post_motion")
        }
    }

    @EventListener
    fun onSlowdown(event: SlowdownEvent) {
        this.call("on_slowdown")
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.call("on_update")
    }

    @EventListener
    fun onLivingUpdate(event: LivingUpdateEvent) {
        this.call("on_living_update")
    }

    @EventListener
    fun onConnecting(event: ConnectingEvent) {
        this.call("on_connecting")
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        when (event.direction) {
            EventDirection.INCOMING -> this.call("on_packet_incoming")
            EventDirection.OUTGOING -> this.call("on_packet_outgoing")
        }
    }

    @EventListener
    fun onKeydown(event: KeydownEvent) {
        this.call("on_keydown")
    }

    @EventListener
    fun onMouseClick(event: MouseClickEvent) {
        this.call("on_mouse_clicked")
    }

    @EventListener
    fun onTick(event: TickEvent) {
        this.call("on_tick")
    }

    @EventListener
    fun onRender2D(event: Render2DEvent) {
        this.call("on_render_2d")
    }

    @EventListener
    fun onRender2D(event: Render3DEvent) {
        this.call("on_render_3d")
    }

    private fun call(key: String, data: Any? = null) {
        if (!this.globals.get(key).isnil()) {
            this.globals.get(key).call()
        }
    }

}