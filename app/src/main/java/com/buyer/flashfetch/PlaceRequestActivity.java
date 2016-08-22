package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    private Context context;
    private ProgressDialog progressDialog;
    private Button placeRequestButton;
    private TextView setTimeButton;
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
        placeRequestButton = (Button)findViewById(R.id.place_request_button);

        Spinner spinner = (Spinner)findViewById(R.id.places_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.places_array, android.R.layout.simple_spinner_dropdown_item);

        if (spinner != null) {
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    customerLocation = places[adapterView.getSelectedItemPosition()];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

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
                if(Utils.isInternetAvailable(context)){

                    progressDialog.show();

                    BargainObject bargainObject  = new BargainObject();

                    bargainObject.setProductName(productName);
                    bargainObject.setProductPrice(productPrice);
                    bargainObject.setImageURL(imageURL);
                    bargainObject.setProductCategory(productCategory);
                    //TODO: need to set this
                    bargainObject.setExpiryTime("");
                    bargainObject.setCustomerName(UserProfile.getName(context));
                    bargainObject.setCustomerLocation(customerLocation);
                    bargainObject.setCustomerEmail(UserProfile.getEmail(context));

                    ServiceManager.callBargainService(context, bargainObject, new UIListener() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();

                            if(UserProfile.getEmail(context) != ""){
                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(context,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onFailure(int result) {

                        }

                        @Override
                        public void onConnectionError() {

                        }

                        @Override
                        public void onCancelled() {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Toast.makeText(context,hourOfDay+minute+second+"",Toast.LENGTH_SHORT).show();
    }
}