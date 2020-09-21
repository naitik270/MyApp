package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.work.WorkManager;

import com.demo.nspl.restaurantlite.Adapter.AdapterRestoreItem;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsGetBackupDetailsList;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsGetBackupDetailsParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceGetBackupDetails;
import com.demo.nspl.restaurantlite.SendEmailUtility.Scheduler;
import com.demo.nspl.restaurantlite.SendEmailUtility.SharedPreferenceTime;
import com.demo.nspl.restaurantlite.backGroundTask.SendEmailTask;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFiles_from_folder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.set_current_Day_Remainder;
import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class ActivityRestoreList extends AppCompatActivity {

    RecyclerView rv_restore;
    TextView txt_nodata;
    String _customerId = "";
    Toolbar toolbar;
    List<ClsGetBackupDetailsList> lsClsGetBackupDetailsLists = new ArrayList<>();
    SharedPreferenceTime mSharedPreferenceTime;
    private AsyncTask mMyTask;
    private String restoreFileName = "", zipPath="";
    private SharedPreferences mPreferencesAutoBackUp;
    private static final String mPreferncesFileName = "AutoBackUpSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_list);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityRestoreList"));
        }

        ClsPermission.checkpermission(ActivityRestoreList.this);

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

        Call<ClsGetBackupDetailsParams> obj = interfaceGetBackupDetails.value(
                _customerId, "Document", ClsGlobal.AppName);

        Log.e("--URL--", "URL: " + obj.request().url());
        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityRestoreList.this,
                        "Loading...", true);
        pd.show();

        obj.enqueue(new Callback<ClsGetBackupDetailsParams>() {
            @Override
            public void onResponse(Call<ClsGetBackupDetailsParams> call,
                                   Response<ClsGetBackupDetailsParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String success = response.body().getSuccess();
                    Log.e("--success--", "HHTRequestReport:-- " + success);
                    if (success.equals("0")) {
                        Toast.makeText(ActivityRestoreList.this, "No record found", Toast.LENGTH_LONG).show();
                    } else {
                        pd.dismiss();

                        lsClsGetBackupDetailsLists = new ArrayList<>();
                        lsClsGetBackupDetailsLists = response.body().getData();
                        if (lsClsGetBackupDetailsLists != null
                                && lsClsGetBackupDetailsLists.size() != 0) {

                            txt_nodata.setVisibility(View.GONE);

                            AdapterRestoreItem adapter = new AdapterRestoreItem(
                                    ActivityRestoreList.this, lsClsGetBackupDetailsLists);
                            RecyclerView.LayoutManager layoutManager = new
                                    LinearLayoutManager(ActivityRestoreList.this);

                            rv_restore.setLayoutManager(layoutManager);
                            rv_restore.setAdapter(adapter);

                            adapter.SetOnClickListener((clsGetBackupDetailsList, position) -> {

                                Log.e("onClick", "onClick call");
                                AlertDialog alertDialog = new AlertDialog.Builder(ActivityRestoreList.this,
                                        R.style.AppCompatAlertDialogStyle).create(); //Read Update.
                                alertDialog.setTitle("Confirmation");
                                alertDialog.setMessage("sure to start restore?");
                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Log.e("onClick", "onClick call");
                                        // Toast.makeText(context, "Id: " + i, Toast.LENGTH_SHORT).show();
                                        String url = clsGetBackupDetailsList.getFileUrl();

                                        String[] list = url.split("/");

                                        Log.e("url", url);
                                        restoreFileName = list[list.length-1];

                                        mMyTask = new DownloadFileFromURL().execute(url);

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

    void showDialogBox(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    // Set a click listener for progress dialog cancel button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Tell the system about cancellation
                        mMyTask.cancel(true);
                        // dismiss the progress dialog
                        pDialog.dismiss();

                        Toast.makeText(ActivityRestoreList.this, "Restore canceled", Toast.LENGTH_LONG).show();


                    }
                });
                pDialog.show();
            default:

        }
    }

    void dismissDialogBox(int id) {
        pDialog.dismiss();
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialogBox(progress_bar_type);

            Log.e("--Restore--", "onPreExecute");

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            Log.e("--Restore--", "doInBackground");

            String result = "";

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Restore Backup Started (Restore From Ftouch Cloud). \n");

            // Delete SharedPreferences files from SettingsBackup folder.
            deleteAllFiles_from_folder(new File(SharedPreferencesPathBackupFolder));

            // Delete SharedPreferences files from our app folder.
            deleteAllFile(ActivityRestoreList.this);

            int count;
            try {

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Download Started (Restore From Ftouch Cloud). \n");
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

                File dir = new File(_saveLocation.getAbsolutePath().concat("/ShapBackup/"));
                Log.d("generateBackupFile", "dir:- " + dir);

                Log.e("--TakeBackUp--", "Step:3");

                if (!dir.exists()) {
                    Log.e("--TakeBackUp--", "Step:4");
                    dir.mkdirs();
                }

                Log.e("--TakeBackUp--", "Step:5");

                OutputStream output = new FileOutputStream(dir.getAbsolutePath()
                        + "/ShapBKP.zip");
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


                    // If the AsyncTask cancelled
                    if (isCancelled()) {
                        break;
                    }

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


                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                zipPath = SDPath + "/ShapBackup/ShapBKP.zip";

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Unzipping Backup file Started (Restore From Ftouch Cloud). FileName: "
                        +restoreFileName+ "\n");


                if(FileZipOperation.unzip(zipPath,
                        SDPath + "/" + ClsGlobal.AppFolderName)){
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Restore Backup Unzipping file Successfully (Restore From Ftouch Cloud). FileName: "
                            +restoreFileName+ "\n");
                }else {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Restore Backup Unzipping file Failed (Restore From Ftouch Cloud). FileName: "
                            +restoreFileName+ "\n");
                }

//                FileZipOperation.unzip(zipPath, SDPath + "/" + ClsGlobal.AppFolderName);
                Log.d("zipFilePath", "STEP--2");


//                RESTORE();

                List<String> SharedPreferences_Files_From_Folder = new ArrayList<>();

                File file = new File(SharedPreferencesPathBackupFolder);
                if (file.exists()) {
                    for (File f : file.listFiles()) {
                        if (f.isFile()) {
                            String name = f.getName();
                            Log.d("generateBackupFile", "else:- " + name);
                            SharedPreferences_Files_From_Folder.add(name);
                        }
                    }
                }

                if (SharedPreferences_Files_From_Folder.size() > 0) {
                    ClsGlobal.writeSharedPreferencesFile(SharedPreferences_Files_From_Folder,
                            SharedPreferencesPath, ActivityRestoreList.this);
                }


                result = "successfully";


            } catch (Exception e) {

                result = "failed";

                Log.e("--TakeBackUp--", "Step:18");
                Log.e("Error: ", e.getMessage());

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Download Failed (Restore From Google Drive)." +
                         " \n" + " Error: " + e.getMessage() +"\n");

            }
            Log.e("--TakeBackUp--", "Step:19");


            return result;
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
        protected void onPostExecute(String result) {
            // dismiss the dialog after the file was downloaded
            dismissDialogBox(progress_bar_type);
            Log.e("--Restore--", "onPostExecute");

            Log.e("--Restore--", "Step " + result);


            if (result.equalsIgnoreCase("successfully")) {

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Downloaded Successfully (Restore From Ftouch Cloud). FileName: "
                                +restoreFileName+ "\n");

                //Restore here....
                if (RESTORE().equalsIgnoreCase("")){
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Restore Backup Successfully (Restore From Ftouch Cloud). FileName: "
                            +restoreFileName+ "\n");
                }else {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Restore Backup Failed (Restore From Ftouch Cloud). FileName: "
                            +restoreFileName+ "\n");
                }

                Log.e("--Restore--", "Step - 1");

                mPreferencesAutoBackUp = getSharedPreferences(mPreferncesFileName, MODE_PRIVATE);
                mSharedPreferenceTime = new SharedPreferenceTime(ActivityRestoreList.this);

                // Setting up Auto Backup.
                if (mPreferencesAutoBackUp.getString("AutoBackUpTime", "") != null
                        && !mPreferencesAutoBackUp.getString("AutoBackUpTime", "")
                        .equalsIgnoreCase("")) {
                    String getAutoBackUpTime = mPreferencesAutoBackUp.getString("AutoBackUpTime"
                            , "");

                    switch (getAutoBackUpTime) {

                        case "Daily":
                            ClsGlobal.SetAutoBackUp(1, "REPLACE");
                            break;
                        case "Weekly":
                            ClsGlobal.SetAutoBackUp(7, "REPLACE");
                            break;
                        case "Monthly":
                            ClsGlobal.SetAutoBackUp(30, "REPLACE");
                            break;
                        case "Yearly":
                            ClsGlobal.SetAutoBackUp(365, "REPLACE");
                            break;
                        case "Never":
                            WorkManager.getInstance().cancelUniqueWork(
                                    ClsGlobal.AppPackageName.concat("AutoBackUp"));
                            break;
                    }

                }

                // Setting Auto Email
                Scheduler.setReminder(ActivityRestoreList.this, SendEmailTask.class
                        , mSharedPreferenceTime.get_hour(), mSharedPreferenceTime.get_min());

                AsyncTask<Void, Void, Void> databaseAdd_edit = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {

                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore add_Edit_DataBase_Columns Started (Restore From Ftouch Cloud). \n");
                            ClsGlobal.add_Edit_DataBase_Columns(ActivityRestoreList.this);
                            Log.d("--customerReport--", "doInBackground: ");


                            // Set current Day in SharedPreferences file
                            // Show that the Backup Remainder Dialog Box do
                            // not open every time when user restore.
                            set_current_Day_Remainder(ActivityRestoreList.this);

                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore add_Edit_DataBase_Columns Successfully." +
                                    " (Restore From Ftouch Cloud). \n");

                        } catch (Exception e) {
                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore add_Edit_DataBase_Columns Failed. (Restore From Ftouch Cloud). \n"
                            + "Error: " + e.getMessage());
                        }

                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = ClsGlobal._prProgressDialog(ActivityRestoreList.this
                                , "Please Wait...", false);
                        pDialog.show();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Log.d("--customerReport--", "aVoid: ");



                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityRestoreList.this);
                        alertDialog.setTitle("Restart App");
                        alertDialog.setMessage("Your app will restart to restore Settings.");
                        alertDialog.setIcon(R.drawable.ic_restore);
                        alertDialog.setPositiveButton("OK", (dialog, id) -> {

                            Log.d("--customerReport--", "zipPath: " +zipPath);
                            // Delete Downloaded backup file.
                            Thread thread = new Thread(() -> {
                                try {
                                    Log.d("--customerReport--", "thread: ");

                                    deleteAllFiles_from_folder(new File(zipPath));

                                }catch (Exception e){
                                    Log.d("--customerReport--", "aVoid: " + e.getMessage());
                                }

                            });

                            thread.start();

                            Intent mStartActivity = new Intent(ActivityRestoreList.this, SHAP_Lite_Activity.class);
                            int mPendingIntentId = 123456;
                            PendingIntent mPendingIntent = PendingIntent.getActivity(ActivityRestoreList.this, mPendingIntentId, mStartActivity,
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                            System.exit(0);
                        });

                        alertDialog.setCancelable(false);
                        alertDialog.show();


                    }
                };

                databaseAdd_edit.execute();


            } else {
                Log.e("--Restore--", "Step - 2");
            }


        }

    }

    private String RESTORE() {
        String result = "";
        try {

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Restore Backup Database Started (Restore From Ftouch Cloud). \n");

            File _saveLocation = Environment.getExternalStorageDirectory();
            File root = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Backup/");
            if (!root.exists()) {
                root.mkdirs();
            }

            String currentDBPath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
            String BackupDbFileName = "dbfile.db";

            File data = Environment.getDataDirectory();
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(root, BackupDbFileName);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                result = "successfully";

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Database Successfully (Restore From Ftouch Cloud) FileName:"
                                                        +restoreFileName+". \n");

            } else {
                result = "failed";
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Database failed (Restore From Ftouch Cloud) FileName: "+ restoreFileName +
                        ". \n");
            }
        } catch (Exception e) {
            Log.e("e", e.getMessage());
            result = "failed";

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Restore Backup Database failed (Restore From Ftouch Cloud)." +
                    "FileName: "+restoreFileName + " \n" +"Error: "
                    + e.getMessage() + "\n");

        }

        return result;
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
                        ActivityRestoreList.this);
                return;
            }

        }

    }

}
