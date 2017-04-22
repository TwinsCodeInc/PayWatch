package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cz.muni.fi.paywatch.model.Entry;
import cz.muni.fi.paywatch.adapters.EntryAdapter;
import cz.muni.fi.paywatch.R;
import io.realm.RealmResults;
import io.realm.Sort;

public class OverviewFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

//        TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
//        tv.setText(getArguments().getString("msg"));

        // Find views by ID
        final ListView entryList = (ListView) v.findViewById(R.id.entry_list);

        RealmResults<Entry> entries = realm.where(Entry.class).findAllSorted("date", Sort.DESCENDING);
        EntryAdapter adapter = new EntryAdapter(getActivity(), entries);
        entryList.setAdapter(adapter);

        return v;
    }

    // Refresh all the data
    public void refreshControls() {

    }

    public static OverviewFragment newInstance() {
        OverviewFragment f = new OverviewFragment();
        return f;
    }

}