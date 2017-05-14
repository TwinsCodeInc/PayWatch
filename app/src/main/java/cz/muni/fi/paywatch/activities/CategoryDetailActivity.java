package cz.muni.fi.paywatch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.adapters.IconAdapter;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Category;

public class CategoryDetailActivity extends AppCompatActivity {

    private int catType;
    private boolean catNew;
    private int catId;
    private EditText editName;
    private GridView gridView;
    private IconAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        // get bundle parameters
        Bundle b = getIntent().getExtras();
        catType = b.getInt("cat_type");
        catNew = b.getBoolean("cat_new");
        catId = b.getInt("cat_id");

        gridView = (GridView) findViewById(R.id.grid_view);
        editName = (EditText) findViewById(R.id.edit_category_name);

        // Prepare icon grid view
        adapter = new IconAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        // Load data to widgets
        if (!catNew) {
            Category category = RealmController.with(this).getCategory(catId);
            editName.setText(category.getName());
            gridView.setItemChecked(adapter.getItemPosition(category.getIcon()), true);
        } else {
            editName.setText("");
            gridView.setItemChecked(0, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_category_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save_category:
//                Toast.makeText(this, gridView.getCheckedItemCount(), Toast.LENGTH_SHORT).show();

                String name = editName.getText().toString().trim();
                if (name.isEmpty()) return false;
                String icon = Constants.CAT_ICONS[0];
                if (catNew) {
                    // Add new category
                    RealmController.with(this).addCategory(catType, name, icon);
                } else {
                    // Edit existing category
                    RealmController.with(this).updateCategory(catId, name, icon);
                }
                setResult(Constants.ACTIVITY_RESULT_UPDATED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
