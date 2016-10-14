package com.buyer.flashfetch.Network;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.IEventConstants;
import com.buyer.flashfetch.Constants.NearByDealsConstants;
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
import com.buyer.flashfetch.ServiceResponseObjects.DealsVoucherResponse;
import com.buyer.flashfetch.ServiceResponseObjects.ProductDetailsResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public class ServiceManager {

    private static final String TAB = ServiceManager.class.getSimpleName();

    public static UserSignUpTask userSignUpTask = null;
    public static UserLoginTask userLoginTask = null;
//    public static ProductFetchTask productFetchTask = null;
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
            postParams.add(new PostParam("password", password));
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

                    if(!TextUtils.isEmpty(response.getJSONObject("data").getString("referral_code"))){
                        UserProfile.setReferralCode(context, response.getJSONObject("data").getString("referral_code"));
                    }

                    if(!TextUtils.isEmpty(response.getJSONObject("data").getString("referral_code"))){
                        UserProfile.setReferralCode(context, response.getJSONObject("data").getString("referral_code"));
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

    public static void callUserLoginService(Context context, String mobile, String password, final UIListener uiListener) {

        userLoginTask = new UserLoginTask(context, mobile, password, uiListener);
        userLoginTask.execute();
    }

    public static class UserLoginTask extends AsyncTask<Void, Void, Void> {

        private JSONObject response;
        private String mobile;
        private String password;
        private Context context;
        private UIListener uiListener;

        public UserLoginTask(Context context, String mobile, String password, final UIListener uiListener) {
            this.context = context;
            this.mobile = mobile;
            this.password = password;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostParam> postParams = new ArrayList<PostParam>();

            postParams.add(new PostParam("mobile", mobile));
            postParams.add(new PostParam("password", password));

            response = PostRequest.execute(URLConstants.URL_LOGIN, postParams, null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            userLoginTask = null;

            try {
                if (response.getJSONObject("data").getInt("result") == 1) {

                    UserProfile.setPhone(mobile, context);
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

//        productFetchTask = new ProductFetchTask(context, productText, uiResponseListener);
//        productFetchTask.execute();
    }

//    public static class ProductFetchTask extends AsyncTask<Void, Void, Void> {
//
//        private JSONObject response;
//        private Context context;
//        private String productText;
//        private UIResponseListener<ProductDetailsResponse> uiResponseListener;
//
//        public ProductFetchTask(Context context, String productText, final UIResponseListener<ProductDetailsResponse> uiResponseListener) {
//
//            this.context = context;
//            this.productText = productText;
//            this.uiResponseListener = uiResponseListener;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            ArrayList<PostParam> postParams = new ArrayList<>();
//
//            postParams.add(new PostParam("item", productText));
//            response = PostRequest.execute(URLConstants.URL_FETCH_PRODUCT, postParams, null);
//
//            Log.d("RESPONSE", response.toString());
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            try {
//                JSONObject data = response.getJSONObject("data");
//
//                if (data.getInt("result") == 1) {
//                    ProductDetailsResponse productDetailsResponse = new Gson().fromJson(data.toString(), ProductDetailsResponse.class);
//                    uiResponseListener.onSuccess(productDetailsResponse);
//                } else {
//                    uiResponseListener.onFailure();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            uiResponseListener.onCancelled();
//        }
//    }

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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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

    public static void callFetchDealsService(Context context, final UIListener uiListener) {

        fetchDealsTask = new FetchDealsTask(context, uiListener);
        fetchDealsTask.execute();
    }

    public static class FetchDealsTask extends AsyncTask<Void, Void, Void>{

        private JSONObject response;
        private Context context;
        private UIListener uiListener;

        public FetchDealsTask(Context context, UIListener uiListener){
            this.context = context;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
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

                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("deals");

                    DatabaseHelper databaseHelper = new DatabaseHelper(context);

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
                            contentValues.put(NearByDealsDataModel.VOUCHER_ID, jsonObject.getString("deal_id"));
                        }else{
                            contentValues.put(NearByDealsDataModel.VOUCHER_ID, "");
                        }
                        contentValues.put(NearByDealsDataModel.SHOP_NAME, jsonObject.getString("store_name"));
                        contentValues.put(NearByDealsDataModel.SHOP_PHONE, jsonObject.getString("store_phone"));
                        contentValues.put(NearByDealsDataModel.SHOP_LOCATION, jsonObject.getString("store_location"));
                        contentValues.put(NearByDealsDataModel.SHOP_ADDRESS, jsonObject.getString("store_address"));
                        contentValues.put(NearByDealsDataModel.SHOP_LATITUDE, jsonObject.getString("store_latitude"));
                        contentValues.put(NearByDealsDataModel.SHOP_LONGITUDE, jsonObject.getString("store_longitude"));

                        databaseHelper.addDeal(contentValues);
                    }
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

//    private static boolean checkUniqueDeal(ArrayList<NearByDealsDataModel> nearByDealsDataModels, NearByDealsDataModel nearByDealsDataModel) {
//        for (int i = 0; i < nearByDealsDataModels.size(); i++) {
//            if (nearByDealsDataModel.getDealId().equalsIgnoreCase(nearByDealsDataModels.get(i).getDealId())) {
//                return false;
//            }
//        }
//        return true;
//    }

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
            return true;
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
            return true;
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

    public static void callPasswordVerificationService(Context context, String number, String verificationCode, final UIListener uiListener) {

        PasswordVerificationTask passwordVerificationTask = new PasswordVerificationTask(context,number,verificationCode,uiListener);
        passwordVerificationTask.execute();
    }

    public static class PasswordVerificationTask extends AsyncTask<Void,Void,Void>{

        private JSONObject response;
        private String number,verificationCode;
        private UIListener uiListener;

        public PasswordVerificationTask(Context context, String number, String verificationCode, final UIListener uiListener){
            this.number = number;
            this.verificationCode = verificationCode;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("mobile",number));
            postParams.add(new PostParam("verif",verificationCode));

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

    public static void callPasswordChangeService(Context context, String mobileNumber, String password, final UIListener uiListener) {

        ChangePasswordTask changePasswordTask = new ChangePasswordTask(context,mobileNumber,password,uiListener);
        changePasswordTask.execute();
    }

    public static class ChangePasswordTask extends AsyncTask<Void,Void,Void> {

        private JSONObject response;
        private String mobileNumber;
        private String password;
        private UIListener uiListener;

        public ChangePasswordTask(Context context, String mobileNumber, String password, final UIListener uiListener){
            this.mobileNumber = mobileNumber;
            this.password = password;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("mobile",mobileNumber));
            postParams.add(new PostParam("npass",password));

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

    public static void callUploadContactsService(Context context, String mobileNumber, ArrayList<String> contactList, final UIListener uiListener) {

        UpLoadContactsTask upLoadContactsTask = new UpLoadContactsTask(context,mobileNumber,contactList,uiListener);
        upLoadContactsTask.execute();
    }

    public static class UpLoadContactsTask extends AsyncTask<Void,Void,Void> {

        private JSONObject response;
        private String mobileNumber;
        private ArrayList<String> contactList;
        private UIListener uiListener;

        public UpLoadContactsTask(Context context, String mobileNumber, ArrayList<String> contactList, final UIListener uiListener){
            this.mobileNumber = mobileNumber;
            this.contactList = contactList;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("mobile",mobileNumber));
            postParams.add(new PostParam("contacts",contactList.toString()));

            response = PostRequest.execute(URLConstants.URL_UPLOAD_CONTACTS,postParams,null);
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

    public static void callGetVoucherIdService(Context context, String dealId, final UIResponseListener<DealsVoucherResponse> uiListener) {

        GetVoucherIdTask getVoucherIdTask = new GetVoucherIdTask(context, dealId, uiListener);
        getVoucherIdTask.execute();
    }

    public static class GetVoucherIdTask extends AsyncTask<Void,Void,Void> {

        private Context context;
        private JSONObject response;
        private String dealId;
        private UIResponseListener<DealsVoucherResponse> uiListener;

        public GetVoucherIdTask(Context context, String dealId, final UIResponseListener<DealsVoucherResponse> uiListener){
            this.context = context;
            this.dealId = dealId;
            this.uiListener = uiListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<PostParam> postParams = new ArrayList<>();
            postParams.add(new PostParam("mobile", UserProfile.getPhone(context)));
            postParams.add(new PostParam("token", UserProfile.getToken(context)));
            postParams.add(new PostParam("deal_id", dealId));

            response = PostRequest.execute(URLConstants.URL_VOUCHER_ID,postParams,null);
            Log.d("RESPONSE", response.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if(response.getJSONObject("data").getInt("result") == 1){
                    DealsVoucherResponse dealsVoucherResponse = new DealsVoucherResponse();
                    dealsVoucherResponse.setVoucherID(response.getJSONObject("data").getString("voucher_id"));
                    uiListener.onSuccess(dealsVoucherResponse);
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

//    public static void callFetchDealsService(Context context, int dealCategory, final UIListener uiListener) {
//
//        fetchDealsTask = new FetchDealsTask(context, dealCategory, uiListener);
//        fetchDealsTask.execute();
//    }
//
//    public static class FetchDealsTask extends AsyncTask<Void, Void, Void>{
//
//        private ArrayList<NearByDealsDataModel> totalDeals = new ArrayList<>();
//        private ArrayList<NearByDealsDataModel> shoppingDeals = new ArrayList<>();
//        private ArrayList<NearByDealsDataModel> foodDeals = new ArrayList<>();
//        private ArrayList<NearByDealsDataModel> servicesDeals = new ArrayList<>();
//        private JSONObject response;
//        private Context context;
//        private UIListener uiListener;
//        private int dealCategory;
//
//        public FetchDealsTask(Context context, int dealCategory, UIListener uiListener){
//            this.context = context;
//            this.dealCategory = dealCategory;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayList<PostParam> postParams = new ArrayList<>();
//
//            postParams.add(new PostParam("mobile", UserProfile.getPhone(context)));
//            postParams.add(new PostParam("token", UserProfile.getToken(context)));
//
//            response = PostRequest.execute(URLConstants.URL_NEARBY_DEALS, postParams, null);
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            try {
//                if (response.getJSONObject("data").getInt("result") == 1) {
//
//                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("deals");
//
//                    ArrayList<NearByDealsDataModel> nearByDealsDataModelList = new ArrayList<>();
//
//                    for(int i = 0; i < jsonArray.length(); i++){
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//
//                        NearByDealsDataModel nearByDealsDataModel = new NearByDealsDataModel();
//
//                        nearByDealsDataModel.setDealId(jsonObject.getString("deal_id"));
//                        nearByDealsDataModel.setDealsCategory(jsonObject.getInt("deal_category"));
//                        nearByDealsDataModel.setDealsType(jsonObject.getInt("deal_type"));
//                        nearByDealsDataModel.setShopName(jsonObject.getString("store_name"));
//                        nearByDealsDataModel.setShopLocation(jsonObject.getString("store_location"));
//                        nearByDealsDataModel.setShopPhone(jsonObject.getString("store_phone"));
//                        nearByDealsDataModel.setShopLatitude(jsonObject.getString("store_latitude"));
//                        nearByDealsDataModel.setShopLongitude(jsonObject.getString("store_longitude"));
//                        nearByDealsDataModel.setImageUrl(jsonObject.getString("image"));
//                        nearByDealsDataModel.setItemHeading(jsonObject.getString("deal_heading"));
//                        nearByDealsDataModel.setItemDescription(jsonObject.getString("deal_description"));
//                        nearByDealsDataModel.setValidTo(jsonObject.getString("deal_validity"));
//                        nearByDealsDataModel.setActivated(jsonObject.getInt("activated"));
//
//                        if(jsonObject.getString("activated").equalsIgnoreCase(1 + "")){
//                            nearByDealsDataModel.setVoucherId(jsonObject.getString("voucher_id"));
//                        }
//
//                        nearByDealsDataModelList.add(nearByDealsDataModel);
//                    }
//
//                    totalDeals = nearByDealsDataModelList;
//
//                    for (int i = 0; i < totalDeals.size(); i++) {
//
//                        switch (totalDeals.get(i).getDealsCategory()) {
//                            case NearByDealsConstants.SHOPPING:
//                                if (checkUniqueDeal(shoppingDeals, totalDeals.get(i))) {
//                                    shoppingDeals.add(totalDeals.get(i));
//                                }
//
//                                break;
//                            case NearByDealsConstants.FOOD:
//                                if (checkUniqueDeal(foodDeals, totalDeals.get(i))) {
//                                    foodDeals.add(totalDeals.get(i));
//                                }
//                                break;
//                            case NearByDealsConstants.SERVICES:
//                                if (checkUniqueDeal(servicesDeals, totalDeals.get(i))) {
//                                    servicesDeals.add(totalDeals.get(i));
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//
//                    if(dealCategory == IEventConstants.EVENT_SHOPPING_DEALS_NEARBY){
//                        Utils.postEvent(TAB, IEventConstants.EVENT_SHOPPING_DEALS_NEARBY, shoppingDeals);
//                    }else if(dealCategory == IEventConstants.EVENT_FOOD_DEALS_NEARBY){
//                        Utils.postEvent(TAB, IEventConstants.EVENT_FOOD_DEALS_NEARBY, foodDeals);
//                    }else if(dealCategory == IEventConstants.EVENT_SERVICES_DEALS_NEARBY){
//                        Utils.postEvent(TAB, IEventConstants.EVENT_SERVICES_DEALS_NEARBY, servicesDeals);
//                    }
//                }else{
//                    uiListener.onFailure();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//    }
//
//    private static boolean checkUniqueDeal(ArrayList<NearByDealsDataModel> nearByDealsDataModels, NearByDealsDataModel nearByDealsDataModel) {
//        for (int i = 0; i < nearByDealsDataModels.size(); i++) {
//            if (nearByDealsDataModel.getDealId().equalsIgnoreCase(nearByDealsDataModels.get(i).getDealId())) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static void callVerificationService(Context context, String verificationCode, String mobileNumber, String token, final UIListener uiListener) {
//
//        accountVerificationTask = new AccountVerificationTask(context, verificationCode, mobileNumber, token, uiListener);
//        accountVerificationTask.execute();
//    }
//
//    public static class AccountVerificationTask extends AsyncTask<Void, Void, Boolean> {
//
//        private JSONObject response;
//        private Context context;
//        private String verificationCode, mobileNumber, token;
//        private UIListener uiListener;
//
//        public AccountVerificationTask(Context context, String verificationCode, String mobileNumber, String token, final UIListener uiListener) {
//            this.context = context;
//            this.verificationCode = verificationCode;
//            this.mobileNumber = mobileNumber;
//            this.token = token;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            ArrayList<PostParam> postParams = new ArrayList<>();
//
//            postParams.add(new PostParam("otp_in", verificationCode));
//            postParams.add(new PostParam("mobile", mobileNumber));
//            postParams.add(new PostParam("token", token));
//
//            response = PostRequest.execute(URLConstants.URL_ACCOUNT_VERIFICATION, postParams, null);
//
//            Log.d("RESPONSE", response.toString());
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//            try {
//                if (response.getJSONObject("data").getInt("result") == 1) {
//                    uiListener.onSuccess();
//                }else{
//                    uiListener.onFailure();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//    }
//
//    public static void callRetryVerificationService(Context context, String mobileNumber, String token, final UIListener uiListener) {
//
//        retryOTPTask = new RetryVerificationTask(context, mobileNumber, token, uiListener);
//        retryOTPTask.execute();
//    }
//
//    public static class RetryVerificationTask extends AsyncTask<Void, Void, Boolean> {
//
//        private JSONObject response;
//        private Context context;
//        private String mobileNumber, token;
//        private UIListener uiListener;
//
//        public RetryVerificationTask(Context context, String mobileNumber, String token, final UIListener uiListener) {
//            this.context = context;
//            this.mobileNumber = mobileNumber;
//            this.token = token;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            ArrayList<PostParam> postParams = new ArrayList<>();
//
//            postParams.add(new PostParam("mobile", mobileNumber));
//            postParams.add(new PostParam("token", token));
//
//            response = PostRequest.execute(URLConstants.URL_RETRY_VERIFICATION, postParams, null);
//
//            Log.d("RESPONSE", response.toString());
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//            try {
//                if (response.getJSONObject("data").getInt("result") == 1 && response.getJSONObject("data").getInt("eflag") == 1) {
//                    uiListener.onSuccess();
//                }else{
//                    uiListener.onFailure();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//    }
//
//    public static void callForgotPasswordService(Context context, String mobileNumber, final UIListener uiListener) {
//
//        ForgotPasswordTask forgotPasswordTask = new ForgotPasswordTask(context, mobileNumber, uiListener);
//        forgotPasswordTask.execute();
//    }
//
//    public static class ForgotPasswordTask extends AsyncTask<Void, Void, Void>{
//
//        private JSONObject response;
//        private String mobileNumber;
//        private Context context;
//        private UIListener uiListener;
//
//        public ForgotPasswordTask(Context context, String mobileNumber, final UIListener uiListener){
//            this.context = context;
//            this.mobileNumber = mobileNumber;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            ArrayList<PostParam> postParams = new ArrayList<>();
//            postParams.add(new PostParam("mobile",mobileNumber));
//
//            response = PostRequest.execute(URLConstants.URL_FORGOT_PASSWORD,postParams,null);
//            Log.d("RESPONSE", response.toString());
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            try{
//                if(response.getJSONObject("data").getInt("result") == 1){
//                    if(response.getJSONObject("data").getInt("eflag") == 0){
//                        uiListener.onFailure(0);
//                    }else{
//                        uiListener.onSuccess();
//                    }
//                }else if(response.getJSONObject("data").getInt("result") == 0){
//                    uiListener.onFailure();
//                }else{
//                    uiListener.onConnectionError();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            uiListener.onCancelled();
//        }
//    }
//
//    public static void callPasswordVerificationService(Context context, String number, String verificationCode, final UIListener uiListener) {
//
//        PasswordVerificationTask passwordVerificationTask = new PasswordVerificationTask(context,number,verificationCode,uiListener);
//        passwordVerificationTask.execute();
//    }
//
//    public static class PasswordVerificationTask extends AsyncTask<Void,Void,Void>{
//
//        private JSONObject response;
//        private String number,verificationCode;
//        private UIListener uiListener;
//
//        public PasswordVerificationTask(Context context, String number, String verificationCode, final UIListener uiListener){
//            this.number = number;
//            this.verificationCode = verificationCode;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            ArrayList<PostParam> postParams = new ArrayList<>();
//            postParams.add(new PostParam("mobile",number));
//            postParams.add(new PostParam("verif",verificationCode));
//
//            response = PostRequest.execute(URLConstants.URL_PASSWORD_VERIFICATION,postParams,null);
//            Log.d("RESPONSE", response.toString());
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            try{
//                if(response.getJSONObject("data").getInt("result") == 1){
//                    uiListener.onSuccess();
//                }else if(response.getJSONObject("data").getInt("result") == 0){
//                    uiListener.onFailure();
//                }else{
//                    uiListener.onConnectionError();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            uiListener.onCancelled();
//        }
//    }
//
//    public static void callPasswordChangeService(Context context, String mobileNumber, String password, final UIListener uiListener) {
//
//        ChangePasswordTask changePasswordTask = new ChangePasswordTask(context,mobileNumber,password,uiListener);
//        changePasswordTask.execute();
//    }
//
//    public static class ChangePasswordTask extends AsyncTask<Void,Void,Void> {
//
//        private JSONObject response;
//        private String mobileNumber;
//        private String password;
//        private UIListener uiListener;
//
//        public ChangePasswordTask(Context context, String mobileNumber, String password, final UIListener uiListener){
//            this.mobileNumber = mobileNumber;
//            this.password = password;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            ArrayList<PostParam> postParams = new ArrayList<>();
//            postParams.add(new PostParam("mobile",mobileNumber));
//            postParams.add(new PostParam("npass",password));
//
//            response = PostRequest.execute(URLConstants.URL_PASSWORD_CHANGE,postParams,null);
//            Log.d("RESPONSE", response.toString());
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            try{
//                if(response.getJSONObject("data").getInt("result") == 1){
//                    uiListener.onSuccess();
//                }else{
//                    uiListener.onFailure();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            uiListener.onConnectionError();
//        }
//    }
//
//    public static void callUploadContactsService(Context context, String mobileNumber, ArrayList<String> contactList, final UIListener uiListener) {
//
//        UpLoadContactsTask upLoadContactsTask = new UpLoadContactsTask(context,mobileNumber,contactList,uiListener);
//        upLoadContactsTask.execute();
//    }
//
//    public static class UpLoadContactsTask extends AsyncTask<Void,Void,Void> {
//
//        private JSONObject response;
//        private String mobileNumber;
//        private ArrayList<String> contactList;
//        private UIListener uiListener;
//
//        public UpLoadContactsTask(Context context, String mobileNumber, ArrayList<String> contactList, final UIListener uiListener){
//            this.mobileNumber = mobileNumber;
//            this.contactList = contactList;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            ArrayList<PostParam> postParams = new ArrayList<>();
//            postParams.add(new PostParam("mobile",mobileNumber));
//            postParams.add(new PostParam("contacts",contactList.toString()));
//
//            response = PostRequest.execute(URLConstants.URL_UPLOAD_CONTACTS,postParams,null);
//            Log.d("RESPONSE", response.toString());
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            try{
//                if(response.getJSONObject("data").getInt("result") == 1){
//                    uiListener.onSuccess();
//                }else{
//                    uiListener.onFailure();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            uiListener.onConnectionError();
//        }
//    }
//
//    public static void callGetVoucherIdService(Context context, String dealId, final UIResponseListener<DealsVoucherResponse> uiListener) {
//
//        GetVoucherIdTask getVoucherIdTask = new GetVoucherIdTask(context, dealId, uiListener);
//        getVoucherIdTask.execute();
//    }
//
//    public static class GetVoucherIdTask extends AsyncTask<Void,Void,Void> {
//
//        private Context context;
//        private JSONObject response;
//        private String dealId;
//        private UIResponseListener<DealsVoucherResponse> uiListener;
//
//        public GetVoucherIdTask(Context context, String dealId, final UIResponseListener<DealsVoucherResponse> uiListener){
//            this.context = context;
//            this.dealId = dealId;
//            this.uiListener = uiListener;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            ArrayList<PostParam> postParams = new ArrayList<>();
//            postParams.add(new PostParam("mobile", UserProfile.getPhone(context)));
//            postParams.add(new PostParam("token", UserProfile.getToken(context)));
//            postParams.add(new PostParam("deal_id", dealId));
//
//            response = PostRequest.execute(URLConstants.URL_VOUCHER_ID,postParams,null);
//            Log.d("RESPONSE", response.toString());
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            try{
//                if(response.getJSONObject("data").getInt("result") == 1){
//                    DealsVoucherResponse dealsVoucherResponse = new DealsVoucherResponse();
//                    dealsVoucherResponse.setVoucherID(response.getJSONObject("data").getString("voucher_id"));
//                    uiListener.onSuccess(dealsVoucherResponse);
//                }else{
//                    uiListener.onFailure();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                uiListener.onConnectionError();
//            }
//        }
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            uiListener.onConnectionError();
//        }
//    }
}
