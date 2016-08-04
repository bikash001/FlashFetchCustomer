package com.buyer.flashfetch.CommonUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.LoginActivity;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.Services.IE_RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by KRANTHI on 05-06-2016.
 */
public class Utils {

//    private static EventBus eventBus;

    public static boolean isValidEmail(String emailText) {
        if (emailText == null) {
            return false;
        } else {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(emailText).matches();
        }
    }

    public static boolean isValidPassword(String password) {
        if (password.length() > 4) {
            return true;
        }
        return false;
    }

    public static boolean checkSamePassword(String newPassword, String confirmPassword) {
        if (newPassword.equals(confirmPassword)) {
            return true;
        }
        return false;
    }

    public static int[] generatePrimeNumbers(int noOfPrimeNumbers) {
        int a = noOfPrimeNumbers;
        int x = 1;
        int currentNumber = 0;
        boolean isPrimeNumber = false;
        int[] list = new int[a];
        list[0] = 2;
        for (int i = 1; i < 38; i++) {
            for (int j = i - 1; j > 1; j--) {
                if (i % j != 0) {
                    isPrimeNumber = true;
                    currentNumber = i;
                } else {
                    isPrimeNumber = false;
                    break;
                }

            }
            if (isPrimeNumber) {
                if (x < a) {
                    list[x] = currentNumber;
                    x++;
                }
            }

            if(x == 10){
                break;
            }
        }
        return list;
    }

    public static void startPlayServices(Activity activity) {
        if (Utils.checkPlayServices(activity)) {
            Intent intent = new Intent(activity, IE_RegistrationIntentService.class);
            activity.startService(intent);
        } else {
            activity.finish();
        }
    }

    public static boolean checkPlayServices(Activity activity) {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
                GoogleApiAvailability.getInstance().getErrorDialog(activity, resultCode,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("tag", "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public static boolean isInternetAvailable(Context ctx) {
        boolean lRetVal = false;

        try {
            ConnectivityManager cm = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                if (null != nInfo) {
                    lRetVal = nInfo.isConnectedOrConnecting();
                }
            }
        } catch (Exception e) {
            return lRetVal;
        }

        return lRetVal;
    }

    public static boolean isSelectedDateGreater(Date startDate, Date endDate) {
        if (endDate.after(startDate)) {
            return true;
        }
        return false;
    }

    public static boolean checkPhoneNumberLength(String phoneNumber) {
        if (phoneNumber.length() == 10) {
            return true;
        }
        return false;
    }

    public static boolean validatePhoneNumberUpdate(String previousText, String updatedText) {
        if (!previousText.equals(updatedText)) {
            return true;
        }
        return false;
    }

    public static void doLogout(final Activity activity, final UIListener uiListener) {
        final AlertDialog alertDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uiListener.onSuccess();
                logout(activity);
                uiListener.onCancelled();
                Toast.makeText(activity, "Successfully Logged out", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private static void logout(Activity activity) {
        clearApplicationData(activity);

        SharedPreferences prefs = activity.getSharedPreferences("sharedPreferences", 0);
        prefs.edit().putString("delete", "hellothisisacheck").apply();

        Log.d("delete", prefs.getString("delete", "nope"));

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        UserProfile.clear(activity);
        Log.d("delete", prefs.getString("delete", "nope"));

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(activity.getBaseContext());
        try {
            gcm.unregister();
        } catch (IOException e) {
            System.out.println("Error Message: " + e.getMessage());
        }

        Intent i = new Intent(activity, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(i);
        activity.finish();
    }

    private static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

//    private static void initEventBus(){
//        if (eventBus == null) {
//            eventBus = EventBus.getDefault();
//        }
//    }
//
//    public static void registerEventBus(Object receiver){
//        initEventBus();
//        eventBus.register(receiver);
//    }
//
//    public static void unregisterEventBus(Object receiver){
//        initEventBus();
//        eventBus.unregister(receiver);
//    }
//
//    public static void postEvent(Object event){
//        initEventBus();
//        eventBus.post(event);
//    }
//
//    public static void postStickyEvent(Object event){
//        initEventBus();
//        eventBus.postSticky(event);
//    }
//
//    public static void registerSticky(Object receiver){
//        initEventBus();
//        eventBus.register(receiver);
//    }
//
//    public static void postEvent(String eventName, int eventId, Object object){
//        IEvent.Builder builder = new IEvent.Builder();
//        builder.setEventID(eventId);
//        builder.setEventName(eventName);
//        builder.setEventObject(object);
//        postEvent(builder.build());
//    }
}
