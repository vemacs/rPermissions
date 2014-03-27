package me.vemacs.rperms.data;

import lombok.Data;
import lombok.NonNull;
import me.vemacs.rperms.rPermissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PermissionData {
    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private Map<String, Boolean> perms;
    @NonNull
    private List<String> ancestors;

    public void reload() {
        PermissionData data = rPermissions.getBackend().loadData(name);
        this.name = data.getName();
        this.prefix = data.getPrefix();
        this.perms = data.getPerms();
        this.ancestors = data.getAncestors();
    }

    private List<PermissionData> getRealAncestors() {
        List<PermissionData> realAncestors = new ArrayList<>();
        for (String ancestor : ancestors)
            realAncestors.add(rPermissions.getDataStore().get(ancestor));
        return realAncestors;
    }

    /*
    Adapted from Privileges by krinsdeath
     */

    public List<PermissionData> calculateGroupTree() {
        List<PermissionData> tree = new ArrayList<>();
        tree.add(0, this);
        for (PermissionData top : getRealAncestors()) {
            if (top.getName().equalsIgnoreCase(getName()))
                continue;
            for (PermissionData trunk : calculateBackwardTree(top))
                tree.add(0, trunk);
        }
        return tree;
    }

    private List<PermissionData> calculateBackwardTree(PermissionData group) {
        List<PermissionData> tree = new ArrayList<>();
        tree.add(group);
        for (PermissionData top : group.getRealAncestors()) {
            if (top.getName().equalsIgnoreCase(group.getName()))
                continue;
            for (PermissionData trunk : calculateBackwardTree(top))
                tree.add(trunk);
        }
        return tree;
    }
}
