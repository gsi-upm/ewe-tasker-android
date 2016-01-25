package es.dit.gsi.rulesframework.performers;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by afernandez on 25/01/16.
 */
public class WifiPerformer {
    Context context;

    public WifiPerformer(Context context){
        this.context = context;
    }

    public void turnOn(){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }
    public void turnOff(){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }
}
