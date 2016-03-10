package es.dit.gsi.rulesframework.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.dit.gsi.rulesframework.services.RuleExecutionModule;

/**
 * Created by afernandez on 26/10/15.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            String user="afll";
            String channel="Bluetooth";
            String input = "";

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            //EYE Functions
            RuleExecutionModule ruleExecutionModule = new RuleExecutionModule(context);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    //"Bluetooth off"
                    Log.i("RULESFW", "Bluetooth OFF");
                    input = ruleExecutionModule.generateInput(channel,"Turn OFF");
                    ruleExecutionModule.sendInputToEye(input,user);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    //"Turning Bluetooth off..."
                    break;
                case BluetoothAdapter.STATE_ON:
                    //"Bluetooth on"
                    Log.i("RULESFW", "Bluetooth ON");
                    //Send input to EYE
                    input = ruleExecutionModule.generateInput(channel,"Turn ON");
                    ruleExecutionModule.sendInputToEye(input, user);
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    //"Turning Bluetooth on..."
                    break;
            }
    }
}
