package cz.muni.fi.paywatch;

/**
 * Created by Daniel on 3. 4. 2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


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
        if (position == 0) {
            return OverviewFragment.newInstance("some parameter");
        } else if (position == 2) {
            return SettingsFragment.newInstance("some parameter");
        }
        return AddFragment.newInstance("some parameter");
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.menu_overview);
            case 1:
                return context.getResources().getString(R.string.menu_add);
            case 2:
                return context.getResources().getString(R.string.menu_settings);
        }
        return null;
    }
}
