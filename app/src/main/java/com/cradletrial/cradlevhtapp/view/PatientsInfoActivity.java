package com.cradletrial.cradlevhtapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.model.Patient;
import com.cradletrial.cradlevhtapp.model.Reading;
import com.cradletrial.cradlevhtapp.model.WebReading;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PatientsInfoActivity extends AppCompatActivity {
    final List<Reading> readings = new ArrayList<>();
    Patient patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        patient = (Patient) getIntent().getExtras().getParcelable("patient");
        getReadingsFromServer(patient.getPatientId());

        TextView patientETId = (TextView) findViewById(R.id.Patient_AT_ID);
        TextView patientName = (TextView) findViewById(R.id.Patient_name);
        TextView patientAge = (TextView) findViewById(R.id.Patient_age);
        TextView patientCountry = (TextView) findViewById(R.id.Patient_country);
        TextView patientLocation = (TextView) findViewById(R.id.Patient_location);

        patientETId.setText(patient.getAttestationID());
        patientName.setText(patient.getFirstName() + " " + patient.getLastName());
        patientAge.setText(patient.getAgeYears().toString());
        patientCountry.setText(patient.getCountry());
        patientLocation.setText(patient.getLastName());
        //setReadingSpinner(readings);
    }

    private void setReadingSpinner(List<Reading> readings) {
        Spinner spinner = (Spinner) findViewById(R.id.patientReadings);
        List<String> list = new ArrayList<>();
        if(readings.size() == 0) {
            list.add("No Readings");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        } else {
            for(int i = 0; i < readings.size(); i++) {
                list.add(readings.get(i).readingId.toString());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }
    }

    private void getReadingsFromServer(Integer patientID) {
        RequestQueue mRequestQueue;
        String url = "http://192.168.19.1:8080/android/reading/findByPatientID/";
        String TAG = PatientsActivity.class.getName();

        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + patientID.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //List<Reading> readings = new ArrayList<>();
                    if(jsonArray != null) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.findAndRegisterModules();
                            WebReading webReading = objectMapper.readValue(jsonArray.get(i).toString(), WebReading.class);
                            Reading reading = new Reading(webReading);
                            readings.add(reading);
                            System.out.println(readings.size());
                        }
                    }
                    setReadingSpinner(readings);
                }
                catch (Throwable tx) {
                    Log.e("PatientInfoActivity: ", "Could not parse malformed JSON: \"" + response + "\"");
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
}
