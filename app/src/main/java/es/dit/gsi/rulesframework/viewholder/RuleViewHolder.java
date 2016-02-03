package es.dit.gsi.rulesframework.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 27/11/15.
 */
public class RuleViewHolder extends RecyclerView.ViewHolder {

    public ImageView ifChannel,doChannel;
    public TextView ifElement,doElement,descriptionRule;

    public RuleViewHolder(View itemView) {
        super(itemView);
        ifChannel = (ImageView) itemView.findViewById(R.id.ifChannel);
        doChannel = (ImageView) itemView.findViewById(R.id.doChannel);
        ifElement = (TextView) itemView.findViewById(R.id.ifElement);
        doElement = (TextView) itemView.findViewById(R.id.doElement);
        descriptionRule = (TextView) itemView.findViewById(R.id.descriptionRule);
    }
}
