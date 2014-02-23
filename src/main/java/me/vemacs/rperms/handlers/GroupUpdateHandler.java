package me.vemacs.rperms.handlers;

import me.vemacs.rperms.rPermissions;
import me.vemacs.rperms.redis.MessageHandler;

public class GroupUpdateHandler extends MessageHandler {
    public GroupUpdateHandler(String channel, String name, boolean ignoreSelf) {
        super(rPermissions.getConnectionManager(), channel, name, ignoreSelf);
    }

    @Override
    public void onReceive(String origin, String message) {
        rPermissions.getGroups().get(message.toLowerCase()).reload();
    }
}
