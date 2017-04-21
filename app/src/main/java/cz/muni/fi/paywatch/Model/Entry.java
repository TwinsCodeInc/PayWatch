package cz.muni.fi.paywatch.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Jirka on 14.04.2017.
 */

public class Entry extends RealmObject {

    private Double sum;
    private Integer category;
    private Integer account;
    private Date date;

    public Entry(Double sum, Date date, Integer categoryId, Integer accountId ) {
        this.sum = sum;
        this.category = categoryId;
        this.account = accountId;
        this.date = date;
    }

    public Entry() {
        super();
    }

    public Double getSum() {
        return sum;
    }

    public Integer getCategory() {
        return category;
    }

    public Integer getAccount() {
        return account;
    }

    public Date getDate() {
        return date;
    }
}

