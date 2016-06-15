package es.dit.gsi.rulesframework;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import es.dit.gsi.rulesframework.util.CacheMethods;
import es.dit.gsi.rulesframework.util.Tasks;

/**
 * Created by afernandez on 16/03/16.
 */
public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View layout = inflater.inflate(R.layout.set_door_login, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialogThemeAppCompat));

        alert.setView(layout);

        alert.setPositiveButton("LOG IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText passField = (EditText) layout.findViewById(R.id.passField);
                CheckBox remember = (CheckBox) layout.findViewById(R.id.remember);

                //Login server task
                new Tasks.LoginGSIServerTask(getApplicationContext()).execute(passField.getText().toString(), String.valueOf(remember.isChecked()));
                finish();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
