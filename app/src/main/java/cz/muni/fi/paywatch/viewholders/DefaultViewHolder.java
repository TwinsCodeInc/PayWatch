package cz.muni.fi.paywatch.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.OverviewAdapter;

/**
 * Created by Jirka on 01.05.2017.
 */

public class DefaultViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitle, mPosition;
    public OverviewAdapter mAdapter;

    public DefaultViewHolder(View itemView, OverviewAdapter mAdapter) {
        super(itemView);
        this.mAdapter = mAdapter;
        mTitle = (TextView) itemView.findViewById(R.id.item_date);
        mPosition = (TextView) itemView.findViewById(R.id.item_sum);
    }
}