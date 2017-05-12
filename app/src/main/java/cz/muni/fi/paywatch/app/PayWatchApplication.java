package cz.muni.fi.paywatch.app;

import android.app.Application;
import android.content.Context;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
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
                String expenseCategories[] = {getResources().getString(R.string.init_exp_cat1),
                                              getResources().getString(R.string.init_exp_cat2),
                                              getResources().getString(R.string.init_exp_cat3),
                                              getResources().getString(R.string.init_exp_cat4),
                                              getResources().getString(R.string.init_exp_cat5),
                                              getResources().getString(R.string.init_exp_cat6),
                                              getResources().getString(R.string.init_exp_cat7),
                                              getResources().getString(R.string.init_exp_cat8),
                                              getResources().getString(R.string.init_exp_cat9)};

                for (String cName : expenseCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(cName);
                    c.setType(Constants.CAT_TYPE_EXPENSE);
                    c.setUseCount(0);
                    i += 1;
                }

                // Income categories
                String incomeCategories[] = {getResources().getString(R.string.init_inc_cat1),
                                             getResources().getString(R.string.init_inc_cat2),
                                             getResources().getString(R.string.init_inc_cat3),
                                             getResources().getString(R.string.init_inc_cat4),
                                             getResources().getString(R.string.init_inc_cat5)};
                for (String cName : incomeCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(cName);
                    c.setType(Constants.CAT_TYPE_INCOME);
                    c.setUseCount(0);
                    i += 1;
                }

                // Accounts
                Account a = realm.createObject(Account.class, 0);
                a.setName(getResources().getString(R.string.init_acc1));
                a.setCurrency(Constants.ACCOUNT1_CURRENCY);
                Account b = realm.createObject(Account.class, 1);
                b.setName(getResources().getString(R.string.init_acc2));
                b.setCurrency(Constants.ACCOUNT2_CURRENCY);

            }
        }).name(Realm.DEFAULT_REALM_NAME)
          .schemaVersion(0)
          .deleteRealmIfMigrationNeeded()
          .build();

        Realm.setDefaultConfiguration(config);
    }
}
