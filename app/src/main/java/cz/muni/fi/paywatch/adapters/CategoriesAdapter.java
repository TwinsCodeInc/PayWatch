package cz.muni.fi.paywatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.model.Category;

/**
 * Created by Daniel on 14. 5. 2017.
 */

public class CategoriesAdapter extends ArrayAdapter<Category> {
    private final Context context;
    private final List<Category> categories;

    public CategoriesAdapter(Context context, List<Category> categories) {
        super(context, R.layout.category_list_item, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.category_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(categories.get(position).getName());
        imageView.setImageResource(categories.get(position).getIconId());
        return rowView;
    }
}
