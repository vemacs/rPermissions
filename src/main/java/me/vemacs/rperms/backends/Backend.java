package me.vemacs.rperms.backends;

import me.vemacs.rperms.data.PermissionData;

public interface Backend {
    public void loadGroups();

    public void saveGroupList();

    public void saveData(PermissionData group);

    public PermissionData loadData(String name);
}
