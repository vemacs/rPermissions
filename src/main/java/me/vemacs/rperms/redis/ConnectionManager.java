package me.vemacs.rperms.redis;

import lombok.Getter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

public class ConnectionManager {
    @Getter
    JedisPool pool;
    PubSubListener psl;

    public ConnectionManager(String redisServer, int port, String redisPass, Set<MessageHandler> channels) {
        if (redisPass != null && (redisPass.equals("") || redisPass.equals("none")))
            redisPass = null;
        pool = new JedisPool(new JedisPoolConfig(), redisServer, port, 0, redisPass);
        psl = new PubSubListener(MessageHandler.getPrefix(), channels);
    }

    public void end() {
        psl.poison();
        pool.destroy();
    }
}