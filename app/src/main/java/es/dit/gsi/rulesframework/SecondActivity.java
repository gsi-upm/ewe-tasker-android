package es.dit.gsi.rulesframework;


import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import es.dit.gsi.rulesframework.adapters.MyRecyclerViewAdapter;
import es.dit.gsi.rulesframework.model.DoAction;
import es.dit.gsi.rulesframework.model.DoElement;
import es.dit.gsi.rulesframework.model.IfAction;
import es.dit.gsi.rulesframework.model.IfElement;

public class SecondActivity extends AppCompatActivity {
    List<Object> items = new ArrayList<>();

    android.support.design.widget.FloatingActionButton floatingActionButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public String json;
    public  static List<IfElement> ifElementsList = new ArrayList();
    public  static List<DoElement> doElementsList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.listRules);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new OnFloatingButtonClickListener());

        if(ifElementsList.size()>0) {
            ifElementsList.clear();
        }else {
            json = loadJSONFromAsset("elements.json");
            JSONParse(json);
            addItems();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void addItems(){
        items.add(new Rule("Example Rule","BLUETOOTH","ON","TOAST","SHOW"));

        mAdapter = new MyRecyclerViewAdapter(this,items);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class OnFloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(),NewRuleActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    //Método que lee un fichero json almacenado en assets
    public String loadJSONFromAsset(String nameFile) {
        String json = null;
        try {

            InputStream is = getAssets().open(nameFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    //Read JSON
    public void JSONParse(String json){

        try{
            JSONObject jsonObject = new JSONObject(json);

            //IF
            JSONObject ifElements = jsonObject.getJSONObject("ifElements");

            Iterator iterIdIfElement = ifElements.keys();
            while (iterIdIfElement.hasNext()){
                String idElement= String.valueOf(iterIdIfElement.next());
                JSONObject element = ifElements.getJSONObject(idElement);

                IfElement ifElement= new IfElement();
                ifElement.setName(element.getString("Name"));
                ifElement.setType(element.getString("Type"));
                ifElement.setResourceName(element.getString("ResourceId"));
                ifElement.setReceiverName(element.getString("ReceiverName"));
                //Actions
                JSONObject ifActions = element.getJSONObject("Actions");
                List<IfAction> actions = new ArrayList<IfAction>();
                Iterator iterIDIfAction = ifActions.keys();
                while(iterIDIfAction.hasNext()){
                    IfAction action = new IfAction();
                    String id = (String) iterIDIfAction.next();
                    action.setName(ifActions.getJSONObject(id).getString("name"));
                    action.setParamType(ifActions.getJSONObject(id).getString("type"));
                    actions.add(action);
                    Log.i("JSONPARSER", action.getName());
                    Log.i("JSONPARSER", action.getParamType());
                }
                ifElement.setActions(actions);
                ifElementsList.add(ifElement);
            }

            //DO
            JSONObject doElements = jsonObject.getJSONObject("doElements");
            Iterator iterIdDoElement = ifElements.keys();
            while (iterIdDoElement.hasNext()){
                String idElement= String.valueOf(iterIdDoElement.next());
                JSONObject element = doElements.getJSONObject(idElement);

                DoElement doElement= new DoElement();
                doElement.setName(element.getString("Name"));
                doElement.setType(element.getString("Type"));
                doElement.setResourceName(element.getString("ResourceId"));
                doElement.setReceiverName(element.getString("ReceiverName"));
                //Actions
                JSONObject doActions = element.getJSONObject("Actions");
                List<DoAction> actions = new ArrayList<DoAction>();
                Iterator iterIDDoAction = doActions.keys();
                while(iterIDDoAction.hasNext()){
                    DoAction action = new DoAction();
                    String id = (String) iterIDDoAction.next();
                    action.setName(doActions.getJSONObject(id).getString("name"));
                    action.setParamType(doActions.getJSONObject(id).getString("type"));
                    actions.add(action);
                    Log.i("JSONPARSER", action.getName());
                    Log.i("JSONPARSER", action.getParamType());
                }
                doElement.setActions(actions);
                doElementsList.add(doElement);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}