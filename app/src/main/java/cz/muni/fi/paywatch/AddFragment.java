package cz.muni.fi.paywatch;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        // Find views by ID
        final EditText editValue = (EditText) v.findViewById(R.id.edit_value);
        final EditText editDate = (EditText) v.findViewById(R.id.edit_date);
        final TabLayout tabSelector = (TabLayout) v.findViewById(R.id.tab_selector);

        // Set selector tabs - Expense and Income
        tabSelector.addTab(tabSelector.newTab().setText("Expense"));
        tabSelector.addTab(tabSelector.newTab().setText("Income"));

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
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editDate.setText("datum zvoleny");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        return v;
    }

    public static AddFragment newInstance(String text) {
        AddFragment f = new AddFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }
}