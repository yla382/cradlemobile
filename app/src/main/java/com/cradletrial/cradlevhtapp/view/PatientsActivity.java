package com.cradletrial.cradlevhtapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.dagger.MyApp;
import com.cradletrial.cradlevhtapp.model.GestationalAgeUnit;
import com.cradletrial.cradlevhtapp.model.Patient;
import com.cradletrial.cradlevhtapp.model.Reading;
import com.cradletrial.cradlevhtapp.model.ReadingManager;
import com.cradletrial.cradlevhtapp.model.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class PatientsActivity extends TabActivityBase {

    // Data Model
    @Inject
    ReadingManager readingManager;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Settings settings;


    public static Intent makeIntent(Context context) {
        return new Intent(context, PatientsActivity.class);
    }

    // set who we are for tab code
    public PatientsActivity() {
        super(R.id.nav_patients);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inject:
        ((MyApp) getApplication()).getAppComponent().inject(this);

        // setup UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // bottom bar nav in base class
        setupBottomBarNavigation();

        // list view with patients
        getPatientsFromServer();

        // buttons
        setupAddSampleDataButton();
        setupClearDBButton();
        setupPretendToUnUploadToServer();
        setupSettingsAddFake();
        setupSettingsClear();
    }

    private void getPatientsFromServer() {
        RequestQueue mRequestQueue;
        String url = "http://192.168.19.1:8080/android/patients";
        String TAG = PatientsActivity.class.getName();

        mRequestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response);
                ListView listView = findViewById(R.id.fts);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("Response: ", jsonArray.toString());

                    setupListView(listView, jsonArray);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = listView.getItemAtPosition(position).toString();
                            String[] string_array = item.split("\\s+");
                            Integer patientId = Integer.parseInt(string_array[2]);
                        }
                    });
                }
                catch (Throwable tx) {
                    Log.e("PatientsActivity: ", "Could not parse malformed JSON: \"" + response + "\"");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
            }
        });

        // Fires the request
        mRequestQueue.add(stringRequest);


    }

    private void setupListView(ListView listView, JSONArray jsonArray) throws java.io.IOException, JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            ObjectMapper objectMapper = new ObjectMapper();
            Patient patient = objectMapper.readValue(jsonArray.get(i).toString(), Patient.class);
            Log.d("For loop: ", patient.toString());

            stringList.add(patient.getFirstName() + " " + patient.getLastName() + " " +
                    patient.getAgeYears());
        }

        final ArrayAdapter adapter = new ArrayAdapter<>(PatientsActivity.this, android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(adapter);
    }

    private void setupAddSampleDataButton() {
        Button btn = findViewById(R.id.btnAddSampleData);
        btn.setOnClickListener(view -> {
            Toast.makeText(this, "Adding sample data...", Toast.LENGTH_LONG).show();

            List<Reading> tmpList = new ArrayList<>();
            long timeDelta = 0;
            for (int i = 0; i < 30; i++ ) {
                int makeNeg = (i % 2 == 0) ? 1 : -1;
                Reading r = new Reading();
                r.patientFirstName = "P" + (char)('A' + i);
                r.patientLastName = "P" + (char)('A' + i);
                r.patientId = String.valueOf(48300027400L + i + ((i * new Random().nextLong() % 10000000L)* 1000));
                r.ageYears = 20 + i;
                r.dateTimeTaken = ZonedDateTime.now().minus(timeDelta, ChronoUnit.MINUTES);
                r.bpSystolic = 120 + (i * 15) * makeNeg;
                r.bpDiastolic = 80 + (i * 5) * makeNeg;
                r.heartRateBPM = 100 + (i * 10) * makeNeg;
                r.gpsLocationOfReading = "1.3733° N, 32.2903° E";
                r.setFlaggedForFollowup( i % 3 == 1);
                if (i % 2 == 1) {
                    r.setReferredToHealthCentre(
                            "Bibi Bibi health centre #5",
                            r.dateTimeTaken.plus(5, ChronoUnit.MINUTES)
                    );
                }

                if (i >= 10) {
                    r.dateUploadedToServer = r.dateTimeTaken.plusHours(3);
                }

                if (i < 20) {
                    r.dateRecheckVitalsNeeded = ZonedDateTime.now().plusSeconds( (i-1) * 60);
                }

                if (i % 3 == 0) {
                    r.gestationalAgeUnit = GestationalAgeUnit.GESTATIONAL_AGE_UNITS_WEEKS;
                    r.gestationalAgeValue = "" + (i % 45);
                }
                if (i % 3 == 1) {
                    r.gestationalAgeUnit = GestationalAgeUnit.GESTATIONAL_AGE_UNITS_MONTHS;
                    r.gestationalAgeValue = "" + (i % 11);
                }
                if (i % 3 == 2) {
                    r.gestationalAgeUnit = GestationalAgeUnit.GESTATIONAL_AGE_UNITS_NONE;
                }


                timeDelta = (timeDelta) * 2 + 1;
                if (timeDelta > 1_000_000_000) {
                    timeDelta = 7;
                }

                tmpList.add(r);
            }
            for (int i = tmpList.size() - 1; i >= 0; i--) {
                Reading r = tmpList.get(i);
                readingManager.addNewReading(this, r);
            }
        });

    }

    private void setupClearDBButton() {
        Button btn = findViewById(R.id.btnClearDatabase);
        btn.setOnClickListener(view -> {
            readingManager.deleteAllData(this);
            Toast.makeText(this, "Cleared all data", Toast.LENGTH_LONG).show();

        });

    }

    private void setupPretendToUnUploadToServer() {
        Button btn = findViewById(R.id.btnDatabaseTest);
        btn.setOnClickListener(view -> {
            int count = 0;
            List<Reading> readings = readingManager.getReadings(this);
            for (Reading r : readings) {
                if (r.isUploaded()) {
                    count++;
                }
//                r.dateUploadedToServer = ZonedDateTime.now();
                r.dateUploadedToServer = null;
                readingManager.updateReading(this, r);
            }
            Toast.makeText(
                    PatientsActivity.this,
                    "Pretended to un-upload " + count + " readings.",
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    private void setupSettingsAddFake() {
        Button btn = findViewById(R.id.btnSettingsAddFake);
        btn.setOnClickListener(view -> {
            // Write fake settings
            sharedPreferences.edit().clear().commit();
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(Settings.PREF_KEY_VHT_NAME, "Clark the Shark");
            edit.putInt(Settings.PREF_KEY_NUM_HEALTH_CENTRES, 2);
            edit.putString(Settings.PREF_KEY_HEALTH_CENTRE_NAME_+"0", "Bidibidi Health Centre #1 (Zone 5)");
            edit.putString(Settings.PREF_KEY_HEALTH_CENTRE_CELL_+"0", "+5 235 2352-2352");
            edit.putString(Settings.PREF_KEY_HEALTH_CENTRE_NAME_+"1", "Bidibidi Regional Hospital");
            edit.putString(Settings.PREF_KEY_HEALTH_CENTRE_CELL_+"1", "+1 325 2352 3523 52");

            edit.putString(Settings.PREF_KEY_SERVER_URL, Settings.DEFAULT_SERVER_URL);
            String LINEFEED = "\r\n";
            edit.putString(Settings.PREF_KEY_RSAPUBKEY, Settings.DEFAULT_SERVER_RSA);
            edit.putString(Settings.PREF_KEY_SERVER_USERNAME, Settings.DEFAULT_SERVER_USERNAME);
            edit.putString(Settings.PREF_KEY_SERVER_PASSWORD, Settings.DEFAULT_SERVER_USERPASSWORD);

            edit.commit();
            settings.loadFromSharedPrefs();



            Toast.makeText(this, "Add fake settings (testing)", Toast.LENGTH_LONG).show();
        });

    }

    private void setupSettingsClear() {
        Button btn = findViewById(R.id.btnSettingsClear);
        btn.setOnClickListener(view -> {
            sharedPreferences.edit().clear().commit();

            // settings: load defaults if not previously set
            android.support.v7.preference.PreferenceManager.setDefaultValues(
                    this,R.xml.preferences, true);

            settings.loadFromSharedPrefs();
            Toast.makeText(this, "Cleared all settings", Toast.LENGTH_LONG).show();
        });
    }

}
