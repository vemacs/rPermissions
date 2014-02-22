package me.vemacs.rperms.redis;

import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JedisPubSubHandler extends JedisPubSub {
    Set<MessageHandler> handlers;
    Map<String, MessageHandler> channels = new HashMap<>();

    public JedisPubSubHandler(Set<MessageHandler> handlers) {
        this.handlers = handlers;
        for (MessageHandler handler : handlers)
            channels.put(MessageHandler.getPrefix() + handler.getChannel(), handler);
    }

    @Override
    public void onMessage(String channel, String message) {
        String[] split = message.split(MessageHandler.getDelim());
        MessageHandler handler = channels.get(channel);
        if (!(handler.isIgnoreSelf() && split[0].equals(handler.getName())))
            handler.onReceive(split[0], split[1]);
    }

    @Override
    public void onPMessage(String s, String s2, String s3) {
    }

    @Override
    public void onSubscribe(String s, int i) {
    }

    @Override
    public void onUnsubscribe(String s, int i) {
    }

    @Override
    public void onPUnsubscribe(String s, int i) {
    }

    @Override
    public void onPSubscribe(String s, int i) {
    }
}
