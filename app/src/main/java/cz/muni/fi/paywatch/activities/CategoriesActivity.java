package cz.muni.fi.paywatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.CategoriesAdapter;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Category;

public class CategoriesActivity extends AppCompatActivity {

    private int cat_type;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // get category type
        Bundle b = getIntent().getExtras();
        cat_type = b.getInt("cat_type");

        listView = (ListView) findViewById(R.id.list_view);
        refreshCategories();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(CategoriesActivity.this, CategoryDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("cat_type", cat_type);
                b.putBoolean("cat_new", false);
                b.putInt("cat_id", ((Category) listView.getAdapter().getItem(position)).getId());
                intent.putExtras(b);
                startActivityForResult(intent, Constants.ACTIVITY_RESULT_UPDATED);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ACTIVITY_RESULT_UPDATED && resultCode == RESULT_OK) {
            refreshCategories();
        }
    }

    private void refreshCategories() {
        CategoriesAdapter adapter = new CategoriesAdapter(this, RealmController.with(this).getCategoriesOrdered(cat_type));
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_categories_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add_category:
                Intent intent = new Intent(CategoriesActivity.this, CategoryDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("cat_type", cat_type);
                b.putBoolean("cat_new", true);
                intent.putExtras(b);
                startActivityForResult(intent, Constants.ACTIVITY_RESULT_UPDATED);
        }
        return super.onOptionsItemSelected(item);
    }
}
