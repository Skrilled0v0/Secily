package me.kl0udy92.apart.config.implementations

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.kl0udy92.apart.config.AbstractConfig
import me.kl0udy92.apart.config.annotations.ConfigData
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.screen.menu.account.Account
import java.io.FileReader
import java.io.FileWriter

@ConfigData("accounts")
class AccountsConfig : AbstractConfig() {

    override fun read() {
        kotlin.runCatching {
            val fileReader = FileReader(this.path.toFile())
            val jsonObject: JsonObject = JsonParser().parse(fileReader).asJsonObject
            fileReader.close()
            jsonObject.entrySet().forEach {
                Main.accountManager.addAccount(Account(it.key, it.value.asJsonObject.get("Password").asString))
            }
        }.onFailure { it.printStackTrace() }
    }

    override fun write() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonObject = JsonObject()
        Main.accountManager.accounts.forEach {
            val accoutnsElement = JsonObject()
            accoutnsElement.addProperty("Password", it.password)
            jsonObject.add(it.name, accoutnsElement)
        }
        kotlin.runCatching {
            val fileWriter = FileWriter(this.path.toFile())
            gson.toJson(jsonObject, fileWriter)
            fileWriter.close()
        }.onFailure { it.printStackTrace() }
    }

}