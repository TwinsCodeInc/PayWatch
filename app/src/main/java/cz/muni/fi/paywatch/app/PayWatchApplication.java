package cz.muni.fi.paywatch.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;

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
                int[][] expenseCategories = {{R.string.init_exp_cat1, R.drawable.ic_cat_bills},
                                             {R.string.init_exp_cat2, R.drawable.ic_cat_food},
                                             {R.string.init_exp_cat3, R.drawable.ic_cat_entertainment},
                                             {R.string.init_exp_cat4, R.drawable.ic_cat_travel},
                                             {R.string.init_exp_cat5, R.drawable.ic_cat_transport},
                                             {R.string.init_exp_cat6, R.drawable.ic_cat_hobbies},
                                             {R.string.init_exp_cat7, R.drawable.ic_cat_gifts},
                                             {R.string.init_exp_cat8, R.drawable.ic_cat_clothing},
                                             {R.string.init_exp_cat9, R.drawable.ic_cat_other}};

                for (int[] row : expenseCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(getResources().getString(row[0]));
                    c.setType(Constants.CAT_TYPE_EXPENSE);
                    c.setIconId(row[1]);
                    c.setUseCount(0);
                    i += 1;
                }

                // Income categories
                int[][] incomeCategories = {{R.string.init_inc_cat1, R.drawable.ic_cat_extra},
                                            {R.string.init_inc_cat2, R.drawable.ic_cat_business},
                                            {R.string.init_inc_cat3, R.drawable.ic_cat_gifts},
                                            {R.string.init_inc_cat4, R.drawable.ic_cat_salary},
                                            {R.string.init_inc_cat5, R.drawable.ic_cat_loan}};
                for (int[] row : incomeCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(getResources().getString(row[0]));
                    c.setType(Constants.CAT_TYPE_INCOME);
                    c.setIconId(row[1]);
                    c.setUseCount(0);
                    i += 1;
                }

                // Accounts
                Account a = realm.createObject(Account.class, 0);
                a.setName(getResources().getString(R.string.init_acc1));
                a.setCurrency(Constants.ACCOUNT1_CURRENCY);
                a.setColor(ContextCompat.getColor(mContext, R.color.light_blue));
                Account b = realm.createObject(Account.class, 1);
                b.setName(getResources().getString(R.string.init_acc2));
                b.setCurrency(Constants.ACCOUNT2_CURRENCY);
                b.setColor(ContextCompat.getColor(mContext, R.color.lime));

            }
        }).name(Realm.DEFAULT_REALM_NAME)
          .schemaVersion(0)
          .deleteRealmIfMigrationNeeded()
          .build();

        Realm.setDefaultConfiguration(config);
    }
}
