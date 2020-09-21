package com.demo.nspl.restaurantlite.Global;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.work.WorkManager;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SendEmailUtility.Scheduler;
import com.demo.nspl.restaurantlite.SendEmailUtility.SharedPreferenceTime;
import com.demo.nspl.restaurantlite.activity.SHAP_Lite_Activity;
import com.demo.nspl.restaurantlite.backGroundTask.GoogleDriveBackupTask;
import com.demo.nspl.restaurantlite.backGroundTask.SendEmailTask;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.GoogleDrive_Bkp_Process_Notification_id;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Google_Bkp_Folder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.LocalBackup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Set_Folder_Id;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.convertLongDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFiles_from_folder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteUnWanted_Bkp;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getMimeType;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.get_FolderId;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.set_current_Day_Remainder;

public class DriveServiceHelper {

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    GoogleAccountCredential credential;
    public Drive mDriveService;
    int DEFAULT_BUFFER_SIZE = 0;
    private static String mode = "";
    private static final String TAG = GoogleDriveBackupTask.class.getSimpleName();
    SharedPreferenceTime mSharedPreferenceTime;
    private SharedPreferences mPreferencesAutoBackUp;
    String zipPath = "";
    private static final String mPreferncesFileName = "AutoBackUpSettings";


    public DriveServiceHelper(String mode) {
        Log.e(TAG, "DriveServiceHelper call :- " + mode);
        this.mode = mode;
    }


    public void Initialize_DriveServiceHelper(Context context) {
        Log.e(TAG, "Initialize_DriveServiceHelper call :- " + mode);
        if (isSignedIn(context)) {
            Log.e("Check", "User is login");

            GoogleSignInAccount googleSignIn =
                    GoogleSignIn.getLastSignedInAccount(context);

            if (googleSignIn != null) {

                Log.e("Check", "googleSignIn != null");

                Collection<String> scopes = new ArrayList<>();
                scopes.add(DriveScopes.DRIVE);
                scopes.add(DriveScopes.DRIVE_FILE);
//                scopes.add(DriveScopes.DRIVE_APPDATA);
                scopes.add(DriveScopes.DRIVE_METADATA);

                credential =
                        GoogleAccountCredential.usingOAuth2(
                                context, scopes);
                credential.setSelectedAccount(googleSignIn.getAccount());
                mDriveService =
                        new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName(ClsGlobal.AppName)
                                .build();

            }


        }
    }

    static String getToken(Context context) {
        Log.e(TAG, "Prepare_Upload_File call :- " + mode);
        String token = "";
        GoogleSignInAccount googleSignIn =
                GoogleSignIn.getLastSignedInAccount(context);

        if (googleSignIn != null) {

            Collection<String> scopes = new ArrayList<>();
            scopes.add(DriveScopes.DRIVE);
            scopes.add(DriveScopes.DRIVE_FILE);
//            scopes.add(DriveScopes.DRIVE_APPDATA);
            scopes.add(DriveScopes.DRIVE_METADATA);


//            String accountName = "nathanisoftware@gmail.com";
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(context,
                            scopes).setBackOff(new ExponentialBackOff())
                            .setSelectedAccountName(googleSignIn.getEmail());


            try {
                Log.e("Check", "Token:- " + credential.getToken());
                token = credential.getToken();

            } catch (IOException | GoogleAuthException e) {
                Log.e("Check", "Exception:- " + e.getMessage());
                e.printStackTrace();
            }

        }
        return token;
    }


    // Get all file from Folder.
    public List<GoogleDriveFileHolder> GetFilesInFolder(Context context) {
        Log.e("folder", "GetFilesInFolder: ");
        List<GoogleDriveFileHolder> list = new ArrayList<>();

        Log.e("folder", "Callable<GoogleDriveFileHolder>:- ");
        String pageToken = null;
        try {
            do {

                Log.e("folder", "get_FolderId:- " + get_FolderId(context));
                FileList result = mDriveService.files().list()
                        .setQ("'" + get_FolderId(context) + "' in parents and trashed = false")
                        .setSpaces("drive")
                        .setFields("nextPageToken, files(id, name,size, createdTime)")
                        .setPageToken(pageToken)
                        .execute();


                Gson gson = new Gson();
                String jsonInString = gson.toJson(result.getFiles());
                Log.e("folder", "list---" + jsonInString);
                Log.e("folder", "size---" + result.getFiles().size());
                for (File file : result.getFiles()) {
                    GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();

                    googleDriveFileHolder.setName(file.getName());
                        googleDriveFileHolder.setCreatedTime(
                                convertLongDateTime(file.getCreatedTime().getValue()));
                    googleDriveFileHolder.setSize(ClsGlobal.readableFileSize(file.getSize()));
                    googleDriveFileHolder.setId(file.getId());
//                        googleDriveFileHolder.setMimeType(file.getMimeType());
//                        googleDriveFileHolder.setCreatedTime(file.getCreatedTime());

                    list.add(googleDriveFileHolder);


                }
                pageToken = result.getNextPageToken();


            } while (pageToken != null);
            Log.e("folder", "list:- " + list.size());

        } catch (Exception e) {
            Log.e("Exception", "GetFilesInFolder: " + e.getMessage());
        }


//        Tasks.call(mExecutor, (Callable<GoogleDriveFileHolder>) () -> {


//            return null;


//        });


        return list;
    }


    public static ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    void showDialogBox(int id, Context context) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(context);
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

                        Toast.makeText(context, "Restore canceled", Toast.LENGTH_LONG).show();


                    }
                });
                pDialog.show();
            default:

        }
    }

    private AsyncTask mMyTask;

    // Download File By file id.
    @SuppressLint("CheckResult")
    public void DownloadFile_by_Id(String file_id, String fileName, Context context) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... voids) {
                String currentToken = getToken(context);
                String result = "";

                Log.e("--TakeBackUp--", "DownloadFile_by_Id");

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Started (Restore From Google Drive). \n");

                try {

                    // Delete SharedPreferences files from SettingsBackup folder.
                    deleteAllFiles_from_folder(new
                            java.io.File(SharedPreferencesPathBackupFolder));

                    // Delete SharedPreferences files from our app folder.
                    deleteAllFile(context);

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Restore Backup Download Started (Restore From Google Drive). \n");

                    int count;
                    URL url = new URL("https://www.googleapis.com/drive/v3/files/" + file_id + "?alt=media");
                    HttpURLConnection request = (HttpURLConnection) url.openConnection();
                    request.setRequestMethod("GET");
                    request.setConnectTimeout(10000);
                    request.setRequestProperty("Authorization", "Bearer " + currentToken);
                    request.connect();

                    int lenghtOfFile = request.getContentLength();


                    // download the file
                    InputStream input = new BufferedInputStream(request.getInputStream(), 8192);


                    java.io.File _saveLocation = Environment.getExternalStorageDirectory();
                    Log.d("camera", "filepath:- " + _saveLocation);

                    Log.e("--TakeBackUp--", "Step:2");

                    java.io.File dir = new java.io.File(_saveLocation.getAbsolutePath()
                            .concat("/ShapBackup/"));

                    Log.d("generateBackupFile", "dir:- " + dir);

                    Log.e("--TakeBackUp--", "Step:3");

                    if (!dir.exists()) {
                        Log.e("--TakeBackUp--", "Step:4");
                        dir.mkdirs();
                    }

                    Log.e("--TakeBackUp--", "Step:5");

                    OutputStream output = new FileOutputStream(dir.getAbsolutePath() + "/ShapDriveBKP.zip");
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


                        Log.e("--TakeBackUp--", "total:- " + total);


                        // If the AsyncTask cancelled
//                    if (isCancelled()) {
//                        break;
//                    }

                    }

                    Log.e("--TakeBackUp--", "Step:14");
                    // flushing output
                    output.flush();
                    Log.e("--TakeBackUp--", "Step:15");
                    // closing streams
                    output.close();
                    Log.e("--TakeBackUp--", "Step:16");

                    input.close();


                    String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    zipPath = SDPath + "/ShapBackup/ShapDriveBKP.zip";


                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - - Restore Backup Downloaded Successfully " +
                            "(Restore From Google Drive). FileName:  " +
                            fileName + "\n");

                    if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    String sessionUri1 = request.getHeaderField("location");
                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " - Restore Backup Unzipping Backup file Started " +
                                "(Restore From Google Drive). FileName: "
                                + fileName + "\n");

                        if (FileZipOperation.unzip(zipPath,
                                SDPath + "/" + ClsGlobal.AppFolderName)) {
                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore Backup Unzipping file Successfully (Restore From Google Drive). FileName: "
                                    + fileName + "\n");
                        } else {
                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore Backup Unzipping file Failed (Restore From Google Drive). FileName: "
                                    + fileName + "\n");
                        }


                        RESTORE();

                        List<String> SharedPreferences_Files_From_Folder = new ArrayList<>();

                        java.io.File file = new java.io.File(SharedPreferencesPathBackupFolder);
                        if (file.exists()) {
                            for (java.io.File f : file.listFiles()) {
                                if (f.isFile()) {
                                    String name = f.getName();
                                    Log.d("generateBackupFile", "else:- " + name);

                                    Log.d("generateBackupFile", "else:- " + name);
                                    SharedPreferences_Files_From_Folder.add(name);

                                }
                            }
                        }

                        if (SharedPreferences_Files_From_Folder.size() > 0) {
                            ClsGlobal.writeSharedPreferencesFile(SharedPreferences_Files_From_Folder,
                                    SharedPreferencesPath, context);
                        }


                        Log.e("Upload", "sessionUri:- " + request.getHeaderFields());
                        Log.e("Upload", "sessionUri:- " + HttpURLConnection.HTTP_OK);

                    }


                    result = "successfully";

                    Log.e("--TakeBackUp--", "result" +result);
                } catch (Exception e) {
                    result = "failed";
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Restore Backup Download Failed (Restore From Google Drive)." +
                            " \n" + " Error: " + e.getMessage() + "\n");

                    Log.e("Exception", e.getMessage());

                    Log.e("--TakeBackUp--", "result" +e.getMessage());
                }


                return result;
            }


            /**
             * Updating progress bar
             */
            protected void onProgressUpdate(String... progress) {
                // setting progress percentage
                pDialog.setProgress(Integer.parseInt(progress[0]));

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dismissDialogBox(progress_bar_type);
                Log.e("--TakeBackUp--", "onPostExecute" );
                if (result.equalsIgnoreCase("successfully")) {
                    Log.e("--TakeBackUp--", "result.equalsIgnoreCase" );

                    // Cancel Daily logout and AutoLocal Bkp Task.
                    ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName
                            .concat("DailyTaskLogoutRetail"));
                    ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName
                            .concat(".DailyTaskAutoLocalBkp_ftouchPos"));


                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Restore Backup Successfully (Restore From Google Drive). FileName: "
                            + fileName + "\n");

                    mPreferencesAutoBackUp = context.getSharedPreferences(mPreferncesFileName, MODE_PRIVATE);
                    mSharedPreferenceTime = new SharedPreferenceTime(context);

                    // Setting up Auto Backup.
                    if (mPreferencesAutoBackUp.getString("AutoBackUpTime", "") != null
                            && !mPreferencesAutoBackUp.getString("AutoBackUpTime", "")
                            .equalsIgnoreCase("")) {
                        String getAutoBackUpTime = mPreferencesAutoBackUp
                                .getString("AutoBackUpTime"
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
                    Scheduler.setReminder(context, SendEmailTask.class
                            , mSharedPreferenceTime.get_hour(),
                            mSharedPreferenceTime.get_min());

                    AsyncTask<Void, Void, Void> databaseAdd_edit = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {

                                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                        " - Restore add_Edit_DataBase_Columns Started (Restore From Google Drive). \n");
                                ClsGlobal.add_Edit_DataBase_Columns(context);
                                Log.d("--customerReport--", "doInBackground: ");


                                // Set current Day in SharedPreferences file
                                // Show that the Backup Remainder Dialog Box do
                                // not open every time when user restore.
                                set_current_Day_Remainder(context);

                                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                        " - Restore add_Edit_DataBase_Columns Successfully." +
                                        " (Restore From Google Drive). \n");

                            } catch (Exception e) {
                                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                        " - Restore add_Edit_DataBase_Columns Failed. (Restore From Google Drive). \n"
                                        + "Error: " + e.getMessage());
                            }

                            return null;
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            pDialog = ClsGlobal._prProgressDialog(context
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


                        // Delete Downloaded backup file.
                        Thread thread = new Thread(() -> {
                            try {
                                Log.d("--customerReport--", "thread: ");
                                new java.io.File(zipPath).delete();

                            } catch (Exception e) {
                                Log.d("--customerReport--", "aVoid: " + e.getMessage());
                            }

                        });

                        thread.start();

                            Log.e("--TakeBackUp--", "result.equalsIgnoreCase" );
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setTitle("Restart App");
                            alertDialog.setMessage("Your app will restart to restore Settings.");
                            alertDialog.setIcon(R.drawable.ic_restore);
                            alertDialog.setPositiveButton("OK", (dialog, id) -> {

                                Intent mStartActivity = new Intent(context, SHAP_Lite_Activity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);

                            });
                            alertDialog.setCancelable(false);
                            alertDialog.show();


                        }
                    };

                    databaseAdd_edit.execute();


                }


            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showDialogBox(progress_bar_type, context);
            }
        };

        mMyTask = asyncTask.execute();




    }

    public static void dismissDialogBox(int id) {
        pDialog.dismiss();
    }

    public void Prepare_Upload_File(java.io.File file,
                                    Context context,
                                    NotificationCompat.Builder notificationBuilder) {

        Log.e(TAG, "Prepare_Upload_File call :- " + mode);
        Log.e(TAG, "get_FolderId(context) call :- " + get_FolderId(context));

        Create_if_Folder_Not_exists(context);


        String token = getToken(context);
        Log.e("Upload", "token: " + token);

        Log.e("Upload", "Upload_File call");

        if (file.length() < 524288000) {
            DEFAULT_BUFFER_SIZE = 399999;
        }

        try {
            Log.e("Upload", "run call");
            URL url = new URL("https://www.googleapis.com/upload/drive/v3/files?" +
                    "uploadType=resumable");

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("POST");
            request.setDoInput(true);
            request.setDoOutput(true);
            request.setChunkedStreamingMode(DEFAULT_BUFFER_SIZE);

            request.setRequestProperty("Authorization", "Bearer " + token);
            request.setRequestProperty("X-Upload-Content-Type", getMimeType(file.getPath()));
            request.setRequestProperty("X-Upload-Content-Length", String.format(Locale.ENGLISH, "%d", file.length()));
            request.setRequestProperty("Content-Type", "application/json; charset=UTF-8");


            String body = "{\"name\": \"" + file.getName() + "\", \"parents\":" +
                    " [\"" + get_FolderId(context) + "\"]}";
            request.setRequestProperty("Content-Length", String.format(Locale.ENGLISH, "%d",
                    body.getBytes().length));
            OutputStream outputStream = request.getOutputStream();
            outputStream.write(body.getBytes());

            outputStream.close();
//                request.setFixedLengthStreamingMode(20000048);
            request.connect();
            Log.e("Upload", "url:- " + url.toURI());
            Log.e("Upload", "run end");
            Log.e("Upload", "getResponseMessage:- " + request.getResponseMessage());
            Log.e("Upload", "getResponseCode:- " + request.getResponseCode());

            if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String sessionUri = request.getHeaderField("location");
                Log.e("Upload", "sessionUri:- " + sessionUri);
                Log.e("Upload", "sessionUri:- " + request.getHeaderFields());
                if (mode.equalsIgnoreCase("Manual")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " Uploading Backup Zip file Started (Manual Google Drive Backup). File Name:- " +
                            file.getName() + " \n");

                } else if (mode.equalsIgnoreCase("Auto")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " Uploading Backup Zip file Started (Auto Google Drive Backup). File Name:- "
                            + file.getName() + "\n");
                }

                Start_Upload_File(sessionUri, file, notificationBuilder, context);
            }

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }


    public void Start_Upload_File(String sessionUri, java.io.File file,
                                  NotificationCompat.Builder notificationBuilder,
                                  Context context) {

        Log.e("Upload", "Upload_File call");
        Log.e(TAG, "Prepare_Upload_File call :- " + mode);
        try {


            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);


            URL url = new URL(sessionUri);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("PUT");
            request.setDoOutput(true);
            request.setConnectTimeout(10000);
            request.setChunkedStreamingMode(DEFAULT_BUFFER_SIZE);

            request.setRequestProperty("Content-Type", getMimeType(file.getPath()));

            long uploadedBytes =
                    file.length() * 1024 * 1024;
            Log.e("Upload", "uploadedBytes first:- " + uploadedBytes);

            long chunkStart = 0l;

            if (chunkStart + uploadedBytes > file.length()) {
                uploadedBytes = (int) file.length() - chunkStart;
                Log.e("Upload", "uploadedBytes:- " + uploadedBytes);
            }

            request.setRequestProperty("Content-Length", String.format(Locale.ENGLISH, "%d", uploadedBytes));
            request.setRequestProperty("Content-Range", "bytes " + chunkStart + "-" + (chunkStart + uploadedBytes - 1) + "/" + file.length());
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.getChannel().position(chunkStart);

//                if (fileInputStream.read(buffer, 0, (int) uploadedBytes) == -1)
//                { /* break, return, exit*/ }

            OutputStream outputStream = request.getOutputStream();
//                outputStream.write(buffer);


            int bytesRead = -1;
            long TotalUploadedBytes = 0l;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                TotalUploadedBytes += bytesRead;

                Log.e("Upload", "loading " +
                        "buffer:- " + buffer + " bytesRead:- " + bytesRead);
                Log.e("Upload", "loading "
                        + "TotalUploadedBytes:- " + TotalUploadedBytes);

                int current_percent = (int) (100 * TotalUploadedBytes / file.length());
                int total_percent = (int) (100 * (TotalUploadedBytes) / file.length());

                // Notification When progressing backup.
                notificationBuilder.setContentTitle("Backup in progress");
                notificationBuilder.setProgress(100, current_percent, false);
                notificationBuilder.setContentText("File Size: " +
                        ClsGlobal.readableFileSize(TotalUploadedBytes) +
                        "/" + ClsGlobal.readableFileSize(file.length()));


                notificationManager.notify(GoogleDrive_Bkp_Process_Notification_id,
                        notificationBuilder.build());

                outputStream.flush();

            }


            Log.e(TAG, "TotalUploadedBytes call :- " + mode);

            outputStream.close();
            outputStream.flush();
            fileInputStream.close();

            request.connect();


            Log.e("Upload", "run end");
            Log.e("Upload", "getResponseMessage Resumable:- " + request.getResponseMessage());
            Log.e("Upload", "getResponseCode Resumable:- " + request.getResponseCode());


            if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Notification When Finished.
                notificationBuilder.setContentTitle("Finished");
                notificationBuilder.setContentText("Google Drive Backup completed")
                        // Removes the progress bar
                        .setProgress(0, 0, false);
                notificationManager.notify(GoogleDrive_Bkp_Process_Notification_id,
                        notificationBuilder.build());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                notificationManager.cancel(GoogleDrive_Bkp_Process_Notification_id);

                ClsGlobal.BackupNotification("Finished",
                        "Google Drive Backup Completed."
                                + getEntryDateFormat(getCurruntDateTime()), context
                        , ClsGlobal.GoogleDrive_Bkp_Notification_id);


                // Delete unwanted backup files.
                deleteUnWanted_Bkp(new java.io.File(
                        ClsGlobal.SDPath + "/" + LocalBackup + "/"));

                String sessionUri1 = request.getHeaderField("location");

                if (mode.equalsIgnoreCase("Manual")) {
                    ClsGlobal.InsertBackupLogs("Manual Backup (Google Drive) ",
                            "GDrive Backup Uploaded Successfully.", context);

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " GDrive Backup Uploaded Successfully (Manual Google Drive Backup). File Name:- " +
                            file.getName() + " \n");

                } else if (mode.equalsIgnoreCase("Auto")) {
                    ClsGlobal.InsertBackupLogs("Auto Backup(Google Drive) ",
                            "GDrive Backup Uploaded Successfully.", context);
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " GDrive Backup Uploaded Successfully (Auto Google Drive Backup). File Name:- " +
                            file.getName() + "\n");
                }

                Log.e("Upload", "sessionUri1 Resumable:- " + sessionUri1);
                Log.e("Upload", "sessionUri:- " + request.getHeaderFields());

            } else {
                if (mode.equalsIgnoreCase("Manual")) {
                    ClsGlobal.InsertBackupLogs("Manual Backup (Google Drive) ",
                            "GDrive Backup Upload Failed.", context);

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " GDrive Backup Upload Failed (Manual Google Drive Backup). File Name:- " +
                            file.getName() + " \n");

                } else if (mode.equalsIgnoreCase("Auto")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " GDrive Backup Upload Failed (Auto Google Drive Backup). File Name:- " +
                            file.getName() + "\n");
                }
            }


            // This for resuming upload file.
//                if (request.getResponseCode() == 308) {
//                    Log.e("Upload", "request.getResponseCode() == 308:- ");
//                    Resume_Upload_File(sessionUri, file,
//                            request.getHeaderField("range"));
//                }

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }


    // Create if Folder Not exists.
    public void Create_if_Folder_Not_exists(Context context) {
        Log.e(TAG, "Create_if_Folder_Not_exists call :- " + mode);
        Log.e("Upload", " GoogleDriveFileHolder call:- ");
        String pageToken = null;
        try {

            do {
                FileList result = mDriveService.files().list()
                        .setQ("mimeType='application/vnd.google-apps.folder'" +
                                " and name='" + Google_Bkp_Folder + "' and trashed = false")
                        .setSpaces("drive")
                        .setFields("nextPageToken, files(id, name)")
                        .setPageToken(pageToken)
                        .execute();

                Log.e("Upload", "do:- ");



                if (result.getFiles().size() <= 0) {
                    // Create Folder Here.
                    Log.e("Upload", "result.getFiles():- ");
                    Log.e("folder", "No folder found!");
                    createFolder(Google_Bkp_Folder, "", context);

                } else {
                    // Folder Found.
                    Log.e("Upload", "result.getFiles() else:- ");
                    for (File file : result.getFiles()) {
//                                System.out.printf("Found file: %s (%s)\n",
//                                        file.getName(), file.getId());
                        Log.e("folder", "folder:- "
                                + file.getName() + " folder id:- " + file.getId());

                        if (get_FolderId(context).equalsIgnoreCase("")) {
                            Log.e("Upload", "get_FolderId is empty:- ");
                            if (file.getName().equalsIgnoreCase(Google_Bkp_Folder)) {
                                Set_Folder_Id(context, file.getId());
                                break;
                            }

                        }
                    }
                    pageToken = result.getNextPageToken();
                }

            } while (pageToken != null);

        } catch (Exception e) {
            Log.e("Upload", "Create_if_Folder_Not_exists:- ");
            Log.e("Exception", e.getMessage());
        }


    }

    public void createFolder(final String fileName,
                             @Nullable final String folderId,
                             Context context) {
        try {
            Log.e("Upload", "call:- ");
            File metadata = new File()
                    .setParents(Collections.singletonList("root"))
                    .setName(fileName)
                    .setMimeType("application/vnd.google-apps.folder");


            File googleFile = mDriveService.files().create(metadata)
                    .setFields("id")
                    .execute();

            Log.e("Upload", "Created Folder id:- " + googleFile.getId());
            // Save fTouch POS Backup folder id to preference.
            Set_Folder_Id(context, googleFile.getId());

            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }
        } catch (Exception e) {
            Log.e("Upload", "call:- " + e.getMessage());
        }


    }

    public static boolean isSignedIn(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }



    public static String RESTORE() {
        String result = "";
        try {


            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Restore Backup Database Started (Restore From Google Drive Backup). \n");

            java.io.File _saveLocation = Environment.getExternalStorageDirectory();
            java.io.File root = new java.io.File(_saveLocation.getAbsolutePath() +
                    "/" + ClsGlobal.AppFolderName + "/Backup/");
            if (!root.exists()) {
                root.mkdirs();
            }

            String currentDBPath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
            String BackupDbFileName = "dbfile.db";

            java.io.File data = Environment.getDataDirectory();
            java.io.File currentDB = new java.io.File(data, currentDBPath);
            java.io.File backupDB = new java.io.File(root, BackupDbFileName);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                result = "successfully";
                //  Toast.makeText(context, "Database Restored successfully", Toast.LENGTH_SHORT).show();

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Database Successfully (Restore From Google Drive Backup). \n");
            } else {
                result = "failed";
                //  Toast.makeText(context, "Database File Not found", Toast.LENGTH_SHORT).show();
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " -Restore Backup Database failed (Restore From Google Drive Backup). \n");

            }
        } catch (Exception e) {
            Log.e("e", e.getMessage());
            result = "failed";

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " -Restore Backup Database failed  (Restore From Google Drive Backup). \n");
            // Toast.makeText(context, "Error Exception in Restore", Toast.LENGTH_SHORT).show();
        }
        Log.e("result", result);
        return result;
    }


}
