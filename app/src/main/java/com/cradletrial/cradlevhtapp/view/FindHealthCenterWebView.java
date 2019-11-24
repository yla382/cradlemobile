package com.cradletrial.cradlevhtapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cradletrial.cradlevhtapp.R;

import java.util.ArrayList;
import java.util.List;

public class FindHealthCenterWebView extends AppCompatActivity {
    private static String startLat = "49.2935433";
    private static String startLon = "-122.8805809";
    private static String destLat1 = "49.278094";
    private static String destLon1 = "-122.919883";
    private static String destLat2 = "49.185494";
    private static String destLon2 = "-122.8570614";
    private static String destLat3 = "49.2845417";
    private static String destLon3 = "-123.1138346";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_health_center_web_view);
        setSpinner();
    }


    public String getUrl(String startLat, String startLon, String destLat, String destLon) {
        String url = "http://maps.google.com/maps?";
        String startLatLon = "saddr=" + startLat + "," + startLon;
        String destLatLong = "&daddr=" + destLat + "," + destLon;

        url = url + startLatLon + destLatLong;

        return url;
    }


    public void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Health Center 1");
        list.add("Health Center 2");
        list.add("Health Center 3");

        Spinner healthCenter = (Spinner) findViewById(R.id.healthcenter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healthCenter.setAdapter(arrayAdapter);

        healthCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    String url = getUrl(startLat, startLon, destLat1, destLon1);
                    displayMap(url);

                } else if (position == 1) {
                    String url = getUrl(startLat, startLon, destLat2, destLon2);
                    displayMap(url);
                } else {
                    String url = getUrl(startLat, startLon, destLat3, destLon3);
                    displayMap(url);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void displayMap(String url) {
        WebView webView = (WebView) findViewById(R.id.MapWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadData(, "text/html", null);
        webView.loadUrl(url);
    }
}
