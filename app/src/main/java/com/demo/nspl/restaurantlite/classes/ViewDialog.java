package com.demo.nspl.restaurantlite.classes;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Desktop on 3/13/2018.
 */

public class ViewDialog {

    public void showDialog(AppCompatActivity activity, String msg) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);

        TextView text = dialog.findViewById(R.id.msg);
        text.setText(msg);


        Button btn_yes =  dialog.findViewById(R.id.btn_yes);



        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }
}
