package me.vemacs.rperms.data;

import lombok.Data;
import lombok.NonNull;
import me.vemacs.rperms.rPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

@Data
public class PlayerData {
    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private Group group;

    public void update() {
        Player player = getPlayer();
    }

    public void setup() {
        Player player = getPlayer();
        for (Group member : group.calculateGroupTree())
            member.attach(player);
        player.recalculatePermissions();
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
