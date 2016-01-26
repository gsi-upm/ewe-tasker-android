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

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.NewRuleActivity;
import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.adapters.MyRecyclerViewAdapter;
import es.dit.gsi.rulesframework.model.Channel;

/**
 * Created by afernandez on 1/12/15.
 */
public class DoElementsFragment extends Fragment {
    List<Object> items = new ArrayList<>();

    Context context;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.do_fragment, container, false);

        //RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listElements);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        context = rootView.getContext();
        if(items.size()>0){
            items.clear();
            addDoElementsToLayout();
        }else{
            addDoElementsToLayout();
        }
        return rootView;
    }

    public void addDoElementsToLayout(){
        for(Channel channel : NewRuleActivity.channelList){
            if(channel.hasActions()){
                items.add(channel);
                Log.i("ADD ITEMS: ", channel.title);
            }
        }
        mAdapter = new MyRecyclerViewAdapter(context,items,getParentFragment());
        mRecyclerView.setAdapter(mAdapter);    }
}
