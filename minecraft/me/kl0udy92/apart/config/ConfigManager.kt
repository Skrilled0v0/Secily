package me.kl0udy92.apart.config

import me.kl0udy92.apart.core.Main
import org.reflections.Reflections
import java.nio.file.Files
import java.nio.file.Paths

class ConfigManager {

    val configs = mutableListOf<AbstractConfig>()
    private val directory = Paths.get(Main.directory.toString(), "configs")

    init {
        this.configs.clear()
        if (!this.directory.toFile().exists()) this.directory.toFile().mkdirs()
        Reflections("me.kl0udy92.${Main.name.lowercase()}.config.implementations").getSubTypesOf(AbstractConfig::class.java)
            .forEach {
                runCatching {
                    this.configs.add(
                        it.getDeclaredConstructor().newInstance()
                    )
                }.onFailure { exception -> exception.printStackTrace() }
            }
    }

    fun read(abstractConfig: AbstractConfig) {
        if (abstractConfig.path.toFile().exists()) abstractConfig.read()
    }

    fun readAll() {
        this.configs.filter { it.path.toFile().exists() }.forEach { it.read() }
    }

    fun write(abstractConfig: AbstractConfig) {
        if (abstractConfig.path.toFile().exists()) abstractConfig.write()
    }

    fun writeAll() {
        this.configs.forEach {
            kotlin.runCatching {
                Files.createDirectories(it.path.parent)
                it.path.toFile().createNewFile()
            }.onFailure { exception -> exception.printStackTrace() }
            it.write()
        }
    }

}