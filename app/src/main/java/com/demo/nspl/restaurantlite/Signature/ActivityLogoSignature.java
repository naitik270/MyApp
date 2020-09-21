package com.demo.nspl.restaurantlite.Signature;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


public class ActivityLogoSignature extends AppCompatActivity {


    Bitmap bmp;
    Boolean _Selected = false;
    ImageView iv_doc_file;
    Toolbar toolbar;
    Button btn_browse, btn_sign, mClear, mGetSign, mCancel, btn_reset_signature;
    String _customerId = "";
    Dialog dialog;
    LinearLayout mContent;
    signature mSignature;
    View view;

    ImageView iv_signature;
    Button btn_reset_logo;
    Button iv_done;
    Button iv_set_bill_title;
    EditText edt_signatory_name;
    EditText edt_bill_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_signature);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityLogoSignature"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        main();


    }

    private void main() {
        Intent intent = getIntent();
        _customerId = intent.getStringExtra("_customerId");


        btn_browse = findViewById(R.id.btn_browse);
        btn_reset_signature = findViewById(R.id.btn_reset_signature);
        btn_reset_logo = findViewById(R.id.btn_reset_logo);
        btn_sign = findViewById(R.id.btn_sign);
        iv_doc_file = findViewById(R.id.iv_doc_file);
        iv_signature = findViewById(R.id.iv_signature);
        iv_done = findViewById(R.id.iv_done);
        edt_signatory_name = findViewById(R.id.edt_signatory_name);
        edt_bill_title = findViewById(R.id.edt_bill_title);
        iv_set_bill_title = findViewById(R.id.iv_set_bill_title);


        // Dialog Function
        dialog = new Dialog(ActivityLogoSignature.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(false);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);


        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_signatory_name.getText() == null || edt_signatory_name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter name.", Toast.LENGTH_SHORT).show();
                    edt_signatory_name.requestFocus();
                } else {
                    ClsGlobal.setSignatoryName(edt_signatory_name.getText().toString(), getApplicationContext());
                    setSignatoryName();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_set_bill_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_bill_title.getText() == null || edt_bill_title.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter bill title.", Toast.LENGTH_SHORT).show();
                    edt_bill_title.requestFocus();
                } else {
                    ClsGlobal.setBillTitle(edt_bill_title.getText().toString(), getApplicationContext());
                    setBillTitle();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseImage();
            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_action();
            }
        });

        btn_reset_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLogoAlert("LOGO");
            }
        });

        btn_reset_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLogoAlert("SIGNATURE");
            }
        });


        setLogo();
        setSignature();
        setSignatoryName();
        setBillTitle();


    }


    void setSignatoryName() {

        ClsUserInfo objSignatoryName = ClsGlobal.getUserInfo(getApplicationContext());
        edt_signatory_name.setText(objSignatoryName.getCustomerSignatory());

    }

    void setBillTitle() {

        ClsUserInfo objSignatoryName = ClsGlobal.getUserInfo(getApplicationContext());
        edt_bill_title.setText(objSignatoryName.getBillTitle());

    }

    void resetLogoAlert(String mode) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.message_logout_prompt, null);

        TextView tvMessage = alertLayout.findViewById(R.id.tvPromptMessage);

        AlertDialog alertDialog = new AlertDialog.Builder(ActivityLogoSignature.this,
                R.style.AppCompatAlertDialogStyle).create();
        alertDialog.setView(alertLayout);
        alertDialog.setTitle("Confirmation");
        tvMessage.setText("ARE YOU SURE TO DELETE OR REPLACE IT?");

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes",
                (dialog, which) -> {

                    if (mode.equalsIgnoreCase("LOGO")) {

                        File fdelete = new File(ClsGlobal.getLogoPath());

                        if (fdelete.exists()) {
                            fdelete.delete();
                            setLogo();
                        }
                    } else if (mode.equalsIgnoreCase("SIGNATURE")) {

                        File fdelete = new File(ClsGlobal.getSignaturePath());

                        if (fdelete.exists()) {
                            fdelete.delete();
                            setSignature();
                        }
                    }

                    dialog.cancel();
                });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    void setLogo() {

        File img = new File(ClsGlobal.getLogoPath());
        Log.v("--mGetSign--", "step-9" + img);

        if (img.exists()) {
            Log.v("--mGetSign--", "step-10");
            Bitmap bitmap = BitmapFactory.decodeFile(ClsGlobal.getLogoPath());
            Log.v("--mGetSign--", "step-11");
            iv_doc_file.setImageBitmap(bitmap);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_photo);

           /* Bitmap bitmap= BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_photo);
            iv_doc_file.setImageBitmap(bitmap);*/
            iv_doc_file.setImageDrawable(drawable);
        }
        Log.v("--mGetSign--", "step-13 ");

    }

    void setSignature() {

        File img = new File(ClsGlobal.getSignaturePath());
        if (img.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(ClsGlobal.getSignaturePath());
            iv_signature.setImageBitmap(bitmap);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_photo);
            iv_signature.setImageDrawable(drawable);
        }
    }


    // Function for Digital Signature
    public void dialog_action() {

        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code


        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mCancel = (Button) dialog.findViewById(R.id.cancel);


        view = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                dialog.setCancelable(false);
                mSignature.clear();
//                recreate();

            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.v("--mGetSign--", "step-1");
                Log.v("log_tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                Log.v("--mGetSign--", "step-2");

                File _saveLocation = Environment.getExternalStorageDirectory();
                File dir = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Signature/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String StoredPath = dir.getAbsolutePath() + "/Signature" + ".png";
                mSignature.save(view, StoredPath);
                dialog.dismiss();

                Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                setSignature();
                mSignature.clear();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");

                // Calling the same class
                mSignature.clear();
                dialog.dismiss();
                recreate();
            }
        });
        dialog.show();
    }

    private static final int PICK_FROM_FILE = 2;

    public void browseImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setType("image/*");
        intent.putExtra("return-data", false);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_FILE);
    }

    void saveLogo(Bitmap _bmp) {

        File _saveLocation = Environment.getExternalStorageDirectory();

        File dir = new File(_saveLocation.getAbsolutePath() + "/"
                + ClsGlobal.AppFolderName + "/Logo");

        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fname = "Logo.png";
        File file = new File(dir.getAbsolutePath(), fname);

        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            _bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_FILE && resultCode == RESULT_OK && data != null) {
            Uri _SelectedFileUri = data.getData();
            _Selected = true;
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), _SelectedFileUri);
                saveLogo(bmp);
                setLogo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Event Call", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
