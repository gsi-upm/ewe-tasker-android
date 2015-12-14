package es.dit.gsi.rulesframework.adapters;

import android.app.Activity;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.MainActivity;
import es.dit.gsi.rulesframework.NewRuleActivity;
import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.Rule;
import es.dit.gsi.rulesframework.SecondActivity;
import es.dit.gsi.rulesframework.fragments.BaseContainerFragment;
import es.dit.gsi.rulesframework.fragments.DoActionFragment;
import es.dit.gsi.rulesframework.fragments.IfActionFragment;
import es.dit.gsi.rulesframework.model.DoAction;
import es.dit.gsi.rulesframework.model.DoElement;
import es.dit.gsi.rulesframework.model.IfAction;
import es.dit.gsi.rulesframework.model.IfElement;
import es.dit.gsi.rulesframework.services.EYEService;
import es.dit.gsi.rulesframework.viewholder.ActionViewHolder;
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

    private final int RULE = 0, IF_ELEMENT=1, IF_ACTION=2, DO_ELEMENT =3, DO_ACTION =4;


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
        if(items.get(position) instanceof IfAction){
            return IF_ACTION;
        }
        if(items.get(position) instanceof DoElement){
            return DO_ELEMENT;
        }
        if(items.get(position) instanceof DoAction){
            return DO_ACTION;
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
            case IF_ACTION:
                View v3 = inflater.inflate(R.layout.recyclerview_item_action,parent,false);
                viewHolder = new ActionViewHolder(v3);
                break;
            case DO_ELEMENT:
                View v4 = inflater.inflate(R.layout.recyclerview_item_element,parent,false);
                viewHolder = new ElementViewHolder(v4);
                break;
            case DO_ACTION:
                View v5 = inflater.inflate(R.layout.recyclerview_item_action,parent,false);
                viewHolder = new ActionViewHolder(v5);
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
                configureIfElementViewHolder(vh2, position);
                break;
            case IF_ACTION:
                ActionViewHolder vh3 = (ActionViewHolder) holder;
                configureIfActionViewHolder(vh3, position);
                break;
            case DO_ELEMENT:
                ElementViewHolder vh4 = (ElementViewHolder) holder;
                configureDoElementViewHolder(vh4, position);
                break;
            case DO_ACTION:
                ActionViewHolder vh5 = (ActionViewHolder) holder;
                configureDoActionViewHolder(vh5, position);
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

    public void configureIfElementViewHolder(ElementViewHolder vh, final int position){
        final IfElement iE = (IfElement) items.get(position);
        vh.name.setText(iE.getName());
        Log.i("ADAPTER onClick", iE.getActions().get(0).getName());

        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save on EYEService
                NewRuleActivity.mService.setCanal(iE.getName());
                //Optional set parameter on bundle
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("actions", (ArrayList<? extends Parcelable>) ((IfElement) items.get(position)).getActions());
                //fragment.setArguments(bundle)
                if (parentFragment != null) {
                    IfActionFragment fragment = new IfActionFragment();
                    fragment.setArguments(bundle);
                    ((BaseContainerFragment) parentFragment).replaceFragment(fragment, true);
                }
            }
        });
    }
    public void configureIfActionViewHolder(final ActionViewHolder vh,final int position){
        IfAction iA = (IfAction) items.get(position);
        vh.name.setText(iA.getName());
        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                IfAction iA = (IfAction)items.get(position);
                LayoutInflater inflater = LayoutInflater.from(context);
                switch (iA.getParamType()){
                    case "boolean":
                        changeToDoTab();
                        break;
                    case "String":
                        //Request String parameter
                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        //this is what I did to added the layout to the alert dialog
                        View layout=inflater.inflate(R.layout.set_parameter,null);
                        alert.setView(layout);
                        alert.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO:SAVE
                                changeToDoTab();
                            }
                        });
                        alert.show();
                        break;
                    case "Integer":
                        //Request Integer parameter
                        final AlertDialog.Builder alertInt = new AlertDialog.Builder(context);
                        //this is what I did to added the layout to the alert dialog
                        View layoutInt=inflater.inflate(R.layout.set_parameter,null);
                        alertInt.setView(layoutInt);
                        alertInt.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            //TODO:SAVE
                            changeToDoTab();
                            }
                        });
                        alertInt.show();
                        break;

                }
            }
            public void changeToDoTab() {
                NewRuleActivity.tabHost.getTabWidget().getChildTabViewAt(0).setEnabled(false);
                NewRuleActivity.tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(true);
                NewRuleActivity.tabHost.setCurrentTab(1);
            }
        });
    }

    public void configureDoElementViewHolder(ElementViewHolder vh,final int position){
        DoElement dE = (DoElement) items.get(position);
        vh.name.setText(dE.getName());
        Log.i("ADAPTER onClick", dE.getActions().get(0).getName());

        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("actions", (ArrayList<? extends Parcelable>) ((DoElement) items.get(position)).getActions());
                //fragment.setArguments(bundle)
                if (parentFragment != null) {
                    DoActionFragment fragment = new DoActionFragment();
                    fragment.setArguments(bundle);
                    ((BaseContainerFragment) parentFragment).replaceFragment(fragment, true);
                }
            }
        });
    }
    public void configureDoActionViewHolder (ActionViewHolder vh,final int position){
        DoAction dA = (DoAction) items.get(position);
        vh.name.setText(dA.getName());
        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                DoAction dA = (DoAction) items.get(position);
                LayoutInflater inflater = LayoutInflater.from(context);
                switch (dA.getParamType()) {
                    case "boolean":
                        saveAndBack();
                        break;
                    case "String":
                        //Request String parameter
                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        //this is what I did to added the layout to the alert dialog
                        View layout = inflater.inflate(R.layout.set_parameter, null);
                        alert.setView(layout);
                        alert.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO:SAVE
                                saveAndBack();
                            }
                        });
                        alert.show();
                        break;
                    case "Integer":
                        //Request Integer parameter
                        final AlertDialog.Builder alertInt = new AlertDialog.Builder(context);
                        //this is what I did to added the layout to the alert dialog
                        View layoutInt = inflater.inflate(R.layout.set_parameter, null);
                        alertInt.setView(layoutInt);
                        alertInt.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO:SAVE
                                saveAndBack();
                            }
                        });
                        alertInt.show();
                        break;
                }
            }

            public void saveAndBack() {
                //TODO:SAVE
                /*Intent mIntent=new Intent(context,SecondActivity.class);
                context.startActivity(mIntent);*/
                ((Activity) context).finish();

            }
        });
    }


}
