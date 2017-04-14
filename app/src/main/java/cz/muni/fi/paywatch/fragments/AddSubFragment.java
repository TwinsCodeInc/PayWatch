package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.Model.Entry;
import cz.muni.fi.paywatch.R;
import io.realm.Realm;

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

        // On OK click
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                editValue.setText("0.00");

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

        return v;
    }

    public static AddSubFragment newInstance(int subFragment) {
        AddSubFragment f = new AddSubFragment();
        Bundle b = new Bundle();
        b.putInt("subFragment", subFragment);
        f.setArguments(b);
        return f;
    }
}