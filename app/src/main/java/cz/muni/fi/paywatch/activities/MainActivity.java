package cz.muni.fi.paywatch.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.SectionsPagerAdapter;
import cz.muni.fi.paywatch.app.Helpers;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.custom.CustomViewPager;
import cz.muni.fi.paywatch.fragments.AddFragment;
import cz.muni.fi.paywatch.fragments.AddSubFragment;
import cz.muni.fi.paywatch.fragments.OverviewFragment;
import cz.muni.fi.paywatch.fragments.SettingsFragment;
import cz.muni.fi.paywatch.model.Account;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private CustomViewPager mViewPager;
    private BottomNavigationView bottomNavigationView;
    private Menu hamburgerMenu;
    private Integer currentAccountId;
    private Date currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views by ID
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Set default fragment
        mViewPager.setCurrentItem(Constants.F_ADD);

        // Disable swipe
        mViewPager.setPagingEnabled(false);

        // Bottom navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_overview:
                        mViewPager.setCurrentItem(Constants.F_OVERVIEW);
                        OverviewFragment fragmentOverview = (OverviewFragment) getSupportFragmentManager().findFragmentByTag(mSectionsPagerAdapter.getFragmentTag(Constants.F_OVERVIEW));
                        fragmentOverview.refreshControls();
                        break;
                    case R.id.action_add:
                        mViewPager.setCurrentItem(Constants.F_ADD);
                        AddFragment fragmentAdd = (AddFragment) getSupportFragmentManager().findFragmentByTag(mSectionsPagerAdapter.getFragmentTag(Constants.F_ADD));
                        AddSubFragment fragmentAddSubExpense = (AddSubFragment) getSupportFragmentManager().findFragmentByTag(fragmentAdd.getSubFragmentTag(Constants.FSUB_EXPENSE));
                        AddSubFragment fragmentAddSubIncome = (AddSubFragment) getSupportFragmentManager().findFragmentByTag(fragmentAdd.getSubFragmentTag(Constants.FSUB_INCOME));
                        // We need to refresh the currency on both fragments
                        fragmentAddSubExpense.refreshControls();
                        fragmentAddSubIncome.refreshControls();

                        break;
                    case R.id.action_settings:
                        mViewPager.setCurrentItem(Constants.F_SETTINGS);
                        SettingsFragment fragmentSettings = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(mSectionsPagerAdapter.getFragmentTag(Constants.F_SETTINGS));
                        fragmentSettings.refreshControls();
                        break;
                }
                return true;
            }
        });

        // Set default bottom bar navigation action
        bottomNavigationView.getMenu().getItem(Constants.F_ADD).setChecked(true);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                // Hide keyboard
                Helpers.hideSoftKeyboard(MainActivity.this);
                // Refresh header labels
                refreshHamburgerAccountLabel();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set navigation listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        hamburgerMenu = navigationView.getMenu();

        // Refresh accounts in hamburger menu
        refreshAccounts();

    }

    // Refreshes the menu of accounts
    public void refreshAccounts() {
        boolean wasSelected = false;
        hamburgerMenu.clear();
        List<Account> items = RealmController.with(this).getAccounts();
        for (Account a : items) {
            hamburgerMenu.add(R.id.group_accounts, a.getId(), Menu.NONE, a.getName());
            if (a.getId() == currentAccountId) {
                hamburgerMenu.findItem(a.getId()).setChecked(true);
                wasSelected = true;
            }
        }
        hamburgerMenu.setGroupCheckable(R.id.group_accounts, true, true);
        // If nothing was selected, select the first item
        if (!wasSelected) {
            MenuItem i = hamburgerMenu.findItem(getSettingAccountId());  // first account
            i = i == null ? hamburgerMenu.getItem(0) : i;
            i.setChecked(true);
            currentAccountId = i.getItemId();
        }
        // Rest of the menu
        hamburgerMenu.add(R.id.group_operations, Constants.ITEM_ADD_ACCOUNT, Menu.NONE, R.string.navigation_add_account);
        hamburgerMenu.add(R.id.group_operations, Constants.ITEM_TRANSFER, Menu.NONE, R.string.navigation_transfer);
        hamburgerMenu.findItem(Constants.ITEM_ADD_ACCOUNT).setIcon(R.drawable.ic_add_black_24dp);
        hamburgerMenu.findItem(Constants.ITEM_TRANSFER).setIcon(R.drawable.ic_swap_horiz_black_24dp);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == Constants.ITEM_ADD_ACCOUNT) {
            showAddAccountDialog();
        } else if (id == Constants.ITEM_TRANSFER) {
            Intent intent = new Intent(this, TransferActivity.class);
            startActivity(intent);
        } else {
            // Set current account
            currentAccountId = id;
            // Save account position as last used to te settings
            setSettingAccountId(id);
            // Refresh SETTINGS section
            SettingsFragment fragmentSettings = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(mSectionsPagerAdapter.getFragmentTag(Constants.F_SETTINGS));
            fragmentSettings.refreshControls();
            // Refresh ADD section
            AddFragment fragmentAdd = (AddFragment) getSupportFragmentManager().findFragmentByTag(mSectionsPagerAdapter.getFragmentTag(Constants.F_ADD));
            AddSubFragment fragmentAddSubExpense = (AddSubFragment) getSupportFragmentManager().findFragmentByTag(fragmentAdd.getSubFragmentTag(Constants.FSUB_EXPENSE));
            AddSubFragment fragmentAddSubIncome = (AddSubFragment) getSupportFragmentManager().findFragmentByTag(fragmentAdd.getSubFragmentTag(Constants.FSUB_INCOME));
            // We need to refresh the currency on both fragments
            fragmentAddSubExpense.refreshControls();
            fragmentAddSubIncome.refreshControls();
            // Refresh OVERVIEW section
            OverviewFragment fragmentOverview = (OverviewFragment) getSupportFragmentManager().findFragmentByTag(mSectionsPagerAdapter.getFragmentTag(Constants.F_OVERVIEW));
            fragmentOverview.refreshControls();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Shows dialog for a new account name
    public void showAddAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.acc_dialog_title);

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.acc_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if (!name.isEmpty()) {
                    Integer id = RealmController.with(MainActivity.this).addAccount(name);
                    refreshAccounts();
                    Toast.makeText(MainActivity.this, R.string.acc_added, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.acc_dialog_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Returns the currently selected account id
    public Integer getCurrentAccountId() {
        return currentAccountId;
    }

    public Date getCurrentMonthStart() {
        if ( currentMonth == null ) {
            currentMonth = new Date();
        }
        Calendar defDate = Calendar.getInstance();
        defDate.setTime(currentMonth);
        defDate.set(Calendar.DATE, 1);
        defDate.set(Calendar.MILLISECOND, 0);
        defDate.set(Calendar.SECOND, 0);
        defDate.set(Calendar.MINUTE, 0);
        defDate.set(Calendar.HOUR_OF_DAY, 0);
        return defDate.getTime();
    }

    public Date getCurrentMonthEnd() {
        if ( currentMonth == null ) {
            currentMonth = new Date();
        }
        Calendar defDate = Calendar.getInstance();
        defDate.setTime(currentMonth);
        defDate.set(Calendar.DATE, defDate.getActualMaximum(Calendar.DATE));
        defDate.set(Calendar.MILLISECOND, 999);
        defDate.set(Calendar.SECOND, 59);
        defDate.set(Calendar.MINUTE, 59);
        defDate.set(Calendar.HOUR_OF_DAY, 23);
        return defDate.getTime();
    }

    public void setCurrentMonth(Date month) {
        this.currentMonth = month;
    }

    // Returns id of the current section / fragment
    public int getCurrentSectionId() {
        return mViewPager.getCurrentItem();
    }

    public void refreshHamburgerAccountLabel() {
        TextView hamburgerCurrency = (TextView) findViewById(R.id.hamburger_currency_label);
        if (hamburgerCurrency != null) {
            hamburgerCurrency.setText(getResources().getString(R.string.hamburger_acc_label_sub) + ": " + RealmController.with(this).getAccountCurrency(getCurrentAccountId()));
        }
        TextView hamburgerBalance = (TextView) findViewById(R.id.hamburger_account_balance_label);
        if (hamburgerBalance != null) {
            hamburgerBalance.setText(getResources().getString(R.string.hamburger_acc_label_main) + ": " +
                    RealmController.with(this).getAccountBalance(getCurrentAccountId()).toString());
        }
    }

    public Integer getSettingAccountId() {
        SharedPreferences prefs = this.getSharedPreferences("cz.muni.fi.paywatch", Context.MODE_PRIVATE);
        return prefs.getInt("lastUsedAccountId", 0);
    }

    public void setSettingAccountId(Integer id) {
        SharedPreferences prefs = this.getSharedPreferences("cz.muni.fi.paywatch", Context.MODE_PRIVATE);
        prefs.edit().putInt("lastUsedAccountId", id).apply();
    }

}
