package es.dit.gsi.rulesframework.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.SecondActivity;
import es.dit.gsi.rulesframework.model.NamedGeofence;

/**
 * Created by afernandez on 21/01/16.
 */
public class GeofenceIntentService extends IntentService {

    private final String TAG = SecondActivity.class.getName();
    private SharedPreferences prefs;
    private Gson gson;

    public  GeofenceIntentService(){
        super("GeofenceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        prefs = getApplicationContext().getSharedPreferences(
                "prefs", Context.MODE_PRIVATE);
        gson = new Gson();

// 1. Get the event
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null) {
            if (event.hasError()) {
                onError(event.getErrorCode());
            } else {

                // 2. Get the transition type
                int transition = event.getGeofenceTransition();
                if (transition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                        transition == Geofence.GEOFENCE_TRANSITION_DWELL ||
                        transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    List<String> geofenceIds = new ArrayList<>();

                    // 3. Accumulate a list of event geofences
                    for (Geofence geofence : event.getTriggeringGeofences()) {
                        geofenceIds.add(geofence.getRequestId());
                    }
                    if (transition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                            transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                        // 4. Pass the geofence list to the notification method
                        onEnteredGeofences(geofenceIds);
                    }
                }
            }

        }
    }

    public void onEnteredGeofences(List<String> geofenceIds){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "prefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // 1. Outer loop over all geofenceIds
        for (String geofenceId : geofenceIds) {
            String geofenceName = "";

            // 2, Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreferences
            Map<String, ?> keys = prefs.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String jsonString = prefs.getString(entry.getKey(), null);
                NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
                if (namedGeofence.id.equals(geofenceId)) {
                    geofenceName = namedGeofence.name;
                    break;
                }
            }

            // 1. Create a NotificationManager
            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            // 2. Create a PendingIntent for AllGeofencesActivity
            Intent intent = new Intent(this, SecondActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 3. Create and send a notification
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Geofence Entered")
                    .setContentText(geofenceName)
                    .setContentIntent(pendingNotificationIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(geofenceName))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(0, notification);

            //TODO:Send Entered to EYE server
        }
    }
    private void onError(int i) {
        Log.e(TAG, "Geofencing Error: " + i);
    }
}
