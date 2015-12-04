package es.dit.gsi.rulesframework.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.model.IfAction;

/**
 * Created by afernandez on 1/12/15.
 */
public class IfActionFragment extends Fragment {

    List<IfAction > listActions = new ArrayList();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_bluetooth_fragment,container,false);
        listActions = getArguments().getParcelableArrayList("actions");
        Log.i("PARCELABLE", listActions.get(0).getName());
        return rootView;
    }
}
