package es.dit.gsi.rulesframework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.estimote.sdk.SystemRequirementsChecker;

/**
 * Created by afernandez on 14/03/16.
 */
public class ChooseAppActivity extends ActionBarActivity {

    LinearLayout rulesFrameworkLayout, beaconsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_app);

        rulesFrameworkLayout = (LinearLayout) findViewById(R.id.rulesFrameworkLayout);
        beaconsLayout = (LinearLayout) findViewById(R.id.beaconsLayout);

        rulesFrameworkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ListRulesActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        beaconsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),BeaconActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

    }
}
