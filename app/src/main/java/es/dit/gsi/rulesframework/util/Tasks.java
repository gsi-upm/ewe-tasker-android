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

    private static final String urlRulesApi = "http://138.4.3.211/server/tfgfinal/rulescreator.php";
    private static final String urlInputApi = "http://138.4.3.211/server/tfgfinal/inputinserter.php";

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
            params.add(new BasicNameValuePair("place", par[1]));
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