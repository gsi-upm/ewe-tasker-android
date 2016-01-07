package es.dit.gsi.rulesframework.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.atooma.plugin.IModulePlugin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import es.dit.gsi.rulesframework.EYEHandler;
import es.dit.gsi.rulesframework.NewRuleActivity;
import es.dit.gsi.rulesframework.database.RulesSQLiteHelper;
import es.dit.gsi.rulesframework.model.Rule;
import es.dit.gsi.rulesframework.util.ParameterBundle;


/**
 * Created by afernandez on 24/11/15.
 */
public class EYEService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private String canal = "";
    private String action = "";
    private String evento = "";
    private String eventoAction = "";

    private String input = "";
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public EYEService getService() {
            // Return this instance of EYEService so clients can call public methods
            return EYEService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            input = intent.getStringExtra("input");
            Log.i("INPUT", input);
            postInputInServer(input);
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * method for clients
     */
    //GETTERS
    public String getCanal() {
        return canal;
    }

    public String getAction() {
        return action;
    }

    public String getEvento() {
        return evento;
    }

    public String getEventoAction() {
        return eventoAction;
    }

    //SETTERS
    public void setCanal(String ifElement) {
        Log.i("EYEService", "Channel: " + ifElement);
        canal = ifElement;
    }

    public void setAction(String ifAction) {
        action = ifAction;
    }

    public void setEvento(String doElement) {
        evento = doElement;
    }

    public void setEventoAction(String doAction) {
        eventoAction = doAction;
    }

    //POST to server
    public void postRuleInServer() {
        new EYEHandler.PostRuleToServerTask().execute(new Rule("name",getCanal(),getAction(),getEvento(),getEventoAction()));
    }

    public void postInputInServer(String input) {
        new EYEHandler.PostInputToServerTask().execute(input);
    }

    ;

    public void deleteRuleInServer() {
    }

    ;

    //LOCAL
    public void saveRuleInLocal(Context context) {
        Rule mRule = new Rule("name", getCanal(), getAction(), getEvento(), getEventoAction());
        RulesSQLiteHelper db = new RulesSQLiteHelper(this);
        db.addRule(mRule);
        Log.i("LOCAL", "Rule saved");
    }

    //DEBUG
    public void testService() {
        Log.i("EYEService", "Testing Service...");
    }
    /*public void registerModule(IModulePlugin var1){
    }
    public void trigger(String moduleId, String tirggerid, String ruleId, ParameterBundle variables){
        //TODO:Execute trigger
        Log.i("EYEService","Inside trigger");
    }*/
}