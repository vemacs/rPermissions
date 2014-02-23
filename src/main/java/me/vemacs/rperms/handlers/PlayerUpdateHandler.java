package me.vemacs.rperms.handlers;

import me.vemacs.rperms.rPermissions;
import me.vemacs.rperms.redis.MessageHandler;

public class PlayerUpdateHandler extends MessageHandler {
    public PlayerUpdateHandler(String channel, String name, boolean ignoreSelf) {
        super(rPermissions.getConnectionManager(), channel, name, ignoreSelf);
    }

    @Override
    public void onReceive(String origin, String message) {
        rPermissions.getPlayers().get(message.toLowerCase()).reload();
    }
}
