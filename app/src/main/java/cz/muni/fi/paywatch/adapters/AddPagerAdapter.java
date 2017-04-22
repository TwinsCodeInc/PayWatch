package cz.muni.fi.paywatch.adapters;

/**
 * Created by Daniel on 3. 4. 2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.fragments.AddSubFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AddPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String expenseTag;
    private String incomeTag;

    public AddPagerAdapter(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    public String getFragmentTag(int fragment) {
        switch (fragment) {
            case Constants.FSUB_EXPENSE:
                return expenseTag;
            case Constants.FSUB_INCOME:
                return incomeTag;
        }
        return "";
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // get the tags set by FragmentPagerAdapter
        switch (position) {
            case Constants.FSUB_EXPENSE:
                expenseTag = createdFragment.getTag();
                break;
            case Constants.FSUB_INCOME:
                incomeTag = createdFragment.getTag();
                break;
        }
        return createdFragment;
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
