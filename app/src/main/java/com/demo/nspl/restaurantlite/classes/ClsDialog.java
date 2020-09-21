package com.demo.nspl.restaurantlite.classes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.demo.nspl.restaurantlite.R;

import java.util.Random;

public class ClsDialog {

    static String _Result = "";

    public static long generateRandom(int length) {

        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    public static void error(String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true)
                .setMessage("Please Enter data")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //All of the fun happens inside the CustomListener now.
                        //I had to move it to enable data validation.
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    public static void ErrorDialog(String msgTitle, String msg, Context context1) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context1);
        builder.setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(msgTitle);
        alert.show();

    }



    public static String MasterPassword(Context c, String _Title) {


        final Dialog dialog = new Dialog(c);

//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(Html.fromHtml("<font color='#FF7F27'>" + _Title + "</font>"));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_pass_dialog);
        dialog.show();

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
//        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        final EditText edt_password = dialog.findViewById(R.id.edt_password);

//        txt_title.setText("SERVICE MASTER");
        Button btn_login = (Button) dialog.findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getTXt Box value
                // CHeck authentocate
                //set Result in result string
                _Result = edt_password.getText().toString();
                Log.d("CONTEXT VALUE", "CONTEXT sampleDialog: " + _Result);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        return _Result;
    }
}
