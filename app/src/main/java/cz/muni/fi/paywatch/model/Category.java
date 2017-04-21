package cz.muni.fi.paywatch.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Daniel on 21. 4. 2017.
 */

public class Category extends RealmObject {

    @PrimaryKey
    private Integer id;
    private String name;
    private String icon;
    private Integer useCount;
    private Integer type;

    @Override
    public String toString() {
        return name;
    }

    public Category(Integer id, String name, String icon, Integer useCount, Integer type) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.useCount = useCount;
        this.type = type;
    }

    public Category() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }
}

