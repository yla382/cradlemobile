package com.cradletrial.cradlevhtapp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cradletrial.cradlevhtapp.R;

public class HelpActivity extends TabActivityBase {

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }

    public HelpActivity() {
        super(R.id.nav_help);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setup UI components
        setupBottomBarNavigation();
        setupHelpVideo();
        openPDF();
        openWebMap();
    }

    public void openWebMap() {
        Button webMapButton = (Button) findViewById(R.id.MapButton);
        webMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindHealthCenterWebView.class);
                startActivity(intent);
            }
        });
    }

    public void openPDF() {
        Button pdfButton = (Button) findViewById(R.id.pdfButton);
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupHelpVideo() {
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cradle_vsa_tutorial_for_health_care);

        VideoView videoView  = findViewById(R.id.videoView);

        // set preview image
        // gives the image; however, then does not show the video!
//        videoView.setBackgroundResource(R.drawable.cradle_video_frame);

        final int START_LOCATION_ms = 9000;
        videoView.setMediaController(new MediaController(HelpActivity.this));
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.seekTo(START_LOCATION_ms);

        videoView.setOnCompletionListener(mediaPlayer -> {
            videoView.seekTo(START_LOCATION_ms);
        });
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
