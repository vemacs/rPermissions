package me.vemacs.rperms.data;

import java.util.List;
import java.util.Map;

public class GroupData extends PermissionData {
    public GroupData(String name, String prefix, Map<String, Boolean> perms, List<String> ancestors) {
        super(name, prefix, perms, ancestors);
    }
}
