package es.dit.gsi.rulesframework.interfaces;

import android.content.Context;

import com.atooma.plugin.IModulePlugin;
import com.atooma.plugin.ITriggerPlugin;

/**
 * Created by afernandez on 28/10/15.
 */
public class ITriggerModel {

    private String nameTrigger;
    Context c;

    ITriggerModel(Context c, String name){
        this.nameTrigger = name;
        this.c = c;
    }

}
