package es.dit.gsi.rulesframework;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import es.dit.gsi.rulesframework.fragments.BaseContainerFragment;
import es.dit.gsi.rulesframework.fragments.DoContainerFragment;
import es.dit.gsi.rulesframework.fragments.DoFragment;
import es.dit.gsi.rulesframework.fragments.IfContainerFragment;
import es.dit.gsi.rulesframework.model.IfAction;
import es.dit.gsi.rulesframework.model.IfElement;

/**
 * Created by afernandez on 27/11/15.
 */
public class NewRuleActivity extends FragmentActivity{

    private static final int ID_FRAGMENT_DO=1;
    FragmentPagerAdapter adapterViewPager;
    public static FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rule);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("if_fragment").setIndicator("IF"), IfContainerFragment.class, null); //Añado las pestañas
        tabHost.addTab(tabHost.newTabSpec("do_fragment").setIndicator("DO"), DoContainerFragment.class, null);
        //Hago que no sea clickable para que no cargue un navegador vacio
        tabHost.getTabWidget().getChildTabViewAt(ID_FRAGMENT_DO).setEnabled(false);
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
