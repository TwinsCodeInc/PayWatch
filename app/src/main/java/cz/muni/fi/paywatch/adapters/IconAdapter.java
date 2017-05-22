package cz.muni.fi.paywatch.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.custom.CheckableImageView;

import static cz.muni.fi.paywatch.app.PayWatchApplication.getContext;

/**
 * Created by Daniel on 14. 5. 2017.
 */

public class IconAdapter extends BaseAdapter {
    private Context mContext;
    private List<CheckableImageView> views;

    public IconAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return Constants.CAT_ICONS.length;
    }

    public Object getItem(int position) {
        return Constants.CAT_ICONS[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemPosition(String icon) {
        int position = 0;
        for (String s : Constants.CAT_ICONS) {
            if (s.equals(icon)) break;
            position += 1;
        }
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckableImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new CheckableImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (CheckableImageView) convertView;
        }
        imageView.setBackground(mContext.getResources().getDrawable(R.drawable.grid_icon_drawable));
        imageView.setImageResource(mContext.getResources().getIdentifier(
                Constants.CAT_ICONS[position], "drawable", mContext.getPackageName())
        );

        return imageView;
    }
}
