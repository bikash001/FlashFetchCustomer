package com.example.bikash.flashfetchcustomer;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        IconGenerator ui = new IconGenerator(this);
        Bitmap mp = ui.makeIcon("₹500");

        // Add a marker in Sydney and move the camera
        LatLng research = new LatLng(12.988082, 80.246662);
        mMap.addMarker(new MarkerOptions().position(research).title("IIT Research Park").icon(BitmapDescriptorFactory.fromBitmap(mp)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng iit = new LatLng(12.988082,80.2368);
        mMap.addMarker(new MarkerOptions().position(iit).title("IIT Madras").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹700"))));
        LatLng hostel = new LatLng(12.986843,85.239006);
        mMap.addMarker(new MarkerOptions().position(hostel).title("Ganga Hostel").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹550"))));
        LatLng gate = new LatLng(12.9880820,83.239353);
        mMap.addMarker(new MarkerOptions().position(gate).title("Taramani Gate").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹600"))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(research));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

    }
}
