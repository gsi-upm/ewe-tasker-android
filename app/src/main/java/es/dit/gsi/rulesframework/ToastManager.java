package es.dit.gsi.rulesframework;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by afernandez on 16/11/15.
 */

public class ToastManager {
    Context mContext;
    ToastManager(Context context){
        this.mContext=context;
    }

    public void showCustomToast(String text){
        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
    }
}
