package es.dit.gsi.rulesframework.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.SecondActivity;
import es.dit.gsi.rulesframework.adapters.MyRecyclerViewAdapter;
import es.dit.gsi.rulesframework.model.IfElement;

/**
 * Created by afernandez on 1/12/15.
 */
public class IfElementsFragment extends Fragment {

    List<Object> items = new ArrayList<>();

    Context context;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.if_fragment, container, false);

        /*bluetoothBut = (Button) rootView.findViewById(R.id.bluetoothButton);

        bluetoothBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                //fragment.setArguments(bundle)
                IfActionFragment fragment = new IfActionFragment();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
            }
        });*/
        //RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listElements);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        context = rootView.getContext();
        if(items.size()>0){
            items.clear();
            addIfElementsToLayout();
        }else{
            addIfElementsToLayout();
        }
        return rootView;
    }

    public void addIfElementsToLayout(){
        for(IfElement iE : SecondActivity.ifElementsList){
            items.add(iE);
            Log.i("ADD ITEMS: ", iE.getName());
            Log.i("ADD ITEMS: ",iE.getActions().get(0).getName());

        }
        mAdapter = new MyRecyclerViewAdapter(context,items,getParentFragment());
        mRecyclerView.setAdapter(mAdapter);
    }
}
