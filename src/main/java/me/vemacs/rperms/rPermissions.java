package me.vemacs.rperms;

import lombok.Getter;
import me.vemacs.rperms.backends.Backend;
import me.vemacs.rperms.data.Group;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class rPermissions extends JavaPlugin {
    @Getter
    private static Plugin instance;
    @Getter
    private static Backend backend;
    @Getter
    private static Map<String, Group> groups = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
    }
}