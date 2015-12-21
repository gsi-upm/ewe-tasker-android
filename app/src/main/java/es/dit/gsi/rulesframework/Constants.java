package es.dit.gsi.rulesframework;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

    public static void savePreferences(Activity activity, String key, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readPreferences(Activity activity, String key, String defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
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
}
