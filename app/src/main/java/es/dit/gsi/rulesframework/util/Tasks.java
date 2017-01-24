package es.dit.gsi.rulesframework.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
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
import es.dit.gsi.rulesframework.model.Rule;

/**
 * Created by afernandez on 25/01/16.
 */
public class Tasks {

    public static String ipServer = "http://javtfg.barcolabs.com";
    //public static String ipServer = "http://ewetasker.cluster.gsi.dit.upm.es";
    //public static final String defaultGsiUrl = "http://ewetasker.cluster.gsi.dit.upm.es";
    public static final String defaultGsiUrl = "http://javtfg.barcolabs.com";
    private static final String urlRulesApi =ipServer +  "/mobileConnectionHelper.php";
    private static final String urlInputApi =ipServer +  "/controller/eventsManager.php";
    private static final String urlGetChannelApi =ipServer +  "/mobileConnectionHelper.php";
    private static final String urlBifrost = "http://bifrost.gsi.dit.upm.es/index.php";
    public static final String urlImages = ipServer + "/img/";
    public static final String urlCMS = "http://javtfg.barcolabs.com/cms/api.php";

    public static class PostRuleToServerTask extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Object[] par) {
            // do above Server call here
            Rule mRule = (Rule) par[0];
            String user = (String) par[1];

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlRulesApi);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            //Parameters
            params.add(new BasicNameValuePair("rule_title", mRule.getRuleName()));
            params.add(new BasicNameValuePair("rule_description", mRule.getDescription()));
            params.add(new BasicNameValuePair("rule_channel_one", mRule.getIfElement()));
            params.add(new BasicNameValuePair("rule_channel_two", mRule.getDoElement()));
            params.add(new BasicNameValuePair("rule_event_title", mRule.getIfAction()));
            params.add(new BasicNameValuePair("rule_action_title",mRule.getDoAction()));
            params.add(new BasicNameValuePair("rule_place", mRule.getPlace()));
            params.add(new BasicNameValuePair("rule_creator", user));
            params.add(new BasicNameValuePair("rule", mRule.getEyeRule()));//EYE rule with prefix
            params.add(new BasicNameValuePair("command", "createRule"));

            Log.i("RULE","My ruleee"+ mRule.getEyeRule());
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

            params.add(new BasicNameValuePair("inputEvent", par[0]));
            params.add(new BasicNameValuePair("user", par[1]));
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
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.i("SERVER", "Response input: " + response);

        }
    }

    public static class GetChannelsFromServerTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... par) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlGetChannelApi);
            Log.i("GET",urlGetChannelApi);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("command", "getChannels"));

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
    }

    public static class LoginGSIServerTask extends AsyncTask<String , Void, String> {
        Context context;
        String pass,remember = "";
        public LoginGSIServerTask(Context context){
            this.context=context;
        }

        @Override
        protected String doInBackground(String... par) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlBifrost);

            //String user = par[0];
             pass = par[0];
             remember = par[1];

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            //Log.i("Task",user);

            //params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("pass", pass));

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
            super.onPostExecute(response);
            if (response.equals("1")){
                if(remember.equals("true")) {
                    //Save in local
                    CacheMethods cacheMethods = CacheMethods.getInstance(context);
                    cacheMethods.saveInPreferences("doorKey",pass);
                }
                Toast.makeText(context,"Door opened. Press back to stop listening.",Toast.LENGTH_LONG).show();
            }
            Log.i("Task", "LogIn reponse: " + response);
        }
    }

    public static class GetUrlFromCMS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... par) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlCMS+"?location="+par[0]);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            //params.add(new BasicNameValuePair("location", par[0]));

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
    }
}
