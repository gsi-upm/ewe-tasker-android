package es.dit.gsi.rulesframework.util;

import android.os.AsyncTask;
import android.util.Log;

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

    //private static final String urlRulesApi = "http://138.4.3.211/taskautomationweb/mobileConnectionHelper.php";
    //private static final String urlInputApi = "http://138.4.3.211/server/tfgfinal/inputinserter.php";
    //private static final String urlGetChannelApi = "http://138.4.3.211/taskautomationweb/mobileConnectionHelper.php";

    private static final String urlRulesApi = "http://taskautomationserver.ddns.net/taskautomationweb/mobileConnectionHelper.php";
    private static final String urlInputApi = "http://taskautomationserver.ddns.net/taskautomationweb/controller/eventsManager.php";
    private static final String urlGetChannelApi = "http://taskautomationserver.ddns.net/taskautomationweb/mobileConnectionHelper.php";

    public static class PostRuleToServerTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object[] par) {
            // do above Server call here
            Rule mRule = (Rule) par[0];

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
            params.add(new BasicNameValuePair("rule_creator", "afll"));
            params.add(new BasicNameValuePair("rule", mRule.getEyeRule()));//EYE rule with prefix
            params.add(new BasicNameValuePair("command", "createRule"));


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
}
