package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.activities.MainActivity;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Category;
import cz.muni.fi.paywatch.model.Entry;

public class AddSubFragment extends BaseFragment {

    private int subFragment;
    private EditText editValue;
    private TextView editDate;
    private Spinner spinCategory;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_sub, container, false);

        // Get activity pointer
        mainActivity = (MainActivity) getActivity();

        // Set the type of current instance (EXPENSE or INCOME)
        subFragment = getArguments().getInt("subFragment", 0);

        // Find views by ID
        editValue = (EditText) v.findViewById(R.id.edit_value);
        editDate = (TextView) v.findViewById(R.id.edit_date);
        final Button btnOk = (Button) v.findViewById(R.id.btn_ok);
        final Button btnOkAndClose = (Button) v.findViewById(R.id.btn_ok_close);
        spinCategory = (Spinner) v.findViewById(R.id.spin_category);

        // On OK click
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
                Toast.makeText(getActivity(), getResources().getString(R.string.f_add_toast_entry_added), Toast.LENGTH_SHORT).show();
                // Reset widgets to the default state
                refreshControls();
            }
        });

        // On OK click
        btnOkAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
                Toast.makeText(getActivity(), getResources().getString(R.string.f_add_toast_entry_added), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        // Show calendar on click on date edit
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth+". "+(monthOfYear+1)+". "+year;
                                editDate.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "DatePicker");
            }
        });

        // Refresh controlls
        refreshControls();

        return v;
    }

    // Refreshes all data and widgets that could be changed
    public void refreshControls() {
        // Set default value
        editValue.setText(getResources().getString(R.string.f_add_edit_value_def));
        // Reload categories
        loadCategories();
        // Set current date
        String formattedDate = new SimpleDateFormat("dd. MM. yyyy").format(Calendar.getInstance().getTime());
        editDate.setText(formattedDate);
    }

    private void loadCategories() {
        // Load categories
        int catType = (subFragment == Constants.FSUB_EXPENSE) ? Constants.CAT_TYPE_EXPENSE : Constants.CAT_TYPE_INCOME;
        List<Category> items = RealmController.with(this).getCategories(catType);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, items);
        spinCategory.setAdapter(adapter);
        spinCategory.setSelection(0);
    }

    private void saveEntry() {
        Date date = new java.util.Date();
        Double sum = Double.parseDouble(editValue.getText().toString());
        if (subFragment == Constants.FSUB_EXPENSE) {
            sum *= -1;
        }
        Category c = (Category) spinCategory.getSelectedItem();
        Integer categoryId = (c != null) ? c.getId() : 0;
        Integer accountId = mainActivity.getCurrentAccountId();

        // Insert new entry into database
        RealmController.with(this).addEntry(new Entry(sum, date, categoryId, accountId));

        // Increment the number of use count for used category
        RealmController.with(this).incrementCategoryUseCount(categoryId);
    }

    public static AddSubFragment newInstance(int subFragment) {
        AddSubFragment f = new AddSubFragment();
        Bundle b = new Bundle();
        b.putInt("subFragment", subFragment);
        f.setArguments(b);
        return f;
    }
}