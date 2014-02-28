package me.vemacs.rperms.backends.sql;

import lombok.Data;

@Data
public class DbPermission {
    private long id;
    private DbGroup group;
    private String permission;
    private boolean value;
}
