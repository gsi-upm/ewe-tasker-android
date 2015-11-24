package es.dit.gsi.rulesframework.framework;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.atooma.plugin.IIntentBasedTriggerPlugin;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.services.LocalService;
import es.dit.gsi.rulesframework.util.ParameterBundle;
import es.dit.gsi.rulesframework.util.Values;

/**
 * Created by afernandez on 24/11/15.
 */
public abstract class IntentBasedTrigger extends Binder {
    public LocalService mService;
    protected boolean bound;
    private Context context;
    private int normalIcon;
    private int titleResource;
    private int version;
    private String id;
    private String moduleId;
    private ArrayList<Values> variables = new ArrayList();
    private List<Integer> variableLabels = new ArrayList();
    private ArrayList<Values> parameters = new ArrayList();
    private List<Integer> parameterLabels = new ArrayList();
    private List<Integer> parameterNullLabels = new ArrayList();
    public ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder)(service);
            mService = binder.getService();
            IntentBasedTrigger.this.bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            IntentBasedTrigger.this.bound = false;
        }
    };

    public abstract void defineUI();

    public void declareParameters() {}

    public void declareVariables() {}

    //Constructor
    public IntentBasedTrigger(Context context, String id, int version) {
        this.id = id;
        this.version = version;
        this.context = context;
        this.defineUI();
        this.declareParameters();
        this.declareVariables();
    }

    //Lanzar trigger
    public void trigger(String ruleId, ParameterBundle parameters) {
        if(this.bound) {
            //TODO: Send input
            //this.mService.trigger(this.getModuleId(), this.getId(), ruleId, parameters);
        }

    }

    public void receive(String ruleId, ParameterBundle parameters, Bundle bundleIntent) throws RemoteException {
        this.onReceive(ruleId, parameters, bundleIntent);
    }

    public abstract void onReceive(String var1, ParameterBundle var2, Bundle var3);

    public void revoke(String ruleId) throws RemoteException {
        this.onRevoke(ruleId);
    }

    public abstract void onRevoke(String var1);

    public IBinder asBinder() {
        return this;
    }

    public void addParameter(int labelResId, int labelNullResId, String name, String type, boolean isRequired, String activity) {
        Values value = new Values(name, type, isRequired, activity);
        this.parameters.add(value);
        this.parameterLabels.add(Integer.valueOf(labelResId));
        this.parameterNullLabels.add(Integer.valueOf(labelNullResId));
    }

    public void addVariable(int labelResId, String name, String type) {
        Values value = new Values(name, type);
        this.variables.add(value);
        this.variableLabels.add(Integer.valueOf(labelResId));
    }

    public List<Values> getParameters() {
        return this.parameters;
    }

    public List getParameterLabelIfNullResources() {
        return this.parameterNullLabels;
    }

    public List getParameterTitleResources() {
        return this.parameterLabels;
    }

    public List<Values> getVariables() {
        return this.variables;
    }

    public List getVariableTitleResources() {
        return this.variableLabels;
    }

    public String getId() {
        return this.id;
    }

    public boolean isVisible() {
        return true;
    }

    public Context getContext() {
        return this.context;
    }

    public void setTitle(int titleResource) {
        this.titleResource = titleResource;
    }

    public void setIcon(int normalIcon) {
        this.normalIcon = normalIcon;
    }

    public int getIconResourceNormal() {
        return this.normalIcon;
    }

    public int getTitleResource() {
        return this.titleResource;
    }

    public int getVersion() {
        return this.version;
    }

    String getModuleId() {
        return this.moduleId;
    }

    void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
