package es.dit.gsi.rulesframework.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.dit.gsi.rulesframework.NewRuleActivity;

/**
 * Created by afernandez on 23/10/15.
 */
public class Rule{

    private int id;
    private String ruleName;
    private String place;
    private String description;

    private String ifElement;
    private String ifAction;

    private String doElement;
    private String doAction;

    private List<String> ifParameter = new ArrayList<>();
    private List<String> doParameter = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIfElement() {
        return ifElement;
    }

    public void setIfElement(String ifElement) {
        this.ifElement = ifElement;
    }

    public String getIfAction() {
        return ifAction;
    }

    public void setIfAction(String ifAction) {
        this.ifAction = ifAction;
    }

    public String getDoElement() {
        return doElement;
    }

    public void setDoElement(String doElement) {
        this.doElement = doElement;
    }

    public String getDoAction() {
        return doAction;
    }

    public void setDoAction(String doAction) {
        this.doAction = doAction;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public List<String> getIfParameter() {
        return ifParameter;
    }

    public void setIfParameter(List<String> ifParameter) {
        this.ifParameter = ifParameter;
    }

    public List<String> getDoParameter() {
        return doParameter;
    }

    public void setDoParameter(List<String> doParameter) {
        this.doParameter = doParameter;
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

    public Rule(String ruleName, String ifElement, String ifAction, String doElement, String doAction,List<String> ifParameter, List<String> doParameter,String place, String description) {
        this.ruleName = ruleName;

        this.ifElement = ifElement;
        this.ifAction = ifAction;
        this.doAction = doAction;
        this.doElement = doElement;
        this.ifParameter = ifParameter;
        this.doParameter = doParameter;
        this.ruleName = ruleName;
        this.place = place;
        this.description = description;
    }
    public Rule (){}
    public String getFullRule(){
        return "IF " + ifElement + " " + ifAction + " -> DO " + doElement + " " + doAction + "\n";
    }
    public String getEyeRule(){
        String prefixChannelOne = "", prefixChannelTwo = "", eventRule = "", actionRule = "";
        List<Channel> channelList = NewRuleActivity.channelList;
        boolean eventHasParameters = false;
        boolean actionHasParameters = false;
        //PREFIX
        for (int i = 0; i<channelList.size();i++) {
            String titleChannel = channelList.get(i).title;
            if (titleChannel.equals(ifElement)) {
                for (Event e : channelList.get(i).events) {
                    if (e.title.equals(ifAction)) {
                        prefixChannelOne = e.prefix;
                        eventRule = e.rule;
                        eventHasParameters = e.hasParameters();
                    }
                }
            }
            if(titleChannel.equals(doElement)){
                for (Action a : channelList.get(i).actions) {
                    if(a.title.equals(doAction)){
                        prefixChannelTwo = a.prefix;
                        Log.i("EYERule",a.rule);
                        actionRule = a.rule;
                        actionHasParameters = a.hasParameters();
                    }
                }
            }
        }



        String prefix = prefixChannelOne + " "  +prefixChannelTwo;

        //Parameters
        if(eventHasParameters){
            eventRule = changeParameterOnRule(eventRule,"event");
        }
        if(actionHasParameters){
            actionRule = changeParameterOnRule(actionRule, "action");
        }

        String ifStatement = "{" + eventRule + "}=> ";
        String doStatement = "{" + actionRule + "}.";
        Log.i("EYERule",prefix+ifStatement+doStatement);
        return prefix+ifStatement+doStatement;
    }

    public String changeParameterOnRule(String rule,String type){
        String ruleReplaced = "";
        //1. Get de nParams of the event
        int nParamsEvent= 0;
        for(Channel ch: NewRuleActivity.channelList){
            if(ch.title.equals(ifElement)){
                for(Event e: ch.events){
                    if(e.title.equals(ifAction)){
                        nParamsEvent = Integer.parseInt(e.numParameters);
                    }
                }
            }
        }

        int nParamsAction= 0;
        for(Channel ch: NewRuleActivity.channelList){
            if(ch.title.equals(doElement)){
                for(Action a: ch.actions){
                    if(a.title.equals(doAction)){
                        nParamsAction = Integer.parseInt(a.numParameters);
                    }
                }
            }
        }

        if(type.equals("event")){
            for(int i = 0; i<nParamsEvent;i++){
                ruleReplaced = rule.replace("#PARAM_"+ String.valueOf(i+1) + "#", ifParameter.get(i));
            }
        }
        if(type.equals("action")){
            for(int i = 0; i<nParamsAction;i++){
                ruleReplaced = rule.replace("#PARAM_"+ String.valueOf(i+1) + "#", doParameter.get(i));
            }
        }
        Log.i("RuleReplaced",ruleReplaced);
        return ruleReplaced;
    }
}
