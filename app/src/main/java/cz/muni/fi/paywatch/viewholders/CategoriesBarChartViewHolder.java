package cz.muni.fi.paywatch.viewholders;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

public class CategoriesBarChartViewHolder extends RecyclerView.ViewHolder {

    OverviewAdapter mAdapter;
    BarChart chart;
    float dailyExpensePrediction;
    Realm realm;
    Date startDate = null;
    Date endDate = null;

    public CategoriesBarChartViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);

        this.mAdapter = mAdapter;
        chart = (BarChart) itemView.findViewById(R.id.categoriesBarChart);

        realm = Realm.getDefaultInstance();

        List<com.github.mikephil.charting.data.BarEntry> entries = new ArrayList<>();
        List<com.github.mikephil.charting.data.BarEntry> prediction = new ArrayList<>();


        startDate = mAdapter.activity.getCurrentMonthStart();
        endDate = mAdapter.activity.getCurrentMonthEnd();

        for (Category category : RealmController.with((Activity)itemView.getContext()).getCategories(Constants.CAT_TYPE_EXPENSE) ) {
             float sum = realm.where(Entry.class)
                     .equalTo("categoryId", category.getId())
                     .greaterThanOrEqualTo("date", startDate)
                     .lessThanOrEqualTo("date", endDate)
                     .equalTo("accountId", mAdapter.activity.getCurrentAccountId())
                     .sum("sum").floatValue() * (-1);

             entries.add(new com.github.mikephil.charting.data.BarEntry(category.getId().floatValue(), sum));
             prediction.add(new com.github.mikephil.charting.data.BarEntry(category.getId().floatValue(), getAverage(category.getId())));

         }

        BarDataSet dataSet = new BarDataSet(entries, "Reality"); // add entries to dataset

        BarDataSet dataSet2 = new BarDataSet(prediction, "Prediction"); // add entries to dataset
        dataSet2.setColor(Color.RED);


        BarData data = new BarData(dataSet, dataSet2);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setData(data);
        chart.groupBars(0.0f, 0.03f, 0.08f);
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh

    }

    float getAverage (int categoryId) {

        float sum = realm.where(Entry.class)
                .equalTo("categoryId", categoryId )
                .equalTo("accountId", mAdapter.activity.getCurrentAccountId())
                .sum("sum").floatValue() * (-1);

        if ( sum == 0.0f ) {
            return 0.0f;
        }
        Date firstDate = realm.where(Entry.class)
                .equalTo("accountId", mAdapter.activity.getCurrentAccountId() )
                .minimumDate("date");
        Date lastDate = realm.where(Entry.class)
                .equalTo("accountId", mAdapter.activity.getCurrentAccountId() )
                .maximumDate("date");

        float daysDiff = ( lastDate.getTime() - firstDate.getTime() ) / ( 24 * 60 * 60 * 1000 ) + 1;

        return sum / daysDiff * 30;
    }
}
