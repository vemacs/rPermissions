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
    private static String existingKey;

    public RedisBackend(String prefix) {
        groupPrefix = prefix + "-group:";
        playerPrefix = prefix + "-player:";
        permPrefix = prefix + "-perms:";
        existingKey = prefix + "-existing";
    }

    private static final Joiner joiner = Joiner.on(",").skipNulls();

    @Override
    public void loadGroups() {
        Jedis jedis = ConnectionManager.getPool().getResource();
        try {
            if (jedis.exists(existingKey))
                for (String grpStr : jedis.smembers(existingKey))
                    loadGroup(grpStr);
            else
                loadGroup("default");
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
    }

    @Override
    public void saveGroup(Group group) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        String name = group.getName().toLowerCase();
        List<String> perms = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : group.getPerms().entrySet())
            perms.add(entry.getValue() ? "" : "-" + entry.getKey());
        try {
            jedis.hset(groupPrefix + name, "prefix", group.getPrefix());
            jedis.hset(groupPrefix + name, "ancestors", joiner.join(group.getAncestors()));
            Set<String> remotePerms = jedis.smembers(permPrefix + group);
            for (String perm : remotePerms)
                if (!perms.contains(perm))
                    jedis.srem(permPrefix + name, perm);
            for (String perm : perms)
                if (!remotePerms.contains(perm))
                    jedis.sadd(permPrefix + name, perm);
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
        PlayerData newData;
        try {
            if (jedis.exists(playerPrefix + player)) newData = new PlayerData(player,
                    jedis.hget(playerPrefix + player, "prefix"),
                    rPermissions.getGroups().get(jedis.hget(playerPrefix + player, "group").toLowerCase())
            );
            else
                newData = new PlayerData(player, "", rPermissions.getGroups().get("default"));
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
        rPermissions.getPlayers().put(player, newData);
        return newData;
    }

    @Override
    public Group loadGroup(String group) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        group = group.toLowerCase();
        Group newGrp;
        try {
            if (jedis.exists(groupPrefix + group)) {
                Set<String> permSet = jedis.smembers(permPrefix + group);
                Map<String, Boolean> perms = new HashMap<>();
                for (String perm : permSet)
                    perms.put(perm, !perm.startsWith("-"));
                List<String> ancestorList = Arrays.asList(jedis.hget(groupPrefix + group, "ancestors").split(","));
                List<Group> ancestors = new ArrayList<>();
                for (String ancestor : ancestorList) {
                    Group ancestorGrp;
                    if (rPermissions.getGroups().containsKey(ancestor.toLowerCase()))
                        ancestorGrp = rPermissions.getGroups().get(ancestor.toLowerCase());
                    else
                        ancestorGrp = loadGroup(ancestor);
                    ancestors.add(ancestorGrp);
                }
                newGrp = new Group(group, "", perms, ancestors);
            } else {
                newGrp = new Group(group, "", new HashMap<String, Boolean>(), Collections.<Group>emptyList());
            }
            jedis.sadd(existingKey, group);
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
        rPermissions.getGroups().put(group, newGrp);
        return newGrp;
    }
}
