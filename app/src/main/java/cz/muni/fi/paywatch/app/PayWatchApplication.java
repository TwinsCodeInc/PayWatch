package cz.muni.fi.paywatch.app;

import android.app.Application;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.model.Category;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Daniel on 21. 4. 2017.
 */

public class PayWatchApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().initialData(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Expense categories
                int i = 0;
                for (String cName : Constants.expenseCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(cName);
                    c.setType(Constants.CAT_TYPE_EXPENSE);
                    c.setUseCount(0);
                    i += 1;
                }

                // Income categories
                for (String cName : Constants.incomeCategories){
                    Category c = realm.createObject(Category.class, i);
                    c.setName(cName);
                    c.setType(Constants.CAT_TYPE_INCOME);
                    c.setUseCount(0);
                    i += 1;
                }
            }
        }).name(Realm.DEFAULT_REALM_NAME)
          .schemaVersion(0)
          .deleteRealmIfMigrationNeeded()
          .build();

        Realm.setDefaultConfiguration(config);
    }
}
