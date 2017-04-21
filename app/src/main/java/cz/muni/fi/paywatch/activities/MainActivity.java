package cz.muni.fi.paywatch.activities;

import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.SectionsPagerAdapter;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.custom.CustomViewPager;
import cz.muni.fi.paywatch.model.Account;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private CustomViewPager mViewPager;
    private BottomNavigationView bottomNavigationView;
    private Menu hamburgerMenu;
    private Integer currentAccountId;

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
                        break;
                    case R.id.action_add:
                        mViewPager.setCurrentItem(Constants.F_ADD);
                        break;
                    case R.id.action_settings:
                        mViewPager.setCurrentItem(Constants.F_SETTINGS);
                        break;
                }
                return true;
            }
        });

        // Set default bottom bar navigation action
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set navigation listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        hamburgerMenu = navigationView.getMenu();

        // Refresh accounts in hamburger menu
        refreshAccounts();

        // Set default bottom bar navigation action
        MenuItem i = hamburgerMenu.getItem(0);
        i.setChecked(true);
        currentAccountId = i.getItemId();

    }

    // Refreshes the menu of accounts
    private void refreshAccounts() {
        hamburgerMenu.clear();
        List<Account> items = RealmController.with(this).getAccounts();
        for (Account a : items) {
            hamburgerMenu.add(R.id.group_accounts, a.getId(), Menu.NONE, a.getName());
        }
        hamburgerMenu.setGroupCheckable(R.id.group_accounts, true, true);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == Constants.ITEM_ADD_ACCOUNT) {
            showAddAccountDialog();
        } else if (id == Constants.ITEM_TRANSFER) {
            Toast.makeText(this, "Transfer between two accounts", Toast.LENGTH_SHORT).show();
        } else {
            // Set current account
            currentAccountId = id;
            Toast.makeText(this, "account id: " + currentAccountId.toString(), Toast.LENGTH_SHORT).show();
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

    public Integer getCurrentAccountId() {
        return currentAccountId;
    }
}
