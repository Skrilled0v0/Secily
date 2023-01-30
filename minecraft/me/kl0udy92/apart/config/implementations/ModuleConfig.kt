package me.kl0udy92.apart.config.implementations

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.kl0udy92.apart.config.AbstractConfig
import me.kl0udy92.apart.config.annotations.ConfigData
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.*
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.io.FileReader
import java.io.FileWriter

@ConfigData("module")
class ModuleConfig : AbstractConfig() {

    override fun read() {
        kotlin.runCatching {
            val fileReader = FileReader(this.path.toFile())
            val jsonObject: JsonObject = JsonParser().parse(fileReader).asJsonObject
            fileReader.close()
            jsonObject.entrySet().forEach {
                val module = Main.moduleManager.getModuleByName(it.key)
                if (module != null) {
                    val moduleElement = it.value.asJsonObject
                    module.state = moduleElement.get("State").asBoolean
                    module.keyCode = Keyboard.getKeyIndex(moduleElement.get("KeyBind").asString)
                    module.visible = moduleElement.get("Visible").asBoolean
                    moduleElement.get("Options").asJsonObject.entrySet().forEach { entrySet ->
                        val option = module.getOption(entrySet.key)
                        if (option != null) {
                            if (option is DoubleOption) {
                                option.value = entrySet.value.asDouble
                            } else if (option is BooleanOption) {
                                option.value = entrySet.value.asBoolean
                            } else if (option is ArrayOption) {
                                option.value = entrySet.value.asString
                            } else if (option is StringOption) {
                                option.value = entrySet.value.asString
                            } else if (option is ColourOption) {
                                option.value = Color(entrySet.value.asJsonObject.get("RGB").asInt)
                                option.alpha = entrySet.value.asJsonObject.get("Alpha").asInt
                            } else if (option is MultiBooleanOption) {
                                option.value.forEach { booleanOption ->
                                    booleanOption.value = entrySet.value.asJsonObject.get(booleanOption.name).asBoolean
                                }
                            }
                        }
                    }
                }
            }
        }.onFailure { it.printStackTrace() }
    }

    override fun write() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonObject = JsonObject()
        Main.moduleManager.modules.forEach {
            val modulesElement = JsonObject()
            modulesElement.addProperty("State", it.state)
            modulesElement.addProperty("KeyBind", Keyboard.getKeyName(it.keyCode))
            modulesElement.addProperty("Visible", it.visible)
            val optionsElement = JsonObject()
            it.options.forEach { option ->
                if (option is DoubleOption) optionsElement.addProperty(option.name, option.value.toString())
                else if (option is ArrayOption) optionsElement.addProperty(option.name, option.value)
                else if (option is BooleanOption) optionsElement.addProperty(option.name, option.value.toString())
                else if (option is StringOption) optionsElement.addProperty(option.name, option.value)
                else if (option is ColourOption) {
                    val colourElements = JsonObject()
                    colourElements.addProperty("RGB", option.value.rgb)
                    colourElements.addProperty("Alpha", option.alpha)
                    optionsElement.add(option.name, colourElements)
                }
                else if (option is MultiBooleanOption) {
                    val boolOptionElements = JsonObject()
                    option.value.forEach { boolOption ->
                        boolOptionElements.addProperty(boolOption.name, boolOption.value)
                    }
                    optionsElement.add(option.name, boolOptionElements)
                }
            }
            modulesElement.add("Options", optionsElement)
            jsonObject.add(it.name, modulesElement)
        }
        kotlin.runCatching {
            val fileWriter = FileWriter(this.path.toFile())
            gson.toJson(jsonObject, fileWriter)
            fileWriter.close()
        }.onFailure { it.printStackTrace() }
    }

}