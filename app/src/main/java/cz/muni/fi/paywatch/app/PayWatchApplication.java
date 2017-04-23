package cz.muni.fi.paywatch.app;

import android.app.Application;
import android.content.Context;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.model.Account;
import cz.muni.fi.paywatch.model.Category;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Daniel on 21. 4. 2017.
 */

public class PayWatchApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().initialData(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Expense categories
                int i = 0;
                for (String cName : Constants.EXPENSE_CATEGORIES){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(cName);
                    c.setType(Constants.CAT_TYPE_EXPENSE);
                    c.setUseCount(0);
                    i += 1;
                }

                // Income categories
                for (String cName : Constants.INCOME_CATEGORIES){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(cName);
                    c.setType(Constants.CAT_TYPE_INCOME);
                    c.setUseCount(0);
                    i += 1;
                }

                // Accounts
                Account a = realm.createObject(Account.class, 0);
                a.setName(Constants.ACCOUNT1_NAME);
                a.setCurrency(Constants.ACCOUNT1_CURRENCY);
                Account b = realm.createObject(Account.class, 1);
                b.setName(Constants.ACCOUNT2_NAME);
                b.setCurrency(Constants.ACCOUNT2_CURRENCY);

            }
        }).name(Realm.DEFAULT_REALM_NAME)
          .schemaVersion(0)
          .deleteRealmIfMigrationNeeded()
          .build();

        Realm.setDefaultConfiguration(config);
    }
}
