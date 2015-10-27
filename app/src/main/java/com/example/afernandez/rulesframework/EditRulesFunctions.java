package com.example.afernandez.rulesframework;

import android.app.Notification;
import android.content.Context;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

/**
 * Created by afernandez on 27/10/15.
 */
public class EditRulesFunctions {
    Context context;
    final static String TARGET_BASE_PATH = "/sdcard/EYEClient/";


    EditRulesFunctions(Context context){
        this.context=context;
    }

    public void addRuleToN3(String ruleName,String ifElement, String ifAction, String doElement, String doAction){
        File myFile = new File(TARGET_BASE_PATH + "browser/demo/rules.n3");

        String ruleString = "\n#" +ruleName + "\n"
                            +"{ :"+ ifElement + " :status :" + ifAction + " }\n"
                            + "=>\n"
                            + "{ :"+ doElement + " :status :" + doAction + " }.\n";

        if(myFile.exists())
        {
            try
            {
                FileWriter fileWritter = new FileWriter(TARGET_BASE_PATH + "browser/demo/rules.n3",true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.append(ruleString);
                bufferWritter.close();
            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //Delete all doc and rewrite it
    public String sendInputToEye(String ifElement,String ifAction){
        String response = "";
        //Config input request
        File myFile = new File(TARGET_BASE_PATH + "browser/demo/input.n3");

        String inputString ="@prefix : <ppl#>.\n"
                            +":"+ ifElement + " :status :" + ifAction + ".\n";

        if(myFile.exists())
        {
            try
            {
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.write(inputString);
                myOutWriter.close();
                fOut.close();
            } catch(Exception e)
            {

            }
        }
        response = executeEye();
        //Handle input response
        return response;
    }
    public String executeEye(){
        //Click Execute EYE button
        MainActivity.mWebView.loadUrl("javascript:(function(){document.getElementById('run').click();})()");

        return MainActivity.JSInterface.result;
    }
}
