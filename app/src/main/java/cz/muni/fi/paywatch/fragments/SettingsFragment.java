package cz.muni.fi.paywatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cz.muni.fi.paywatch.R;
import cz.muni.fi.paywatch.activities.MainActivity;
import cz.muni.fi.paywatch.app.RealmController;
import cz.muni.fi.paywatch.model.Account;

public class SettingsFragment extends BaseFragment {

    private MainActivity mainActivity;
    private TextView accNameView;
    private TextView accCurrencyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        // Get activity pointer
        mainActivity = (MainActivity) getActivity();

        accNameView = (TextView) v.findViewById(R.id.acc_name);
        accCurrencyView = (TextView) v.findViewById(R.id.acc_currency);

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
    }

    public static SettingsFragment newInstance() {
        SettingsFragment f = new SettingsFragment();
        return f;
    }
}