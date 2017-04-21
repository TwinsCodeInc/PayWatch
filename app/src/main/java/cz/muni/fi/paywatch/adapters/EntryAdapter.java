package cz.muni.fi.paywatch.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by Jirka on 14.04.2017.
 */
public class EntryAdapter extends RealmBaseAdapter<Entry> implements ListAdapter {


    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    Activity activity;

    public EntryAdapter(Activity activity, OrderedRealmCollection<Entry> realmResults) {
        super(realmResults);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.list_overview, null);

            txtFirst=(TextView) convertView.findViewById(R.id.date);
            txtSecond=(TextView) convertView.findViewById(R.id.sum);
            txtThird=(TextView) convertView.findViewById(R.id.category);

        }


        if (adapterData != null) {
            Entry entry = adapterData.get(position);
            SimpleDateFormat format = new SimpleDateFormat("d.M.y");
            txtFirst.setText(format.format(entry.getDate()));
            txtSecond.setText(entry.getSum().toString());
            txtThird.setText(entry.getCategory().toString());
        }
        return convertView;
    }
}
