package es.dit.gsi.rulesframework.performers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import es.dit.gsi.rulesframework.LoginActivity;
import es.dit.gsi.rulesframework.NewRuleActivity;
import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.util.Tasks;

/**
 * Created by afernandez on 16/03/16.
 */
public class DoorPerformer {
    Context context;

    public DoorPerformer(Context context){
        this.context = context;
    }

    public void openDoor(Context context){
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
