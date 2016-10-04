package com.buyer.flashfetch.Network;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.buyer.flashfetch.Constants.URLConstants;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Interfaces.UIResponseListener;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.Objects.PlaceRequestObject;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.SignUpObject;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.ServiceResponseObjects.ProductDetailsResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
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
    private static FetchDealsTask fetchDealsTask = null;
    private static RetryVerificationTask retryOTPTask = null;
    private static AccountVerificationTask accountVerificationTask = null;

    public static void callUserRegisterService(Context context, SignUpObject signUpObject, final UIListener uiListener) {

        userSignUpTask = new UserSignUpTask(context, signUpObject, uiListener);
        userSignUpTask.execute();
    }

    public static class UserSignUpTask extends AsyncTask<String, Void, Void> {

        private JSONObject response;
        private String personName;
        private String personEmail;
        private String phoneNumber;
        private String password;
        private String referralCode;
        private UIListener uiListener;
        private Context context;

        public UserSignUpTask(Context context, SignUpObject signUpObject, final UIListener uiListener) {
            this.context = context;
            this.personName = signUpObject.getPersonName();
            this.personEmail = signUpObject.getPersonEmail();
            this.phoneNumber = signUpObject.getPhoneNumber();
            this.password = signUpObject.getPassword();
            this.referralCode = signUpObject.getReferralCode();
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
            postParams.add(new PostParam("mobile", String.valueOf(phoneNumber)));
            postParams.add(new PostParam("referral", referralCode));

            response = PostRequest.execute(URLConstants.URL_SIGN_UP, postParams, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    UserProfile.setToken(response.getJSONObject("data").getString("token"), context);

                    UserProfile.setName(personName, context);
                    UserProfile.setEmail(personEmail, context);
                    UserProfile.setPhone(String.valueOf(phoneNumber), context);
                    UserProfile.setPassword(password, context);

                    if(response.getJSONObject("data").getInt("eflag") == 1){
                        UserProfile.sentVerificationOTP(true,context);
                    }else{
                        UserProfile.sentVerificationOTP(false,context);
                    }

                    uiListener.onSuccess();

                } else if (response.getJSONObject("data").getInt("result") == 0) {
                    uiListener.onFailure();
                } else {
                    uiListener.onConnectionError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }

            userSignUpTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            userSignUpTask.cancel(true);
            uiListener.onCancelled();
        }
    }

    public static void callUserLoginService(Context context, String email, String password, final UIListener uiListener) {

        userLoginTask = new UserLoginTask(context, email, password, uiListener);
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
            postParams.add(new PostParam("pass", password));

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
                    UserProfile.setPassword(password,context);
                    UserProfile.setToken(response.getJSONObject("data").getString("token"), context);

                    if(response.getJSONObject("data").getInt("otp_ver") == 1){
                        UserProfile.setAccountVerified(context,true);
                    }else{
                        UserProfile.setAccountVerified(context,false);
                    }

                    uiListener.onSuccess();

                } else if (response.getJSONObject("data").getInt("result") == 0) {
                    uiListener.onFailure(0);
                } else if (response.getJSONObject("data").getInt("result") == -1) {
                    uiListener.onFailure(-1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }

            userLoginTask = null;
        }

        @Override
        protected void onCancelled() {
            userSignUpTask.cancel(true);
            uiListener.onCancelled();
        }
    }

    public static void callProductFetchService(Context context, String productText, final UIResponseListener<ProductDetailsResponse> uiResponseListener) {

        productFetchTask = new ProductFetchTask(context, productText, uiResponseListener);
        productFetchTask.execute();
    }

    public static class ProductFetchTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private Context context;
        private String productText;
        private UIResponseListener<ProductDetailsResponse> uiResponseListener;

        public ProductFetchTask(Context context, String productText, final UIResponseListener<ProductDetailsResponse> uiResponseListener) {

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
                } else {
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

    public static void callProductRequestService(Context context, PlaceRequestObject placeRequestObject, final UIListener uiListener) {

        bargainTask = new BargainTask(context, placeRequestObject, uiListener);
        bargainTask.execute();
    }

    public static class BargainTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private Context context;
        private PlaceRequestObject placeRequestObject;
        private UIListener uiListener;

        public BargainTask(Context context, PlaceRequestObject placeRequestObject, final UIListener uiListener) {
            this.context = context;
            this.placeRequestObject = placeRequestObject;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();

            iPostParams.add(new PostParam("pname", placeRequestObject.getProductName()));
            iPostParams.add(new PostParam("price", placeRequestObject.getProductPrice()));
            iPostParams.add(new PostParam("img", placeRequestObject.getImageUrl()));
            iPostParams.add(new PostParam("cat", String.valueOf(placeRequestObject.getProductCategory())));

            //TODO: find in what format we have to send the time
            iPostParams.add(new PostParam("time", placeRequestObject.getBargainExpTime()));

            //TODO: find what we have to send in the customer location field
            iPostParams.add(new PostParam("cus_loc", placeRequestObject.getCustomerLocation()));

            iPostParams.add(new PostParam("name", UserProfile.getName(context)));
            iPostParams.add(new PostParam("token", UserProfile.getToken(context)));
            iPostParams.add(new PostParam("cus_email", UserProfile.getEmail(context)));

            response = PostRequest.execute(URLConstants.BARGAIN_URL, iPostParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    ContentValues cv = new ContentValues();

//                    cv.put("productId", placeRequestObject.get);
                    cv.put("productName", placeRequestObject.getProductName());
                    cv.put("productPrice", placeRequestObject.getProductPrice());
                    cv.put("imageURL", placeRequestObject.getImageUrl());
                    cv.put("expiryTime", String.valueOf(System.currentTimeMillis() + 10000000));
                    cv.put("productCategory", placeRequestObject.getProductCategory());
                    cv.put("deliveryLatitude", placeRequestObject.getCustomerLatitude());
                    cv.put("deliveryLongitude", placeRequestObject.getCustomerLongitude());

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

    public static void callQuoteBargainService(Context context, Quote quote, String bargainPrice, final UIListener uiListener) {

        quoteBargainTask = new QuoteBargainTask(context, quote, bargainPrice, uiListener);
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

            postParams.add(new PostParam("selid", quote.quoteId + ""));
            postParams.add(new PostParam("btime", String.valueOf(System.currentTimeMillis() + 10000000)));
            postParams.add(new PostParam("bprice", bargainPrice));
            postParams.add(new PostParam("token", UserProfile.getToken(context)));
            postParams.add(new PostParam("email", UserProfile.getEmail(context)));

            response = PostRequest.execute(URLConstants.QUOTE_BARGAIN_URL, postParams, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    ContentValues cv = new ContentValues();

                    cv.put("bargained", 1);
                    cv.put("bgprice", bargainPrice);
                    cv.put("bgexptime", String.valueOf(System.currentTimeMillis() + 10000000));

                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.updateQuote(quote.quoteId, cv);
                } else {
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

    public static void callPlaceOrderService(Context context, int deliveryType, String qId, final UIListener uiListener) {

        placeOrderTask = new PlaceOrderTask(context, deliveryType, qId, uiListener);
        placeOrderTask.execute();
    }

    public static class PlaceOrderTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private int deliveryType;
        private String qId;
        private UIListener uiListener;

        public PlaceOrderTask(Context context, int deliveryType, String qId, final UIListener uiListener) {

            this.context = context;
            this.deliveryType = deliveryType;
            this.qId = qId;
            this.uiListener = uiListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("Sel_id", qId + ""));
            postParams.add(new PostParam("delivery", Integer.toString(deliveryType)));
            postParams.add(new PostParam("token", UserProfile.getToken(context)));
            postParams.add(new PostParam("email", UserProfile.getEmail(context)));

            response = PostRequest.execute(URLConstants.URL_PLACE_ORDER, postParams, null);

            Log.d("RESPONSE", response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    ContentValues cv = new ContentValues();

                    cv.put("cuscon", 1);
                    cv.put("del", deliveryType);

                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.updateQuote(qId+"", cv);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getRoadDistance(Context context, double quoteId, double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
        GetRoadDistanceTask getRoadDistanceTask = new GetRoadDistanceTask(context, quoteId, fromLatitude, fromLongitude, toLatitude, toLongitude);
        getRoadDistanceTask.execute();
    }

    public static class GetRoadDistanceTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private Context context;
        private double quoteId;
        private double fromLatitude;
        private double fromLongitude;
        private double toLatitude;
        private double toLongitude;

        public GetRoadDistanceTask(Context context, double quoteId, double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
            this.context = context;
            this.quoteId = quoteId;
            this.fromLatitude = fromLatitude;
            this.fromLongitude = fromLongitude;
            this.toLatitude = toLatitude;
            this.toLongitude = toLongitude;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url = "http://maps.google.com/maps/api/directions/xml?origin="
                    + fromLatitude + "," + fromLongitude + "&destination=" + toLatitude
                    + "," + toLongitude + "&sensor=false&units=metric";

            response = PostRequest.execute(url, null, null);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                JSONArray routeArray = response.getJSONObject("data").getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);

                JSONArray newTempARr = routes.getJSONArray("legs");
                JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

                JSONObject distance = newDisTimeOb.getJSONObject("distance");
                JSONObject time = newDisTimeOb.getJSONObject("duration");

                ContentValues contentValues = new ContentValues();
                contentValues.put(Quote.DISTANCE, distance.getString("text"));

                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.updateQuote(quoteId+"", contentValues);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void callFetchDealsService(Context context, final UIResponseListener<ArrayList<NearByDealsDataModel>> uiListener) {

        fetchDealsTask = new FetchDealsTask(context, uiListener);
        fetchDealsTask.execute();
    }

    public static class FetchDealsTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private UIResponseListener<ArrayList<NearByDealsDataModel>> uiListener;

        public FetchDealsTask(Context context, final UIResponseListener<ArrayList<NearByDealsDataModel>> uiListener) {
            this.context = context;
            this.uiListener = uiListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("mobile", UserProfile.getPhone(context)));
            postParams.add(new PostParam("token", UserProfile.getToken(context)));

            response = PostRequest.execute(URLConstants.URL_NEARBY_DEALS, postParams, null);

            Log.d("RESPONSE", response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("deals");

                    ArrayList<NearByDealsDataModel> nearByDealsDataModelList = new ArrayList<>();

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        NearByDealsDataModel nearByDealsDataModel = new NearByDealsDataModel();

                        nearByDealsDataModel.setDealId(jsonObject.getString("deal_id"));
                        nearByDealsDataModel.setDealsCategory(jsonObject.getInt("deal_category"));
                        nearByDealsDataModel.setDealsType(jsonObject.getInt("deal_type"));
                        nearByDealsDataModel.setShopName(jsonObject.getString("store_name"));
                        nearByDealsDataModel.setShopLocation(jsonObject.getString("store_location"));
                        nearByDealsDataModel.setShopPhone(jsonObject.getString("store_phone"));
                        nearByDealsDataModel.setShopLatitude(jsonObject.getString("store_latitude"));
                        nearByDealsDataModel.setShopLongitude(jsonObject.getString("store_longitude"));
                        nearByDealsDataModel.setImageUrl(jsonObject.getString("image"));
                        nearByDealsDataModel.setItemHeading(jsonObject.getString("deal_heading"));
                        nearByDealsDataModel.setItemDescription(jsonObject.getString("deal_description"));
                        nearByDealsDataModel.setValidTo(jsonObject.getString("deal_validity"));

                        nearByDealsDataModelList.add(nearByDealsDataModel);
                    }

                    uiListener.onSuccess(nearByDealsDataModelList);
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }
    }

    public static void callVerificationService(Context context, String verificationCode, String mobileNumber, String token, final UIListener uiListener) {

        accountVerificationTask = new AccountVerificationTask(context, verificationCode, mobileNumber, token, uiListener);
        accountVerificationTask.execute();
    }

    public static class AccountVerificationTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private String verificationCode, mobileNumber, token;
        private UIListener uiListener;

        public AccountVerificationTask(Context context, String verificationCode, String mobileNumber, String token, final UIListener uiListener) {
            this.context = context;
            this.verificationCode = verificationCode;
            this.mobileNumber = mobileNumber;
            this.token = token;
            this.uiListener = uiListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("otp_in", verificationCode));
            postParams.add(new PostParam("mobile", mobileNumber));
            postParams.add(new PostParam("token", token));

            response = PostRequest.execute(URLConstants.URL_ACCOUNT_VERIFICATION, postParams, null);

            Log.d("RESPONSE", response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }
    }

    public static void callRetryVerificationService(Context context, String mobileNumber, String token, final UIListener uiListener) {

        retryOTPTask = new RetryVerificationTask(context, mobileNumber, token, uiListener);
        retryOTPTask.execute();
    }

    public static class RetryVerificationTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject response;
        private Context context;
        private String mobileNumber, token;
        private UIListener uiListener;

        public RetryVerificationTask(Context context, String mobileNumber, String token, final UIListener uiListener) {
            this.context = context;
            this.mobileNumber = mobileNumber;
            this.token = token;
            this.uiListener = uiListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<PostParam> postParams = new ArrayList<>();

            postParams.add(new PostParam("mobile", mobileNumber));
            postParams.add(new PostParam("token", token));

            response = PostRequest.execute(URLConstants.URL_RETRY_VERIFICATION, postParams, null);

            Log.d("RESPONSE", response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try {
                if (response.getJSONObject("data").getInt("result") == 1 && response.getJSONObject("data").getInt("eflag") == 1) {
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }
    }

    public static void callForgotPasswordService(Context context, String mobileNumber, final UIListener uiListener) {

        ForgotPasswordTask forgotPasswordTask = new ForgotPasswordTask(context, mobileNumber, uiListener);
        forgotPasswordTask.execute();
    }

    public static class ForgotPasswordTask extends AsyncTask<Void, Void, Void>{

        private JSONObject response;
        private String mobileNumber;
        private Context context;
        private UIListener uiListener;

        public ForgotPasswordTask(Context context, String mobileNumber, final UIListener uiListener){
            this.context = context;
            this.mobileNumber = mobileNumber;
            this.uiListener = uiListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("mobile",mobileNumber));

            response = PostRequest.execute(URLConstants.URL_FORGOT_PASSWORD,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    if(response.getJSONObject("data").getInt("eflag") == 0){
                        uiListener.onFailure(0);
                    }else{
                        uiListener.onSuccess();
                    }
                }else if(response.getJSONObject("data").getInt("result") == 0){
                    uiListener.onFailure();
                }else{
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

    public static void callPasswordVerificationService(Context context, String email, String verificationCode, final UIListener uiListener) {

        PasswordVerificationTask passwordVerificationTask = new PasswordVerificationTask(context,email,verificationCode,uiListener);
        passwordVerificationTask.execute();
    }

    public static class PasswordVerificationTask extends AsyncTask<Void,Void,Void>{

        private JSONObject response;
        private String email,verificationCode;
        private UIListener uiListener;

        public PasswordVerificationTask(Context context, String email, String verificationCode, final UIListener uiListener){
            this.email = email;
            this.verificationCode = verificationCode;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("email",email));
            postParams.add(new PostParam("code",verificationCode));

            response = PostRequest.execute(URLConstants.URL_PASSWORD_VERIFICATION,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else if(response.getJSONObject("data").getInt("result") == 0){
                    uiListener.onFailure();
                }else{
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

    public static void callPasswordChangeService(Context context, String email, String password, final UIListener uiListener) {

        ChangePasswordTask changePasswordTask = new ChangePasswordTask(context,email,password,uiListener);
        changePasswordTask.execute();
    }

    public static class ChangePasswordTask extends AsyncTask<Void,Void,Void> {

        private JSONObject response;
        private String email;
        private String password;
        private UIListener uiListener;

        public ChangePasswordTask(Context context, String email, String password, final UIListener uiListener){
            this.email = email;
            this.password = password;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("email",email));
            postParams.add(new PostParam("password",password));

            response = PostRequest.execute(URLConstants.URL_PASSWORD_CHANGE,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    uiListener.onSuccess();
                }else{
                    uiListener.onFailure();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                uiListener.onConnectionError();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            uiListener.onConnectionError();
        }
    }
}
