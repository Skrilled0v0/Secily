package me.kl0udy92.apart.screen.font

import me.cubex2.ttfr.CFontRenderer
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.util.ResourceLocation
import org.luaj.vm2.LuaValue
import java.awt.Font
import java.io.InputStream

class FontBuffer : MinecraftInstance() {

    val microsoftYahei12 = this.getFont(FontType.MicrosoftYahei, 12)
    val microsoftYahei14 = this.getFont(FontType.MicrosoftYahei, 14)
    val microsoftYahei15 = this.getFont(FontType.MicrosoftYahei, 15)
    val microsoftYahei16 = this.getFont(FontType.MicrosoftYahei, 16)
    val microsoftYahei17 = this.getFont(FontType.MicrosoftYahei, 17)
    val microsoftYahei18 = this.getFont(FontType.MicrosoftYahei, 18)
    val microsoftYahei20 = this.getFont(FontType.MicrosoftYahei, 20)
    val microsoftYahei22 = this.getFont(FontType.MicrosoftYahei, 22)
    val microsoftYahei24 = this.getFont(FontType.MicrosoftYahei, 24)
    val microsoftYahei36 = this.getFont(FontType.MicrosoftYahei, 36)
    val microsoftYahei48 = this.getFont(FontType.MicrosoftYahei, 48)
    val microsoftYahei60 = this.getFont(FontType.MicrosoftYahei, 60)
    val microsoftYahei72 = this.getFont(FontType.MicrosoftYahei, 72)

    val SF12 = this.getFont(FontType.SF, 12)
    val SF15 = this.getFont(FontType.SF, 15)
    val SF16 = this.getFont(FontType.SF, 16)
    val SF17 = this.getFont(FontType.SF, 17)
    val SF18 = this.getFont(FontType.SF, 18)
    val SF19 = this.getFont(FontType.SF, 19)
    val SF20 = this.getFont(FontType.SF, 20)
    val SF24 = this.getFont(FontType.SF, 24)
    val SF36 = this.getFont(FontType.SF, 36)
    val SF48 = this.getFont(FontType.SF, 48)
    val SF60 = this.getFont(FontType.SF, 60)
    val SF72 = this.getFont(FontType.SF, 72)

    val tahomaBold11 = this.getFont(FontType.TahomaBold, 11)
    val tahomaBold12 = this.getFont(FontType.TahomaBold, 12)
    val tahomaBold18 = this.getFont(FontType.TahomaBold, 18)
    val tahomaBold20 = this.getFont(FontType.TahomaBold, 20)
    val tahomaBold24 = this.getFont(FontType.TahomaBold, 24)
    val tahomaBold36 = this.getFont(FontType.TahomaBold, 36)
    val tahomaBold48 = this.getFont(FontType.TahomaBold, 48)
    val tahomaBold60 = this.getFont(FontType.TahomaBold, 60)
    val tahomaBold72 = this.getFont(FontType.TahomaBold, 72)

    val latoBold11 = this.getFont(FontType.LatoBold, 11)
    val latoBold12 = this.getFont(FontType.LatoBold, 12)
    val latoBold16 = this.getFont(FontType.LatoBold, 16)
    val latoBold17 = this.getFont(FontType.LatoBold, 17)
    val latoBold18 = this.getFont(FontType.LatoBold, 18)
    val latoBold20 = this.getFont(FontType.LatoBold, 20)
    val latoBold24 = this.getFont(FontType.LatoBold, 24)
    val latoBold36 = this.getFont(FontType.LatoBold, 36)
    val latoBold48 = this.getFont(FontType.LatoBold, 48)
    val latoBold60 = this.getFont(FontType.LatoBold, 60)
    val latoBold72 = this.getFont(FontType.LatoBold, 72)

    fun getFont(fontName: String, size: Int): CFontRenderer {
        var font: Font
        try {
            val inputStream: InputStream = mc.resourceManager.getResource(
                ResourceLocation(
                    Main.name.lowercase() + "/fonts/" + fontName + ".ttf"
                )
            ).inputStream
            font = Font.createFont(0, inputStream)
            font = font.deriveFont(Font.PLAIN, size.toFloat())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Error loading font")
            font = Font("default", Font.PLAIN, size)
        }
        return CFontRenderer(font, size, true)
    }

    fun getFont(fontName: String, size: Int, antiAlias: Boolean): CFontRenderer {
        var font: Font
        try {
            val inputStream: InputStream = mc.resourceManager.getResource(
                ResourceLocation(
                    Main.name.lowercase() + "/fonts/" + fontName + ".ttf"
                )
            ).inputStream
            font = Font.createFont(0, inputStream)
            font = font.deriveFont(Font.PLAIN, size.toFloat())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Error loading font")
            font = Font("default", Font.PLAIN, size)
        }
        return CFontRenderer(font, size, antiAlias)
    }

    fun getFont(fontType: FontType, size: Int): CFontRenderer {
        var font: Font
        try {
            val inputStream: InputStream = mc.resourceManager.getResource(
                ResourceLocation(
                    Main.name.lowercase() + "/fonts/" + fontType.identifier + ".ttf"
                )
            ).inputStream
            font = Font.createFont(0, inputStream)
            font = font.deriveFont(Font.PLAIN, size.toFloat())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Error loading font")
            font = Font("default", Font.PLAIN, size)
        }
        return CFontRenderer(font, size, true)
    }

    fun getFont(fontType: FontType, size: Int, antiAlias: Boolean): CFontRenderer {
        var font: Font
        try {
            val inputStream: InputStream = mc.resourceManager.getResource(
                ResourceLocation(
                    Main.name.lowercase() + "/fonts/" + fontType.name + ".ttf"
                )
            ).inputStream
            font = Font.createFont(0, inputStream)
            font = font.deriveFont(Font.PLAIN, size.toFloat())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Error loading font")
            font = Font("default", Font.PLAIN, size)
        }
        return CFontRenderer(font, size, antiAlias)
    }

    fun find(key: LuaValue, size: LuaValue): CFontRenderer {
        return when (key.toString()) {
            "Lato-Bold" -> {
                this.getFont(FontType.LatoBold, size.toint())
            }
            "Microsoft-Yahei" -> {
                this.getFont(FontType.MicrosoftYahei, size.toint())
            }
            "SF" -> {
                this.getFont(FontType.SF, size.toint())
            }
            "Tahoma-Bold" -> {
                this.getFont(FontType.TahomaBold, size.toint())
            }
            else -> this.getFont("unknown", size.toint())
        }
    }

}