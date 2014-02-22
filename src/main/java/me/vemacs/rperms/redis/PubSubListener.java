package me.vemacs.rperms.redis;

import redis.clients.jedis.JedisPubSub;

import java.util.Set;

public class PubSubListener {
    String prefix;
    private JedisPubSub jpsh;
    Set<MessageHandler> channels;

    public PubSubListener(final String prefix, Set<MessageHandler> channels) {
        this.prefix = prefix;
        this.channels = channels;
        jpsh = new JedisPubSubHandler(channels);
        for (final MessageHandler entry : channels) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ConnectionManager.getPool().getResource().subscribe(jpsh, prefix + entry.getChannel());
                }
            }).start();
        }
    }

    public void poison() {
        jpsh.unsubscribe();
        ConnectionManager.getPool().returnResource(ConnectionManager.getPool().getResource());
    }
}

