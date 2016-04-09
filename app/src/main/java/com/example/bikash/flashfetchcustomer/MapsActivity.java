package com.example.bikash.flashfetchcustomer;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LinearLayout filter,lisview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        filter = (LinearLayout) findViewById(R.id.maps_filter);
        filter.setOnClickListener(this);
        lisview = (LinearLayout) findViewById(R.id.maps_list);
        lisview.setOnClickListener(this);
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
        Marker marker1,marker2,marker3,marker4;
        marker1 = mMap.addMarker(new MarkerOptions().position(research).title("IIT Research Park").icon(BitmapDescriptorFactory.fromBitmap(mp)));
        marker1.setTitle("research");
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng iit = new LatLng(12.988082,80.2368);
        marker2 = mMap.addMarker(new MarkerOptions().position(iit).title("IIT Madras").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹700"))));
        marker2.setTitle("iit madras");
        LatLng hostel = new LatLng(12.986843,85.239006);
        marker3 = mMap.addMarker(new MarkerOptions().position(hostel).title("Ganga Hostel").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹550"))));
        marker3.setTitle("ganga");
        LatLng gate = new LatLng(12.9880820,83.239353);
        marker4 = mMap.addMarker(new MarkerOptions().position(gate).title("Taramani Gate").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹600"))));
        marker4.setTitle("taramani");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gate));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.maps_filter){

        }
        else if(id == R.id.maps_list){
            finish();
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.v("marker","showing");
        Toast toast = Toast.makeText(this,"hello", Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }
}