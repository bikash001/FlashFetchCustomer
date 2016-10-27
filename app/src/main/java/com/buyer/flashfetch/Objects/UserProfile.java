package com.buyer.flashfetch.Objects;

import android.content.Context;
import android.content.SharedPreferences;


public class UserProfile {

    public static final String SHARED_PREFERENCES = "flash_fetch_buyer";

    private static final String USER_NAME = "name";
    private static final String USER_PHONE = "phone";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "email";
    private static final String USER_TOKEN = "token";
    private static final String GCM_TOKEN = "gcm-token";
    private static final String USER_REFERRAL_CODE= "referral_code";
    private static final String SENT_OTP_VERIFICATION= "otp_verification";
    private static final String USER_ACCOUNT_VERIFIED= "account_verified";
    private static final String NO_OF_USER_VISITS = "visits";
    private static final String USER_CONTACTS_RETRIEVED = "contacts_retrieved";

    public static void setName(String name, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, name);
        editor.commit();
    }

    public static String getName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(USER_NAME, "");
    }

    public static void setPhone(String phone, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_PHONE, phone);
        editor.commit();
    }

    public static String getPhone(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(USER_PHONE, "");
    }

    public static void setEmail(String email,Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_EMAIL,email);
        editor.commit();
    }

    public static String getEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(USER_EMAIL, "");
    }

    public static void setPassword(String password, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_PASSWORD, password);
        editor.commit();
    }

    public static String getPassword(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(USER_PASSWORD, "");
    }

    public static void setToken(String token, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(USER_TOKEN, "");
    }

    public static void setReferralCode(Context context, String referralCode){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_REFERRAL_CODE, referralCode);
        editor.commit();
    }

    public static String getReferralCode(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(USER_REFERRAL_CODE, "");
    }

    public static void sentVerificationOTP(boolean status, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SENT_OTP_VERIFICATION, status);
        editor.commit();
    }

    public static void setAccountVerified(Context context, boolean isVerified){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_ACCOUNT_VERIFIED,isVerified);
        editor.commit();
    }

    public static boolean isAccountVerified(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(USER_ACCOUNT_VERIFIED, false);
    }

    public static void setVisits(int visits, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NO_OF_USER_VISITS, visits);
        editor.commit();
    }

    public static int getVisits(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getInt(NO_OF_USER_VISITS, 0);
    }

    public static void setContactsRetrieved(boolean retrieved, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_CONTACTS_RETRIEVED, retrieved);
        editor.commit();
    }

    public static boolean isContactsRetrieved(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(USER_CONTACTS_RETRIEVED, false);
    }

    public static void setGCMToken(String gcmToken, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GCM_TOKEN, gcmToken);
        editor.commit();
    }

    public static String getGcmToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(GCM_TOKEN, "");
    }

    public static boolean clear(Context context){
        setName("", context);
        setEmail("", context);
        setPhone("", context);
        setToken("", context);
        setVisits(0, context);
        return true;
    }


}


