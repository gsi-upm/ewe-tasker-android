package es.dit.gsi.rulesframework;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import es.dit.gsi.rulesframework.services.RuleExecutionModule;
import es.dit.gsi.rulesframework.util.Tasks;

/**
 * Created by afernandez on 14/03/16.
 */
public class BeaconActivity extends ActionBarActivity {
    BeaconManager beaconManager;
    private Region region;
    CountDownTimer cdt;

    RuleExecutionModule ruleExecutionModule;

    boolean sendEvent = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacons);

        ruleExecutionModule = new RuleExecutionModule(getApplicationContext());

        beaconManager = new BeaconManager(getApplicationContext());
        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                didRangeBeacons(list, region);
            }
        });

        cdt = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.i("Countdown","Finished");
                sendEvent = true;
                start();
            }

        }.start();


    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, BeaconManager.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public void didRangeBeacons(List<Beacon> beacons, Region region){
        if(sendEvent) {
            for (Beacon beacon : beacons) {
                int beaconId = beacon.getMajor();
                double accuracy = Utils.computeAccuracy(beacon);

                String event = "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . @prefix ewe-presence: <http://gsi.dit.upm.es/ontologies/ewe-connected-home-presence/ns/#> . @prefix ewe: <http://gsi.dit.upm.es/ontologies/ewe/ns/#> . @prefix ewe-presence: <http://gsi.dit.upm.es/ontologies/ewe-connected-home-presence/ns/#> . ewe-presence:PresenceSensor" +
                        beaconId +
                        " rdf:type ewe-presence:PresenceDetectedAtDistance. ewe-presence:PresenceSensor"+
                        beaconId+
                        " ewe:sensorID \"" +
                        beaconId +
                        "\". ewe-presence:PresenceSensor" +
                        beaconId +
                        " ewe:distance " +
                        accuracy+
                        ".";
                String user = "afll";
                String inputEvent = event;
                String place = "";
                String[] params = {inputEvent,user};

                //Task send to sever
                String response = "";
                try {
                    response = new Tasks.PostInputToServerTask().execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.i("Countdown", "Beacon delivery finished");

                //Send response to RuleExecutionModule
                ruleExecutionModule.handleServerResponse(response);
            }
            sendEvent =false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cdt.cancel();
        beaconManager.stopRanging(region);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
