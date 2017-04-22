package cz.muni.fi.paywatch.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Daniel on 21. 4. 2017.
 */

public class Account extends RealmObject {

    @PrimaryKey
    private Integer id;
    private String name;
    private String color;
    private String currency;

    public Account(Integer id, String name, String color, String currency) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.currency = currency;
    }

    public Account() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getCurrency() {
        return currency;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
