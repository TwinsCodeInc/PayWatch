package cz.muni.fi.paywatch.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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

public class TotalIncomeExpenseViewHolder extends RecyclerView.ViewHolder {

    Realm realm;
    Date startDate = null;
    Date endDate = null;

    public TotalIncomeExpenseViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);

        realm = Realm.getDefaultInstance();
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-01");
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-31");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView totalExpenseSumView = (TextView) itemView.findViewById(R.id.totalExpenseSum);
        TextView totalIncomeSumPredictionView = (TextView) itemView.findViewById(R.id.totalIncomeSum);
        TextView totalDifferenceView = (TextView) itemView.findViewById(R.id.totalDifferenceSum);
        float totalIncomeSum = realm.where(Entry.class)
                .equalTo("accountId", 0)
                .greaterThanOrEqualTo("date",startDate)
                .lessThanOrEqualTo("date", endDate)
                .greaterThan("sum",0.0d)
                .sum("sum").floatValue();
        float totalExpenseSum = realm.where(Entry.class)
                .equalTo("accountId", 0)
                .greaterThanOrEqualTo("date",startDate)
                .lessThanOrEqualTo("date", endDate)
                .lessThan("sum",0.0d)
                .sum("sum").floatValue() * (-1);
        float totalDifferenceSum = totalIncomeSum - totalExpenseSum;
        totalIncomeSumPredictionView.setText(Float.toString(totalIncomeSum));
        totalExpenseSumView.setText(Float.toString(totalExpenseSum));
        totalDifferenceView.setText(Float.toString(totalDifferenceSum));
    }

}
