package me.kl0udy92.apart.config

import me.kl0udy92.apart.config.annotations.ConfigData
import me.kl0udy92.apart.config.interfaces.IConfig
import me.kl0udy92.apart.core.Main
import java.nio.file.Path
import java.nio.file.Paths

abstract class AbstractConfig : IConfig {

    val name: String
    val path: Path

    init {
        this.name = this.javaClass.getAnnotation(ConfigData::class.java).name
        this.path = Paths.get(Main.directory.toString(), "configs", "${this.name}.json")
    }

}