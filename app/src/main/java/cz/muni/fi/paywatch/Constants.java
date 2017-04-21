package cz.muni.fi.paywatch;

/**
 * Created by Daniel on 4. 4. 2017.
 */

public interface Constants {
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
    public static final String expenseCategories[] = {"Bills", "Food", "Entertainment", "Travel",
                                                      "Transportation", "Hobbies", "Gifts", "Clothing", "Other"};
    public static final String incomeCategories[] = {"Extra", "Business", "Gifts", "Salary", "Loan"};


}
