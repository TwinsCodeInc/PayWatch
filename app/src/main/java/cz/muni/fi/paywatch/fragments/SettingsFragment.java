package cz.muni.fi.paywatch.fragments;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.activities.MainActivity;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Account;

public class SettingsFragment extends Fragment {

    private MainActivity mainActivity;
    private TextView accNameView;
    private TextView accColor;
    private TextView accCurrencyView;
    private Button accRemoveBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        // Get activity pointer
        mainActivity = (MainActivity) getActivity();

        accNameView = (TextView) v.findViewById(R.id.acc_name);
        accCurrencyView = (TextView) v.findViewById(R.id.acc_currency);
        accRemoveBtn = (Button) v.findViewById(R.id.acc_remove_btn);
        accColor = (TextView) v.findViewById(R.id.acc_color);

        // Listeners
        accNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAccountDialogName();
            }
        });
        accCurrencyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAccountDialogCurency();
            }
        });
        accRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RealmController.with(SettingsFragment.this).getAccountsCount() == 1) {
                    Toast.makeText(getActivity(), R.string.sett_acc_remove_not_allowed, Toast.LENGTH_SHORT).show();
                    return;
                }
                showAccountRemoveConfirmation();
            }
        });
        accColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = ((ColorDrawable) accColor.getBackground()).getColor();
                int red = (c >> 16) & 0xff;
                int green = (c >> 8) & 0xff;
                int blue = c & 0xff;
                final ColorPicker cp = new ColorPicker(getActivity(), red, green, blue);
                // Set a new Listener called when user click "select"
                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        RealmController.with(SettingsFragment.this).updateAccountColor(mainActivity.getCurrentAccountId(), color);
                        refreshAccount();
                        cp.dismiss();
                    }
                });
                cp.show();

            }
        });

        refreshControls();

        return v;
    }

    // Refreshes the controls for whole settings
    public void refreshControls() {
        refreshAccount();
    }

    // Refreshes the controls for account
    public void refreshAccount() {
        Account a = RealmController.with(this).getAccount(mainActivity.getCurrentAccountId());
        accNameView.setText(a.getName());
        accCurrencyView.setText(a.getCurrency());
        accColor.setBackgroundColor(a.getColor());
    }

    public static SettingsFragment newInstance() {
        SettingsFragment f = new SettingsFragment();
        return f;
    }

    // Shows dialog for a new account name
    public void showAddAccountDialogName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.acc_dialog_title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setText(accNameView.getText());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.acc_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if (!name.isEmpty()) {
                    RealmController.with(SettingsFragment.this).updateAccountName(mainActivity.getCurrentAccountId(), name);
                    refreshAccount();
                    mainActivity.refreshAccounts();
                }
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

    // Shows dialog for a account currency
    public void showAddAccountDialogCurency() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.acc_currency_dialog_title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setText(accCurrencyView.getText());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.acc_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currency = input.getText().toString().trim();
                if (!currency.isEmpty()) {
                    RealmController.with(SettingsFragment.this).updateAccountCurrency(mainActivity.getCurrentAccountId(), currency);
                    refreshAccount();
                }
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

    // Shows dialog for a new account name
    public void showAccountRemoveConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.sett_acc_remove_confirmation);

        // Set up the buttons
        builder.setPositiveButton(R.string.acc_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RealmController.with(SettingsFragment.this).removeAccount(mainActivity.getCurrentAccountId());
                mainActivity.refreshAccounts();
                refreshAccount();
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