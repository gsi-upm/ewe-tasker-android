package es.dit.gsi.rulesframework.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import es.dit.gsi.rulesframework.RuleExecutionModule;
import es.dit.gsi.rulesframework.database.RulesSQLiteHelper;
import es.dit.gsi.rulesframework.model.Rule;
import es.dit.gsi.rulesframework.util.Tasks;


/**
 * Created by afernandez on 24/11/15.
 */
public class RuleDefinitionModule extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private String canal = "";
    private String action = "";
    private String evento = "";
    private String eventoAction = "";

    private Object ifParameter = "";
    private  Object doParameter = "";

    private String input = "";
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public RuleDefinitionModule getService() {
            // Return this instance of RuleDefinitionModule so clients can call public methods
            return RuleDefinitionModule.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * method for clients
     */
    //GETTERS
    public String getCanal() {
        return canal;
    }

    public String getAction() {
        return action;
    }

    public String getEvento() {
        return evento;
    }

    public String getEventoAction() {
        return eventoAction;
    }

    //SETTERS
    public void setCanal(String ifElement) {
        Log.i("RuleDefinitionModule", "Channel: " + ifElement);
        canal = ifElement;
    }

    public void setAction(String ifAction) {
        action = ifAction;
    }

    public void setEvento(String doElement) {
        evento = doElement;
    }

    public void setEventoAction(String doAction) {
        eventoAction = doAction;
    }

    public Object getDoParameter() {
        return doParameter;
    }

    public void setDoParameter(Object doParameter) {
        this.doParameter = doParameter;
    }

    public Object getIfParameter() {
        return ifParameter;
    }

    public void setIfParameter(Object ifParameter) {
        this.ifParameter = ifParameter;
    }

    /*************SERVER FUNCTIONS****************/
    public void postRuleInServer() {
        new Tasks.PostRuleToServerTask().execute(new Rule("name",getCanal(),getAction(),getEvento(),getEventoAction(),getIfParameter(),getDoParameter()));
    }

    public void deleteRuleInServer() {
    }
    /*******************************************/


    //LOCAL
    public void saveRuleInLocal(Context context) {
        Rule mRule = new Rule("name",getCanal(),getAction(),getEvento(),getEventoAction(),getIfParameter(),getDoParameter());
        RulesSQLiteHelper db = new RulesSQLiteHelper(this);
        db.addRule(mRule);
        Log.i("LOCAL", "Rule saved");
        Log.i("RuleSavedInfo", "Name: " + mRule.getRuleName());
        Log.i("RuleSavedInfo", "ifElement: " + mRule.getIfElement());
        Log.i("RuleSavedInfo", "ifAction: " + mRule.getIfAction());
        Log.i("RuleSavedInfo", "ifParameter: " + mRule.getIfParameter());
        Log.i("RuleSavedInfo", "doElement: " + mRule.getDoElement());
        Log.i("RuleSavedInfo", "doAction: " + mRule.getDoAction());
        Log.i("RuleSavedInfo", "doParameter: " + mRule.getDoParameter());

    }

    //DEBUG
    public void testService() {
        Log.i("RuleDefinitionModule", "Testing Service...");
    }
    /*public void registerModule(IModulePlugin var1){
    }
    public void trigger(String moduleId, String tirggerid, String ruleId, ParameterBundle variables){
        //TODO:Execute trigger
        Log.i("RuleDefinitionModule","Inside trigger");
    }*/
}