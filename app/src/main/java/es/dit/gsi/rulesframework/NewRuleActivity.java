package es.dit.gsi.rulesframework;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.dit.gsi.rulesframework.database.RulesSQLiteHelper;
import es.dit.gsi.rulesframework.fragments.BaseContainerFragment;
import es.dit.gsi.rulesframework.fragments.DoContainerFragment;
import es.dit.gsi.rulesframework.fragments.IfContainerFragment;
import es.dit.gsi.rulesframework.model.Action;
import es.dit.gsi.rulesframework.model.Channel;
import es.dit.gsi.rulesframework.model.Event;
import es.dit.gsi.rulesframework.services.RuleDefinitionModule;
import es.dit.gsi.rulesframework.util.Tasks;

/**
 * Created by afernandez on 27/11/15.
 */
public class NewRuleActivity extends AppCompatActivity{

    public static boolean isIfChannelSelected = false;


    public static List<Channel> channelList;

    private static final int ID_FRAGMENT_DO=1;
    FragmentPagerAdapter adapterViewPager;
    public static FragmentTabHost tabHost;

    public static RuleDefinitionModule mService;
    boolean isBound = false;

    RulesSQLiteHelper db;

    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            RuleDefinitionModule.LocalBinder binder = (RuleDefinitionModule.LocalBinder) service;
            mService = binder.getService();
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rule);

        //ActionBar
        getSupportActionBar().setTitle("IF - DO Rules Definition");

        //Service
        Log.i("RuleDefinitionModule", "Service Connected");
        Intent intent = new Intent(this, RuleDefinitionModule.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        channelList=new ArrayList<>();
        try {
            getChannels();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Tabhost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("if_fragment").setIndicator("IF"), IfContainerFragment.class, null); //Añado las pestañas
        tabHost.addTab(tabHost.newTabSpec("do_fragment").setIndicator("DO"), DoContainerFragment.class, null);
        //Hago que no sea clickable para que no cargue un navegador vacio
        tabHost.getTabWidget().getChildTabViewAt(ID_FRAGMENT_DO).setEnabled(false);

        //Tab color
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View v = tabHost.getTabWidget().getChildAt(i);
            if(v.isSelected()){
                v.setBackgroundColor(getResources().getColor(R.color.blueDesc));
            }else{
                v.setBackgroundColor(getResources().getColor(R.color.grey));
            }
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "century-gothic-bold.ttf"));
            tv.setTextColor(getResources().getColor(R.color.white));
        }
        //DB
        db =  new RulesSQLiteHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        String currentTabTag = tabHost.getCurrentTabTag();
        if (currentTabTag.equals("if_fragment")) {
            isIfChannelSelected = false;
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("if_fragment")).popFragment();
        } else if (currentTabTag.equals("do_fragment")) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("do_fragment")).popFragment();
        }
        if (!isPopFragment) {
            isIfChannelSelected = false;
            finish();
        }
    }

    //Channels

    public void getChannels() throws JSONException {
        String json = Constants.readPreferences(getApplicationContext(),"channelsJson","");

        channelList = SecondActivity.translateJSONtoList(json);
    }
}
