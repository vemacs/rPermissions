package me.vemacs.rperms.listener;

import me.vemacs.rperms.rPermissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPrePlayerLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            // Load player data now - it might take some time to load!
            rPermissions.getPlayers().put(event.getName(), rPermissions.getBackend().loadPlayerData(event.getName()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLoginEarly(PlayerLoginEvent event) {
        // This runs early in the login process, so we can load data for players early.
        PermissionAttachment attachment = event.getPlayer().addAttachment(rPermissions.getInstance());
        rPermissions.getAttachments().put(event.getPlayer().getName(), attachment);
        rPermissions.getPlayers().get(event.getPlayer().getName()).register();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginMonitor(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            cleanUp(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cleanUp(event.getPlayer());
    }

    private void cleanUp(Player player) {
        rPermissions.getAttachments().get(player.getName()).remove();
        rPermissions.getPlayers().remove(player.getName());
    }
}
