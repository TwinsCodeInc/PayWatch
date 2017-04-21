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
import cz.muni.fi.paywatch.model.Category;
import cz.muni.fi.paywatch.model.Entry;
import cz.muni.fi.paywatch.R;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class AddSubFragment extends BaseFragment {

    private int subFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_sub, container, false);

        // Set the type of current instance (EXPENSE or INCOME)
        subFragment = getArguments().getInt("subFragment", 0);

        // Find views by ID
        final EditText editValue = (EditText) v.findViewById(R.id.edit_value);
        final TextView editDate = (TextView) v.findViewById(R.id.edit_date);
        final Button btnOk = (Button) v.findViewById(R.id.btn_ok);
        final Button btnOkAndClose = (Button) v.findViewById(R.id.btn_ok_close);
        final Spinner spinCategory = (Spinner) v.findViewById(R.id.spin_category);

        // On OK click
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry(editValue);
                Toast.makeText(getActivity(), getResources().getString(R.string.f_add_toast_entry_added), Toast.LENGTH_SHORT).show();
            }
        });

        // On OK click
        btnOkAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry(editValue);
                Toast.makeText(getActivity(), getResources().getString(R.string.f_add_toast_entry_added), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        // Select text in value_edit
        editValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editValue.selectAll();
            }
        });

        // Set current date
        String formattedDate = new SimpleDateFormat("dd. MM. yyyy").format(Calendar.getInstance().getTime());
        editDate.setText(formattedDate);
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

        // Load categories
        int catType = (subFragment == Constants.FSUB_EXPENSE) ? Constants.CAT_TYPE_EXPENSE : Constants.CAT_TYPE_INCOME;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Category> realmResults = realm.where(Category.class).equalTo("type", catType).findAllSorted("useCount", Sort.DESCENDING);
        List<Category> items = realm.copyFromRealm(realmResults);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, items);
        spinCategory.setAdapter(adapter);

        return v;
    }

    private void saveEntry(EditText editValue) {
        Date date = new java.util.Date();
        Double sum = Double.parseDouble(editValue.getText().toString());
        if (subFragment == Constants.FSUB_EXPENSE) {
            sum *= -1;
        }
        Integer categoryId = 1;
        Integer accountId = 1;

        realm.beginTransaction();
        final Entry realmEntry = realm.copyToRealm(new Entry(sum, date, categoryId, accountId));
        realm.commitTransaction();

        editValue.setText(getResources().getString(R.string.f_add_edit_value_def));
    }

    public static AddSubFragment newInstance(int subFragment) {
        AddSubFragment f = new AddSubFragment();
        Bundle b = new Bundle();
        b.putInt("subFragment", subFragment);
        f.setArguments(b);
        return f;
    }
}