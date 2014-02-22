package me.vemacs.rperms.backends;

import me.vemacs.rperms.data.Group;
import me.vemacs.rperms.data.PlayerData;
import me.vemacs.rperms.rPermissions;
import me.vemacs.rperms.redis.ConnectionManager;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisBackend implements Backend {
    @Override
    public void saveGroup(Group group) {

    }

    @Override
    public void savePlayerData(PlayerData playerData) {

    }

    @Override
    public PlayerData loadPlayerData(String player) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        player = player.toLowerCase();
        try {
            if (jedis.exists("player:" + player))
                return new PlayerData(player,
                        jedis.hget("player:" + player, "prefix"),
                        rPermissions.getGroups().get(jedis.hget("player:" + player, "group"))
                );
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
        return new PlayerData(player, "", rPermissions.getGroups().get("default"));
    }

    @Override
    public Group loadGroup(String name) {
        Jedis jedis = ConnectionManager.getPool().getResource();
        name = name.toLowerCase();
        try {
            if (jedis.exists("group:" + name)) {
                Set<String> permSet = jedis.smembers("perms:" + name);
                Map<String, Boolean> perms = new HashMap<>();
                for (String perm : permSet)
                    perms.put(perm, !perm.startsWith("-"));
                List<String> ancestorList = Arrays.asList(jedis.hget("group:" + name, "ancestors").split(","));
                List<Group> ancestors = new ArrayList<>();
                for (String ancestor : ancestorList)
                    ancestors.add(loadGroup(ancestor));
                return new Group(name, "", perms, ancestors);
            }
        } finally {
            ConnectionManager.getPool().returnResource(jedis);
        }
        return new Group(name, "", new HashMap<String, Boolean>(), Collections.<Group>emptyList());
    }
}
