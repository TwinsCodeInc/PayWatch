package cz.muni.fi.paywatch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

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
    private TextView monthTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        // Get activity pointer
        mainActivity = (MainActivity) getActivity();

        // Find views by ID
        entryList = (RecyclerView) v.findViewById(R.id.recycler_view);
        entryList.setHasFixedSize(true);
        entryList.setLayoutManager(new StaggeredGridLayoutManager(2,1));

        monthTitle = (TextView) v.findViewById(R.id.month_title);
        Calendar cal = Calendar.getInstance();
        cal.setTime(mainActivity.getCurrentMonthStart());
        monthTitle.setText( new DateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)] + " " + String.valueOf(cal.get(Calendar.YEAR)) );

        monthTitle.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                pd.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar tmpCal = Calendar.getInstance();
                        tmpCal.set(Calendar.YEAR, year);
                        tmpCal.set(Calendar.MONTH, month - 1);
                        tmpCal.set(Calendar.DATE, 1);
                        mainActivity.setCurrentMonth( tmpCal.getTime() );

                        monthTitle.setText( new DateFormatSymbols().getMonths()[month - 1] + " " + String.valueOf(year));
                        refreshControls();
                    }
                });
                pd.show(mainActivity.getFragmentManager(), "MonthYearPickerDialog");
            }
        });

        // Refresh controls
        refreshControls();

        return v;
    }

    // Refresh all the data
    public void refreshControls() {
        OverviewAdapter adapter = new OverviewAdapter(mainActivity);
        entryList.setAdapter(adapter);
    }

    public static OverviewFragment newInstance() {
        OverviewFragment f = new OverviewFragment();
        return f;
    }

}