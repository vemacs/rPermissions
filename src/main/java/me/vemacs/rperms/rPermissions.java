package me.vemacs.rperms;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class rPermissions extends JavaPlugin {
    @Getter
    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }
}