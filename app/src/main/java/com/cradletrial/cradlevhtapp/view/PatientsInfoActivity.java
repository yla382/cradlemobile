package com.cradletrial.cradlevhtapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.model.GestationalAgeUnit;
import com.cradletrial.cradlevhtapp.model.Patient;
import com.cradletrial.cradlevhtapp.model.Reading;
import com.cradletrial.cradlevhtapp.model.ReadingAnalysis;
import com.cradletrial.cradlevhtapp.model.ReadingManager;
import com.cradletrial.cradlevhtapp.model.ReadingRetestAnalysis;
import com.cradletrial.cradlevhtapp.model.WebReading;
import com.cradletrial.cradlevhtapp.utilitiles.DateUtil;
import com.cradletrial.cradlevhtapp.view.ui.reading.BaseFragment;
import com.cradletrial.cradlevhtapp.viewmodel.ReadingAnalysisViewSupport;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.cradletrial.cradlevhtapp.model.ReadingAnalysis.analyze;
import static java.security.AccessController.getContext;

public class PatientsInfoActivity extends AppCompatActivity{
    final List<Reading> readings = new ArrayList<>();
    Patient patient;
    @Inject
    ReadingManager readingManager;
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

    private void setReadingGest(Reading reading) {
        TextView txt_PatientHeader = (TextView) findViewById(R.id.txt_PatientHeader);
        String unit = "";
        if(reading.gestationalAgeUnit.equals(GestationalAgeUnit.GESTATIONAL_AGE_UNITS_MONTHS)) {
            unit = "m";
        } else if(reading.gestationalAgeUnit.equals(GestationalAgeUnit.GESTATIONAL_AGE_UNITS_WEEKS)){
            unit = "w";
        } else {
            unit = "Not pregnant";
        }

        txt_PatientHeader.setText(patient.getFirstName() + " " + patient.getLastName() + " @ " + reading.gestationalAgeValue + " " + unit);
    }

    private  void setSymptoms(Reading reading) {
        TextView txt_Symptoms = (TextView) findViewById(R.id.txt_Symptoms);
        String symtoms = reading.getSymptomsString();

        if(symtoms.isEmpty()) {
            txt_Symptoms.setText("No Symptoms");
        } else {
            txt_Symptoms.setText(symtoms);
        }
    }

//    private void advice(Reading reading) {
//        ReadingRetestAnalysis retestAnalysis = ReadingRetestAnalysis(reading, readingManager);
//        String message = "";
//        if (retestAnalysis.isRetestRecommendedNow()) {
//            message = getString(R.string.brief_advice_retest_now);
//        }
//
//        else if (retestAnalysis.isRetestRecommendedIn15Min()) {
//            message = getString(R.string.brief_advice_retest_after15);
//        }
//
//        else {
//            ReadingAnalysis analysis = retestAnalysis.getMostRecentReadingAnalysis();
//            message = analysis.getBriefAdviceText(getContext());
//        }
//    }

    private void reading_analyze(Reading reading) {
            TextView tv;
            ImageView iv;
            String errorMessage = "";

            LinearLayout layoutReadings = findViewById(R.id.layout_Readings);
            layoutReadings.removeAllViews();

            View v = View.inflate(getApplicationContext(), R.layout.reading_vitals_with_icons, null);
            layoutReadings.addView(v);

            // date & condition summary
            String time = DateUtil.getDateString(reading.dateTimeTaken);
            ReadingAnalysis analysis = analyze(reading);
            String analysisText = analysis.getAnalysisText(getApplicationContext());
            tv = v.findViewById(R.id.txtReadingHeading);
            tv.setText( getString(R.string.reading_time_summary,
                    time, analysisText));
            if (time.length() == 0) {
                errorMessage += "- date/time of reading" + "\n";
            }

            // blood pressure
            tv = v.findViewById(R.id.txtBloodPressure);
            Integer sys = reading.bpSystolic;
            Integer dia = reading.bpDiastolic;
            if (sys != null && dia != null) {
                tv.setText(getString(R.string.reading_blood_pressure,
                        sys, dia));
                tv.setVisibility(View.VISIBLE);
            } else {
                errorMessage += "- blood pressure (systolic and/or diastolic)" + "\n";
                tv.setVisibility(View.GONE);
            }

            // heart rate
            tv = v.findViewById(R.id.txtHeartRate);
            if (reading.heartRateBPM != null) {
                tv.setText(getString(R.string.reading_heart_rate,
                        reading.heartRateBPM));
                tv.setVisibility(View.VISIBLE);
            } else {
                errorMessage += "- heart rate" + "\n";
                tv.setVisibility(View.GONE);
            }

            // icons
            iv = v.findViewById(R.id.imageCircle);
            iv.setImageResource(ReadingAnalysisViewSupport.getColorCircleImageId(analysis));

            iv = v.findViewById(R.id.imageArrow);
            iv.setImageResource(ReadingAnalysisViewSupport.getArrowImageId(analysis));

            // error messages
            tv = v.findViewById(R.id.txtReadingErrors);
            if (errorMessage.length() > 0) {
                tv.setVisibility(View.VISIBLE);
                // TODO: put in strings.xml
                errorMessage = "Missing required patient vitals:" + "\n" + errorMessage;
                tv.setText(errorMessage);
            } else {
                tv.setVisibility(View.GONE);
            }

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

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Reading reading = readings.get(position);
                    setReadingGest(reading);
                    setSymptoms(reading);
                    reading_analyze(reading);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
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
                            System.out.println(jsonArray.get(i));
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
