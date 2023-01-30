package me.kl0udy92.apart.screen.resource

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.implementations.visual.PostFXModule
import net.minecraft.client.resources.IResource
import net.minecraft.client.resources.data.IMetadataSection
import net.minecraft.util.ResourceLocation
import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

class PostFXResource: IResource {

    private val JSON =
        "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}"

    override fun getResourceLocation(): ResourceLocation? {
        return null
    }

    override fun getInputStream(): InputStream? {
        val amount: Double = 0.7 + (Main.moduleManager.getModuleByClass(PostFXModule().javaClass) as PostFXModule).strengthOption.value / 100.0 * 3.0 - 0.01
        return IOUtils.toInputStream(
            String.format(Locale.ENGLISH, JSON, amount, amount, amount),
            Charset.defaultCharset()
        )
    }

    override fun hasMetadata(): Boolean {
        return false
    }

    override fun <T : IMetadataSection?> getMetadata(p_110526_1_: String?): T? {
        return null
    }

    override fun getResourcePackName(): String? {
        return null
    }

}