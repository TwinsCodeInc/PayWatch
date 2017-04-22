package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.AddPagerAdapter;
import cz.muni.fi.paywatch.custom.CustomViewPager;

public class AddFragment extends BaseFragment {

    private CustomViewPager mViewPager;
    private AddPagerAdapter mAddPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        // Find views by ID
        final TabLayout tabSelector = (TabLayout) v.findViewById(R.id.tab_selector);
        mViewPager = (CustomViewPager) v.findViewById(R.id.add_container);

        // Set selector tabs - Expense and Income
        tabSelector.addTab(tabSelector.newTab().setText(R.string.f_add_expense));
        tabSelector.addTab(tabSelector.newTab().setText(R.string.f_add_income));

        // Create the adapter that will return a fragment
        mAddPagerAdapter = new AddPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mAddPagerAdapter);
        // Set default page
        mViewPager.setCurrentItem(Constants.FSUB_EXPENSE);
        // Disable swipe
        mViewPager.setPagingEnabled(false);

       return v;
    }

    public String getCurrentSubFragmentTag() {
        return mAddPagerAdapter.getFragmentTag(mViewPager.getCurrentItem());
    }

    public String getSubFragmentTag(int fragment) {
        return mAddPagerAdapter.getFragmentTag(fragment);
    }

    public static AddFragment newInstance() {
        AddFragment f = new AddFragment();
        return f;
    }

}