package cz.muni.fi.paywatch.adapters;

/**
 * Created by Daniel on 23. 5. 2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.model.Category;


public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.MyViewHolder> {

    private List<Category> categoriesList;
    private final Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.category_icon);
            title = (TextView) view.findViewById(R.id.category_title);
        }
    }

    public RecyclerCategoryAdapter(Context context, List<Category> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
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
                Constants.CAT_ICONS[position], "drawable", context.getPackageName()));

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }
}
