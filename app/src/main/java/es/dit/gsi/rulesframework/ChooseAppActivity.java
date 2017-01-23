package es.dit.gsi.rulesframework;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.SystemRequirementsChecker;

import es.dit.gsi.rulesframework.services.RuleExecutionModule;
import es.dit.gsi.rulesframework.util.CacheMethods;
import es.dit.gsi.rulesframework.util.Tasks;

/**
 * Created by afernandez on 14/03/16.
 */
public class ChooseAppActivity extends ActionBarActivity {

    LinearLayout rulesFrameworkLayout, beaconsLayout;
    Button editButton;
    TextView server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_app);

        rulesFrameworkLayout = (LinearLayout) findViewById(R.id.rulesFrameworkLayout);
        beaconsLayout = (LinearLayout) findViewById(R.id.beaconsLayout);
        editButton = (Button) findViewById(R.id.editServer);
        server = (TextView) findViewById(R.id.server);

        //TEST
        RuleExecutionModule ruleExecutionModule = new RuleExecutionModule(getApplicationContext());
        ruleExecutionModule.executeDoResponse("CMS","Show","Library");
        //Set IP SERVER
        CacheMethods cacheMethods = CacheMethods.getInstance(getApplicationContext());
        Tasks.ipServer = cacheMethods.getFromPreferences("ipServer",Tasks.defaultGsiUrl);
        server.setText(Tasks.ipServer);

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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final CacheMethods cacheMethods = CacheMethods.getInstance(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                final View layout = inflater.inflate(R.layout.set_server_data, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), R.style.myDialogThemeAppCompat));

                alert.setView(layout);
                EditText ipField = (EditText) layout.findViewById(R.id.ipField);
                ipField.setText(cacheMethods.getFromPreferences("ipServer",Tasks.defaultGsiUrl));
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText ipField = (EditText) layout.findViewById(R.id.ipField);

                            //Save IP address
                            cacheMethods.saveInPreferences("ipServer", ipField.getText().toString());
                            Tasks.ipServer = ipField.getText().toString();
                            server.setText(Tasks.ipServer);
                        }
                    });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                alert.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

    }
}
