package com.cradletrial.cradlevhtapp.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.cradletrial.cradlevhtapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindHealthCenter extends FragmentActivity implements OnMapReadyCallback {
    private final static String GOOGLE_API_URL = "https://maps.googleapis.come/maps/api/directions/json?";
    private String GOOGLE_API_KEY = getResources().getString(R.string.google_maps_key);
    private String current_location = "";
    private String destination = "";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_health_center);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        double lat = 49.2768;
        double lon = -122.9180;
        LatLng health_center_one = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(health_center_one).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(health_center_one));
    }
}
