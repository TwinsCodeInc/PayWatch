package cz.muni.fi.paywatch.app;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
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

    // Change the category for all entries to "other"
    public void reCategorize(Integer sourceCatId, Integer catType) {
        int otherCatId = catType == Constants.CAT_TYPE_EXPENSE ? Constants.CAT_DEF_EXP_OTHER : Constants.CAT_DEF_INC_OTHER;
        realm.beginTransaction();
        RealmResults<Entry> realmResults = realm.where(Entry.class).equalTo("categoryId", sourceCatId).findAll();
        List<Entry> items = realm.copyFromRealm(realmResults);
        for (Entry item : items) {
            item.setCategoryId(otherCatId);
        }
        realm.copyToRealmOrUpdate(items);
        realm.commitTransaction();
    }

    // Remove category
    public void removeCategory(Integer catId) {
        realm.beginTransaction();
        realm.where(Category.class).equalTo("id", catId).findFirst().deleteFromRealm();
        realm.commitTransaction();
    }

    // Returns the next value for ID
    private Integer getCategoryNextId() {
        return realm.where(Category.class).max("id").intValue() + 1;
    }

    // Inserts new Category object into database
    public Integer addCategory(Integer type, String name, String icon) {
        realm.beginTransaction();
        Integer id = getCategoryNextId();
        Category c = new Category(id, name, icon, 0, type);
        realm.copyToRealm(c);
        realm.commitTransaction();
        return id;
    }

    // Updates category name and icon
    public void updateCategory(Integer id, String name, String icon) {
        realm.beginTransaction();
        Category c = getCategory(id);
        c.setName(name);
        c.setIcon(icon);
        realm.copyToRealm(c);
        realm.commitTransaction();
    }

    // Returns list of categories ordered by use frequency
    public List<Category> getCategories(Integer catType) {
        RealmResults<Category> realmResults = realm.where(Category.class).equalTo("type", catType).findAllSorted("useCount", Sort.DESCENDING);
        List<Category> items = realm.copyFromRealm(realmResults);
        return items;
    }

    // Returns list of categories ordered by name
    public List<Category> getCategoriesOrdered(Integer catType) {
        RealmResults<Category> realmResults = realm.where(Category.class).equalTo("type", catType).findAllSorted("name");
        List<Category> items = realm.copyFromRealm(realmResults);
        return items;
    }

    // Returns category name for its id
    public String getCategoryName(Integer id) {
        Category c = realm.where(Category.class).equalTo("id", id).findFirst();
        return (c != null) ? c.getName() : "";
    }

    // Returns category
    public Category getCategory(Integer catId) {
        return realm.where(Category.class).equalTo("id", catId).findFirst();
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

    // Returns list of accounts without one specific account
    public List<Account> getAccountsForTransfer(Integer id, String currency) {
        RealmResults<Account> realmResults = realm.where(Account.class)
                .notEqualTo("id", id)
                .equalTo("currency", currency)
                .findAllSorted("name", Sort.ASCENDING);
        List<Account> items = realm.copyFromRealm(realmResults);
        return items;
    }

    // Returns count of accounts
    public int getAccountsCount() {
        RealmResults<Account> realmResults = realm.where(Account.class).findAll();
        return realmResults.size();
    }

    // Returns the next value for ID
    private Integer getAccountNextId() {
        return realm.where(Account.class).max("id").intValue() + 1;
    }

    // Inserts new Account object into database
    public Integer addAccount(String name) {
        realm.beginTransaction();
        Integer id = getAccountNextId();
        Account a = new Account(id, name, R.color.materialcolorpicker__black, Constants.DEFAULT_CURRENCY, 0.0);  // default budget 0
        realm.copyToRealm(a);
        realm.commitTransaction();
        return id;
    }

    // Returns list of accounts
    public Account getAccount(Integer id) {
        return realm.where(Account.class).equalTo("id", id).findFirst();
    }

    // Returns currency for account
    public String getAccountCurrency(Integer id) {
        Account a = realm.where(Account.class).equalTo("id", id).findFirst();
        return (a != null) ? a.getCurrency() : "";
    }

    // Returns color for account
    public Integer getAccountColor(Integer id) {
        Account a = realm.where(Account.class).equalTo("id", id).findFirst();
        return (a != null) ? a.getColor() : 0;
    }

    // Returns account's overall balance
    public Double getAccountBalance(Integer accountId) {
        return realm.where(Entry.class).equalTo("accountId", accountId).sum("sum").doubleValue();
    }

    // Updates account name
    public void updateAccountName(Integer id, String name) {
        realm.beginTransaction();
        Account a = getAccount(id);
        a.setName(name);
        realm.copyToRealm(a);
        realm.commitTransaction();
    }

    // Updates account budget
    public void updateAccountBudget(Integer id, Double budget) {
        realm.beginTransaction();
        Account a = getAccount(id);
        a.setBudget(budget);
        realm.copyToRealm(a);
        realm.commitTransaction();
    }

    // Updates account color
    public void updateAccountColor(Integer id, Integer color) {
        realm.beginTransaction();
        Account a = getAccount(id);
        a.setColor(color);
        realm.copyToRealm(a);
        realm.commitTransaction();
    }

    // Updates account currency
    public void updateAccountCurrency(Integer id, String currency) {
        realm.beginTransaction();
        Account a = getAccount(id);
        a.setCurrency(currency);
        realm.copyToRealm(a);
        realm.commitTransaction();
    }

    // Removes account
    public void removeAccount(Integer id) {
        realm.beginTransaction();
        RealmResults<Entry> resultEntries = realm.where(Entry.class).equalTo("accountId", id).findAll();
        resultEntries.deleteAllFromRealm();
        RealmResults<Account> resultAccounts = realm.where(Account.class).equalTo("id", id).findAll();
        resultAccounts.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void removeEntry(float id) {
        realm.beginTransaction();
        RealmResults<Entry> entry = realm.where(Entry.class)
                .equalTo("id", id)
                .findAll();
        entry.deleteAllFromRealm();
        realm.commitTransaction();
    }

    // Returns count of entries withinn requested account
    public int getEntriesCountForAccount(Integer id) {
        RealmResults<Entry> resultEntries = realm.where(Entry.class).equalTo("accountId", id).findAll();
        return resultEntries.size();
    }

    public RealmResults<Entry> getEntriesForAccount(Integer accountId) {
        return realm.where(Entry.class).equalTo("accountId", accountId).findAllSorted("date", Sort.DESCENDING);
    }
}
