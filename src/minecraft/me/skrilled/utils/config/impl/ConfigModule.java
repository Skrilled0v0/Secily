package me.skrilled.utils.config.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.config.AbstractConfig;
import me.skrilled.utils.config.ConfigData;
import org.lwjgl.input.Keyboard;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@ConfigData(name = "module")
public class ConfigModule extends AbstractConfig {

    @Override
    public void load() {
        try {
            final FileReader fileReader = new FileReader(this.getPath().toFile());
            final JsonObject jsonObject = new JsonParser().parse(fileReader).getAsJsonObject();
            fileReader.close();
            jsonObject.entrySet().forEach(entry -> {
                final ModuleHeader module = ModuleManager.getModuleByName(entry.getKey());
                if (module != null) {
                    final JsonObject moduleElement = entry.getValue().getAsJsonObject();
                    boolean state = moduleElement.get("State").getAsBoolean();
                    module.setEnabled(state);
                    module.setKey(Keyboard.getKeyIndex(moduleElement.get("KeyBind").getAsString()));
                    module.setCanView(moduleElement.get("Visible").getAsBoolean());
                    moduleElement.get("Options").getAsJsonObject().entrySet().forEach(entrySet -> {
                        final ValueHeader option = module.getValues(entrySet.getKey());
                        if (option != null) {
                            if (option.getValueType() == ValueHeader.ValueType.DOUBLE) {
                                option.setDoubleCurrentValue(entrySet.getValue().getAsDouble());
                            }
                            if (option.getValueType() == ValueHeader.ValueType.BOOLEAN) {
                                option.setOptionOpen(entrySet.getValue().getAsBoolean());
                            }
                            if (option.getValueType() == ValueHeader.ValueType.ENUM_TYPE) {
                                option.setCurrentEnumType(entrySet.getValue().getAsString());
                            }
                            if (option.getValueType() == ValueHeader.ValueType.STRING) {
                                option.setStrValue(entrySet.getValue().getAsString());
                            }
                            if (option.getValueType() == ValueHeader.ValueType.COLOR) {
                                option.setConfigColorValue(entrySet.getValue().getAsInt());
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final JsonObject jsonObject = new JsonObject();
        ModuleManager.mList.forEach(module -> {
            final JsonObject modulesElement = new JsonObject();
            modulesElement.addProperty("State", module.isEnabled());
            modulesElement.addProperty("KeyBind", Keyboard.getKeyName(module.getKey()));
            modulesElement.addProperty("Visible", module.isCanView());
            final JsonObject optionsElement = new JsonObject();
            module.getValueList().forEach(option -> optionsElement.addProperty(option.getValueName(), module.getValue(option).toString()));
            modulesElement.add("Options", optionsElement);
            jsonObject.add(module.getModuleName(), modulesElement);
        });
        try {
            final FileWriter fileWriter = new FileWriter(this.getPath().toFile());
            gson.toJson(jsonObject, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
