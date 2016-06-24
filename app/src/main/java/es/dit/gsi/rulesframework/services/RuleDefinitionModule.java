package es.dit.gsi.rulesframework.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.database.RulesSQLiteHelper;
import es.dit.gsi.rulesframework.model.Rule;
import es.dit.gsi.rulesframework.util.CacheMethods;
import es.dit.gsi.rulesframework.util.Tasks;


/**
 * Created by afernandez on 24/11/15.
 */
public class RuleDefinitionModule extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private String ruleName= "";
    private String place= "";
    private String description = "";

    private String canalIf = "";
    private String action = "";
    private String evento = "";
    private String canalDo = "";

    private List<String> ifParameter = new ArrayList<>();
    private List<String> doParameter = new ArrayList<>();

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
    public String getIfChannel() {
        return canalIf;
    }

    public String getAction() {
        return action;
    }

    public String getEvento() {
        return evento;
    }

    public String getDoChannel() {
        return canalDo;
    }

    //SETTERS
    public void setIfChannel(String ifElement) {
        Log.i("RuleDefinitionModule", "Channel: " + ifElement);
        canalIf = ifElement;
    }

    public void setEvento(String ifAction) {
        evento = ifAction;
    }

    public void setDoChannel(String doElement) {
        canalDo = doElement;
    }

    public void setAction(String doAction) {
        action = doAction;
    }

    public List<String> getDoParameter() {
        return doParameter;
    }

    public void setDoParameter(List<String> doParameter) {
        this.doParameter = doParameter;
    }

    public List<String> getIfParameter() {
        return ifParameter;
    }

    public void setIfParameter(List<String> ifParameter) {
        this.ifParameter = ifParameter;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addDoParameter(String doParam){
        this.doParameter.add(doParam);
    }

    public void addIfParameter(String ifParam){
        this.ifParameter.add(ifParam);
    }

    /*************SERVER FUNCTIONS****************/
    public void postRuleInServer() {
        new Tasks.PostRuleToServerTask().execute(new Rule(getRuleName(), getIfChannel(),getEvento(),getDoChannel(),getAction(),getIfParameter(),getDoParameter(), getPlace(),getDescription()), CacheMethods.getInstance(getApplicationContext()).getFromPreferences("beaconRuleUser","public"));
    }

    public void deleteRuleInServer() {
    }
    /*******************************************/


    //LOCAL
    public void saveRuleInLocal(Context context) {
        Rule mRule = new Rule(getRuleName(), getIfChannel(),getEvento(),getDoChannel(), getAction(),getIfParameter(),getDoParameter(), getPlace(),getDescription());
        RulesSQLiteHelper db = new RulesSQLiteHelper(this);
        db.addRule(mRule);
        Log.i("LOCAL", "Rule saved");
    }

    public void resetService(){
        this.canalIf = "";
        this.canalDo="";
        this.evento = "";
        this.action="";
        this.ifParameter=new ArrayList<>();
        this.doParameter=new ArrayList<>();
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