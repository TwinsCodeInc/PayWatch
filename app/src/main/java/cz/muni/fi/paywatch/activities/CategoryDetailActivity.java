package cz.muni.fi.paywatch.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import cz.muni.fi.paywatch.fragments.SettingsFragment;
import cz.muni.fi.paywatch.model.Category;
import io.realm.Realm;

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

        // Change focus not to show the keyboard immediately
        editName.clearFocus();

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
                String name = editName.getText().toString().trim();
                if (name.isEmpty()) return false;
                String icon = Constants.CAT_ICONS[gridView.getCheckedItemPosition()];
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
            case R.id.action_remove_category:
                showCategoryRemoveConfirmation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Shows dialog for category removal confirmation
    public void showCategoryRemoveConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cat_remove_confirmation);

        // Set up the buttons
        builder.setPositiveButton(R.string.acc_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RealmController.with(CategoryDetailActivity.this).reCategorize(catId, catType);
                RealmController.with(CategoryDetailActivity.this).removeCategory(catId);
                setResult(Constants.ACTIVITY_RESULT_UPDATED);
                finish();
            }
        });
        builder.setNegativeButton(R.string.acc_dialog_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
