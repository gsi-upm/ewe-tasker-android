package es.dit.gsi.rulesframework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.Rule;
import es.dit.gsi.rulesframework.viewholder.RuleViewHolder;

/**
 * Created by afernandez on 27/11/15.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;
    Context context;

    private final int RULE = 0;

    public MyRecyclerViewAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position) instanceof Rule){
            return RULE;
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
}
