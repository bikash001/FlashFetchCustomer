package com.buyer.flashfetch.Network;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Constants.URLConstants;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Interfaces.UIResponseListener;
import com.buyer.flashfetch.MainActivity;
import com.buyer.flashfetch.Objects.BargainObject;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.R;
import com.buyer.flashfetch.ServiceResponseObjects.ProductDetailsResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public class ServiceManager {

    public static UserLoginTask userLoginTask;
    public static ProductFetchTask productFetchTask;
    public static BargainTask bargainTask;

    public static void callUserLoginService(Context context, String email, String password, final UIListener uiListener){

        userLoginTask = new UserLoginTask(context,email,password,uiListener);
        userLoginTask.execute();
    }

    public static class UserLoginTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private String email;
        private String password;
        private Context context;
        private UIListener uiListener;

        public UserLoginTask(Context context, String email, String password, final UIListener uiListener) {
            this.context = context;
            this.email = email;
            this.password = password;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> postParams = new ArrayList<PostParam>();

            postParams.add(new PostParam("email", email));
            postParams.add(new PostParam("pass",password));

            response = PostRequest.execute(URLConstants.URL_LOGIN, postParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            userLoginTask = null;

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    UserProfile.setEmail(email, context);
                    UserProfile.setToken(response.getJSONObject("data").getString("token"), context);
                    uiListener.onSuccess();

                } else if(response.getJSONObject("data").getInt("result") == 0){
                    uiListener.onFailure(0);
                }else if(response.getJSONObject("data").getInt("result") == -1){
                    uiListener.onFailure(-1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
            uiListener.onCancelled();
        }
    }

    public static void callProductFetchService(Context context, String productText, final UIResponseListener<ProductDetailsResponse> uiResponseListener){

        productFetchTask = new ProductFetchTask(context,productText,uiResponseListener);
        productFetchTask.execute();
    }

    public static class ProductFetchTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private Context context;
        private String productText;
        private UIResponseListener<ProductDetailsResponse> uiResponseListener;

        public ProductFetchTask(Context context, String productText, final UIResponseListener<ProductDetailsResponse> uiResponseListener){

            this.context = context;
            this.productText = productText;
            this.uiResponseListener = uiResponseListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> postParams = new ArrayList<PostParam>();

            postParams.add(new PostParam("item", productText));
            response = PostRequest.execute(URLConstants.URL_FETCH_PRODUCT, postParams, null);

            Log.d("RESPONSE", response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            try {
//                JSONObject data = response.getJSONObject("data");
//
//                name = data.getString("result");
//                price = data.getString("price");
//                url = data.getString("img");
//                category = data.getString("category");
//                name = name + " (" + category + ")";
//                cat = data.getInt("cat");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            tvname.setText(name);
//            tvprice.setText("Price: " + price);
//
//            Glide
//                    .with(ExtractActivity.this)
//                    .load(url)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .into(iv);
//
//            showProgress(false);
        }
    }

    public static void callBargainService(Context context, BargainObject bargainObject, final UIListener uiListener){

        bargainTask = new BargainTask(context,bargainObject,uiListener);
        bargainTask.execute();
    }

    public static class BargainTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private Context context;
        private BargainObject bargainObject;
        private UIListener uiListener;

        public BargainTask(Context context, BargainObject bargainObject, final UIListener uiListener){
            this.context = context;
            this.bargainObject = bargainObject;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();

            iPostParams.add(new PostParam("pname", bargainObject.getProductName()));
            iPostParams.add(new PostParam("price", bargainObject.getProductPrice()));
            iPostParams.add(new PostParam("img", bargainObject.getImageURL()));
            iPostParams.add(new PostParam("cat", String.valueOf(bargainObject.getProductCategory())));
            iPostParams.add(new PostParam("time", bargainObject.getExpiryTime()));
            iPostParams.add(new PostParam("cus_loc",bargainObject.getCustomerLocation()));
            iPostParams.add(new PostParam("name", UserProfile.getName(context)));
            iPostParams.add(new PostParam("token",UserProfile.getToken(context)));
            iPostParams.add(new PostParam("cus_email",UserProfile.getEmail(context)));

            response = PostRequest.execute(URLConstants.BARGAIN_URL, iPostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (response.getJSONObject("data").getInt("result") == 1){

                    ContentValues cv= new ContentValues();

                    cv.put("productId",response.getJSONObject("data").getString("id"));
                    cv.put("productName",bargainObject.getProductName());
                    cv.put("productPrice",bargainObject.getProductPrice());
                    cv.put("imageURL",bargainObject.getImageURL());
                    cv.put("expiryTime",String.valueOf(System.currentTimeMillis() + 10000000));
                    cv.put("productCategory",bargainObject.getProductCategory());

                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.addRequest(cv);

                    uiListener.onSuccess();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onFailure();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }
}
