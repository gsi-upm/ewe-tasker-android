package es.dit.gsi.rulesframework;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;

import es.dit.gsi.rulesframework.database.RulesSQLiteHelper;
import es.dit.gsi.rulesframework.fragments.BaseContainerFragment;
import es.dit.gsi.rulesframework.fragments.DoContainerFragment;
import es.dit.gsi.rulesframework.fragments.IfContainerFragment;
import es.dit.gsi.rulesframework.services.RuleDefinitionModule;

/**
 * Created by afernandez on 27/11/15.
 */
public class NewRuleActivity extends FragmentActivity{

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

        //Service
        Log.i("RuleDefinitionModule", "Service Connected");
        Intent intent = new Intent(this, RuleDefinitionModule.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        //Tabhost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("if_fragment").setIndicator("IF"), IfContainerFragment.class, null); //Añado las pestañas
        tabHost.addTab(tabHost.newTabSpec("do_fragment").setIndicator("DO"), DoContainerFragment.class, null);
        //Hago que no sea clickable para que no cargue un navegador vacio
        tabHost.getTabWidget().getChildTabViewAt(ID_FRAGMENT_DO).setEnabled(false);

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
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("if_fragment")).popFragment();
        } else if (currentTabTag.equals("do_fragment")) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("do_fragment")).popFragment();
        }
        if (!isPopFragment) {
            finish();
        }
    }
}
