package com.buyer.flashfetch.Objects;

import android.content.Context;
import android.content.SharedPreferences;


public class UserProfile {

    private static final String SHARED_PREFERENCES = "flash_fetch_buyer";

    public static String name,email,token,text;
    public static int category;

    public static void setName(String name, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static void setEmail(String email,Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",email);
        editor.commit();
    }

    public static void setPassword(String password, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", password);
        editor.commit();
    }

    public static void setToken(String token, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static void sentVerificationEmail(boolean status, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("email_verification", status);
        editor.commit();
    }

    public static void setPhone(String phone, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone", phone);
        editor.commit();
    }

    public static void setVisits(int visits, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("visits", visits);
        editor.commit();
    }

    public static void setShopName(String shopName, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("shopname", shopName);
        editor.commit();
    }

    public static void setShopId(String shopId, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("shopid", shopId);
        editor.commit();
    }

    public static void setShopPhone(String shopPhone, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("shopphone", shopPhone);
        editor.commit();
    }

    public static void setAddress1(String address1, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address1",address1);
        editor.commit();
    }

    public static void setAddress2(String address2, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address2", address2);
        editor.commit();
    }

    public static void setLocation(String location, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("location", location);
        editor.commit();
    }

    public static void setCategory(int category, Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("category", category);
        editor.commit();
    }

    public static int getVisits(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getInt("visits", 0);
    }

    public static String getName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("name", "");
    }

    public static String getEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("email", "");
    }

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("token", "");
    }

    public static String getPhone(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("phone", "");
    }

    public static String getPassword(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("password", "");
    }

    public static String getShopId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("shopid", "");
    }

    public static String getShopPhone(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("shopphone", "");
    }

    public static String getAddress1(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("address1", "");
    }

    public static String getAddress2(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("address2", "");
    }

    public static String getLocation(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("location", "");
    }

    public static String getShopName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("shopname", "");
    }

    public static int getCategory(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getInt("category", 1);
    }

  /*  public static String getText(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString("text", "Thank you for registering with us. Buyers coming soon");
    }

    public static int getCounter(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getInt("counter", 0);
    }

    public static int getUpdate(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getInt("update", 0);
    }*/
    public static int clear(Context context){
        setName("", context);
        setEmail("", context);
        setToken("", context);
        setCategory(1,context);
        return 1;
    }


}


