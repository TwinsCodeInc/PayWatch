package cz.muni.fi.paywatch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.model.Entry;
import cz.muni.fi.paywatch.viewholders.DefaultViewHolder;
import cz.muni.fi.paywatch.viewholders.LineGraphViewHolder;
import cz.muni.fi.paywatch.viewholders.SumViewHolder;
import io.realm.RealmResults;

/**
 * Created by Jirka on 01.05.2017.
 */

public class OverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private RealmResults<Entry> mItemList;
    private AdapterView.OnItemClickListener mOnItemClickListener;


    public OverviewAdapter(Context context, RealmResults<Entry> mItemList) {
        this.context = context;
        this.mItemList = mItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case 0 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_item, parent, false);
                holder = new DefaultViewHolder(itemView, this);
                break;
            case 1 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_item2, parent, false);
                holder = new SumViewHolder(itemView, this);
                break;
            case 2 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_graph, parent, false);
                holder = new LineGraphViewHolder(itemView, this);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_item, parent, false);
                holder = new DefaultViewHolder(itemView, this);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0 :
                DefaultViewHolder holder1 = (DefaultViewHolder) holder;
                configureDefaultViewHolder(holder1, position);
                break;
            case 1 :
                SumViewHolder holder2 = (SumViewHolder) holder;
                configureSumViewHolder(holder2, position);
                break;
            case 2 :
                LineGraphViewHolder holder3 = (LineGraphViewHolder) holder;
                configureLineGraphViewHolder(holder3, position);
                break;
            default :
                DefaultViewHolder holderDef = (DefaultViewHolder) holder;
                configureDefaultViewHolder(holderDef, position);
                break;

        }
    }

    @Override
    public int getItemViewType (int position) {
        if (position == 2) {
            return 2;
        }
        return position % 2;
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    private void configureDefaultViewHolder(DefaultViewHolder holder, int position) {
        Entry entry = mItemList.get(position);
        holder.mTitle.setText(entry.getDate().toString());
        holder.mPosition.setText(entry.getSum().toString());
    }

    private void configureSumViewHolder(SumViewHolder holder, int position) {
        Entry entry = mItemList.get(position);
        holder.mTitle.setText(entry.getDate().toString());
    }

    private void configureLineGraphViewHolder(LineGraphViewHolder holder, int position) {
        //Entry entry = mItemList.get(position);
        //holder.mTitle.setText(entry.getDate().toString());
    }



}