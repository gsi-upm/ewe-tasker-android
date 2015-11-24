package es.dit.gsi.rulesframework.triggers;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;

import es.dit.gsi.rulesframework.util.ParameterBundle;


/**
 * Created by afernandez on 24/11/15.
 */
public class WifiTrigger extends es.dit.gsi.rulesframework.framework.IntentBasedTrigger
{

    public WifiTrigger(Context context, String id, int version) {
        super(context, id, version);
    }

    @Override
    public void defineUI() {

    }

    @Override
    public void onReceive(String s, ParameterBundle parameterBundle, Bundle bundle) {
            ParameterBundle variables = new ParameterBundle();
            //Get variables
            variables.put("STATE","ON");
            trigger(s,variables);
    }

    @Override
    public void onRevoke(String s) {

    }

    public IntentFilter getIntentFilter() throws RemoteException {
        return new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
    }
}
