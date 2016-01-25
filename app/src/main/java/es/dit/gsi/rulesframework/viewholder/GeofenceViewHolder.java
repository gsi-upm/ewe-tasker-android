package es.dit.gsi.rulesframework.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 25/01/16.
 */
public class GeofenceViewHolder extends RecyclerView.ViewHolder{

    public ImageView logoGeofence;
    public TextView title,latitude,longitude,radius;

    public GeofenceViewHolder(View itemView) {
        super(itemView);
        logoGeofence = (ImageView) itemView.findViewById(R.id.logoGeofence);
        title = (TextView) itemView.findViewById(R.id.title);
        latitude = (TextView) itemView.findViewById(R.id.latitude);
        longitude = (TextView) itemView.findViewById(R.id.longitude);
        radius = (TextView) itemView.findViewById(R.id.radius);
    }
}
