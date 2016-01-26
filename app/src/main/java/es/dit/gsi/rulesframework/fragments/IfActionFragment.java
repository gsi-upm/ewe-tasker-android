package es.dit.gsi.rulesframework.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.NewRuleActivity;
import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.adapters.MyRecyclerViewAdapter;
import es.dit.gsi.rulesframework.model.Channel;
import es.dit.gsi.rulesframework.model.Event;

/**
 * Created by afernandez on 1/12/15.
 */
public class IfActionFragment extends Fragment {

    List<Object> items = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Context context;

    String channelSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.actions_fragment, container, false);
        channelSelected = getArguments().getString("channelSelected");


        //RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listActions);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        context = rootView.getContext();
        addIfActionsToLayout();

        return rootView;
    }

    public void addIfActionsToLayout(){
        for(Channel ch : NewRuleActivity.channelList){
            if(ch.title.equals(channelSelected)){
                List<Event> events = ch.events;
                for(Event e : events){
                    items.add(e);
                }
            }
        }
        mAdapter = new MyRecyclerViewAdapter(context,items,getParentFragment());
        mRecyclerView.setAdapter(mAdapter);
    }
}
