package cz.muni.fi.paywatch.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by Jirka on 14.04.2017.
 */
public class EntryAdapter extends RealmBaseAdapter<Entry> implements ListAdapter {

    private static class ViewHolder {
        TextView text;
    }

    public EntryAdapter(OrderedRealmCollection<Entry> realmResults) {
        super(realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null) {
            Entry entry = adapterData.get(position);
            viewHolder.text.setText(entry.getSum().toString());
        }
        return convertView;
    }
}
