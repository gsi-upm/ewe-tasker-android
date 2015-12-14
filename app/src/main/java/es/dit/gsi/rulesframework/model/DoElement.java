package es.dit.gsi.rulesframework.model;

import java.util.List;

/**
 * Created by afernandez on 14/12/15.
 */
public class DoElement {
    private String name;
    private String type;
    private String resId;
    private String receiverName;
    private List<DoAction> actions;

    public DoElement(){

    }

    public DoElement(String name, String type, String resId) {
        this.name = name;
        this.type = type;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResourceName() {
        return resId;
    }

    public void setResourceName(String resId) {
        this.resId = resId;
    }
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public List<DoAction> getActions() {
        return actions;
    }

    public void setActions(List<DoAction> actions) {
        this.actions = actions;
    }
}
