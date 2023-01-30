package me.kl0udy92.apart.config.implementations

import com.google.gson.*
import me.kl0udy92.apart.config.AbstractConfig
import me.kl0udy92.apart.config.annotations.ConfigData
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.friends.Friend
import java.io.FileReader
import java.io.FileWriter

@ConfigData("friends")
class FriendsConfig : AbstractConfig() {

    override fun read() {
        kotlin.runCatching {
            val fileReader = FileReader(this.path.toFile())
            val jsonObject: JsonElement = JsonParser().parse(fileReader)
            fileReader.close()
            jsonObject.asJsonArray.forEach {
                Main.friendManager.addFriend(Friend(it.asJsonObject.get("Name").asString))
            }
        }.onFailure { it.printStackTrace() }
    }

    override fun write() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonArray = JsonArray()
        Main.friendManager.friends.forEach {
            val friendsElement = JsonObject()
            friendsElement.addProperty("Name", it.name)
            jsonArray.add(friendsElement)
        }
        kotlin.runCatching {
            val fileWriter = FileWriter(this.path.toFile())
            gson.toJson(jsonArray, fileWriter)
            fileWriter.close()
        }.onFailure { it.printStackTrace() }
    }

}