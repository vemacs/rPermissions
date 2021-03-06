package me.vemacs.rperms;

import lombok.Getter;
import me.vemacs.rperms.backends.Backend;
import me.vemacs.rperms.backends.RedisBackend;
import me.vemacs.rperms.data.PermissionData;
import me.vemacs.rperms.listener.PlayerListener;
import me.vemacs.rperms.redis.ConnectionManager;
import me.vemacs.rperms.redis.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class rPermissions extends JavaPlugin {
    @Getter
    private static Plugin instance;
    @Getter
    private static Backend backend;
    @Getter
    private static Map<String, PermissionData> dataStore = new HashMap<>();
    @Getter
    private static ConnectionManager connectionManager;
    @Getter
    private static Map<String, PermissionAttachment> attachments = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        FileConfiguration config = getConfig();
        connectionManager = new ConnectionManager(config.getString("ip"), config.getInt("port"),
                config.getString("password"), Collections.<MessageHandler>emptySet());
        backend = new RedisBackend(config.getString("prefix"));
        backend.loadGroups();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        backend.saveGroupList();
    }
}