package es.dit.gsi.rulesframework.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.dit.gsi.rulesframework.NewRuleActivity;
import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 27/11/15.
 */
public class DoFragment extends Fragment {

    private static final int ID_FRAGMENT_DO = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.do_fragment, container, false);
    }

    //Cargado al abrirse la pestaña
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //NewRuleActivity.tabHost.setCurrentTab(ID_FRAGMENT_DO); //Se cambia a la otra pestaña
    }
}
