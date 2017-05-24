package cz.muni.fi.paywatch.adapters;

/**
 * Created by Daniel on 23. 5. 2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.custom.CheckableImageView;
import cz.muni.fi.paywatch.model.Category;


public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.MyViewHolder> {

    private List<Category> categoriesList;
    private final Context context;
    private final RecyclerView recyclerView;
    private Integer selectedPos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckableImageView icon;

        public MyViewHolder(View view) {
            super(view);
            icon = (CheckableImageView) view.findViewById(R.id.category_icon);
            icon.setChecked(false);
            title = (TextView) view.findViewById(R.id.category_title);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Deselect previous choice
                    if (selectedPos != null) {
                        MyViewHolder vh = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedPos);
                        if (vh != null) {
                            vh.icon.setChecked(false);
                        }
                    }
                    // Select new choice
                    icon.setChecked(true);
                    selectedPos = getAdapterPosition();
                }
            });
        }
    }

    public RecyclerCategoryAdapter(Context context, RecyclerView recyclerView, List<Category> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.recyclerView = recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_category_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = categoriesList.get(position);
        holder.title.setText(category.getName());
        holder.icon.setImageResource(context.getResources().getIdentifier(
                category.getIcon(), "drawable", context.getPackageName()));
        holder.icon.setCategoryId(category.getId());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (selectedPos == null || selectedPos != holder.getAdapterPosition()) {
            holder.icon.setChecked(false);
        } else {
            holder.icon.setChecked(true);
        }
    }

    // Returns the icon name of selected item
    public Integer getSelectedCategoryId() {
        if (selectedPos != null) {
            MyViewHolder vh = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedPos);
            if (vh != null) {
                return vh.icon.getCategoryId();
            }
        }
        return null;
    }
}
