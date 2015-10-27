package com.example.afernandez.rulesframework;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner ifElement;
    Spinner doElement;

    Spinner ifAction;
    Spinner doAction;

    EditText ruleName;
    Button crearRegla;
    static WebView mWebView;
    ArrayAdapter<String> adapter;
    static JavaScriptInterface JSInterface;
    EditRulesFunctions eyeHandler;
    //Receivers
    BluetoothReceiver mBluetoothReceiver= new BluetoothReceiver();
    WifiReceiver mWifiReceiver = new WifiReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ifElement = (Spinner) findViewById(R.id.ifElement);
        doElement = (Spinner) findViewById(R.id.doElement);

        ifAction = (Spinner) findViewById(R.id.ifAction);
        doAction = (Spinner) findViewById(R.id.doAction);

        crearRegla = (Button) findViewById(R.id.crearRegla);
        ruleName = (EditText) findViewById(R.id.ruleName);

        ifElement.setOnItemSelectedListener(new refreshSpinnerListener());
        doElement.setOnItemSelectedListener(new refreshSpinnerListener());

        //Check first time
        SharedPreferences settings = getSharedPreferences("my_preferences", 0);
        boolean setupDone = settings.getBoolean("setup_done", false);

        if (!setupDone) {
            //Assets to files (EYE Client)
            AssetsToFileManager mAtFM = new AssetsToFileManager(this);
            try{
                mAtFM.copyFilesToSdCard();
            }catch (Exception e){
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("setup_done", true);
            editor.commit();
        }

        //Load cache rules from previous sessions
        Constants.ACTIVE_RULES_LIST = (ArrayList<Rule>) Constants.getArrayPref(getApplicationContext(),"ACTIVE_RULES_LIST");

        if(Constants.ACTIVE_RULES_LIST.size() > 0){
            fillRules(Constants.ACTIVE_RULES_LIST);
        }

        //WebView
        mWebView = (WebView) findViewById(R.id.eyeWeb);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        JSInterface = new JavaScriptInterface(getApplicationContext());
        mWebView.addJavascriptInterface(this, "JSInterface");
        mWebView.loadUrl("file:///sdcard/EYEClient/browser/demo/demo.html");

        //EYE
        eyeHandler = new EditRulesFunctions(getApplicationContext());

    }

    /**SPINNERS**/
    public void fillSpinners(){
        //IF
        if(ifElement.getSelectedItem().toString().equals("Bluetooth")){
            String array [] = getResources().getStringArray(R.array.bluetooth_actions);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, array);
            ifAction.setAdapter(adapter);
        }else{
            String array [] = getResources().getStringArray(R.array.default_empty);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, array);
            ifAction.setAdapter(adapter);
        }
        //DO
        if(doElement.getSelectedItem().toString().equals("Notificacion")){
            String array [] = getResources().getStringArray(R.array.notif_action);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, array);
            doAction.setAdapter(adapter);
        }else{
            String array [] = getResources().getStringArray(R.array.default_empty);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, array);
            doAction.setAdapter(adapter);
        }
    }

    /**RULES**/
    public void fillRules(ArrayList<Rule> rules){
        for (int i = 0; i<rules.size();i++){
            TextView rule = new TextView(this);
            String ifElementSelected = rules.get(i).getIfElement();
            String ifActionSelected = rules.get(i).getIfAction();
            String doElementSelected = rules.get(i).getDoElement();
            String doActionSelected = rules.get(i).getDoAction();

            rule.setText(rules.get(i).getRuleName() + "\n" + "IF " + ifElementSelected + " " + ifActionSelected + " -> DO " + doElementSelected + " " + doActionSelected + "\n");

            LinearLayout ruleLayout = (LinearLayout) findViewById(R.id.ruleList);
            ruleLayout.addView(rule);

            startReceiver(ifElementSelected);
        }
    }
    public void newRule(View v){
        TextView rule = new TextView(this);
        String ifElementSelected = ifElement.getSelectedItem().toString();
        String ifActionSelected = ifAction.getSelectedItem().toString();
        String doElementSelected = doElement.getSelectedItem().toString();
        String doActionSelected = doAction.getSelectedItem().toString();

        rule.setText(ruleName.getText().toString() + "\n" + "IF " + ifElementSelected + " " + ifActionSelected + " -> DO " + doElementSelected + " " + doActionSelected + "\n");

        LinearLayout ruleLayout = (LinearLayout) findViewById(R.id.ruleList);
        ruleLayout.addView(rule);

        Constants.ACTIVE_RULES_LIST.add(new Rule(ruleName.getText().toString(), ifElementSelected, ifActionSelected, doElementSelected, doActionSelected));
        if(!Constants.activeReceiversList.contains(ifElementSelected)){
            Constants.activeReceiversList.add(ifElementSelected);
        }
        //initializeReceiver for the new rule
        startReceiver(ifElementSelected);
        //Crear regla en rules.n3
        eyeHandler.addRuleToN3(ruleName.getText().toString(),ifElementSelected,ifActionSelected,doElementSelected,doActionSelected);
    }
    public void deleteRules(View v){
        Constants.ACTIVE_RULES_LIST = new ArrayList<>();
        Constants.saveArrayPref(getApplicationContext(),"ACTIVE_RULES_LIST",Constants.ACTIVE_RULES_LIST);
        //Borrar reglas en rules.n3
        recreate();
    }

    /**RECEIVERS**/
    public void startReceiver(String nameReceiver){
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        switch (nameReceiver){
            case "Bluetooth":
                Log.i("RULESFW","Bluetooth receiver registered");
                registerReceiver(mBluetoothReceiver, filter);
                Constants.activeReceiversList.add("Bluetooth");
                break;
            case "Wifi":
                Log.i("RULESFW","Wifi receiver registered");
                registerReceiver(mWifiReceiver, filter);
                Constants.activeReceiversList.add("Wifi");
                break;
        }
    }
    public void stopReceiver(String nameReceiver){
        switch (nameReceiver){
            case "Bluetooth":
                unregisterReceiver(mBluetoothReceiver);break;
            case "Wifi":
                unregisterReceiver(mWifiReceiver);break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class refreshSpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            fillSpinners();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister all active receivers list
        try{
            unregisterReceiver(mBluetoothReceiver);
            unregisterReceiver(mWifiReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }

        Constants.saveArrayPref(getApplicationContext(),"ACTIVE_RULES_LIST",Constants.ACTIVE_RULES_LIST);
    }

    private final class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i("TAG","onPageFinished");
            //Ejecuta EYE
            //view.loadUrl("javascript:(function(){document.getElementById('run').click();})()");
        }
    }
    @JavascriptInterface
    public void showResult(String result){
        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
    }
    public class JavaScriptInterface{
        Context mContext;
        String result;
        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void showResult(String result){
            //Toast.makeText(mContext,result,Toast.LENGTH_LONG).show();
            this.result = result;
        }
    }
}
