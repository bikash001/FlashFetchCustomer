package com.buyer.flashfetch.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.R;

/**
 * Created by kranthikumar_b on 10/17/2016.
 */

public class DialogManager {

    public static AlertDialog alertDialog, contactAlertDialog, feedbackAlertDialog;

    public static void showAlertDialog(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Service Unavailable")
                .setMessage("Service is updating and temporarily busy. Please visit after a while")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        activity.finish();
                    }
                });

        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showContactDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Contact Us")
                .setMessage("Contact us at " + Constants.CONTACT_US + " between 11 AM to 10 PM")
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactAlertDialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.CONTACT_US));
                        context.startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactAlertDialog.dismiss();
                    }
                });

        contactAlertDialog = builder.create();
        contactAlertDialog.show();
    }

    public static void showFeedbackDialog(final Activity context) {

        View view = context.getLayoutInflater().inflate(R.layout.dialog_edit_box, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Feedback")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"Thanks For your feedback", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        feedbackAlertDialog.dismiss();
                    }
                });

        builder.setView(view);

        feedbackAlertDialog = builder.create();
        feedbackAlertDialog.show();
    }
}
