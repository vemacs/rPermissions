package me.vemacs.rperms.backends;

import me.vemacs.rperms.data.Group;
import me.vemacs.rperms.data.PlayerData;

public class RedisBackend implements Backend {
    @Override
    public void saveGroup(Group group) {

    }

    @Override
    public void savePlayerData(PlayerData playerData) {

    }

    @Override
    public PlayerData loadPlayerData(String name) {
        return null;
    }

    @Override
    public Group loadGroup(String name) {
        return null;
    }
}
