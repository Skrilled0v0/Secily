package me.kl0udy92.apart.features.friends

import me.kl0udy92.apart.config.implementations.FriendsConfig
import me.kl0udy92.apart.core.Main

class FriendManager {

    val friends = mutableListOf<Friend>()

    init {
        this.friends.clear()
    }

    fun addFriend(friend: Friend) {
        this.friends.add(friend)
        Main.configManager.write(FriendsConfig())
    }

    fun removeFriend(string: String) {
        this.friends.remove(this.getFriendByName(string))
        Main.configManager.write(FriendsConfig())
    }

    fun getFriendByName(name: String): Friend? {
        return this.friends.stream().filter { it.name.equals(name, true) }.findFirst().orElse(null)
    }

}