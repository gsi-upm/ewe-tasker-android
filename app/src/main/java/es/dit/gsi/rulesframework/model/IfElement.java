package es.dit.gsi.rulesframework.model;

import java.util.List;

/**
 * Created by afernandez on 1/12/15.
 */
public class IfElement {
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
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public List<IfAction> getActions() {
        return actions;
    }

    public void setActions(List<IfAction> actions) {
        this.actions = actions;
    }

    public IfElement() {

    }

    private String name;
    private String type;
    private String resourceName;
    private List<IfAction> actions;
    private String ReceiverName;

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }
}
