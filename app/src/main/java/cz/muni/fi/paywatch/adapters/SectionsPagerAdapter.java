package cz.muni.fi.paywatch.adapters;

/**
 * Created by Daniel on 3. 4. 2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.fragments.AddFragment;
import cz.muni.fi.paywatch.fragments.OverviewFragment;
import cz.muni.fi.paywatch.fragments.SettingsFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == Constants.F_OVERVIEW) {
            return OverviewFragment.newInstance();
        } else if (position == Constants.F_SETTINGS) {
            return SettingsFragment.newInstance();
        }
        return AddFragment.newInstance();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case Constants.F_OVERVIEW:
                return context.getResources().getString(R.string.menu_overview);
            case Constants.F_ADD:
                return context.getResources().getString(R.string.menu_add);
            case Constants.F_SETTINGS:
                return context.getResources().getString(R.string.menu_settings);
        }
        return null;
    }
}
