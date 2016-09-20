package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.Objects.PlaceRequestObject;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.BargainObject;
import com.buyer.flashfetch.Objects.UserProfile;

import java.util.Calendar;

/**
 * Created by KRANTHI on 08-08-2016.
 */
public class PlaceRequestActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener{

    private static final LatLngBounds BOUNDS_CHENNAI = new LatLngBounds(new LatLng(13.080680, 80.260718), new LatLng(13.082680, 80.270718));
    private static final int PLACE_PICKER_REQUEST = 1;

    private Context context;
    private ProgressDialog progressDialog;
    private Button placeRequestButton;
    private double selectedLatitude, selectedLongitude;
    private TextView setTimeButton, setPlaceButton;
    private String productPrice, productName, imageURL, customerLocation;
    private long productCategory;
    private int minTime;
    private String[] places;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = PlaceRequestActivity.this;

        setContentView(R.layout.place_request);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
           productName = bundle.getString(Constants.PRODUCT_NAME);
           productPrice = bundle.getString(Constants.PRODUCT_PRICE);
           imageURL = bundle.getString(Constants.PRODUCT_IMAGE_URL);
           productCategory = bundle.getLong(Constants.PRODUCT_CATEGORY);
        }

        places = getResources().getStringArray(R.array.places_array);

        progressDialog = getProgressDialog(context);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Place Request");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    finish();
                }
            });
        }

        setTimeButton = (TextView)findViewById(R.id.place_request_time);
        setPlaceButton = (TextView)findViewById(R.id.place_request_location);
        placeRequestButton = (Button)findViewById(R.id.place_request_button);

        setPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(BOUNDS_CHENNAI);

                try {
                    startActivityForResult(builder.build(PlaceRequestActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                if(hour < 10 || hour >= 21){
                    hour = 11;
                    minute = 0;
                }else{
                    hour = hour + 1;
                }

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance((TimePickerDialog.OnTimeSetListener) context,hour,minute,true);

                if(hour < 10 || hour >= 21){
                    timePickerDialog.setMinTime(hour,minute,0);
                }else{
                   timePickerDialog.setMinTime(hour,minute,0);
                }

                timePickerDialog.setTitle("Select Time");
                timePickerDialog.setOkText("OK");
                timePickerDialog.setCancelText("CANCEL");
                timePickerDialog.show(getFragmentManager(),"TIME_PICKER_DIALOG");
            }
        });

        placeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(UserProfile.getEmail(context) != ""){

                    if(Utils.isInternetAvailable(context)){

                        progressDialog.show();

                        PlaceRequestObject placeRequestObject = new PlaceRequestObject();

                        placeRequestObject.setProductName(productName);
                        placeRequestObject.setProductPrice(productPrice);
                        placeRequestObject.setImageUrl(imageURL);
                        placeRequestObject.setProductCategory(productCategory);
                        //TODO: need the formaat to set time
                        placeRequestObject.setBargainExpTime("");
                        placeRequestObject.setCustomerLatitude(selectedLatitude);
                        placeRequestObject.setCustomerLongitude(selectedLongitude);

                        ServiceManager.callProductRequestService(context, placeRequestObject, new UIListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();

                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toasts.serverBusyToast(context);
                            }

                            @Override
                            public void onFailure(int result) {

                            }

                            @Override
                            public void onConnectionError() {
                                progressDialog.dismiss();
                                Toasts.serverBusyToast(context);
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }else{
                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    selectedLatitude = place.getLatLng().latitude;
                    selectedLongitude = place.getLatLng().longitude;
                    break;
                }
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Toast.makeText(context,hourOfDay+minute+second+"",Toast.LENGTH_SHORT).show();
    }
}
