package me.vemacs.rperms.backends.ebean;

import javax.persistence.*;

@Embeddable
@Table(name = "permissions")
@UniqueConstraint(columnNames = {"group", "permission"})
public class DbPermission {
    private long id;
    private DbGroup group;
    private String permission;
    private boolean value;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public DbGroup getGroup() {
        return group;
    }

    public void setGroup(DbGroup group) {
        this.group = group;
    }

    @Column(nullable = false)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
