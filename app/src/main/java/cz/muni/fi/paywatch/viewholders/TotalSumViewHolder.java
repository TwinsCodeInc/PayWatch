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
import cz.muni.fi.paywatch.fragments.OverviewFragment;
import cz.muni.fi.paywatch.model.Category;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jirka on 02.05.2017.
 */

public class TotalSumViewHolder extends RecyclerView.ViewHolder {

    Realm realm;
    Date startDate = null;
    Date endDate = null;

    public TotalSumViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);
        mAdapter.activity.getCurrentAccountId();
        realm = Realm.getDefaultInstance();
        try {
            startDate = mAdapter.activity.getCurrentMonth();
            //startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-06-01");
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-06-30");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView totalSumView = (TextView) itemView.findViewById(R.id.totalSum);
        TextView totalSumPredictionView = (TextView) itemView.findViewById(R.id.totalSumPrediction);
        float totalSum = realm.where(Entry.class).sum("sum").floatValue();
        totalSumView.setText(Float.toString(totalSum));
        totalSumPredictionView.setText(Float.toString(totalSum + getDailyExpensePrediction() ));
    }

    float getDailyExpensePrediction () {

        float sum = realm.where(Entry.class)
                .lessThan("sum", 0.0d)
                .equalTo("accountId", 0)
                .greaterThanOrEqualTo("date", startDate)
                .lessThanOrEqualTo("date", endDate)
                .sum("sum").floatValue();
        float daysDiff = ( new Date().getTime() - startDate.getTime() ) / ( 24 * 60 * 60 * 1000 ) + 1;
        float daysToEnd = ( endDate.getTime() - new Date().getTime() ) / ( 24 * 60 * 60 * 1000 );
        return sum / daysDiff * daysToEnd;
    }
}
