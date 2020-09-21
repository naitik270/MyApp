package com.demo.nspl.restaurantlite.Global;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import com.demo.nspl.restaurantlite.R;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class ClsPermission {

    public static final int REQUEST_READ_PHONE_STATE = 123;

    public static void checkpermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(context,Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(context,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(context,Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED
        ) {
//                ||ContextCompat.checkSelfPermission(context,
//                Manifest.permission.READ_CALL_LOG)
//                != PackageManager.PERMISSION_GRANTED) {


            /*.permission.READ_SMS" />
mission.SEND_SMS" />
mission.RECEIVE_SMS" />-->
.permission.READ_CONTACTS" />-->
.permission.RECORD_AUDIO" />-->*/


            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.ACCESS_NETWORK_STATE
                            , Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.READ_PHONE_STATE
                            , Manifest.permission.RECEIVE_BOOT_COMPLETED
                            , Manifest.permission.ACCESS_WIFI_STATE
                            , Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.CAMERA
                            , Manifest.permission.WRITE_CONTACTS
                            , Manifest.permission.READ_CONTACTS
//                            , Manifest.permission.READ_SMS
//                            , Manifest.permission.SEND_SMS
                            , Manifest.permission.CALL_PHONE
//                            , Manifest.permission.RECORD_AUDIO
//                            , Manifest.permission.READ_CONTACTS
                    }
                    , REQUEST_READ_PHONE_STATE);
        }
    }

    private static void permissionAlert(Context context) {

        AlertDialog alertDialog = new AlertDialog.Builder(context,
                R.style.AppCompatAlertDialogStyle).create(); //Read Update.
//        alertDialog.setContentView(R.layout.activity_dialog);

        alertDialog.setTitle("Permission");
        alertDialog.setMessage("Please allow all permissions for access app.");
        alertDialog.setCancelable(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkpermission(context);
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();

    }


    public static void checkPermission(int requestCode,
                                       String permissions[], int[] grantResults, Context context) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED
                && grantResults[4] == PackageManager.PERMISSION_GRANTED
                && grantResults[5] == PackageManager.PERMISSION_GRANTED
                && grantResults[6] == PackageManager.PERMISSION_GRANTED
                && grantResults[7] == PackageManager.PERMISSION_GRANTED
                && grantResults[8] == PackageManager.PERMISSION_GRANTED
                && grantResults[9] == PackageManager.PERMISSION_GRANTED
                && grantResults[10] == PackageManager.PERMISSION_GRANTED
                && grantResults[11] == PackageManager.PERMISSION_GRANTED

//                && grantResults[10] == PackageManager.PERMISSION_GRANTED
//                && grantResults[11] == PackageManager.PERMISSION_GRANTED
//                && grantResults[12] == PackageManager.PERMISSION_GRANTED
//                && grantResults[13] == PackageManager.PERMISSION_GRANTED
        ) {
            // permission granted!
            // you may now do the action that requires this permission
        } else {
            // permission denied

            permissionAlert(context);

        }

    }

}