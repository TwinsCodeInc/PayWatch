package cz.muni.fi.paywatch.viewholders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.OverviewAdapter;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jirka on 02.05.2017.
 */

public class LineGraphViewHolder extends RecyclerView.ViewHolder {

    OverviewAdapter mAdapter;
    LineChart chart;
    float dailyExpensePrediction;
    Realm realm;
    Date startDate = null;
    Date endDate = null;
    Date actualDate = null;

    public LineGraphViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);
        this.mAdapter = mAdapter;
        chart = (LineChart) itemView.findViewById(R.id.chart);

        realm = Realm.getDefaultInstance();

        List<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();
        List<com.github.mikephil.charting.data.Entry> prediction = new ArrayList<>();

        startDate = mAdapter.activity.getCurrentMonthStart();
        endDate = mAdapter.activity.getCurrentMonthEnd();
        actualDate = new Date();
        if ( new Date().after(endDate) ) {
            actualDate = endDate;
        }

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        float initValue = realm.where(Entry.class)
                .equalTo("accountId", mAdapter.activity.getCurrentAccountId() )
                .lessThan("date",startDate)
                .sum("sum").floatValue();

        int index = 0;
        float lastBalance = 0f;
        boolean firstPrediction = true;
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            if ( date.after( new Date() ) ) {
                if ( firstPrediction && index > 0 ) {
                    prediction.add(new com.github.mikephil.charting.data.Entry(index-1, initValue + lastBalance));
                    firstPrediction = false;
                }
                lastBalance += getDailyExpensePrediction();
                prediction.add(new com.github.mikephil.charting.data.Entry(index++, initValue + lastBalance));
            } else {
                lastBalance = realm.where(Entry.class)
                        .equalTo("accountId", mAdapter.activity.getCurrentAccountId() )
                        .greaterThanOrEqualTo("date", startDate)
                        .lessThanOrEqualTo("date", date)
                        .sum("sum").floatValue();
                entries.add(new com.github.mikephil.charting.data.Entry(index++, initValue + lastBalance));
            }
        }

        List<ILineDataSet> dataSets = new ArrayList<>();
        if ( entries.size() > 0 ) {
            LineDataSet dataSet = new LineDataSet(entries, mAdapter.activity.getResources().getString(R.string.line_chart_balance)); // add entries to dataset
            dataSet.setDrawFilled(true);
            dataSets.add(dataSet);
        }
        if ( prediction.size() > 0 ) {
            LineDataSet dataSet2 = new LineDataSet(prediction, mAdapter.activity.getResources().getString(R.string.line_chart_prediction)); // add entries to dataset
            dataSet2.setColor(Color.RED);
            dataSet2.setDrawFilled(true);
            dataSet2.setFillColor(Color.RED);
            dataSets.add(dataSet2);
        }
        LineData data = new LineData(dataSets/*, dataSet2*/);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh

    }

    float getDailyExpensePrediction () {
        if ( dailyExpensePrediction == 0.0f ) {
            float sum = realm.where(Entry.class)
                    .equalTo("accountId", mAdapter.activity.getCurrentAccountId() )
                    .greaterThanOrEqualTo("date", startDate)
                    .lessThanOrEqualTo("date", actualDate)
                    .lessThan("sum", 0.0d)
                    .sum("sum").floatValue();
            float daysDiff = ( actualDate.getTime() - startDate.getTime() ) / ( 24 * 60 * 60 * 1000 ) + 1;
            dailyExpensePrediction = sum/daysDiff;
        }
        return dailyExpensePrediction;
    }
}
