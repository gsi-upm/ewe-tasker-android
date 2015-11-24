package es.dit.gsi.rulesframework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by afernandez on 26/10/15.
 */
public class WifiReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
                WifiManager.WIFI_STATE_UNKNOWN);

        switch(extraWifiState){
            case WifiManager.WIFI_STATE_DISABLED:
                //WifiState.setText("WIFI STATE DISABLED");
                Log.i("RULESFW","Wifi disabled");
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                //WifiState.setText("WIFI STATE DISABLING");
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                //WifiState.setText("WIFI STATE ENABLED");
                Log.i("RULESFW","Wifi enabled");
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                //WifiState.setText("WIFI STATE ENABLING");
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                //WifiState.setText("WIFI STATE UNKNOWN");
                break;
        }
    }
}
