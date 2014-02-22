package me.vemacs.rperms.data;

import lombok.Data;
import lombok.NonNull;
import me.vemacs.rperms.rPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlayerData {
    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private List<Group> groups = new ArrayList<>();

    public void setup() {
        Player player = getPlayer();
        for (Group group : getGroups())  {
            for (Group ancestor : group.getAncestors())
                ancestor.attach(player);
            group.attach(player);
        }

    }

    public Player getPlayer() {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getName().equalsIgnoreCase(name))
                return player;
        return null;
    }

    public void save() {
        rPermissions.getBackend().savePlayerData(this);
    }
}
