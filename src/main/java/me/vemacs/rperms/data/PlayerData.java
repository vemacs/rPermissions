package me.vemacs.rperms.data;

import lombok.Data;
import lombok.NonNull;
import me.vemacs.rperms.rPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

@Data
public class PlayerData {
    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private Group group;

    public void register() {
        Player player = getPlayer();
        for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
            PermissionAttachment attachment = info.getAttachment();
            if (attachment == null)
                continue;
            attachment.unsetPermission(info.getPermission());
        }
        for (Group member : group.calculateGroupTree())
            member.attach(player);
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
