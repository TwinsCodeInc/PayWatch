package cz.muni.fi.paywatch.cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import cz.muni.fi.paywatch.R;

public class AddFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        // Find views by ID
        final EditText editValue = (EditText) v.findViewById(R.id.edit_value);
        final TextView editDate = (TextView) v.findViewById(R.id.edit_date);
        final TabLayout tabSelector = (TabLayout) v.findViewById(R.id.tab_selector);

        // Set selector tabs - Expense and Income
        tabSelector.addTab(tabSelector.newTab().setText(R.string.f_add_expense));
        tabSelector.addTab(tabSelector.newTab().setText(R.string.f_add_income));

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

    public static AddFragment newInstance() {
        AddFragment f = new AddFragment();
        return f;
    }

}