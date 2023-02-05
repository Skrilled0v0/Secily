package me.skrilled.utils.friend;

import me.skrilled.SenseHeader;
import me.skrilled.utils.config.impl.ConfigFriends;

import java.util.LinkedList;

public class FriendManager {

    private final LinkedList<Friend> friends = new LinkedList<>();

    public FriendManager() {
        this.getFriends().clear();
    }

    public void addFriend(Friend friend) {
        this.getFriends().add(friend);
        SenseHeader.getSense.getConfigManager().save(new ConfigFriends());
    }

    public void removeFriend(Friend friend) {
        this.getFriends().remove(friend);
        SenseHeader.getSense.getConfigManager().save(new ConfigFriends());
    }

    public Friend getFriendByName(String name) {
        return this.getFriends().stream().filter(friend -> friend.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public LinkedList<Friend> getFriends() {
        return friends;
    }
}
