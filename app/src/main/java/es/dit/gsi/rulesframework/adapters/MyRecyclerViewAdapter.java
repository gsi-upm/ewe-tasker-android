package es.dit.gsi.rulesframework.adapters;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.Rule;
import es.dit.gsi.rulesframework.SecondActivity;
import es.dit.gsi.rulesframework.fragments.BaseContainerFragment;
import es.dit.gsi.rulesframework.fragments.IfActionFragment;
import es.dit.gsi.rulesframework.model.IfElement;
import es.dit.gsi.rulesframework.viewholder.ElementViewHolder;
import es.dit.gsi.rulesframework.viewholder.RuleViewHolder;

/**
 * Created by afernandez on 27/11/15.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;
    Context context;
    Fragment parentFragment;

    private final int RULE = 0, IF_ELEMENT=1;

    public MyRecyclerViewAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }
    public MyRecyclerViewAdapter(Context context, List<Object> items,Fragment parentFragment) {
        this.context = context;
        this.items = items;
        this.parentFragment = parentFragment;
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position) instanceof Rule){
            return RULE;
        }
        if(items.get(position) instanceof IfElement){
            return IF_ELEMENT;
        }
        return -1;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Dependiendo del tipo de view, cargamos el archivo layout XML correspondiente
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case RULE:
                View v1 = inflater.inflate(R.layout.recyclerview_item_rule,parent,false);
                viewHolder = new RuleViewHolder(v1);
                break;
            case IF_ELEMENT:
                View v2 = inflater.inflate(R.layout.recyclerview_item_element,parent,false);
                viewHolder = new ElementViewHolder(v2);
                break;
            default:
                viewHolder=null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case RULE:
                RuleViewHolder vh1 = (RuleViewHolder) holder;
                configureRuleViewholder(vh1, position);
                break;
            case IF_ELEMENT:
                ElementViewHolder vh2 = (ElementViewHolder) holder;
                configureElementViewHolder(vh2,position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void configureRuleViewholder(RuleViewHolder vh, int position){
        Rule mRule = (Rule) items.get(position);

        vh.ifElement.setText(mRule.getRuleName());
        vh.descriptionRule.setText(mRule.getFullRule());
    }

    public void configureElementViewHolder(ElementViewHolder vh, final int position){
        IfElement iE = (IfElement) items.get(position);
        vh.name.setText(iE.getName());

        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("actions", (ArrayList<? extends Parcelable>) ((IfElement) items.get(position)).getActions());
                //fragment.setArguments(bundle)
                if(parentFragment!=null) {
                    IfActionFragment fragment = new IfActionFragment();
                    fragment.setArguments(bundle);
                    ((BaseContainerFragment) parentFragment).replaceFragment(fragment, true);
                }
            }
        });
    }
}
