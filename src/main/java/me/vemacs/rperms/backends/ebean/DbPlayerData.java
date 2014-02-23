package me.vemacs.rperms.backends.ebean;

import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "player_data")
@UniqueConstraint(columnNames = "username")
public class DbPlayerData {
    private long id;
    private String username;
    private String prefix;
    private DbGroup group;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @ManyToOne
    @Column(nullable = false)
    public DbGroup getGroup() {
        return group;
    }

    public void setGroup(DbGroup group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbPlayerData that = (DbPlayerData) o;

        if (!username.equals(that.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
