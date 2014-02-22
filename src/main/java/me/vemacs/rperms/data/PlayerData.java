package me.vemacs.rperms.data;

import lombok.Data;
import lombok.NonNull;
import me.vemacs.rperms.rPermissions;
import org.bukkit.Bukkit;
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
}
