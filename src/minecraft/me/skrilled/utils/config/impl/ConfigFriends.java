package me.skrilled.utils.config.impl;

import com.google.gson.*;
import me.skrilled.SenseHeader;
import me.skrilled.utils.config.AbstractConfig;
import me.skrilled.utils.config.ConfigData;
import me.skrilled.utils.friend.Friend;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@ConfigData(name = "friends")
public class ConfigFriends extends AbstractConfig {

    @Override
    public void load() {
        try {
            final FileReader fileReader = new FileReader(this.getPath().toFile());
            final JsonElement jsonObject = new JsonParser().parse(fileReader);
            fileReader.close();
            jsonObject.getAsJsonArray().forEach(friendElement -> {
                final JsonObject friendObject = friendElement.getAsJsonObject();
                SenseHeader.getSense.getFriendManager().addFriend(new Friend(friendObject.get("Name").getAsString()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final JsonArray jsonArray = new JsonArray();
        SenseHeader.getSense.getFriendManager().getFriends().forEach(friend -> {
            final JsonObject friendsElement = new JsonObject();
            friendsElement.addProperty("Name", friend.getName());
            jsonArray.add(friendsElement);
        });
        try {
            final FileWriter fileWriter = new FileWriter(this.getPath().toFile());
            gson.toJson(jsonArray, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
