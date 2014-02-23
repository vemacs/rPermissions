package me.vemacs.rperms.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

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
        final Set<String> channels1 = new HashSet<>();
        for (MessageHandler handler : channels) {
            channels1.add(prefix + handler.getChannel());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                listener = connectionManager.getPool().getResource();
                listener.subscribe(jpsh, channels1.toArray(new String[channels1.size()]));
            }
        }, "rPermissions PubSub Listener").start();
    }

    public void poison() {
        jpsh.unsubscribe();
        connectionManager.getPool().returnResource(listener);
    }
}

