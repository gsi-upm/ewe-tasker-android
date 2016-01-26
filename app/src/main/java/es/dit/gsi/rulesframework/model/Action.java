package es.dit.gsi.rulesframework.model;

/**
 * Created by afernandez on 26/01/16.
 */
public class Action {
    public String title;
    public String prefix;
    public String rule;
    public String numParameters;

    public Action(String title, String rule, String prefix, String numParameters) {
        this.title = title;
        this.rule = rule;
        this.prefix = prefix;
        this.numParameters = numParameters;
    }

    public boolean hasParameters(){
        if(Integer.parseInt(numParameters)>0){
            return true;
        }else{
            return false;
        }
    }
}
