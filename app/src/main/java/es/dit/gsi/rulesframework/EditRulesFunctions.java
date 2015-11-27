package es.dit.gsi.rulesframework;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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


    public EditRulesFunctions(Context context){
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
    public String createInputString(String ifElement,String ifAction){
        String res = ":"+ ifElement + " :status :" + ifAction + ".\n";
        return res;
    }
    //Delete all doc and rewrite it
    public void sendInputToEye(String ifElement,String ifAction){
        String response = "";
        //Config input request
        File myFile = new File(TARGET_BASE_PATH + "browser/demo/input.n3");

        String inputString = "@prefix : <ppl#>.\n" + createInputString(ifElement,ifAction);

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
        //Save last input send
        Constants.lastInputSent = createInputString(ifElement,ifAction);
        MainActivity.hasToExecute = true;

        MainActivity.mWebView.reload();
    }
    public void pageFinishReloading(boolean execute){
        if(execute){
            executeEye();
            MainActivity.hasToExecute=false;
        }
    }
    public void executeEye(){
        //MainActivity.mWebView.reload();
        //Click Execute EYE button
        Log.i("EXECUTE EYE", "EXECUTING EYE...");
        MainActivity.mWebView.loadUrl("javascript:(function(){document.getElementById('run').click();})()");
    }

    //Handle EYE result
    public static String getDoFromResult(String result){
        System.out.println("Inicial: " + result);
        //Delete prefix
        result = result.replace("PREFIX : ", "");
        //Delete <?>
        result = result.replaceAll("\\<.*?\\>", "");

        System.out.println("lastInput sent : " + Constants.lastInputSent);
        result = result.replace(Constants.lastInputSent, "");
        System.out.println("Sin input" + result);
        result = result. replaceAll("\n", "");
        System.out.println("Final: " + result);

        return result;
    }

    public void executeDoResponse(String doResponse){
        switch (doResponse){
            case ":Toast :status :SHOW.":
                new ToastManager(context).showCustomToast("SHOW");break;
        }
    }
}
