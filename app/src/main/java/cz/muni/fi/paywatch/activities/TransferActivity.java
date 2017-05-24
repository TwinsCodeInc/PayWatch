package cz.muni.fi.paywatch.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import cz.muni.fi.paywatch.Constants;
import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.app.Helpers;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Account;
import cz.muni.fi.paywatch.model.Entry;

public class TransferActivity extends AppCompatActivity {

    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private EditText editCurrency;
    private Button btnTransfer;
    private EditText editTransferValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        spinnerFrom = (Spinner) findViewById(R.id.spinner_acc_from);
        spinnerTo = (Spinner) findViewById(R.id.spinner_acc_to);
        editCurrency = (EditText) findViewById(R.id.edit_transfer_currency);
        btnTransfer = (Button) findViewById(R.id.btn_make_transfer);
        editTransferValue = (EditText) findViewById(R.id.edit_transfer_value);

        // Load accounts to the first spinner
        List<Account> itemsFrom = RealmController.with(this).getAccounts();
        ArrayAdapter<Account> adapterFrom = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsFrom);
        spinnerFrom.setAdapter(adapterFrom);
        spinnerFrom.setSelection(0);

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshControls();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account aTo = (Account) spinnerTo.getSelectedItem();
                if (aTo == null) {
                    Helpers.showOkDialog(TransferActivity.this, getResources().getString(R.string.dialog_ok_warning_title),
                            getResources().getString(R.string.dialog_ok_account_not_selected));
                    return;
                }
                Account aFrom = (Account) spinnerFrom.getSelectedItem();
                Double sum = Helpers.parseDouble(editTransferValue.getText().toString());
                if (sum == null || sum == 0) {
                    Helpers.showOkDialog(TransferActivity.this, getResources().getString(R.string.dialog_ok_warning_title),
                            getResources().getString(R.string.dialog_ok_incorrect_value));
                    return;
                }
                // Make expense
                RealmController.with(TransferActivity.this).addEntry(new Entry(sum * (-1), Calendar.getInstance().getTime(),
                        Constants.CAT_DEF_EXP_OTHER, aFrom.getId(), getResources().getString(R.string.note_transfer)));
                // Make income
                RealmController.with(TransferActivity.this).addEntry(new Entry(sum, Calendar.getInstance().getTime(),
                        Constants.CAT_DEF_INC_OTHER, aTo.getId(), getResources().getString(R.string.note_transfer)));

                Toast.makeText(TransferActivity.this, getResources().getString(R.string.toast_transfer), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Clear edit when clicked into and value is 0
        editTransferValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTransferValue.getText().toString().equals(getResources().getString(R.string.f_add_edit_value_def))) {
                    editTransferValue.setText("");
                }
            }
        });
    }

    private void refreshControls() {
        Account a = (Account) spinnerFrom.getSelectedItem();
        List<Account> itemsTo = RealmController.with(this).getAccountsForTransfer(a.getId(), a.getCurrency());
        ArrayAdapter<Account> adapterTo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsTo);
        spinnerTo.setAdapter(adapterTo);
        spinnerTo.setSelection(0);

        editCurrency.setText(a.getCurrency());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
