package cz.muni.fi.paywatch.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.app.Helpers;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Entry;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Jirka on 14.04.2017.
 */
public class EntryAdapter extends RealmBaseAdapter<Entry> implements ListAdapter {


    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    Activity activity;
    ListView listView;
    OrderedRealmCollection<Entry> results;

    public EntryAdapter(Activity activity, OrderedRealmCollection<Entry> realmResults) {
        super(realmResults);

        results = realmResults;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Entry getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();

        listView = (ListView) activity.findViewById(R.id.listViewItems);
        if(convertView == null){

            convertView=inflater.inflate(R.layout.overview_item, null);
            Button deleteImageView = (Button) convertView.findViewById(R.id.item_del_butt);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = listView.getPositionForView((View) v.getParent());
                    float id = results.get(position).getId();
                    RealmController.with(activity).removeEntry(id);
                    Toast.makeText(activity, "Entry deleted", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });

            txtFirst=(TextView) convertView.findViewById(R.id.item_date);
            txtSecond=(TextView) convertView.findViewById(R.id.item_note);
            txtThird=(TextView) convertView.findViewById(R.id.item_category);
            txtFourth=(TextView) convertView.findViewById(R.id.item_sum);

        }


        if (results != null) {
            Entry entry = results.get(position);
            txtFirst.setText(Helpers.getDateString(entry.getDate()));
            txtSecond.setText(entry.getNote());
            txtThird.setText(RealmController.with(activity).getCategoryName(entry.getCategoryId()));
            txtFourth.setText(entry.getSum().toString());
        }
        return convertView;
    }
}
