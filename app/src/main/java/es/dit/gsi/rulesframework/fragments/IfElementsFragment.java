package es.dit.gsi.rulesframework.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 1/12/15.
 */
public class IfElementsFragment extends Fragment {

    Button bluetoothBut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.if_fragment, container, false);

        bluetoothBut = (Button) rootView.findViewById(R.id.bluetoothButton);

        bluetoothBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                //fragment.setArguments(bundle)
                BluetoothDetail fragment = new BluetoothDetail();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
            }
        });
        return rootView;
    }
}
