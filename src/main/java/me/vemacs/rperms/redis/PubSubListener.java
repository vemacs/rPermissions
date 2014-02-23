package me.vemacs.rperms.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PubSubListener {
    String prefix;
    ConnectionManager connectionManager;
    private JedisPubSub jpsh;
    Set<MessageHandler> channels;
    Jedis listener;

    public PubSubListener(final String prefix, Set<MessageHandler> channels) {
        this.prefix = prefix;
        this.channels = channels;
        jpsh = new JedisPubSubHandler(channels);
        for (final MessageHandler entry : channels) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jpsh.subscribe(prefix + entry.getChannel());
                }
            }, "rPermissions PubSub Listener").start();
        }
    }

    public void poison() {
        jpsh.unsubscribe();
        connectionManager.getPool().returnResource(listener);
    }
}

