package com.example.bikash.flashfetchcustomer;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        // Add a marker in Sydney and move the camera
        LatLng research = new LatLng(12.988082, 80.246662);
        mMap.addMarker(new MarkerOptions().position(research).title("IIT Research Park"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng iit = new LatLng(12.9923,80.2368);
        mMap.addMarker(new MarkerOptions().position(iit).title("IIT Madras"));
        LatLng hostel = new LatLng(12.986843,80.239006);
        mMap.addMarker(new MarkerOptions().position(hostel).title("Ganga Hostel"));
        LatLng gate = new LatLng(12.984287,80.239353);
        mMap.addMarker(new MarkerOptions().position(gate).title("Taramani Gate"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hostel));

    }
}
