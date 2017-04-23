package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.activities.MainActivity;
import cz.muni.fi.paywatch.adapters.EntryAdapter;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.RealmResults;

public class OverviewFragment extends Fragment {

    private MainActivity mainActivity;
    private ListView entryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        // Get activity pointer
        mainActivity = (MainActivity) getActivity();

        // Find views by ID
        entryList = (ListView) v.findViewById(R.id.entry_list);

        // Refresh controls
        refreshControls();

        return v;
    }

    private void refreshEntriesList() {
        RealmResults<Entry> entries = RealmController.with(this).getEntriesForAccount(mainActivity.getCurrentAccountId());
        EntryAdapter adapter = new EntryAdapter(getActivity(), entries);
        entryList.setAdapter(adapter);
    }

    // Refresh all the data
    public void refreshControls() {
        refreshEntriesList();
    }

    public static OverviewFragment newInstance() {
        OverviewFragment f = new OverviewFragment();
        return f;
    }

}