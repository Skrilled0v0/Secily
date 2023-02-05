package me.skrilled.utils.config;


import me.skrilled.SenseHeader;
import me.skrilled.utils.config.impl.ConfigFriends;
import me.skrilled.utils.config.impl.ConfigModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

public class ConfigManager {

    private final LinkedList<AbstractConfig> abstractConfigs = new LinkedList<>();
    private final Path directory = Paths.get(String.valueOf(SenseHeader.getSense.getDirectory()),"configs");

    public ConfigManager() {
        this.getConfigs().clear();
        this.addConfigs(new ConfigModule(),new ConfigFriends());
        if(!this.directory.toFile().exists()) this.directory.toFile().mkdirs();
    }

    public void addConfigs(AbstractConfig... abstractConfigs) {
        this.getConfigs().addAll(Arrays.asList(abstractConfigs));
    }

    public void load(AbstractConfig abstractConfig) {
        if(abstractConfig != null && abstractConfig.getPath().toFile().exists()) abstractConfig.load();
    }

    public void loadAll() {
        this.getConfigs().stream().filter(abstractConfig -> abstractConfig.getPath().toFile().exists()).forEach(AbstractConfig::load);
    }

    public void save(AbstractConfig abstractConfig) {
        if(abstractConfig != null) abstractConfig.save();
    }

    public void saveAll() {
        this.getConfigs().forEach(abstractConfig -> {
            if(!abstractConfig.getPath().toFile().exists()) {
                try {
                    Files.createDirectories(abstractConfig.getPath().getParent());
                    abstractConfig.getPath().toFile().createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            abstractConfig.save();
        });
    }

    public LinkedList<AbstractConfig> getConfigs() {
        return abstractConfigs;
    }

    public Path getDirectory() {
        return directory;
    }
}
