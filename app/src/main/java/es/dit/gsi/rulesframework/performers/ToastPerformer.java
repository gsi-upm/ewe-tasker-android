package es.dit.gsi.rulesframework.performers;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by afernandez on 25/01/16.
 */
public class ToastPerformer {
    Context context;

    public ToastPerformer(Context context){
        this.context=context;
    }

    public void show(String parameter){
        Toast.makeText(context,parameter,Toast.LENGTH_LONG);
    }
}
