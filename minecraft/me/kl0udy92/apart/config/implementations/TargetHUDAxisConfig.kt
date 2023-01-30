package me.kl0udy92.apart.config.implementations

import com.google.gson.*
import me.kl0udy92.apart.config.AbstractConfig
import me.kl0udy92.apart.config.annotations.ConfigData
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.implementations.visual.TargetHUDModule
import java.io.FileReader
import java.io.FileWriter

@ConfigData("targethud-axis")
class TargetHUDAxisConfig : AbstractConfig() {

    override fun read() {
        kotlin.runCatching {
            val fileReader = FileReader(this.path.toFile())
            val jsonObject: JsonElement = JsonParser().parse(fileReader)
            fileReader.close()
            jsonObject.asJsonArray.forEach {
                val axisObject = it.asJsonObject
                ((Main.moduleManager.getModuleByClass(TargetHUDModule().javaClass)) as TargetHUDModule).x =
                    axisObject.get("X").asInt
                ((Main.moduleManager.getModuleByClass(TargetHUDModule().javaClass)) as TargetHUDModule).y =
                    axisObject.get("Y").asInt
            }
        }.onFailure { it.printStackTrace() }
    }

    override fun write() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonArray = JsonArray()
        val axisElements = JsonObject()
        axisElements.addProperty(
            "X",
            ((Main.moduleManager.getModuleByClass(TargetHUDModule().javaClass)) as TargetHUDModule).x
        )
        axisElements.addProperty(
            "Y",
            ((Main.moduleManager.getModuleByClass(TargetHUDModule().javaClass)) as TargetHUDModule).y
        )
        jsonArray.add(axisElements)
        kotlin.runCatching {
            val fileWriter = FileWriter(this.path.toFile())
            gson.toJson(jsonArray, fileWriter)
            fileWriter.close()
        }.onFailure { it.printStackTrace() }
    }

}