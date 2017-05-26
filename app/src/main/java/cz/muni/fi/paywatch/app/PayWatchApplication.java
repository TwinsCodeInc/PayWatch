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
                String[][] expenseCategories = {{getResources().getString(R.string.init_exp_cat1), "ic_cat_bills"},
                                                {getResources().getString(R.string.init_exp_cat2), "ic_cat_food"},
                                                {getResources().getString(R.string.init_exp_cat3), "ic_cat_entertainment"},
                                                {getResources().getString(R.string.init_exp_cat4), "ic_cat_travel"},
                                                {getResources().getString(R.string.init_exp_cat5), "ic_cat_transport"},
                                                {getResources().getString(R.string.init_exp_cat6), "ic_cat_hobbies"},
                                                {getResources().getString(R.string.init_exp_cat7), "ic_cat_gifts"},
                                                {getResources().getString(R.string.init_exp_cat8), "ic_cat_clothing"},
                                                {getResources().getString(R.string.init_exp_cat9), "ic_cat_other"}};

                for (String[] row : expenseCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(row[0]);
                    c.setType(Constants.CAT_TYPE_EXPENSE);
                    c.setIcon(row[1]);
                    c.setUseCount(0);
                    i += 1;
                }

                // Income categories
                String[][] incomeCategories = {{getResources().getString(R.string.init_inc_cat1), "ic_cat_extra"},
                                               {getResources().getString(R.string.init_inc_cat2), "ic_cat_business"},
                                               {getResources().getString(R.string.init_inc_cat3), "ic_cat_gifts"},
                                               {getResources().getString(R.string.init_inc_cat4), "ic_cat_salary"},
                                               {getResources().getString(R.string.init_inc_cat5), "ic_cat_loan"},
                                               {getResources().getString(R.string.init_inc_cat6), "ic_cat_other"}};
                for (String[] row : incomeCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(row[0]);
                    c.setType(Constants.CAT_TYPE_INCOME);
                    c.setIcon(row[1]);
                    c.setUseCount(0);
                    i += 1;
                }

                // Accounts
                Account a = realm.createObject(Account.class, 0);
                a.setName(getResources().getString(R.string.init_acc1));
                a.setCurrency(Constants.ACCOUNT1_CURRENCY);
                a.setColor(ContextCompat.getColor(mContext, R.color.light_blue));
                a.setBudget(0.0);
                Account b = realm.createObject(Account.class, 1);
                b.setName(getResources().getString(R.string.init_acc2));
                b.setCurrency(Constants.ACCOUNT2_CURRENCY);
                b.setColor(ContextCompat.getColor(mContext, R.color.materialcolorpicker__black));
                b.setBudget(0.0);

            }
        }).name(Realm.DEFAULT_REALM_NAME)
          .schemaVersion(0)
          .deleteRealmIfMigrationNeeded()
          .build();

        Realm.setDefaultConfiguration(config);
    }
}
