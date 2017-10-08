package com.buyer.flashfetch.Services;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.buyer.flashfetch.Constants.URLConstants;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FetchDealsService extends Service{

    private ProgressDialog progressDialog;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        showProgressDialog();

        new FetchDealsTask(this, URLConstants.URL_NEARBY_DEALS).execute();

        return START_STICKY;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();
    }

    private void cancelProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.hide();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static class FetchDealsTask extends AsyncTask<String, Integer, Void>{

        JSONObject response;
        String url;
        Context context;

        FetchDealsTask(Context context, String url){
            this.context = context;
            this.url = url;
        }

        @Override
        protected Void doInBackground(String... params) {
            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("mobile", UserProfile.getPhone(context)));
            postParams.add(new PostParam("token", UserProfile.getToken(context)));

            response = PostRequest.execute(URLConstants.URL_NEARBY_DEALS, postParams, null);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    DatabaseHelper databaseHelper = new DatabaseHelper(context);

                    ArrayList<NearByDealsDataModel> nearByDealsDataModels = NearByDealsDataModel.getAllDeals(context);

                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("deals");

                    if(nearByDealsDataModels != null && nearByDealsDataModels.size() > 0){
                        for(int i = 0; i < nearByDealsDataModels.size(); i++){
                            for(int j =0 ; j < jsonArray.length(); j ++){
                                JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                                if(!nearByDealsDataModels.get(i).getDealId().equalsIgnoreCase(jsonObject.getString("deal_id"))){
                                    databaseHelper.deleteDeal(nearByDealsDataModels.get(i).getDealId());
                                }
                            }
                        }
                    }

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        ContentValues contentValues = new ContentValues();

                        contentValues.put(NearByDealsDataModel.DEALS_ID, jsonObject.getString("deal_id"));
                        contentValues.put(NearByDealsDataModel.DEAL_TYPE, jsonObject.getInt("deal_type"));
                        contentValues.put(NearByDealsDataModel.DEAL_CATEGORY, jsonObject.getInt("deal_category"));
                        contentValues.put(NearByDealsDataModel.IMAGE_URL, jsonObject.getString("image"));
                        contentValues.put(NearByDealsDataModel.DEAL_HEADING, jsonObject.getString("deal_heading"));
                        contentValues.put(NearByDealsDataModel.DEAL_DESCRIPTION, jsonObject.getString("deal_description"));
                        contentValues.put(NearByDealsDataModel.DEAL_VALID_UPTO, jsonObject.getString("deal_validity"));
                        contentValues.put(NearByDealsDataModel.HOW_TO_AVAIL_DEAL, jsonObject.getString("deal_activate"));
                        contentValues.put(NearByDealsDataModel.ACTIVATED, jsonObject.getInt("activated"));

                        if(jsonObject.getString("activated").equalsIgnoreCase(1 + "")){
                            contentValues.put(NearByDealsDataModel.VOUCHER_ID, jsonObject.getString("voucher_id"));
                        }else{
                            contentValues.put(NearByDealsDataModel.VOUCHER_ID, "");
                        }

                        contentValues.put(NearByDealsDataModel.SHOP_NAME, jsonObject.getString("store_name"));
                        contentValues.put(NearByDealsDataModel.SHOP_PHONE, jsonObject.getString("store_phone"));
                        contentValues.put(NearByDealsDataModel.SHOP_LOCATION, jsonObject.getString("store_location"));
                        contentValues.put(NearByDealsDataModel.SHOP_ADDRESS, jsonObject.getString("store_address"));
                        contentValues.put(NearByDealsDataModel.SHOP_LATITUDE, jsonObject.getString("store_latitude"));
                        contentValues.put(NearByDealsDataModel.SHOP_LONGITUDE, jsonObject.getString("store_longitude"));
                        contentValues.put(NearByDealsDataModel.DEAL_WEIGHTAGE, jsonObject.getString("weightage"));

                        databaseHelper.addDeal(contentValues);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
