package es.dit.gsi.rulesframework.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.dit.gsi.rulesframework.EYEHandler;
import es.dit.gsi.rulesframework.model.Rule;
import es.dit.gsi.rulesframework.services.EYEService;

/**
 * Created by afernandez on 26/10/15.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            String channel="";
            String event = "";

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            //EYE Functions
            EYEHandler eyeHandler = new EYEHandler(context);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    //"Bluetooth off"
                    Log.i("RULESFW", "Bluetooth OFF");

                    Intent myIntent=new Intent(context,EYEService.class);
                    myIntent.putExtra("input",Rule.getInput("Bluetooth","OFF"));
                    context.startService(myIntent);

                    eyeHandler.sendInputToEye("Bluetooth", "OFF");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    //"Turning Bluetooth off..."
                    break;
                case BluetoothAdapter.STATE_ON:
                    //"Bluetooth on"
                    Log.i("RULESFW", "Bluetooth ON");
                    //Send input to EYE
                    Intent myIntent2=new Intent(context,EYEService.class);
                    myIntent2.putExtra("input",Rule.getInput("Bluetooth","ON"));
                    context.startService(myIntent2);

                    eyeHandler.sendInputToEye("Bluetooth", "OFF");                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    //"Turning Bluetooth on..."
                    break;
            }
    }
}
