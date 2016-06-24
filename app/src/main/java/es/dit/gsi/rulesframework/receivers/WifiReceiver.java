package es.dit.gsi.rulesframework.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import es.dit.gsi.rulesframework.services.RuleExecutionModule;
import es.dit.gsi.rulesframework.util.CacheMethods;

/**
 * Created by afernandez on 26/10/15.
 */
public class WifiReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        CacheMethods cacheMethods = CacheMethods.getInstance(context);
        String user=cacheMethods.getFromPreferences("beaconRuleUser","public");
        String channel="Wifi";
        String input = "";

        int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
                WifiManager.WIFI_STATE_UNKNOWN);
        RuleExecutionModule ruleExecutionModule = new RuleExecutionModule(context);

        switch(extraWifiState){
            case WifiManager.WIFI_STATE_DISABLED:
                Log.i("RULESFW","Wifi disabled");
                input = ruleExecutionModule.generateInput(channel,"Turn OFF");
                ruleExecutionModule.sendInputToEye(input, user);
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                Log.i("RULESFW","Wifi enabled");
                input = ruleExecutionModule.generateInput(channel,"Turn ON");
                ruleExecutionModule.sendInputToEye(input, user);
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                break;
        }
    }
}
