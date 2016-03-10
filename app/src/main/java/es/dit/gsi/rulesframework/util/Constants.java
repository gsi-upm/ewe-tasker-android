package es.dit.gsi.rulesframework.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import es.dit.gsi.rulesframework.model.Rule;

/**
 * Created by afernandez on 23/10/15.
 */
public class Constants {
    private Constants(){}

    //List active receivers on rules
    public static ArrayList<String> activeReceiversList = new ArrayList<>();
    public static ArrayList<Rule> ACTIVE_RULES_LIST = new ArrayList<>();
    public static String lastInputSent = "";


    public static void savePreferences(Context context, String key, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readPreferences(Context context, String key, String defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);
    }

    public static void saveArrayPref(Context context, String key, ArrayList<?> values) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(values);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public static ArrayList<?> getArrayPref(Context context, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Rule>>() {}.getType();
        ArrayList<Rule> list = gson.fromJson(json, type);
        if(list == null)
            list = new ArrayList<>();
        return list;
    }


    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile, 1.6 km

}
