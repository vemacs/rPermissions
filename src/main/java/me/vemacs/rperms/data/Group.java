package me.vemacs.rperms.data;

import lombok.Data;
import lombok.NonNull;
import me.vemacs.rperms.rPermissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Group {
    @NonNull
    private String name;
    @NonNull
    private String prefix;
    @NonNull
    private Map<String, Boolean> perms;
    @NonNull
    private List<Group> ancestors;

    public void attach(Player p) {
        PermissionAttachment attachment = p.addAttachment(rPermissions.getInstance());
        for (Map.Entry<String, Boolean> entry : getPerms().entrySet())
            attachment.setPermission(entry.getKey(), entry.getValue());
    }

    public void update() {
        Group newGrp = rPermissions.getBackend().loadGroup(this.getName());
        setPrefix(newGrp.getPrefix());
        setPerms(newGrp.getPerms());
        setAncestors(newGrp.getAncestors());
        for (PlayerData data : rPermissions.getPlayers().values())
            if (data.getGroup().calculateGroupTree().contains(this)) {
                rPermissions.getInstance().getLogger().info("Updated group " + name + " for " + data.getName());
                data.register();
            }
    }

    public void save() {
        rPermissions.getBackend().saveGroup(this);
    }

    /*
    Adapted from Privileges by krinsdeath
     */

    public List<Group> calculateGroupTree() {
        List<Group> tree = new ArrayList<>();
        tree.add(0, this);
        for (Group top : ancestors) {
            if (top.getName().equalsIgnoreCase(getName()))
                continue;
            for (Group trunk : calculateBackwardTree(top))
                tree.add(0, trunk);
        }
        return tree;
    }

    private List<Group> calculateBackwardTree(Group group) {
        List<Group> tree = new ArrayList<>();
        tree.add(group);
        for (Group top : group.getAncestors()) {
            if (top.getName().equalsIgnoreCase(group.getName()))
                continue;
            for (Group trunk : calculateBackwardTree(top))
                tree.add(trunk);
        }
        return tree;
    }
}
