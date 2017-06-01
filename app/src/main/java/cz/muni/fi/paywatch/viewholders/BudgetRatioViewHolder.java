package cz.muni.fi.paywatch.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import cz.muni.fi.paywatch.adapters.OverviewAdapter;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Category;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jirka on 02.05.2017.
 */

public class BudgetRatioViewHolder extends RecyclerView.ViewHolder {

    PieChart chart;

    public BudgetRatioViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);

        chart = (PieChart) itemView.findViewById(R.id.budgetRatio);

        Realm realm = Realm.getDefaultInstance();

        List<com.github.mikephil.charting.data.PieEntry> entries = new ArrayList<>();

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-01");
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-06-30");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float totalSum = realm.where(Entry.class)
                .lessThan("sum", 0.0d)
                .greaterThanOrEqualTo("date", startDate)
                .lessThanOrEqualTo("date", endDate)
                .sum("sum").floatValue() * (-1);

        float rest = Math.max(RealmController.getInstance().getAccount(1).getBudget().floatValue() - totalSum, 0);

        entries.add(new com.github.mikephil.charting.data.PieEntry(totalSum, "Spent"));
        entries.add(new com.github.mikephil.charting.data.PieEntry(rest, "Rest"));


        PieDataSet dataSet = new PieDataSet(entries, null); // add entries to dataset
        dataSet.setColors(new int[] {R.color.red, R.color.blue});
        PieData data = new PieData(dataSet);
        chart.setData(data);
        //chart.setHoleRadius(50.0f);
        chart.setDrawSliceText(false);
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.setDrawCenterText(false);
        chart.invalidate(); // refresh

    }
}
