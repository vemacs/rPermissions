package me.vemacs.rperms.backends.sql;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class DbGroup {
    private long id;
    private String name;
    private String prefix;
    private Map<String, Boolean> perms;
    private List<DbGroup> ancestors;
}
