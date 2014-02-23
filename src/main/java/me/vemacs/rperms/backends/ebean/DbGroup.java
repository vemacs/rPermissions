package me.vemacs.rperms.backends.ebean;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "groups")
@UniqueConstraint(columnNames = "name")
public class DbGroup {
    private long id;
    private String name;
    private String prefix;
    private Map<String, Boolean> perms;
    private List<DbGroup> ancestors;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @MapKey(name = "name")
    @ManyToOne
    @JoinColumn(name = "group")
    @Column(nullable = false)
    public Map<String, Boolean> getPerms() {
        return perms;
    }

    public void setPerms(Map<String, Boolean> perms) {
        this.perms = perms;
    }

    @ManyToMany
    @JoinTable(name = "permissions")
    public List<DbGroup> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<DbGroup> ancestors) {
        this.ancestors = ancestors;
    }
}
