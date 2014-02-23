package me.vemacs.rperms.backends;

import me.vemacs.rperms.data.Group;
import me.vemacs.rperms.data.PlayerData;

public interface Backend {
    public void loadGroups();

    public void saveGroup(Group group);

    public void savePlayerData(PlayerData playerData);

    public PlayerData loadPlayerData(String name);

    public Group loadGroup(String name);
}
