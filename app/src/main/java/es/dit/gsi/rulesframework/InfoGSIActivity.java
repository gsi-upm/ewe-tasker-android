package es.dit.gsi.rulesframework;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;

import es.dit.gsi.rulesframework.util.TitilliumTextView;

/**
 * Created by afernandez on 8/04/16.
 */
public class InfoGSIActivity extends ActionBarActivity{

    TitilliumTextView eweReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infogsi);

        eweReference = (TitilliumTextView) findViewById(R.id.eweReference);
        eweReference.setMovementMethod(LinkMovementMethod.getInstance());
        eweReference.setText(Html.fromHtml("<a href=\"" + "http://www.gsi.dit.upm.es/es/investigacion/publicaciones?view=publication&task=show&id=388\"" + ">More info</a>"));
    }
}
