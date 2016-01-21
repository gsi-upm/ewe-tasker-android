package es.dit.gsi.rulesframework;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import es.dit.gsi.rulesframework.model.DoElement;
import es.dit.gsi.rulesframework.model.Rule;
import es.dit.gsi.rulesframework.services.EYEService;

/**
 * Created by afernandez on 27/10/15.
 */
public class EYEHandler {
    Context context;
    final static String TARGET_BASE_PATH = "/sdcard/EYEClient/";
    private static final String urlRulesApi = "http://138.4.3.211/server/tfgfinal/rulescreator.php";
    private static final String urlInputApi = "http://138.4.3.211/server/tfgfinal/inputinserter.php";


    public EYEHandler(Context context){
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

    public void sendInputToEye(String ifElement,String ifAction,String parameter){
        //TODO: Input to Server
        //TODO: Handle Response
        executeDoResponse("Notification","SHOW", "parameter");
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

    public void executeDoResponse(String doElement, String doAction,String parameter){
        //Create instance of manager name
        try {
            switch (doElement){
                //FilterManagers
                case("Notification"):
                    ToastManager tm = new ToastManager(context);
                    //Filter actions
                    switch (doAction){
                        case "SHOW":tm.showCustomToast(parameter);break;
                    }
                    break;
                default:
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        /*switch (doResponse){
            case ":Toast :status :SHOW.":
                new ToastManager(context).showCustomToast("SHOW");break;
        }*/
    }

    public static class PostRuleToServerTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object[] par) {
            // do above Server call here
            Rule mRule = (Rule) par[0];

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlRulesApi);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("title", mRule.getRuleName()));
            params.add(new BasicNameValuePair("channel", mRule.getIfElement()));
            params.add(new BasicNameValuePair("event", mRule.getIfAction()));
            params.add(new BasicNameValuePair("action", mRule.getDoElement()));
            params.add(new BasicNameValuePair("place", "TonoMovil"));
            params.add(new BasicNameValuePair("rule", mRule.getEyeRule()));//EYE rule with prefix
            params.add(new BasicNameValuePair("command", "create"));

            String response = "";
            try {
                post.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse resp = null;
                resp = client.execute(post);

                HttpEntity ent = resp.getEntity();
                response = EntityUtils.toString(ent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            //process message
            Log.i("SERVER", "Response create rule: " + response);
        }
    }

    public static class PostInputToServerTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] par) {
            // do above Server call here

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlInputApi);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("input", par[0]));
            params.add(new BasicNameValuePair("place", "TonoMovil"));
            params.add(new BasicNameValuePair("command", "insertinput"));

            String response = "";
            try {
                post.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse resp = null;
                resp = client.execute(post);

                HttpEntity ent = resp.getEntity();
                response = EntityUtils.toString(ent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;        }

        @Override
        protected void onPostExecute(String response) {
            Log.i("SERVER", "Response input: " + response);
        }
    }
}
