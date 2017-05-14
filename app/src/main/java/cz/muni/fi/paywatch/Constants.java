package cz.muni.fi.paywatch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 4. 4. 2017.
 */

public class Constants {
    // Main fragments
    public static final int F_OVERVIEW = 0;
    public static final int F_ADD = 1;
    public static final int F_SETTINGS = 2;

    // Sub fragments of ADD section
    public static final int FSUB_EXPENSE = 0;
    public static final int FSUB_INCOME = 1;

    // Categories
    public static final int CAT_TYPE_EXPENSE = 0;
    public static final int CAT_TYPE_INCOME = 1;
    public static final String EXPENSE_CATEGORIES[] = {"Bills", "Food", "Entertainment", "Travel",
                                                      "Transportation", "Hobbies", "Gifts", "Clothing", "Other"};
    public static final String INCOME_CATEGORIES[] = {"Extra", "Business", "Gifts", "Salary", "Loan"};
    public static final String CAT_ICONS[] = {"ic_cat_bills",
                                             "ic_cat_business",
                                             "ic_cat_clothing",
                                             "ic_cat_entertainment",
                                             "ic_cat_extra",
                                             "ic_cat_food",
                                             "ic_cat_gifts",
                                             "ic_cat_hobbies",
                                             "ic_cat_loan",
                                             "ic_cat_other",
                                             "ic_cat_salary",
                                             "ic_cat_travel",
                                             "ic_cat_transport",
                                             "ic_cat_shopping"};
    // Accounts
    public static final String ACCOUNT1_CURRENCY = "EUR";
    public static final String ACCOUNT2_CURRENCY = "USD";
    public static final String DEFAULT_CURRENCY = "EUR";
    public static final int ITEM_ADD_ACCOUNT = 1001;
    public static final int ITEM_TRANSFER = 1002;
}
