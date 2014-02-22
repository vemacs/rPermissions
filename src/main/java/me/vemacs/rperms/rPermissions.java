package me.vemacs.rperms;

import lombok.Getter;
import me.vemacs.rperms.backends.Backend;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class rPermissions extends JavaPlugin {
    @Getter
    private static Plugin instance;
    @Getter
    private static Backend backend;

    @Override
    public void onEnable() {
        instance = this;
    }
}