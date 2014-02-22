package me.vemacs.rperms.backends;

import me.vemacs.rperms.data.Group;
import me.vemacs.rperms.data.PlayerData;
import me.vemacs.rperms.rPermissions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An abstract class for JSON-based backends (flatfile/NoSQL). To be specific, this class serializes JSON data..
 */
public abstract class AbstractJSONBackend implements Backend {
    private static final ThreadLocal<JSONParser> parserThreadLocal = new ThreadLocal<JSONParser>() {
        @Override
        protected JSONParser initialValue() {
            return new JSONParser();
        }
    };

    public static String serializeGroup(Group group) {
        JSONObject object = new JSONObject();
        object.put("name", group.getName());
        object.put("prefix", group.getPrefix());
        List<String> ancestors = new ArrayList<>();
        for (Group group1 : group.getAncestors())
            ancestors.add(group1.getName());
        object.put("ancestors", ancestors);
        object.put("permissions", group.getPerms());
        return object.toJSONString();
    }

    public static String serializePlayerData(PlayerData playerData) {
        JSONObject object = new JSONObject();
        object.put("name", playerData.getName());
        object.put("prefix", playerData.getPrefix());
        object.put("group", playerData.getGroup().getName());
        return object.toJSONString();
    }

    public static Group unserializeGroup(String serialized) throws ParseException {
        Object deserialized = parserThreadLocal.get().parse(serialized);
        if (!(deserialized instanceof JSONObject)) {
            throw new IllegalArgumentException("JSON data found was not a JSON object (corrupted data?)");
        }
        return reconstructGroup((JSONObject)deserialized);
    }

    public static PlayerData unserializePlayerData(String serialized) throws ParseException {
        Object deserialized = parserThreadLocal.get().parse(serialized);
        if (!(deserialized instanceof JSONObject)) {
            throw new IllegalArgumentException("JSON data found was not a JSON object (corrupted data?)");
        }
        return reconstructPlayerData((JSONObject) deserialized);
    }

    public static Group reconstructGroup(JSONObject data) {
        if (!(data.get("name") instanceof String) ||
                !(data.get("prefix") instanceof String) ||
                !(data.get("ancestors") instanceof List) ||
                !(data.get("permissions") instanceof Map))
            throw new IllegalArgumentException("JSON data found was not in a valid format (corrupted data?)");
        List<Group> ancestors = new ArrayList<>();
        for (String string : (List<String>)data.get("ancestors")) {
            ancestors.add(rPermissions.getGroups().get(string));
        }
        return new Group((String)data.get("name"), (String)data.get("prefix"),
                (Map<String, Boolean>)data.get("permissions"), ancestors);
    }

    public static PlayerData reconstructPlayerData(JSONObject data) {
        if (!(data.get("name") instanceof String) ||
                !(data.get("prefix") instanceof String) ||
                !(data.get("group") instanceof String))
            throw new IllegalArgumentException("JSON data found was not in a valid format (corrupted data?)");
        return new PlayerData((String)data.get("name"), (String)data.get("prefix"), rPermissions.getGroups().get(data.get("group")));
    }
}
