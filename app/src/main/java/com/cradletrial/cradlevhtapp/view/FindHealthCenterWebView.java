package com.cradletrial.cradlevhtapp.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cradletrial.cradlevhtapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FindHealthCenterWebView extends AppCompatActivity{
    protected LocationManager locationManager;
    private static String startLat; // = "49.2935433";
    private static String startLon; // = "-122.8805809";
    private static String destLat1 = "49.278094";
    private static String destLon1 = "-122.919883";
    private static String destLat2 = "49.185494";
    private static String destLon2 = "-122.8570614";
    private static String destLat3 = "49.2845417";
    private static String destLon3 = "-123.1138346";

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_health_center_web_view);
        //setSpinner();
        requestPermission();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(FindHealthCenterWebView.this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(FindHealthCenterWebView.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    startLat = Double.toString(location.getLatitude());
                    startLon = Double.toString(location.getLongitude());
                    setSpinner();
                }
            }
        });
    }


    public String getUrl(String startLat, String startLon, String destLat, String destLon) {
        String url = "http://maps.google.com/maps?";
        //String url = "<iframe width=\"550\" height=\"500\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" src=\"http://maps.google.com/maps?" + startLat + startLon + "&source=embed&output=svembed\"></iframe>";
        String startLatLon = "saddr=" + startLat + "," + startLon;
        String destLatLon = "&daddr=" + destLat + "," + destLon;

        url = url + startLatLon + destLatLon;

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
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //view.loadUrl(url);
                //System.out.println("hello");
                return false;
            }
        });
        //webView.loadData(url, "text/html", null);
        webView.loadUrl(url);
    }

    private  void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
