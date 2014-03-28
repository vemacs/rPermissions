package me.vemacs.rperms.backends;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.vemacs.rperms.data.GroupData;
import me.vemacs.rperms.data.PermissionData;
import me.vemacs.rperms.rPermissions;
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
    private static final Gson gson = new Gson();


    @Override
         public void loadGroups() {
        Jedis jedis = rPermissions.getConnectionManager().getPool().getResource();
        try {
            if (jedis.exists(existingKey)) {
                Set<String> groups = gson.fromJson(jedis.get(existingKey), new TypeToken<Set<String>>(){}.getType());
                for (String grpStr : groups)
                    loadData(groupPrefix + grpStr);
            } else {
                loadData(groupPrefix + "default");
            }
        } finally {
            rPermissions.getConnectionManager().getPool().returnResource(jedis);
        }
    }

    @Override
    public void saveGroupList() {
        Jedis jedis = rPermissions.getConnectionManager().getPool().getResource();
        try {
            jedis.set(existingKey, gson.toJson(rPermissions.getDataStore().keySet()));
        } finally {
            rPermissions.getConnectionManager().getPool().returnResource(jedis);
        }
    }

    @Override
    public void saveData(PermissionData data) {
        Jedis jedis = rPermissions.getConnectionManager().getPool().getResource();
        try {
            jedis.set(groupPrefix + data.getName(), gson.toJson(data));
        } finally {
            rPermissions.getConnectionManager().getPool().returnResource(jedis);
        }
    }

    @Override
    public PermissionData loadData(String name) {
        Jedis jedis = rPermissions.getConnectionManager().getPool().getResource();
        try {
            PermissionData data;
            if (jedis.exists(groupPrefix + name))
               data = gson.fromJson(jedis.get(groupPrefix + name), GroupData.class);
            else
                data = new GroupData(name, "", new HashMap<String, Boolean>(),
                        new ArrayList<String>());
            rPermissions.getDataStore().put(name, data);
            return data;
        } finally {
            rPermissions.getConnectionManager().getPool().returnResource(jedis);
        }
    }
}
