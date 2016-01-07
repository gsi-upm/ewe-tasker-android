package es.dit.gsi.rulesframework.model;

/**
 * Created by afernandez on 23/10/15.
 */
public class Rule{
    public static String prefix = "@prefix ppl: <http://example.org/people#>. " +
            "@prefix foaf: <http://xmlns.com/foaf/0.1/>. ";

    private int id;
    private String ruleName;
    private String ifElement;
    private String ifAction;
    private String doElement;
    private String doAction;

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

    public Rule(String ruleName, String ifElement, String ifAction, String doElement, String doAction) {
        this.ruleName = ruleName;

        this.ifElement = ifElement;
        this.ifAction = ifAction;
        this.doAction = doAction;
        this.doElement = doElement;
    }
    public Rule (){}
    public String getFullRule(){
        return "IF " + ifElement + " " + ifAction + " -> DO " + doElement + " " + doAction + "\n";
    }
    public String getEyeRule(){

        String ifStatement = "{ppl:" + ifElement + " foaf:knows " + "ppl:" + ifAction + "}=> ";
        String doStatement = "{ppl:" + doElement + " foaf:knows " + "ppl:" + doAction + "}.";

        return prefix+ifStatement+doStatement;
    }

    public static String getInput(String ifElement, String ifAction){
        String input = "ppl:" + ifElement + " foaf:knows " + "ppl:" + ifAction + ".";
        return prefix + input;
    }
}
