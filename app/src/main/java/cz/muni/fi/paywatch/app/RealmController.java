package cz.muni.fi.paywatch.app;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.model.Account;
import cz.muni.fi.paywatch.model.Category;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Daniel on 21. 4. 2017.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    // Increments the useCount number after the category was used
    public void incrementCategoryUseCount(Integer catId) {
        Category c = realm.where(Category.class).equalTo("id", catId).findFirst();
        if (c != null) {
            realm.beginTransaction();
            c.incrementUseCount();
            realm.commitTransaction();
        }
    }

    // Returns list of categories
    public List<Category> getCategories(Integer catType) {
        RealmResults<Category> realmResults = realm.where(Category.class).equalTo("type", catType).findAllSorted("useCount", Sort.DESCENDING);
        List<Category> items = realm.copyFromRealm(realmResults);
        return items;
    }

    // Inserts new Entry object into database
    public void addEntry(Entry e) {
        realm.beginTransaction();
        realm.copyToRealm(e);
        realm.commitTransaction();
    }

    // Returns list of accounts
    public List<Account> getAccounts() {
        RealmResults<Account> realmResults = realm.where(Account.class).findAllSorted("name", Sort.ASCENDING);
        List<Account> items = realm.copyFromRealm(realmResults);
        return items;
    }

    // Returns the next value for ID
    private Integer getAccountNextId() {
        return realm.where(Account.class).max("id").intValue() + 1;
    }

    // Inserts new Account object into database
    public Integer addAccount(String name) {
        // TODO: fixnut defaultne hodnoty ake sa pouziju pri vytvoreni accountu
        realm.beginTransaction();
        Integer id = getAccountNextId();
        Account a = new Account(id, name, "", Constants.DEFAULT_CURRENCY);
        realm.copyToRealm(a);
        realm.commitTransaction();
        return id;
    }

    // Returns list of accounts
    public Account getAccount(Integer id) {
        return realm.where(Account.class).equalTo("id", id).findFirst();
    }

}
