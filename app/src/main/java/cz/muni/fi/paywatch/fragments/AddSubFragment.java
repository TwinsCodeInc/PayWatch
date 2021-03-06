package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.activities.MainActivity;
import cz.muni.fi.paywatch.adapters.RecyclerCategoryAdapter;
import cz.muni.fi.paywatch.app.Helpers;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Category;
import cz.muni.fi.paywatch.model.Entry;

public class AddSubFragment extends Fragment {

    private int subFragment;
    private EditText editValue;
    private EditText editCurrency;
    private EditText editNote;
    private TextView editDate;
    private MainActivity mainActivity;
    private Button btnOk;
    private Button btnOkAndClose;
    private RecyclerView recyclerView;

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
        editCurrency = (EditText) v.findViewById(R.id.edit_currency);
        btnOk = (Button) v.findViewById(R.id.btn_ok);
        btnOkAndClose = (Button) v.findViewById(R.id.btn_ok_close);
        editNote = (EditText) v.findViewById(R.id.edit_note);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_categories);

        // Prepare recyclerview for categories
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Horizontal scrolling
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Clear edit when clicked into and value is 0
        editValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editValue.getText().toString().equals(getResources().getString(R.string.f_add_edit_value_def))) {
                    editValue.setText("");
                }
            }
        });

        // On OK click
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.hideSoftKeyboard(getActivity());
                if (saveEntry()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.f_add_toast_entry_added), Toast.LENGTH_SHORT).show();
                    // Reset widgets to the default state
                    refreshControls();
                }
            }
        });

        // On OK and CLOSE click
        btnOkAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveEntry()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.f_add_toast_entry_added), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });

        // Show calendar on click on date edit
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.hideSoftKeyboard(getActivity());
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                editDate.setText(Helpers.intToDateString(year, monthOfYear, dayOfMonth));
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
        editNote.setText("");
        // Set default currency
        refreshAccountDetails();
        // Reload categories
        refreshCategories();
        // Set current date
        editDate.setText(getResources().getString(R.string.f_add_date_today));
    }

    // Refresh the value of currency on ADD section
    public void refreshAccountDetails() {
        editCurrency.setText(RealmController.with(this).getAccountCurrency(mainActivity.getCurrentAccountId()));
        btnOk.setTextColor(RealmController.with(this).getAccountColor(mainActivity.getCurrentAccountId()));
        btnOkAndClose.setTextColor(RealmController.with(this).getAccountColor(mainActivity.getCurrentAccountId()));
    }

    private void refreshCategories() {
        // Load categories
        List<Category> categories = RealmController.with(this).getCategories(subFragment);
        RecyclerCategoryAdapter mAdapter = new RecyclerCategoryAdapter(getActivity(), recyclerView, categories);
        recyclerView.setAdapter(mAdapter);
    }

    private boolean saveEntry() {
        // Parse date
        Date date;
        String dateString = editDate.getText().toString();
        if (dateString.equals(getResources().getString(R.string.f_add_date_today))) {
            date = Calendar.getInstance().getTime();
        } else {
            date = Helpers.stringToDate(dateString);
            if (date == null) {
                Helpers.showOkDialog(mainActivity, getResources().getString(R.string.dialog_ok_warning_title),
                        getResources().getString(R.string.dialog_ok_incorrect_date));
                return false; // unable to parse date from string
            }
        }
        // Parse sum
        Double sum = Helpers.parseDouble(editValue.getText().toString());
        if (sum == null || sum == 0) {
            Helpers.showOkDialog(mainActivity, getResources().getString(R.string.dialog_ok_warning_title),
                    getResources().getString(R.string.dialog_ok_incorrect_value));
            return false;
        }
        if (subFragment == Constants.FSUB_EXPENSE) {
            sum *= -1;
        }
        // Get selected category
        Integer categoryId = ((RecyclerCategoryAdapter) recyclerView.getAdapter()).getSelectedCategoryId();
        if (categoryId == null) {
            Helpers.showOkDialog(mainActivity, getResources().getString(R.string.dialog_ok_warning_title),
                    getResources().getString(R.string.dialog_ok_select_category));
            return false;
        }

        // Get other data
        Integer accountId = mainActivity.getCurrentAccountId();
        String note = editNote.getText().toString().trim();

        // Insert new entry into database
        RealmController.with(this).addEntry(new Entry(sum, date, categoryId, accountId, note));
        // Increment the number of use count for used category
        RealmController.with(this).incrementCategoryUseCount(categoryId);
        return true;
    }

    public static AddSubFragment newInstance(int subFragment) {
        AddSubFragment f = new AddSubFragment();
        Bundle b = new Bundle();
        b.putInt("subFragment", subFragment);
        f.setArguments(b);
        return f;
    }
}