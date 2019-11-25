package com.cradletrial.cradlevhtapp.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.dagger.MyApp;
import com.cradletrial.cradlevhtapp.model.Reading;
import com.cradletrial.cradlevhtapp.model.ReadingManager;
import com.cradletrial.cradlevhtapp.model.Settings;
import com.cradletrial.cradlevhtapp.utilitiles.DateUtil;
import com.cradletrial.cradlevhtapp.utilitiles.GsonUtil;
import com.cradletrial.cradlevhtapp.utilitiles.HybridFileEncrypter;
import com.cradletrial.cradlevhtapp.view.ui.network_volley.MultiReadingUploader;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UploadActivity extends TabActivityBase {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    private static final String TAG = "UploadActivity";
    private static final String LAST_UPLOAD_DATE = "pref last upload date";
    //private String phoneNo = "6045183070";
    private String phoneNo = "+12078020179";
    private String msg;

    // Data Model
    @Inject
    ReadingManager readingManager;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    Settings settings;

    MultiReadingUploader multiUploader;

    public static Intent makeIntent(Context context) {
        return new Intent(context, UploadActivity.class);
    }

    // set who we are for tab code
    public UploadActivity() {
        super(R.id.nav_upload);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inject:
        ((MyApp) getApplication()).getAppComponent().inject(this);

        // setup UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // bottom bar nav in base class
        setupBottomBarNavigation();

        // buttons
        setupUploadDataButton();
        setupUploadDataSMSButton();
        setupErrorHandlingButtons();
        updateReadingUploadLabels();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (multiUploader != null && multiUploader.isUploading()) {
            multiUploader.abortUpload();
            setUploadUiElementVisibility(false);
            updateReadingUploadLabels();
        }
    }

    private void updateReadingUploadLabels() {
        // reading count
        int numReadingsToUpload = getReadingsToUpload().size();
        TextView tvReadingCount = findViewById(R.id.tvReadingsToUpload);
        tvReadingCount.setText(String.format("%d patient readings ready to upload", numReadingsToUpload));

        // upload date
        String uploadDate = sharedPreferences.getString(LAST_UPLOAD_DATE, null);
        String message = "No readings uploaded to server yet";
        if (uploadDate != null) {
            message = "Last upload: " + uploadDate;
        }
        TextView tvUploadDate = findViewById(R.id.tvLastUpdate);
        tvUploadDate.setText(message);
    }


    private void setupUploadDataButton() {
        Button btnStart = findViewById(R.id.btnUploadReadings);
        btnStart.setOnClickListener(view -> {
            // start upload
            uploadData();
        });
        setUploadUiElementVisibility(false);
    }

    private void setupUploadDataSMSButton() {
        Button btnStart = findViewById(R.id.btnUploadReadingsSMS);
        btnStart.setOnClickListener(view -> {
            uploadDataSMS();
            //btnStart.setVisibility(View.INVISIBLE);
        });
        setUploadUiElementVisibility(false);
    }

    private void setupErrorHandlingButtons() {
        setErrorUiElementsVisible(View.GONE);
        Button btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(view -> {
            Toast.makeText(this, "Skipping uploading reading...", Toast.LENGTH_SHORT).show();
            multiUploader.resumeUploadBySkip();
            setErrorUiElementsVisible(View.GONE);
        });

        Button btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(view -> {
            Toast.makeText(this, "Retrying uploading reading...", Toast.LENGTH_SHORT).show();
            multiUploader.resumeUploadByRetry();
            setErrorUiElementsVisible(View.GONE);
        });

        Button btnAbort = findViewById(R.id.btnStopUpload);
        btnAbort.setOnClickListener(view -> {
            Toast.makeText(this, "Stopping upload...", Toast.LENGTH_SHORT).show();
            multiUploader.abortUpload();

            setUploadUiElementVisibility(false);
            updateReadingUploadLabels();
        });
    }

    private void setUploadUiElementVisibility(boolean doingUpload) {
        Button btnStartUploading = findViewById(R.id.btnUploadReadings);
        Button btnAbort = findViewById(R.id.btnStopUpload);
        View groupUploading = findViewById(R.id.layoutUploadingReadings);

        btnStartUploading.setVisibility(doingUpload ? View.INVISIBLE: View.VISIBLE);
        groupUploading.setVisibility(doingUpload ? View.VISIBLE : View.GONE);
        btnAbort.setVisibility(View.VISIBLE);
        if (!doingUpload) {
            setErrorUiElementsVisible(View.GONE);
        }

    }
    private void setErrorUiElementsVisible(int visible) {
        Button btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setVisibility(visible);
        Button btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setVisibility(visible);
        TextView tv = findViewById(R.id.tvUploadErrorMessage);
        tv.setVisibility(visible);

        // upload icon
        ImageView iv = findViewById(R.id.ivUploadAction);
        if (visible == View.VISIBLE) {
            iv.setImageResource(R.drawable.arrow_right_with_x);
        } else {
            iv.setImageResource(R.drawable.arrow_right);
        }

    }

    private void uploadDataSMS() {
        List<Reading> readings = getReadingsToUpload();
        if (readings.size() == 0) {
            Toast.makeText(this, "No readings needing to be uploaded.", Toast.LENGTH_LONG).show();
            return;
        }

        msg = "[";
        for(int i = 0; i < readings.size(); i++) {
            if(i == readings.size() - 1) {
                msg = msg + GsonUtil.getJsonForSyncingToServer(readings.get(i), settings);
            } else {
                msg = msg + GsonUtil.getJsonForSyncingToServer(readings.get(i), settings) + ",";
            }
        }

        multiUploader = new MultiReadingUploader(this, settings, getProgressCallbackListener());
        multiUploader.startUploadSMS(readings);

        msg = msg + "]";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("address", phoneNo);
        intent.putExtra("sms_body",msg);

        startActivity(intent);
        setUploadUiElementVisibility(true);
    }

    private void uploadData() {
        if (multiUploader != null && !multiUploader.isUploadDone()) {
            Toast.makeText(this, "Already uploading; cannot restart", Toast.LENGTH_LONG).show();
            return;
        }
        // ensure settings OK
        if (settings.getServerUrl() == null || settings.getServerUrl().length() == 0) {
            Toast.makeText(this, "Error: Must set server URL in settings", Toast.LENGTH_LONG).show();
            return;
        }
        if (settings.getRsaPubKey() == null || settings.getRsaPubKey().length() == 0) {
            Toast.makeText(this, "Error: Must set RSA Public Key in settings", Toast.LENGTH_LONG).show();
            return;
        }
        // do a small sanity check on key
        // note: Many errors in key will seem valid here! No way to validate.
        try {
            HybridFileEncrypter.convertRsaPemToPublicKey(settings.getRsaPubKey());
        } catch (Exception e) {
            Toast.makeText(this, "Error: Invalid public key in settings: \r\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        // discover un-uploaded readings
        List<Reading> readingsToUpload = getReadingsToUpload();

        // abort if no readings
        if (readingsToUpload.size() == 0) {
            Toast.makeText(this, "No readings needing to be uploaded.", Toast.LENGTH_LONG).show();
            return;
        }

        // upload multiple readings
        multiUploader = new MultiReadingUploader(this, settings, getProgressCallbackListener());
        multiUploader.startUpload(readingsToUpload);
        setUploadUiElementVisibility(true);
    }

    private List<Reading> getReadingsToUpload() {
        List<Reading> allReadings = readingManager.getReadings(this);
        List<Reading> readingsToUpload = new ArrayList<>();
        for (Reading reading : allReadings) {
            if (!reading.isUploaded()) {
                readingsToUpload.add(reading);
            }
        }
        return readingsToUpload;
    }

    MultiReadingUploader.ProgressCallback getProgressCallbackListener() {
        return new MultiReadingUploader.ProgressCallback() {
            @Override
            public void uploadProgress(int numCompleted, int numTotal) {
                if (numCompleted == numTotal) {
                    TextView tv = UploadActivity.this.findViewById(R.id.tvUploadMessage);
                    tv.setText("Done uploading " + numCompleted + " readings to server.");
                    updateReadingUploadLabels();

                    // button visibility
                    Button btnAbort = findViewById(R.id.btnStopUpload);
                    btnAbort.setVisibility(View.GONE);
                    Button btnStart = findViewById(R.id.btnUploadReadings);
                    btnStart.setVisibility(View.VISIBLE);

                    // upload icon
                    ImageView iv = findViewById(R.id.ivUploadAction);
                    iv.setImageResource(R.drawable.arrow_right_with_check);

                    //Toast.makeText(UploadActivity.this, "Done uploading readings!", Toast.LENGTH_LONG).show();
                } else {
                    TextView tv = UploadActivity.this.findViewById(R.id.tvUploadMessage);
                    tv.setText("Uploading reading " + (numCompleted + 1) + " of " + numTotal + "...");
                }
            }

            @Override
            public void uploadReadingSucceeded(Reading reading) {
                // mark reading as uploaded
                reading.dateUploadedToServer = ZonedDateTime.now();
                readingManager.updateReading(UploadActivity.this, reading);

                // record that we did a successful upload
                String dateStr = DateUtil.getFullDateString(ZonedDateTime.now());
                sharedPreferences.edit().putString(LAST_UPLOAD_DATE, dateStr).apply();
            }

            @Override
            public void uploadPausedOnError(String message) {
                TextView tv = UploadActivity.this.findViewById(R.id.tvUploadErrorMessage);
                tv.setText("Error when uploading reading: \r\n" + message);

                setErrorUiElementsVisible(View.VISIBLE);
            }
        };
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
