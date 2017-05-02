package cz.muni.fi.paywatch.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.OverviewAdapter;

/**
 * Created by Jirka on 02.05.2017.
 */

public class LineGraphViewHolder extends RecyclerView.ViewHolder {

    GraphView graph;

    public LineGraphViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);

        graph = (GraphView) itemView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }
}
