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

    public ImageView logoRule;
    public TextView ifElement,descriptionRule;

    public RuleViewHolder(View itemView) {
        super(itemView);
        logoRule = (ImageView) itemView.findViewById(R.id.logoRule);
        ifElement = (TextView) itemView.findViewById(R.id.ifElement);
        descriptionRule = (TextView) itemView.findViewById(R.id.descriptionRule);
    }
}
