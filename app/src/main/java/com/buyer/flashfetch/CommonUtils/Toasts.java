package com.buyer.flashfetch.CommonUtils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kranthikumar_b on 7/7/2016.
 */
public class Toasts {

    public static void internetUnavailableToast(Context context){
        Toast.makeText(context,"Internet unavailable check your network settings", Toast.LENGTH_LONG).show();
    }

    public static void validEmailToast(Context context){
        Toast.makeText(context,"Please enter a valid email address",Toast.LENGTH_SHORT).show();
    }

    public static void serverBusyToast(Context context){
        Toast.makeText(context,"Server is currently busy",Toast.LENGTH_SHORT).show();
    }

    public static void notRegisteredEmailToast(Context context){
        Toast.makeText(context,"Email is not yet registered \n" + " Click register here to create new account",Toast.LENGTH_LONG).show();
    }

    public static void successfullySentMailToast(Context context){
        Toast.makeText(context,"Successfully sent mail to the registered mail Id",Toast.LENGTH_LONG).show();
    }

    public static void emptyPasswordToast(Context context){
        Toast.makeText(context,"Password fields should not be empty",Toast.LENGTH_LONG).show();
    }

    public static void successfulPasswordChangeToast(Context context){
        Toast.makeText(context,"Password successfully changed",Toast.LENGTH_LONG).show();
    }

    public static void serverBusyForLoginToast(Context context){
        Toast.makeText(context,"Server is currently busy please login after sometime",Toast.LENGTH_LONG).show();
    }

    public static void passwordNotSameToast(Context context){
        Toast.makeText(context,"New password and confirm password are not same",Toast.LENGTH_LONG).show();
    }

    public static void serviceInterrupted(Context context){
        Toast.makeText(context,"Service Interrupted",Toast.LENGTH_LONG).show();
    }

    public static void verificationCodeSuccessfullyVerified(Context context){
        Toast.makeText(context,"Successfully Verified",Toast.LENGTH_LONG).show();
    }

    public static void enterValidVerificationCode(Context context){
        Toast.makeText(context,"Enter a valid verification code",Toast.LENGTH_LONG).show();
    }

    public static void enterVerificationCode(Context context){
        Toast.makeText(context,"Enter verification code",Toast.LENGTH_LONG).show();
    }


}
