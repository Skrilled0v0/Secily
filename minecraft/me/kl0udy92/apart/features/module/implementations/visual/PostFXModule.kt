package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.system.TickEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType
import me.kl0udy92.apart.screen.resource.PostFXResourceManager
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.client.resources.SimpleReloadableResourceManager
import net.minecraft.util.ResourceLocation

@ModuleData("PostFX", "¶¯Ì¬Ä£ºý", ["MotionBlur"], "Post FX", Category.VISUAL, "Smooth moving objects.")
class PostFXModule: Module() {

    val strengthOption = DoubleOption("Strength", "Blur strength", 1.0, 0.1, 10.0, 0.1)

    var lastValue = 0.0;

    private val domainResourceManagers = (mc.resourceManager as SimpleReloadableResourceManager).domainResourceManagers

    override fun onEnable() {
        mc.entityRenderer.stopUseShader()
        super.onEnable()
    }

    @EventListener
    fun onTick(event: TickEvent) {
        if (mc.gameSettings.ofFastRender) {
            Main.notificationManager.notifications.add(Notification("Post FX", "Turn off fast render and then turn on Post FX.", NotificationType.ERROR, 3500))
            this.toggle()
        }

        if (!mc.entityRenderer.isShaderActive && mc.theWorld != null) {
            mc.entityRenderer.loadShader(ResourceLocation("postfx", "postfx"))
        }

        if (!domainResourceManagers.containsKey("postfx")) {
            domainResourceManagers["postfx"] = PostFXResourceManager(mc.metadataSerializer_)
        }

        if (this.strengthOption.value != lastValue) {
            domainResourceManagers.remove("postfx")
            domainResourceManagers["postfx"] = PostFXResourceManager(mc.metadataSerializer_)
            mc.entityRenderer.loadShader(ResourceLocation("postfx", "postfx"))
        }

        lastValue = this.strengthOption.value
    }

    override fun onDisable() {
        if (mc.entityRenderer.shaderGroup != null) mc.entityRenderer.stopUseShader()
        super.onDisable()
    }

}