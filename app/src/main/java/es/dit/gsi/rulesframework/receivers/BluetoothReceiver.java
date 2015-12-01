package es.dit.gsi.rulesframework.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.dit.gsi.rulesframework.EditRulesFunctions;

/**
 * Created by afernandez on 26/10/15.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            //EYE Functions
            EditRulesFunctions eyeHandler = new EditRulesFunctions(context);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    //"Bluetooth off"
                    Log.i("RULESFW", "Bluetooth OFF");
                    eyeHandler.sendInputToEye("Bluetooth", "OFF");

                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    //"Turning Bluetooth off..."
                    break;
                case BluetoothAdapter.STATE_ON:
                    //"Bluetooth on"
                    Log.i("RULESFW", "Bluetooth ON");
                    //Send input to EYE
                    eyeHandler.sendInputToEye("Bluetooth","ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    //"Turning Bluetooth on..."
                    break;
            }
    }
}