package es.dit.gsi.rulesframework;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import es.dit.gsi.rulesframework.adapters.MyRecyclerViewAdapter;
import es.dit.gsi.rulesframework.database.RulesSQLiteHelper;
import es.dit.gsi.rulesframework.model.Action;
import es.dit.gsi.rulesframework.model.Channel;
import es.dit.gsi.rulesframework.model.Event;
import es.dit.gsi.rulesframework.model.NamedGeofence;
import es.dit.gsi.rulesframework.model.Rule;
import es.dit.gsi.rulesframework.receivers.GeofenceIntentService;
import es.dit.gsi.rulesframework.util.Tasks;

public class SecondActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    protected static final String TAG = "SecondActivity";


    List<Object> items = new ArrayList<>();
    List<Rule> rules = new ArrayList<>();
    android.support.design.widget.FloatingActionButton floatingActionButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView emptyView;

    //SQL
    RulesSQLiteHelper db;

    //Geofence
    private List<NamedGeofence> namedGeofences;
    protected ArrayList<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    private Gson gson;
    private SharedPreferences prefs;
    private PendingIntent mGeofencePendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);

        //ActionBar
        getSupportActionBar().setTitle("List of Rules");

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.listRules);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new OnFloatingButtonClickListener());

        db = new RulesSQLiteHelper(this);

        //Empty message
        emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "Titillium-Regular.otf"));

        addItems();

        namedGeofences = new ArrayList<>();
        mGeofenceList = new ArrayList<>();
        gson = new Gson();
        prefs = this.getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        mGeofencePendingIntent = null;

        populateGeofenceList();

        buildGoogleApiClient(); //Callback addGeofences()

        try{
            getChannels();
        }catch (Exception e){
            e.printStackTrace();
        }

        //DEBUG
        //new RuleExecutionModule(getApplicationContext()).sendInputToEye("input","tono");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllRules:
                db.deleteAllRules();
                addItems();
                return true;
            case R.id.deleteAllGeofences:
                removeGeofences();
            case R.id.listGeofences:
                Intent i = new Intent(this,ListGeofencesActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addItems() {
        Log.i("Rules Items", "Adding items");
        items.clear();
        rules = db.getAllRules();
        for (Rule r : rules) {
            items.add(r);
            Log.i("Rules Items", r.getRuleName());
        }
        mAdapter = new MyRecyclerViewAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);

        if(items.size()==0){
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }




    public class OnFloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), NewRuleActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    //Método que lee un fichero json almacenado en assets
    public String loadJSONFromAsset(String nameFile) {
        String json = null;
        try {

            InputStream is = getAssets().open(nameFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    //Read JSON
    /*public void JSONParse(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);

            //IF
            JSONObject ifElements = jsonObject.getJSONObject("ifElements");

            Iterator iterIdIfElement = ifElements.keys();
            while (iterIdIfElement.hasNext()) {
                String idElement = String.valueOf(iterIdIfElement.next());
                JSONObject element = ifElements.getJSONObject(idElement);

                IfElement ifElement = new IfElement();
                ifElement.setName(element.getString("Name"));
                ifElement.setType(element.getString("Type"));
                ifElement.setResourceName(element.getString("ResourceId"));
                ifElement.setReceiverName(element.getString("ReceiverName"));
                //Actions
                JSONObject ifActions = element.getJSONObject("Actions");
                List<IfAction> actions = new ArrayList<IfAction>();
                Iterator iterIDIfAction = ifActions.keys();
                while (iterIDIfAction.hasNext()) {
                    IfAction action = new IfAction();
                    String id = (String) iterIDIfAction.next();
                    action.setName(ifActions.getJSONObject(id).getString("name"));
                    action.setParamType(ifActions.getJSONObject(id).getString("type"));
                    actions.add(action);
                    Log.i("JSONPARSER", action.getName());
                    Log.i("JSONPARSER", action.getParamType());
                }
                ifElement.setActions(actions);
                ifElementsList.add(ifElement);
            }

            //DO
            JSONObject doElements = jsonObject.getJSONObject("doElements");
            Iterator iterIdDoElement = ifElements.keys();
            while (iterIdDoElement.hasNext()) {
                String idElement = String.valueOf(iterIdDoElement.next());
                JSONObject element = doElements.getJSONObject(idElement);

                DoElement doElement = new DoElement();
                doElement.setName(element.getString("Name"));
                doElement.setType(element.getString("Type"));
                doElement.setResourceName(element.getString("ResourceId"));
                doElement.setReceiverName(element.getString("ReceiverName"));
                //Actions
                JSONObject doActions = element.getJSONObject("Actions");
                List<DoAction> actions = new ArrayList<DoAction>();
                Iterator iterIDDoAction = doActions.keys();
                while (iterIDDoAction.hasNext()) {
                    DoAction action = new DoAction();
                    String id = (String) iterIDDoAction.next();
                    action.setName(doActions.getJSONObject(id).getString("name"));
                    action.setParamType(doActions.getJSONObject(id).getString("type"));
                    actions.add(action);
                    Log.i("JSONPARSER", action.getName());
                    Log.i("JSONPARSER", action.getParamType());
                }
                doElement.setActions(actions);
                doElementsList.add(doElement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    @Override
    protected void onResume() {
        super.onResume();
        addItems();
    }

    //Geofences
    public void populateGeofenceList() {
        Map<String, ?> keys = prefs.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String jsonString = prefs.getString(entry.getKey(), null);
            NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
            namedGeofences.add(namedGeofence);
            Log.i(TAG, "Geofence loaded");

        }

        // Sort namedGeofences by name
        Collections.sort(namedGeofences);

        for (NamedGeofence ng : namedGeofences) {

            Log.i("GEOFENCE Name", String.valueOf(ng.name));
            Log.i("GEOFENCE Lat", String.valueOf(ng.latitude));
            Log.i("GEOFENCE Long", String.valueOf(ng.longitude));
            Log.i("GEOFENCE Rad", String.valueOf(ng.radius));

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(ng.name)

                            // Set the circular region of this geofence.
                    .setCircularRegion(
                            ng.latitude,
                            ng.longitude,
                            ng.radius
                    )

                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                            // Create the geofence.
                    .build());
        }
    }
    public void addGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            securityException.printStackTrace();
        }
    }

    public void removeGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            //Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            securityException.printStackTrace();
        }
    }
    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if(namedGeofences.size()>0){
            addGeofences();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection to GoogleApiClient Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection to GoogleApiClient Failed");
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            Log.i(TAG,"Geofences added");
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = "Error onResult";
            Log.e(TAG, errorMessage);
        }
    }

    //Channels

    public void getChannels() throws JSONException {
        //Get Task
        //String response = "[{\"title\":\"door\",\"description\":\"This channel represents a door.\",\"events\":[{\"title\":\"Door opened event\",\"rule\":\"Rule for door opened\",\"prefix\":\"\",\"numParameters\":\"0\"}],\"actions\":[{\"title\":\"Open door action\",\"rule\":\"Rule for opening the door\",\"prefix\":\"\",\"numParameters\":\"0\"}]},{\"title\":\"tv\",\"description\":\"This channel represents a TV.\",\"events\":[{\"title\":\"If a knows b\",\"rule\":\"?a :knows ?b\",\"prefix\":\"\",\"numParameters\":\"0\"}],\"actions\":[{\"title\":\"then b knows a\",\"rule\":\"?b :knows ?a\",\"prefix\":\"\",\"numParameters\":\"0\"}]}]";
        String response = "";
        try {
            response = new Tasks.GetChannelsFromServerTask().execute().get();
            Log.i("NewRuleActivity", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Constants.savePreferences(getApplicationContext(), "channelsJson", response);
    }

    public static List<Channel> translateJSONtoList(String json) throws JSONException {
        List<Channel> list = new ArrayList<>();

        JSONArray channels = new JSONArray(json);
        for (int i = 0; i < channels.length(); i++) {
            JSONObject currentChannel = (JSONObject) channels.get(i);
            String title = currentChannel.getString("title");
            String description = currentChannel.getString("description");
            JSONArray events = currentChannel.getJSONArray("events");
            List<Event> eventsList = new ArrayList<>();
            for (int j = 0; j < events.length(); j++) {
                JSONObject currentEvent = (JSONObject) events.get(j);
                String eventTitle = currentEvent.getString("title");
                String eventPrefix = currentEvent.getString("prefix");
                String eventRule = currentEvent.getString("rule");
                String eventNumParameters = currentEvent.getString("num_of_params");
                Event e = new Event(eventTitle, eventNumParameters, eventRule, eventPrefix);
                eventsList.add(e);
            }
            JSONArray actions = currentChannel.getJSONArray("actions");
            List<Action> actionsList = new ArrayList<>();
            for (int n = 0; n < actions.length(); n++) {
                JSONObject currentAction = (JSONObject) actions.get(n);
                String actionTitle = currentAction.getString("title");
                String actionPrefix = currentAction.getString("prefix");
                String actionRule = currentAction.getString("rule");
                String actionNumParameters = currentAction.getString("num_of_params");
                Action ac = new Action(actionTitle, actionRule, actionPrefix, actionNumParameters);
                actionsList.add(ac);
            }
            Channel ch = new Channel(title, description, actionsList, eventsList);
            list.add(ch);
        }

        return list;
    }

}