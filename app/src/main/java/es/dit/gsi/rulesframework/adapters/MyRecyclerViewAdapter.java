package es.dit.gsi.rulesframework.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeoutException;

import es.dit.gsi.rulesframework.NewRuleActivity;
import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.model.Action;
import es.dit.gsi.rulesframework.model.Channel;
import es.dit.gsi.rulesframework.model.Event;
import es.dit.gsi.rulesframework.model.NamedGeofence;
import es.dit.gsi.rulesframework.model.Rule;
import es.dit.gsi.rulesframework.fragments.BaseContainerFragment;
import es.dit.gsi.rulesframework.fragments.DoActionFragment;
import es.dit.gsi.rulesframework.fragments.IfActionFragment;
import es.dit.gsi.rulesframework.viewholder.ActionViewHolder;
import es.dit.gsi.rulesframework.viewholder.ElementViewHolder;
import es.dit.gsi.rulesframework.viewholder.GeofenceViewHolder;
import es.dit.gsi.rulesframework.viewholder.RuleViewHolder;

/**
 * Created by afernandez on 27/11/15.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;
    Context context;
    Fragment parentFragment;
    private Gson gson;
    private SharedPreferences prefs;

    private final int RULE = 0, IF_ELEMENT=1, IF_ACTION=2, DO_ELEMENT =3, DO_ACTION =4, GEOFENCE=5;


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
        if(items.get(position) instanceof Channel && !NewRuleActivity.isIfChannelSelected){
            return IF_ELEMENT;
        }
        if(items.get(position) instanceof Event){
            return IF_ACTION;
        }
        if(items.get(position) instanceof Channel && NewRuleActivity.isIfChannelSelected){
            return DO_ELEMENT;
        }
        if(items.get(position) instanceof Action){
            return DO_ACTION;
        }
        if(items.get(position) instanceof NamedGeofence){
            return GEOFENCE;
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
            case GEOFENCE:
                View v6 = inflater.inflate(R.layout.recyclerview_item_geofence,parent,false);
                viewHolder = new GeofenceViewHolder(v6);
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
            case GEOFENCE:
                GeofenceViewHolder vh6 = (GeofenceViewHolder) holder;
                configureGeofenceViewHolder(vh6, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void configureRuleViewholder(RuleViewHolder vh, int position){
        Log.i("Rules", "Configure Rule ViewHolder");
        Rule mRule = (Rule) items.get(position);

        Picasso.with(context).load("http://taskautomationserver.ddns.net/taskautomationweb/img/"+mRule.getIfElement()+".png").resize(170, 170).into(vh.ifChannel);
        Picasso.with(context).load("http://taskautomationserver.ddns.net/taskautomationweb/img/"+mRule.getDoElement()+".png").resize(170,170).into(vh.doChannel);

        vh.ifElement.setTypeface(Typeface.createFromAsset(context.getAssets(), "century-gothic-bold.ttf"));
        vh.doElement.setTypeface(Typeface.createFromAsset(context.getAssets(), "century-gothic-bold.ttf"));
        vh.descriptionRule.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));
        vh.descriptionRule.setText(mRule.getDescription());
    }

    public void configureIfElementViewHolder(ElementViewHolder vh, final int position){
        final Channel ch = (Channel) items.get(position);
        vh.name.setText(ch.title);
        vh.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));

        Picasso.with(context).load("http://taskautomationserver.ddns.net/taskautomationweb/img/"+ch.title+".png").resize(200,200).into(vh.icon);
        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewRuleActivity.isIfChannelSelected = true;
                //Save on RuleDefinitionModule
                NewRuleActivity.mService.setIfChannel(ch.title);
                //Optional set parameter on bundle
                Bundle bundle = new Bundle();
                bundle.putString("channelSelected", ch.title);
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
        Event event = (Event) items.get(position);
        vh.name.setText(event.title);
        vh.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));

        Picasso.with(context).load(R.drawable.arrow_right).resize(200,200).into(vh.icon);
        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                final Event event = (Event) items.get(position);
                NewRuleActivity.mService.setEvento(event.title);
                LayoutInflater inflater = LayoutInflater.from(context);

                //Get Channel Parent Title
                String channelParentTitle = "";
                if (event.hasParameters()) {
                    for (Channel c : NewRuleActivity.channelList) {
                        String title = c.title;
                        for (Event e : c.events) {
                            if (e.title.equals(event.title)) {
                                channelParentTitle = title;
                            }
                        }
                    }

                    switch (channelParentTitle) {
                        case "Location":
                            //Request Geofence
                            final AlertDialog.Builder alertGeo = new AlertDialog.Builder(context);
                            //this is what I did to added the layout to the alert dialog
                            final View layoutGeo = inflater.inflate(R.layout.set_geofence, null);
                            alertGeo.setView(layoutGeo);
                            alertGeo.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText name = (EditText) layoutGeo.findViewById(R.id.name);
                                    EditText latitude = (EditText) layoutGeo.findViewById(R.id.latitude);
                                    EditText longitude = (EditText) layoutGeo.findViewById(R.id.longitude);
                                    EditText radius = (EditText) layoutGeo.findViewById(R.id.radius);

                                    //Create Geofence
                                    String nombre = name.getText().toString();
                                    float lat = Float.parseFloat(latitude.getText().toString());
                                    float longit = Float.parseFloat(longitude.getText().toString());
                                    float rad = Float.parseFloat(radius.getText().toString());

                                    /**SAVE GEOFENCE**/
                                    NamedGeofence geofence = new NamedGeofence(nombre, lat, longit, rad);
                                    //Get from local
                                    gson = new Gson();
                                    prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

                                    //Save new geofence
                                    String json = gson.toJson(geofence);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString(geofence.id, json);
                                    editor.apply();

                                    //NewRuleActivity.mService.setIfParameter(geofence.name);
                                    changeToDoTab();
                                }
                            });
                            alertGeo.show();
                            break;
                        default:
                            //Request String parameter
                            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            //this is what I did to added the layout to the alert dialog
                            final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.set_parameter, null);


                            int i = 1;
                            while(i<=Integer.parseInt(event.numParameters)){
                                EditText editText = new EditText(context);
                                editText.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT));
                                editText.setId(1+i);
                                layout.addView(editText);
                                i++;
                            }

                            alert.setView(layout);
                            alert.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for(int i = 1; i<=Integer.parseInt(event.numParameters);i++){
                                        EditText et = (EditText) layout.findViewById(1+i);
                                        Log.i("TEST ALERT",et.getText().toString());
                                        //Save parameter
                                        NewRuleActivity.mService.addIfParameter(et.getText().toString());
                                    }
                                    //Save parameter
                                    changeToDoTab();
                                }
                            });
                            alert.show();
                            break;
                    }

                } else {
                    changeToDoTab();
                }
            }

            public void changeToDoTab() {
                NewRuleActivity.tabHost.getTabWidget().getChildTabViewAt(0).setEnabled(false);
                NewRuleActivity.tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(true);
                NewRuleActivity.tabHost.setCurrentTab(1);

                //Tab color
                for (int i = 0; i < NewRuleActivity.tabHost.getTabWidget().getChildCount(); i++) {
                    View v = NewRuleActivity.tabHost.getTabWidget().getChildAt(i);
                    if(v.isSelected()){
                        v.setBackgroundColor(context.getResources().getColor(R.color.blueDesc));
                    }else{
                        v.setBackgroundColor(context.getResources().getColor(R.color.grey));
                    }
                    TextView tv = (TextView) NewRuleActivity.tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "century-gothic-bold.ttf"));
                    tv.setTextColor(context.getResources().getColor(R.color.white));
                }
            }
        });
    }

    public void configureDoElementViewHolder(ElementViewHolder vh,final int position){
        Channel channel = (Channel) items.get(position);
        vh.name.setText(channel.title);
        vh.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));

        Picasso.with(context).load("http://taskautomationserver.ddns.net/taskautomationweb/img/"+channel.title+".png").resize(200,200).into(vh.icon);
        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                Channel channel = (Channel) items.get(position);
                Bundle bundle = new Bundle();
                NewRuleActivity.mService.setDoChannel(channel.title);
                bundle.putString("channelSelected", channel.title);
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
        Action action = (Action) items.get(position);
        Picasso.with(context).load(R.drawable.arrow_right).resize(200,200).into(vh.icon);
        vh.name.setText(action.title);
        vh.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));

        vh.layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Optional set parameter on bundle
                final Action action = (Action) items.get(position);
                NewRuleActivity.mService.setAction(action.title);
                LayoutInflater inflater = LayoutInflater.from(context);
                if (action.hasParameters()) {
                    //Request String parameter
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    //this is what I did to added the layout to the alert dialog
                    final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.set_parameter, null);


                    int i = 1;
                    while(i<=Integer.parseInt(action.numParameters)){
                        EditText editText = new EditText(context);
                        editText.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        editText.setId(1+i);
                        layout.addView(editText);
                        i++;
                    }

                    alert.setView(layout);
                    alert.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i = 1; i<=Integer.parseInt(action.numParameters);i++){
                                EditText et = (EditText) layout.findViewById(1+i);
                                Log.i("TEST ALERT",et.getText().toString());
                                NewRuleActivity.mService.addDoParameter(et.getText().toString());
                            }
                            //Save parameter
                            setRuleNameAndPlace();
                        }
                    });
                    alert.show();

                } else {
                    setRuleNameAndPlace();
                }
            }

            public void saveAndBack() {
                NewRuleActivity.mService.saveRuleInLocal(context);//SQL
                NewRuleActivity.mService.postRuleInServer();
                NewRuleActivity.mService.resetService();
                NewRuleActivity.isIfChannelSelected = false;

                /*Intent mIntent=new Intent(context,ListRulesActivity.class);
                context.startActivity(mIntent);*/
                ((Activity) context).finish();

            }

            public void setRuleNameAndPlace() {
                LayoutInflater inflater = LayoutInflater.from(context);
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                //this is what I did to added the layout to the alert dialog
                final View layout = inflater.inflate(R.layout.set_rulename, null);

                TextView nameTv = (TextView) layout.findViewById(R.id.nametv);
                TextView descTv = (TextView) layout.findViewById(R.id.desctv);
                TextView placeTv = (TextView) layout.findViewById(R.id.placetv);
                nameTv.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));
                descTv.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));
                placeTv.setTypeface(Typeface.createFromAsset(context.getAssets(), "Titillium-Regular.otf"));

                alert.setView(layout);
                alert.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText nameField = (EditText) layout.findViewById(R.id.ruleName);
                        EditText placeField = (EditText) layout.findViewById(R.id.place);
                        EditText descField = (EditText) layout.findViewById(R.id.description);

                        //Save rulename
                        NewRuleActivity.mService.setRuleName(nameField.getText().toString());
                        NewRuleActivity.mService.setPlace(placeField.getText().toString());
                        NewRuleActivity.mService.setDescription(descField.getText().toString());
                        saveAndBack();
                    }
                });
                alert.show();
            }
        });
    }

    public void configureGeofenceViewHolder(final GeofenceViewHolder vh,final int position){
        NamedGeofence ng = (NamedGeofence) items.get(position);

        vh.logoGeofence.setImageResource(R.mipmap.ic_launcher);

        vh.title.setText(ng.name);
        vh.latitude.setText(String.valueOf(ng.latitude));
        vh.longitude.setText(String.valueOf(ng.longitude));
        vh.radius.setText(String.valueOf(ng.radius));
    }

    public boolean isIfChannelSelected(){
        if(!NewRuleActivity.mService.getIfChannel().equals("")){
            return true;
        }else{
            return false;
        }
    }

}
