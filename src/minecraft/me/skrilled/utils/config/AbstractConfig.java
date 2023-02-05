package me.skrilled.utils.config;

import me.skrilled.SenseHeader;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractConfig {

    private final String name;
    private final Path path;

    public AbstractConfig() {
        this.name = this.getClass().getAnnotation(ConfigData.class).name();
        this.path = Paths.get(String.valueOf(SenseHeader.getSense.getDirectory()), "configs", this.getName() + ".sense");
    }

    public abstract void load();

    public abstract void save();

    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }
}
