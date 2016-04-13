package es.dit.gsi.rulesframework;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;

import es.dit.gsi.rulesframework.util.TitilliumTextView;

/**
 * Created by afernandez on 8/04/16.
 */
public class InfoGSIActivity extends ActionBarActivity{
    String eweUrl = "http://www.gsi.dit.upm.es/es/investigacion/publicaciones?view=publication&task=show&id=388";
    String eyeUrl = "http://eulersharp.sourceforge.net/2006/02swap/eye-note";
    String eweTaskerUrl = "http://www.gsi.dit.upm.es/es/investigacion/publicaciones?view=publication&task=show&id=396";

    TitilliumTextView eweReference,eyeReference, eweTaskerReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infogsi);

        eweReference = (TitilliumTextView) findViewById(R.id.eweReference);
        eweReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(eweUrl));
                startActivity(i);
            }
        });

        eyeReference = (TitilliumTextView) findViewById(R.id.eyeReference);
        eyeReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(eyeUrl));
                startActivity(i);
            }
        });

        eweTaskerReference = (TitilliumTextView) findViewById(R.id.eweTaskerReference);
        eweTaskerReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(eweTaskerUrl));
                startActivity(i);
            }
        });

    }
}
