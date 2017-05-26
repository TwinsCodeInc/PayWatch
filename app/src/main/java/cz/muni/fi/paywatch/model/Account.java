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
    private Integer color;
    private String currency;
    private Double budget;


    public Account(Integer id, String name, Integer color, String currency, Double budget) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.currency = currency;
        this.budget = budget;
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

    public Integer getColor() {
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

    public void setColor(Integer color) {
        this.color = color;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return name;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}
