package me.vemacs.rperms.redis;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import redis.clients.jedis.Jedis;

@Data
public abstract class MessageHandler {
    @Getter
    private static final String prefix = "rperms-";
    @Getter
    private static final String delim = "@end-origin";
    @NonNull
    String channel;
    @NonNull
    String name;
    @NonNull
    private boolean ignoreSelf;

    /**
     * Sends a message to the channel that this handler belongs to
     * @param message the contents of the message
     */
    public void send(String message) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        jedis.publish(prefix + channel, name + delim + message);
        ConnectionManager.getPool().returnResource(jedis);
    }

    /**
     * Called when a message is sent to the channel that this handler belongs to
     * @param origin the origin (backend) of the message
     * @param message the contents of the message
     */
    public abstract void onReceive(String origin, String message);
}
