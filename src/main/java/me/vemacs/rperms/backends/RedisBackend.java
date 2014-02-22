package me.vemacs.rperms.backends;

import com.google.common.base.Joiner;
import me.vemacs.rperms.data.Group;
import me.vemacs.rperms.data.PlayerData;
import me.vemacs.rperms.rPermissions;
import me.vemacs.rperms.redis.ConnectionManager;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisBackend implements Backend {
    private static String groupPrefix;
    private static String playerPrefix;
    private static String permPrefix;

    public RedisBackend(String prefix) {
        groupPrefix = prefix + "-group:";
        playerPrefix = prefix + "-player:";
        playerPrefix = prefix + "-perms:";
    }

    private static final Joiner joiner = Joiner.on(",").skipNulls();

    public void saveGroup(Group group) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        String name = group.getName().toLowerCase();
        List<String> perms = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : group.getPerms().entrySet())
            perms.add(entry.getValue() ? "" : "-" + entry.getKey());
        try {
            jedis.hset(groupPrefix + name, "prefix", group.getPrefix());
            jedis.hset(groupPrefix + name, "ancestors", joiner.join(group.getAncestors()));
            jedis.del(permPrefix + group);
            for (String perm : perms)
                jedis.sadd(permPrefix + group, perm);
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
    }

    @Override
    public void savePlayerData(PlayerData playerData) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        String name = playerData.getName().toLowerCase();
        try {
            jedis.hset(playerPrefix + name, "prefix", playerData.getPrefix());
            jedis.hset(playerPrefix + name, "group", playerData.getGroup().getName().toLowerCase());
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
    }

    @Override
    public PlayerData loadPlayerData(String player) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        player = player.toLowerCase();
        try {
            if (jedis.exists(playerPrefix + player))
                return new PlayerData(player,
                        jedis.hget(playerPrefix + player, "prefix"),
                        rPermissions.getGroups().get(jedis.hget(playerPrefix + player, "group").toLowerCase())
                );
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
        return new PlayerData(player, "", rPermissions.getGroups().get("default"));
    }

    @Override
    public Group loadGroup(String group) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        group = group.toLowerCase();
        try {
            if (jedis.exists(groupPrefix + group)) {
                Set<String> permSet = jedis.smembers(permPrefix + group);
                Map<String, Boolean> perms = new HashMap<>();
                for (String perm : permSet)
                    perms.put(perm, !perm.startsWith("-"));
                List<String> ancestorList = Arrays.asList(jedis.hget(groupPrefix + group, "ancestors").split(","));
                List<Group> ancestors = new ArrayList<>();
                for (String ancestor : ancestorList)
                    ancestors.add(rPermissions.getGroups().containsKey(ancestor.toLowerCase()) ?
                            rPermissions.getGroups().get(ancestor.toLowerCase()) : loadGroup(ancestor));
                return new Group(group, "", perms, ancestors);
            }
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
        return new Group(group, "", new HashMap<String, Boolean>(), Collections.<Group>emptyList());
    }
}
