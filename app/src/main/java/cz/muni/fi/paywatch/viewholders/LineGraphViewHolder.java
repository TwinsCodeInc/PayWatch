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

    LineChart chart;
    float dailyExpensePrediction;
    Realm realm;
    Date startDate = null;
    Date endDate = null;

    public LineGraphViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);

        chart = (LineChart) itemView.findViewById(R.id.chart);

        realm = Realm.getDefaultInstance();

        List<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();
        List<com.github.mikephil.charting.data.Entry> prediction = new ArrayList<>();

        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-01");
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-31");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        float initValue = realm.where(Entry.class).lessThan("date",startDate).sum("sum").floatValue();

        int index = 0;
        float lastBalance = 0f;
        boolean firstPrediction = true;
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            if ( date.after( new Date() ) ) {
                if ( firstPrediction ) {
                    prediction.add(new com.github.mikephil.charting.data.Entry(index-1, initValue + lastBalance));
                    firstPrediction = false;
                }
                lastBalance += getDailyExpensePrediction();
                prediction.add(new com.github.mikephil.charting.data.Entry(index++, initValue + lastBalance));
            } else {
                lastBalance = realm.where(Entry.class)
                        .greaterThanOrEqualTo("date", startDate)
                        .lessThanOrEqualTo("date", date)
                        .sum("sum").floatValue();
                entries.add(new com.github.mikephil.charting.data.Entry(index++, initValue + lastBalance));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Account balance"); // add entries to dataset
        dataSet.setDrawFilled(true);

        LineDataSet dataSet2 = new LineDataSet(prediction, "Prediction"); // add entries to dataset
        dataSet2.setColor(Color.RED);
        dataSet2.setDrawFilled(true);
        dataSet2.setFillColor(Color.RED);


        LineData data = new LineData(dataSet, dataSet2);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh

    }

    float getDailyExpensePrediction () {
        if ( dailyExpensePrediction == 0.0f ) {
            float sum = realm.where(Entry.class).lessThan("sum", 0.0d).sum("sum").floatValue();
            float daysDiff = ( new Date().getTime() - startDate.getTime() ) / ( 24 * 60 * 60 * 1000 );
            dailyExpensePrediction = sum/daysDiff;
        }
        return dailyExpensePrediction;
    }
}
