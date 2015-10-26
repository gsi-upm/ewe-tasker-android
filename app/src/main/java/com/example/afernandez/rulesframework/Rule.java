package com.example.afernandez.rulesframework;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afernandez on 23/10/15.
 */
public class Rule{
    private String ruleName;
    private String ifElement;
    private String ifAction;
    private String doElement;
    private String doAction;

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
}
