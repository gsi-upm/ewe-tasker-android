package es.dit.gsi.rulesframework.model;

/**
 * Created by afernandez on 26/01/16.
 */
public class Event {
    public String title;
    public String prefix;
    public String rule;
    public String numParameters;

    public Event(String title, String numParameters, String rule, String prefix) {
        this.title = title;
        this.numParameters = numParameters;
        this.rule = rule;
        this.prefix = prefix;
    }

    public boolean hasParameters(){
        if(Integer.parseInt(numParameters)>0){
            return true;
        }else{
            return false;
        }
    }
}
