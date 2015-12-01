package es.dit.gsi.rulesframework.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 1/12/15.
 */
public class BluetoothDetail extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_bluetooth_fragment,container,false);
        return rootView;
    }
}
