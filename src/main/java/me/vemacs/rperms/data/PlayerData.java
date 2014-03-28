package me.vemacs.rperms.data;

import me.vemacs.rperms.rPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.Map;

public class PlayerData extends PermissionData {
    public PlayerData(String name, String prefix, Map<String, Boolean> perms, List<String> ancestors) {
        super(name, prefix, perms, ancestors);
    }

    public void apply() {
        // getPlayerExact() isn't case-insensitive ;_;
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getName().equalsIgnoreCase(getName())) {
                for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions())
                    if (attachmentInfo.getAttachment().getPlugin().getName().equalsIgnoreCase("rpermissions")) {
                        player.removeAttachment(attachmentInfo.getAttachment());
                        break;
                    }
                PermissionAttachment attachment = player.addAttachment(rPermissions.getInstance());
                for (Map.Entry<String, Boolean> entry : calculatePerms().entrySet())
                    attachment.setPermission(entry.getKey(), entry.getValue());
            }
    }
}
