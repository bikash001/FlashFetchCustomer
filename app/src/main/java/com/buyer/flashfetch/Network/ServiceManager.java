package com.buyer.flashfetch.Network;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Constants.URLConstants;
import com.buyer.flashfetch.DeliveryActivity;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Interfaces.UIResponseListener;
import com.buyer.flashfetch.MainActivity;
import com.buyer.flashfetch.Objects.BargainObject;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.SignUpObject;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.R;
import com.buyer.flashfetch.ServiceResponseObjects.ProductDetailsResponse;
import com.buyer.flashfetch.Services.IE_RegistrationIntentService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public class ServiceManager {

    public static UserSignUpTask userSignUpTask = null;
    public static UserLoginTask userLoginTask = null;
    public static ProductFetchTask productFetchTask = null;
    public static BargainTask bargainTask = null;
    public static QuoteBargainTask quoteBargainTask = null;
    public static PlaceOrderTask placeOrderTask = null;

    public static void callUserRegisterService(Context context, SignUpObject signUpObject, final UIListener uiListener){

        userSignUpTask = new UserSignUpTask(context,signUpObject,uiListener);
        userSignUpTask.execute();
    }

    public static class UserSignUpTask extends AsyncTask<String, Void, Void> {

        private JSONObject response;
        private String personName;
        private String personEmail;
        private String phoneNumber;
        private String password;
        private UIListener uiListener;
        private Context context;

        public UserSignUpTask(Context context, SignUpObject signUpObject, final UIListener uiListener){
            this.context = context;
            this.personName = signUpObject.getPersonName();
            this.personEmail = signUpObject.getPersonEmail();
            this.phoneNumber = signUpObject.getPhoneNumber();
            this.password = signUpObject.getPassword();
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<PostParam> postParams = new ArrayList<PostParam>();

            postParams.add(new PostParam("name", personName));
            postParams.add(new PostParam("email", personEmail));
            postParams.add(new PostParam("pass", password));
            postParams.add(new PostParam("mobile",String.valueOf(phoneNumber)));

            response = PostRequest.execute(URLConstants.URL_SIGN_UP, postParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if(response.getJSONObject("data").getInt("result") == 1){

                    UserProfile.setName(personName, context);
                    UserProfile.setEmail(personEmail, context);
                    UserProfile.setPhone(String.valueOf(phoneNumber), context);
                    UserProfile.setPassword(password, context);

                    UserProfile.setToken(response.getJSONObject("data").getString("token"), context);

                    uiListener.onSuccess();

                } else if(response.getJSONObject("data").getInt("result") == 0) {
                    uiListener.onFailure();
                }
                else{
                    uiListener.onConnectionError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onCancelled();
        }
    }

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
            userSignUpTask.cancel(true);
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

            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("item", productText));
            response = PostRequest.execute(URLConstants.URL_FETCH_PRODUCT, postParams, null);

            Log.d("RESPONSE", response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                JSONObject data = response.getJSONObject("data");

                if (data.getInt("result") == 1) {
                    ProductDetailsResponse productDetailsResponse = new Gson().fromJson(data.toString(), ProductDetailsResponse.class);
                    uiResponseListener.onSuccess(productDetailsResponse);
                }else {
                    uiResponseListener.onFailure();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiResponseListener.onCancelled();
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

    public static void callQuoteBargainService(Context context, Quote quote, String bargainPrice, final UIListener uiListener){

        quoteBargainTask = new QuoteBargainTask(context,quote,bargainPrice,uiListener);
        quoteBargainTask.execute();
    }

    public static class QuoteBargainTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private Context context;
        private Quote quote;
        private String bargainPrice;
        private UIListener uiListener;

        public QuoteBargainTask(Context context, Quote quote, String bargainPrice, final UIListener uiListener) {
            this.context = context;
            this.quote = quote;
            this.bargainPrice = bargainPrice;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("selid",quote.qid));
            postParams.add(new PostParam("btime", String.valueOf(System.currentTimeMillis() + 10000000)));
            postParams.add(new PostParam("bprice",bargainPrice));
            postParams.add(new PostParam("token",UserProfile.getToken(context)));
            postParams.add(new PostParam("email",UserProfile.getEmail(context)));

            response = PostRequest.execute(URLConstants.QUOTE_BARGAIN_URL, postParams, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (response.getJSONObject("data").getInt("result") == 1){

                    ContentValues cv= new ContentValues();

                    cv.put("bargained",1);
                    cv.put("bgprice",bargainPrice);
                    cv.put("bgexptime",String.valueOf(System.currentTimeMillis() + 10000000));

                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.updateQuote(quote.qid,cv);
                }else{
                    //TODO: handle failure condition
                    uiListener.onConnectionError();
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

    public static void callPlaceOrderService(Context context, int deliveryType, String qId, final UIListener uiListener){

        placeOrderTask = new PlaceOrderTask(context,deliveryType,qId,uiListener);
        placeOrderTask.execute();
    }

    public static class PlaceOrderTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private int deliveryType;
        private String qId;
        private UIListener uiListener;

        public PlaceOrderTask(Context context, int deliveryType, String qId, final UIListener uiListener){

            this.context = context;
            this.deliveryType = deliveryType;
            this.qId = qId;
            this.uiListener = uiListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("Sel_id",qId));
            postParams.add(new PostParam("delivery",Integer.toString(deliveryType)));
            postParams.add(new PostParam("token", UserProfile.getToken(context)));
            postParams.add(new PostParam("email",UserProfile.getEmail(context)));

            response = PostRequest.execute(URLConstants.URL_PLACE_ORDER, postParams, null);

            Log.d("RESPONSE", response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try {
                if (response.getJSONObject("data").getInt("result") == 1){
                    ContentValues cv= new ContentValues();

                    cv.put("cuscon",1);
                    cv.put("del",deliveryType);

                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.updateQuote(qId,cv);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
