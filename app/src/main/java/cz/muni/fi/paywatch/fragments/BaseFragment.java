package cz.muni.fi.paywatch.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.muni.fi.paywatch.R;
import io.realm.Realm;


public class BaseFragment extends Fragment {

    Realm realm;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Realm.init(this.getContext());
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
