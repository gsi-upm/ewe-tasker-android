package es.dit.gsi.rulesframework.model;

import android.support.annotation.NonNull;

import com.google.android.gms.location.Geofence;

import java.util.UUID;

/**
 * Created by afernandez on 21/01/16.
 */
public class NamedGeofence implements Comparable{
    public String id;
    public String name;
    public double latitude,longitude;
    public float radius;

    public NamedGeofence(String name, double latitude,double longitude,float radius){
        this.name = name;
        this.latitude = latitude;
        this.longitude=longitude;
        this.radius=radius;
    }
    public Geofence geofence() {
        id = UUID.randomUUID().toString();
        return new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    @Override
    public int compareTo(@NonNull Object another) {
        NamedGeofence other = (NamedGeofence) another;
        return name.compareTo(other.name);
    }
}
