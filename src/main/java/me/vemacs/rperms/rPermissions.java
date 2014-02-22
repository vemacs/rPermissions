package me.vemacs.rperms;

import lombok.Getter;
import me.vemacs.rperms.backends.Backend;
import me.vemacs.rperms.backends.RedisBackend;
import me.vemacs.rperms.data.Group;
import me.vemacs.rperms.redis.ConnectionManager;
import me.vemacs.rperms.redis.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class rPermissions extends JavaPlugin {
    @Getter
    private static Plugin instance;
    @Getter
    private static Backend backend;
    @Getter
    private static Map<String, Group> groups = new HashMap<>();
    @Getter
    private static ConnectionManager connectionManager;

    @Override
    public void onEnable() {
        instance = this;
        FileConfiguration config = getConfig();
        connectionManager = new ConnectionManager(config.getString("ip"), config.getInt("port"),
                config.getString("password"), Collections.<MessageHandler>emptySet());
        backend = new RedisBackend(config.getString("prefix"));
    }
}