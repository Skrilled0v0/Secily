package me.kl0udy92.apart.features.module.implementations.world

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity
import net.minecraft.util.MathHelper

@ModuleData("ThunderDetector", "À×µçÌ½²âÆ÷", ["ThunderChecker"], "Thunder Detector", Category.WORLD, "Automatic detection when thunder is generated in the world.")
class ThunderDetectorModule: Module() {

    @EventListener
    fun onPacket(event: PacketEvent) {
        if (event.direction == EventDirection.INCOMING) {
            if (event.packet is S2CPacketSpawnGlobalEntity && (event.packet as S2CPacketSpawnGlobalEntity).func_149053_g() == 1) {
                val x = (event.packet as S2CPacketSpawnGlobalEntity).func_149051_d() / 32
                val y = (event.packet as S2CPacketSpawnGlobalEntity).func_149050_e() / 32
                val z = (event.packet as S2CPacketSpawnGlobalEntity).func_149049_f() / 32
                val f = (mc.thePlayer.posX - x).toFloat()
                val f2 = (mc.thePlayer.posZ - z).toFloat()
                val distance = MathHelper.sqrt_float(f * f + f2 * f2)
                Main.notificationManager.notifications.add(Notification("Thunder Detector", "Lightning detected on coordinates ($x,$y,$z,${String.format("%.1f",distance)}m)", NotificationType.WARNING, 4000L))
            }
        }
    }

}