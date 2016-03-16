package es.dit.gsi.rulesframework.util;

/**
 * Created by afernandez on 16/03/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;


/**
 * Created by Afll on 15/02/2016.
 */
public class CacheMethods {

    public static CacheMethods instance;
    public Context context;

    public CacheMethods(Context context) {
        this.context = context;
    }


    public static  CacheMethods getInstance(Context context){
        if (instance == null) {
            instance = new CacheMethods(context);
        }
        return instance;
    }


    //Shared Preferences
    public void saveInPreferences(String key, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public String getFromPreferences(String key, String defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);
    }

    public int getIntFromPreferences(String key, int defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);
    }

    public void saveIntInPreferences(String key, int value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean isFirstTime(){
        return Boolean.parseBoolean(getFromPreferences("firstTime","true"));
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}