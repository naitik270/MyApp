package com.demo.nspl.restaurantlite.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.AdapterRestoreItem;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsGetBackupDetailsList;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsGetBackupDetailsParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceGetBackupDetails;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class ActivityRestoreListNew extends AppCompatActivity {

    RecyclerView rv_restore;
    TextView txt_nodata;
    String _customerId = "";
    Toolbar toolbar;
    List<ClsGetBackupDetailsList> lsClsGetBackupDetailsLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_list);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityRestoreList"));
        }

        ClsPermission.checkpermission(ActivityRestoreListNew.this);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        main();
    }

    private void main() {

        txt_nodata = findViewById(R.id.txt_nodata);
        rv_restore = findViewById(R.id.rv_restore);

        _customerId = getIntent().getStringExtra("_customerId");

        Log.e("_customerId", _customerId);
        getBackupDetailsListAPI();
    }

    void getBackupDetailsListAPI() {
        InterfaceGetBackupDetails interfaceGetBackupDetails =
                ApiClient.getRetrofitInstance().create(InterfaceGetBackupDetails.class);

        Call<ClsGetBackupDetailsParams> obj = interfaceGetBackupDetails.value(_customerId, "Document", ClsGlobal.AppName);

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityRestoreListNew.this, "Loading...", true);
        pd.show();

        obj.enqueue(new Callback<ClsGetBackupDetailsParams>() {
            @Override
            public void onResponse(Call<ClsGetBackupDetailsParams> call, Response<ClsGetBackupDetailsParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String success = response.body().getSuccess();
                    Log.e("--success--", "HHTRequestReport:-- " + success);
                    if (success.equals("0")) {
                        Toast.makeText(ActivityRestoreListNew.this, "No record found", Toast.LENGTH_LONG).show();
                    } else {
                        pd.dismiss();

                        lsClsGetBackupDetailsLists = new ArrayList<>();
                        lsClsGetBackupDetailsLists = response.body().getData();
                        if (lsClsGetBackupDetailsLists != null && lsClsGetBackupDetailsLists.size() != 0) {
                            txt_nodata.setVisibility(View.GONE);

                            AdapterRestoreItem adapter = new AdapterRestoreItem(ActivityRestoreListNew.this, lsClsGetBackupDetailsLists);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityRestoreListNew.this);

                            rv_restore.setLayoutManager(layoutManager);
                            rv_restore.setAdapter(adapter);

                            adapter.SetOnClickListener((clsGetBackupDetailsList, position) -> {

                                Log.e("onClick", "onClick call");
                                AlertDialog alertDialog = new AlertDialog.Builder(ActivityRestoreListNew.this,
                                        R.style.AppCompatAlertDialogStyle).create(); //Read Update.
                                alertDialog.setTitle("Confirmation");
                                alertDialog.setMessage("sure to start restore?");
                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Log.e("onClick", "onClick call");
                                        // Toast.makeText(context, "Id: " + i, Toast.LENGTH_SHORT).show();
                                        String url = clsGetBackupDetailsList.getFileUrl();
                                        Log.e("url", url);

                                        new DownloadFileFromURL().execute(url);


                                    }
                                });


                                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.setCancelable(false);
                                alertDialog.show();

                            });

                        } else {
                            txt_nodata.setVisibility(View.VISIBLE);
                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsGetBackupDetailsParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private ProgressDialog pDialog;


    public static final int progress_bar_type = 0;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream


                File _saveLocation = Environment.getExternalStorageDirectory();
                Log.d("camera", "filepath:- " + _saveLocation);

                Log.e("--TakeBackUp--", "Step:2");

                File dir = new File(_saveLocation.getAbsolutePath());
                Log.d("generateBackupFile", "dir:- " + dir);

                Log.e("--TakeBackUp--", "Step:3");


                if (!dir.exists()) {

                    Log.e("--TakeBackUp--", "Step:4");
                    dir.mkdirs();

                }
                Log.e("--TakeBackUp--", "Step:5");

                OutputStream output = new FileOutputStream(dir.getAbsolutePath().concat("/FileDownLoadDemo.zip"));
                Log.e("--TakeBackUp--", "Step:6");
                Log.e("--TakeBackUp--", "Step:7: " + output);

                byte data[] = new byte[1024];
                Log.e("--TakeBackUp--", "Step:8");
                long total = 0;
                Log.e("--TakeBackUp--", "Step:9");

                while ((count = input.read(data)) != -1) {
                    Log.e("--TakeBackUp--", "Step:10");

                    total += count;


                    Log.e("--TakeBackUp--", "Step:11");
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    Log.e("--TakeBackUp--", "Step:12");

                    // writing data to file
                    output.write(data, 0, count);

                    Log.e("--TakeBackUp--", "Step:13");

                }
                Log.e("--TakeBackUp--", "Step:14");
                // flushing output
                output.flush();
                Log.e("--TakeBackUp--", "Step:15");
                // closing streams
                output.close();
                Log.e("--TakeBackUp--", "Step:16");

                input.close();

                Log.e("--TakeBackUp--", "Step:17");

            } catch (Exception e) {

                Log.e("--TakeBackUp--", "Step:18");
                Log.e("Error: ", e.getMessage());
            }
            Log.e("--TakeBackUp--", "Step:19");
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
//            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
//            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults,
                        ActivityRestoreListNew.this);
                return;
            }

        }

    }

}
