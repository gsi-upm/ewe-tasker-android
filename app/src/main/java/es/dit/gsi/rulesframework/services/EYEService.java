package es.dit.gsi.rulesframework.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.atooma.plugin.IModulePlugin;

import java.util.Random;

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

    /** method for clients */
    public void setCanal(String ifElement) {
        canal = ifElement;
    }
    public void setAction(String ifAction){
        action = ifAction;
    }
    public void setEvento(String doElement){

    }
    public void setEventoAction(String doAction){

    }
    public void registerModule(IModulePlugin var1){
    }
    public void trigger(String moduleId, String tirggerid, String ruleId, ParameterBundle variables){
        //TODO:Execute trigger
        Log.i("EYEService","Inside trigger");
    }
}