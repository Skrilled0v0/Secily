package me.kl0udy92.apart.config.implementations

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.kl0udy92.apart.config.AbstractConfig
import me.kl0udy92.apart.config.annotations.ConfigData
import me.kl0udy92.apart.core.Main
import java.io.FileReader
import java.io.FileWriter

@ConfigData("clickgui")
class ClickGuiConfig : AbstractConfig() {

    override fun read() {
        kotlin.runCatching {
            val fileReader = FileReader(this.path.toFile())
            val jsonObject: JsonObject = JsonParser().parse(fileReader).asJsonObject
            fileReader.close()
            jsonObject.entrySet().forEach {
                val frame = Main.clickGui.findFrame(it.key)
                val framesElement = it.value.asJsonObject
                frame!!.x = framesElement.get("X").asDouble
                frame.y = framesElement.get("Y").asDouble
                frame.open = framesElement.get("Open").asBoolean
            }
        }.onFailure { it.printStackTrace() }
    }

    override fun write() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonObject = JsonObject()
        Main.clickGui.frames.forEach {
            val framesElement = JsonObject()
            framesElement.addProperty("X", it.x)
            framesElement.addProperty("Y", it.y)
            framesElement.addProperty("Open", it.open)
            jsonObject.add(it.name, framesElement)
        }
        kotlin.runCatching {
            val fileWriter = FileWriter(this.path.toFile())
            gson.toJson(jsonObject, fileWriter)
            fileWriter.close()
        }.onFailure { it.printStackTrace() }
    }

}