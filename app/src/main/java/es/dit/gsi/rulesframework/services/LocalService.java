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
public class LocalService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
    public void registerModule(IModulePlugin var1){
        //TODO:Register module LocalService
    }
    public void trigger(String moduleId, String tirggerid, String ruleId, ParameterBundle variables){
        //TODO:Execute trigger
        Log.i("LocalService","Inside trigger");
    }
}