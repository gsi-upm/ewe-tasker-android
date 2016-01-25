package es.dit.gsi.rulesframework;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import es.dit.gsi.rulesframework.adapters.MyRecyclerViewAdapter;
import es.dit.gsi.rulesframework.model.NamedGeofence;

/**
 * Created by afernandez on 16/11/15.
 */

    public class ListGeofencesActivity extends AppCompatActivity {

        List<Object> items = new ArrayList<>();

        List<NamedGeofence> namedGeofences = new ArrayList<>();
        private Gson gson;
        private SharedPreferences prefs;

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.listgeofences);

            gson = new Gson();
            prefs = this.getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

            Map<String, ?> keys = prefs.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String jsonString = prefs.getString(entry.getKey(), null);
                NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
                namedGeofences.add(namedGeofence);
                Log.i("List Geofences", "Geofence loaded");
            }

            // Sort namedGeofences by name
            Collections.sort(namedGeofences);

            //RecyclerView
            mRecyclerView = (RecyclerView) findViewById(R.id.listGeofences);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            addItems();

        }

        public void addItems() {
            Log.i("Rules Items", "Adding items");
            items.clear();
            for (NamedGeofence ng : namedGeofences) {
                items.add(ng);
            }
            mAdapter = new MyRecyclerViewAdapter(this, items);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

