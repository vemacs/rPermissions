package me.vemacs.rperms.backends.sql;

import lombok.Data;

@Data
public class DbPlayerData {
    private long id;
    private String username;
    private String prefix;
    private DbGroup group;
}
