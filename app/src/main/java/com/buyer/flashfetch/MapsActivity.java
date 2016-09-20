package com.buyer.flashfetch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.Objects.Quote;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private Context context;
    private GoogleMap mMap;
    private LinearLayout filter, lisview, layout;
    private List<Quote> list;
    private Handler_Dialog handlerDialog;
    private int height, width;
    private Dialog dialog;
    private RadioButton home, shop;
    private EditText minText, hourText, priceText;
    private TextView bargain, bargained, accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = MapsActivity.this;

        setContentView(R.layout.activity_maps);

        height = WindowManager.LayoutParams.WRAP_CONTENT;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        lisview = (LinearLayout) findViewById(R.id.maps_list);
        filter = (LinearLayout) findViewById(R.id.maps_filter);

        filter.setOnClickListener(this);
        lisview.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        list = (List<Quote>) getIntent().getSerializableExtra("Quotes");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        IconGenerator ui = new IconGenerator(this);

        LatLng research = new LatLng(12.988082, 80.246662);
        LatLng iit = new LatLng(12.988082, 80.2368);
        LatLng hostel = new LatLng(12.986843, 85.239006);
        LatLng gate = new LatLng(12.9880820, 83.239353);

        mMap.addMarker(new MarkerOptions().position(research).title("IIT Research Park").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹500"))));
        mMap.addMarker(new MarkerOptions().position(iit).title("IIT Madras").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹700"))));
        mMap.addMarker(new MarkerOptions().position(hostel).title("Ganga Hostel").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹550"))));
        mMap.addMarker(new MarkerOptions().position(gate).title("Taramani Gate").icon(BitmapDescriptorFactory.fromBitmap(ui.makeIcon("₹600"))));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gate));

        LatLngBounds.Builder b = new LatLngBounds.Builder();

        for (int i = 0; i < list.size(); i++) {
//            String latitude = list.get(i).latitude();
//            String longitude = list.get(i).longitude();
//            LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//            b.include(latLng);
        }

        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, 5);
        mMap.animateCamera(cu);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.maps_filter) {

        } else if (id == R.id.maps_list) {
            finish();
        } else if (id == R.id.accept) {

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_accept);
            dialog.getWindow().setLayout((int) (width * 0.8), height);
            handlerDialog = new Handler_Dialog();
            home = (RadioButton) dialog.findViewById(R.id.home_delivery);
            shop = (RadioButton) dialog.findViewById(R.id.visit_shop);
            Button button = (Button) dialog.findViewById(R.id.ok);
            button.setOnClickListener(handlerDialog);
            dialog.show();
        } else if (id == R.id.bargain) {

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog);
            dialog.getWindow().setLayout((int) (width * 0.8), height);
            handlerDialog = new Handler_Dialog();
            priceText = (EditText) dialog.findViewById(R.id.new_price);
            priceText.setFilters(new InputFilter[]{new InputCheck(0, 10000000)});
            hourText = (EditText) dialog.findViewById(R.id.hour);
            hourText.setFilters(new InputFilter[]{new InputCheck(0, 23)});
            minText = (EditText) dialog.findViewById(R.id.min);
            minText.setFilters(new InputFilter[]{new InputCheck(0, 59)});
            Button button1 = (Button) dialog.findViewById(R.id.ok_dialog);
            button1.setOnClickListener(handlerDialog);
            // set the custom dialog components - text, image and button
            dialog.show();

        }
//        else if (id == R.id.newestFirst) {
//            Collections.sort(list, new NewestComparator());
//        } else if (id == R.id.lowToHigh) {
//            Collections.sort(list, new LowToHighComparator());
//        } else if (id == R.id.highToLow) {
//            Collections.sort(list, new HighToLowComparator());
//        } else if (id == R.id.distance) {
//            Collections.sort(list, new DistanceComparator());
//        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title = marker.getTitle();

        layout = (LinearLayout) findViewById(R.id.map_layout);
        bargained = (TextView) layout.findViewById(R.id.bargained);
        bargain = (TextView) layout.findViewById(R.id.bargain);
        accept = (TextView) layout.findViewById(R.id.accept);

        bargain.setOnClickListener(this);
        accept.setOnClickListener(this);

        layout.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        layout.setVisibility(View.GONE);
    }

    private class Handler_Dialog implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.ok) {
                if (home.isChecked()) {
                    Intent intent = new Intent(v.getContext(), HomeDelivery.class);
                    startActivity(intent);
                    dialog.dismiss();
                } else if (shop.isChecked()) {
                    Toast toast = Toast.makeText(v.getContext(), "Visit Shop Selected", Toast.LENGTH_SHORT);
                    toast.show();
                    layout.setVisibility(View.GONE);
                    dialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(v.getContext(), "Select delivery type", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else if (id == R.id.ok_dialog) {
                String price = priceText.getText().toString();
                String hour = hourText.getText().toString();
                String min = minText.getText().toString();
                boolean error = false;
                if (hour.length() == 0) {
                    error = true;
                    hourText.setError("Set Hour");
                }
                if (min.length() == 0) {
                    error = true;
                    minText.setError("Set Min");
                }
                if (price.length() == 0) {
                    error = true;
                    priceText.setError("Set Price");
                }
                if (!error) {
                    bargain.setVisibility(View.GONE);
                    //list.get(getAdapterPosition()).setBargained(false);
                    bargained.setText(String.format("%s %s", "Bargained for ₹", price));
                    // tt.update(Long.valueOf(hour), Long.valueOf(min));
                    //timer.setText(hour+":"+min);
                    bargained.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(v.getContext(), "" + price, Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }
            }


        }
    }
}