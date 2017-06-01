package cz.muni.fi.paywatch.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.EntryAdapter;
import cz.muni.fi.paywatch.adapters.OverviewAdapter;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Category;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Jirka on 02.05.2017.
 */

public class LastItemsViewHolder extends RecyclerView.ViewHolder {

    public LastItemsViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);

        ListView listView=(ListView) itemView.findViewById(R.id.listViewItems);

        Realm realm = Realm.getDefaultInstance();

        OrderedRealmCollection<Entry> results = realm.where(Entry.class)
                .equalTo("accountId", mAdapter.activity.getCurrentAccountId())
                .greaterThanOrEqualTo("date", mAdapter.activity.getCurrentMonthStart())
                .lessThanOrEqualTo("date", mAdapter.activity.getCurrentMonthEnd())
                .findAllSorted("date", Sort.DESCENDING);

        EntryAdapter adapter = new EntryAdapter((Activity)itemView.getContext(), results);
        listView.setAdapter(adapter);
    }
}
