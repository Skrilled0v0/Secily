package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.events.entities.LivingUpdateEvent
import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.visual.Render3DEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.utils.render.particles.Location
import me.kl0udy92.apart.utils.render.particles.Particles
import me.kl0udy92.apart.utils.system.MathUtil
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import java.util.*

@ModuleData(
    "DamageParticles",
    "伤害粒子",
    ["DMGParticles"],
    "Damage Particles",
    Category.VISUAL,
    "Particles that hit the target and display the damage value."
)
class DamageParticlesModule : Module() {

    private val healthMap = hashMapOf<EntityLivingBase, Float>()
    private val particles = mutableListOf<Particles>()
    private val font = mc.fontRendererObj

    @EventListener
    fun onLivingUpdate(event: LivingUpdateEvent) {
        val entity = event.entity
        if (entity == mc.thePlayer) {
            return
        }
        if (!healthMap.containsKey(entity)) {
            healthMap[entity] = entity.health
        }

        val floatValue = healthMap[entity]!!
        val health = entity.health
        if (floatValue != health) {
            val text: String = if (floatValue - health < 0.0f) {
                "\u00a7a" + MathUtil.roundToPlace((floatValue - health) * -1.0, 1)
            } else {
                "\u00a7e" + MathUtil.roundToPlace((floatValue - health).toDouble(), 1)
            }
            val location = Location(entity)
            location.setY(
                entity.entityBoundingBox.minY
                        + (entity.entityBoundingBox.maxY - entity.entityBoundingBox.minY) / 2.0
            )
            location.setX(location.x - 0.5 + Random(System.currentTimeMillis()).nextInt(5) * 0.1)
            location.setZ(
                (location.z - 0.5
                        + Random(System.currentTimeMillis() + (0x203FF36645D9EA2EL xor 0x203FF36645D9EA2FL)).nextInt(5)
                        * 0.1)
            )
            particles.add(Particles(location, text))
            healthMap.remove(entity)
            healthMap[entity] = entity.health
        }
    }

    @EventListener
    fun onRender3D(event: Render3DEvent) {
        kotlin.runCatching {
            this.particles.forEach {
                val x: Double = it.location.x

                val n = x - mc.renderManager.viewerPosX
                val y: Double = it.location.y

                val n2 = y - mc.renderManager.viewerPosY
                val z: Double = it.location.z

                val n3 = z - mc.renderManager.viewerPosZ
                GlStateManager.pushMatrix()
                GlStateManager.enablePolygonOffset()
                GlStateManager.doPolygonOffset(1.0f, -1500000.0f)
                GlStateManager.translate(n.toFloat(), n2.toFloat(), n3.toFloat())
                GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
                val textY: Float = if (mc.gameSettings.thirdPersonView == 2) {
                    -1.0f
                } else {
                    1.0f
                }
                GlStateManager.rotate(mc.renderManager.playerViewX, textY, 0.0f, 0.0f)
                val size = 0.03
                GlStateManager.scale(-size, -size, size)
                GL11.glDepthMask(false)
                font.drawStringWithShadow(
                    it.text,
                    (-(font.getStringWidth(it.text) / 2)).toFloat(),
                    (-(font.FONT_HEIGHT - 1)).toFloat(),
                    0
                )
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                GL11.glDepthMask(true)
                GlStateManager.doPolygonOffset(1.0f, 1500000.0f)
                GlStateManager.disablePolygonOffset()
                GlStateManager.popMatrix()
            }
        }.onFailure { /*Nothing todo*/ }
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        kotlin.runCatching {
            this.particles.forEach {
                ++it.ticks
                if (it.ticks <= 10) {
                    it.location.y = it.location.y + it.ticks * 0.005
                }
                if (it.ticks > 20) {
                    particles.remove(it)
                }
            }
        }.onFailure { /*Nothing todo*/ }
    }

}