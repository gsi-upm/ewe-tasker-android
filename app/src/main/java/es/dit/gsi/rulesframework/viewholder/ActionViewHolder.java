package es.dit.gsi.rulesframework.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 9/12/15.
 */
public class ActionViewHolder extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView name;
    public LinearLayout layoutClickable;
    public ActionViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.icon);
        name = (TextView) itemView.findViewById(R.id.nameAction);
        layoutClickable = (LinearLayout) itemView.findViewById(R.id.layoutClickable);
        //descriptionRule = (TextView) itemView.findViewById(R.id.descriptionRule);
    }
}
