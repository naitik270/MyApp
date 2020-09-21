package com.demo.nspl.restaurantlite.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class RestoreAsynctaskNew extends AsyncTask<String, String, String> {

    private ProgressDialog pDialog;


    public static final int progress_bar_type = 0;
    Context context;
    String url = "";


    public RestoreAsynctaskNew(String url, Context context) {
        this.url = url;
        Log.d("camera", "url:- " + url);
        this.context = context;
    }

    void onCreateDialog() {

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading backup. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();


    }

    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conection.getContentLength();


            Log.d("camera", "url:- " + url);

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

            OutputStream output = new FileOutputStream(dir.getAbsolutePath().concat("/FileDownLoadDemoNew.zip"));
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
//        dismissDialog(progress_bar_type);
        pDialog.dismiss();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onCreateDialog();
    }
}
