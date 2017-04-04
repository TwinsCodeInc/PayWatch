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
import cz.muni.fi.paywatch.fragments.AddSubFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AddPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public AddPagerAdapter(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return AddSubFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case Constants.FSUB_EXPENSE:
                return context.getResources().getString(R.string.f_add_expense);
            case Constants.FSUB_INCOME:
                return context.getResources().getString(R.string.f_add_income);
        }
        return null;
    }
}
