package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.activities.MainActivity;
import cz.muni.fi.paywatch.adapters.EntryAdapter;
import cz.muni.fi.paywatch.adapters.OverviewAdapter;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.RealmResults;

public class OverviewFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView entryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        // Get activity pointer
        mainActivity = (MainActivity) getActivity();

        // Find views by ID
        entryList = (RecyclerView) v.findViewById(R.id.recycler_view);
        entryList.setHasFixedSize(true);
        entryList.setLayoutManager(new StaggeredGridLayoutManager(2,1));

        // Refresh controls
        refreshControls();

        return v;
    }

    private void refreshEntriesList() {
        //RealmResults<Entry> entries = RealmController.with(this).getEntriesForAccount(mainActivity.getCurrentAccountId());
        ArrayList<String> cards = new ArrayList<>();
        cards.add("String 1");
        cards.add("String 2");
        cards.add("String 3");
        cards.add("String 4");
        //cards.add("String 5");
        OverviewAdapter adapter = new OverviewAdapter(getActivity(), cards);
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