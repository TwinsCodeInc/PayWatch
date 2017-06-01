package cz.muni.fi.paywatch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.viewholders.BudgetRatioViewHolder;
import cz.muni.fi.paywatch.viewholders.CategoriesBarChartViewHolder;
import cz.muni.fi.paywatch.viewholders.CategoryRatioViewHolder;
import cz.muni.fi.paywatch.viewholders.DefaultViewHolder;
import cz.muni.fi.paywatch.viewholders.LastItemsViewHolder;
import cz.muni.fi.paywatch.viewholders.LineGraphViewHolder;
import cz.muni.fi.paywatch.viewholders.TotalSumViewHolder;

/**
 * Created by Jirka on 01.05.2017.
 */

public class OverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<String> mItemList;
    private AdapterView.OnItemClickListener mOnItemClickListener;


    public OverviewAdapter(Context context, ArrayList<String> mItemList) {
        this.context = context;
        this.mItemList = mItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case 0 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_graph, parent, false);
                holder = new LineGraphViewHolder(itemView, this);
                break;
            case 1 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_category_ratio, parent, false);
                holder = new CategoryRatioViewHolder(itemView, this);
                break;
            case 2 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_total_sum, parent, false);
                holder = new TotalSumViewHolder(itemView, this);
                break;
            case 3 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_items, parent, false);
                holder = new LastItemsViewHolder(itemView, this);
                break;
            case 4 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_categories_barchart, parent, false);
                holder = new CategoriesBarChartViewHolder(itemView, this);
                break;
            case 5 :
                itemView = LayoutInflater.from(context).inflate(R.layout.overview_budget, parent, false);
                holder = new BudgetRatioViewHolder(itemView, this);
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
                LineGraphViewHolder holder3 = (LineGraphViewHolder) holder;
                configureLineGraphViewHolder(holder3, position);
                break;
            case 1 :
                CategoryRatioViewHolder holder2 = (CategoryRatioViewHolder) holder;
                configureCategoryRatioViewHolder(holder2, position);
                break;
            case 2 :
                TotalSumViewHolder holder1 = (TotalSumViewHolder) holder;
                break;
            case 3 :
                LastItemsViewHolder holder4 = (LastItemsViewHolder) holder;
                configureLastItemsViewHolder(holder4, position);
                break;
            case 4 :
                CategoriesBarChartViewHolder holder5 = (CategoriesBarChartViewHolder) holder;
                configureCategoriesBarChartViewHolder(holder5, position);
                break;
            case 5 :
                BudgetRatioViewHolder holder6 = (BudgetRatioViewHolder) holder;
                configureBudgetRatioChartViewHolder(holder6, position);
                break;
            default :
                CategoryRatioViewHolder holderDef = (CategoryRatioViewHolder) holder;
                configureCategoryRatioViewHolder(holderDef, position);
                break;

        }
    }

    @Override
    public int getItemViewType (int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    private void configureLineGraphViewHolder(LineGraphViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
        //Entry entry = mItemList.get(position);
        //holder.mTitle.setText(entry.getDate().toString());
    }

    private void configureLastItemsViewHolder(LastItemsViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
    }

    private void configureCategoriesBarChartViewHolder(CategoriesBarChartViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
    }

    private void configureCategoryRatioViewHolder(CategoryRatioViewHolder holder, int position) {

    }

    private void configureBudgetRatioChartViewHolder(BudgetRatioViewHolder holder, int position) {

    }



}