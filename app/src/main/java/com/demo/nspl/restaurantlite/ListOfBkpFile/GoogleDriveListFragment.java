package com.demo.nspl.restaurantlite.ListOfBkpFile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.DriveServiceHelper;
import com.demo.nspl.restaurantlite.Global.GoogleDriveFileHolder;
import com.demo.nspl.restaurantlite.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class GoogleDriveListFragment extends Fragment {

    RecyclerView rv;
    ImageView iv_no_bkp;
    LinearLayout ll_no_data;
    TextView txt_no_data, txt_link_new;
    LinearLayout progress_bar_layout;
    GoogleAccountCredential credential;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    DriveServiceHelper mDriveServiceHelper;
    GoogleDriveBkpAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String TAG = GoogleDriveListFragment.class.getSimpleName();
    List<GoogleDriveFileHolder> lstGoogleDriveFileHolders = new ArrayList<>();


    public GoogleDriveListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fargment_bkp_list,
                container, false);
        main(v);
        return v;
    }

    private void main(View v) {

        ll_no_data = v.findViewById(R.id.ll_no_data);
        txt_no_data = v.findViewById(R.id.txt_no_data);
        txt_link_new = v.findViewById(R.id.txt_link_new);
        progress_bar_layout = v.findViewById(R.id.progress_bar_layout);
        mSwipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);

        txt_no_data.setText("NO BACKUP FOUND");
        iv_no_bkp = v.findViewById(R.id.iv_no_bkp);
        iv_no_bkp.setImageResource(R.drawable.ic_no_drive_backup);
        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new GoogleDriveBkpAdapter(getActivity());
        rv.setAdapter(adapter);

        if (DriveServiceHelper.isSignedIn(getActivity())) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            txt_link_new.setVisibility(View.GONE);
        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            txt_link_new.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(this::GetFileFromGDrive);

        txt_link_new.setOnClickListener(v1 -> {
            Log.e("Check","setOnClickListener call");
            if (!ClsGlobal.CheckInternetConnection(getActivity())) {
                Toast.makeText(getActivity(), "You are not connected to Internet",
                        Toast.LENGTH_SHORT).show();
            } else {
                _firstCheckLogin();

            }
        });


        GetFileFromGDrive();

        adapter.SetOnClickListener((googleDriveFileHolder, position) -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            alertDialog.setTitle("Confirm...");
            alertDialog.setMessage("Are you sure! you want to backup now?");
            alertDialog.setIcon(R.drawable.ic_no_drive_backup);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DriveServiceHelper driveServiceHelper = new DriveServiceHelper("");
                    driveServiceHelper.DownloadFile_by_Id(
                            googleDriveFileHolder.getId(), googleDriveFileHolder.getName(), getActivity());


                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            alertDialog.show();
        });

    }


    void _firstCheckLogin() {
        Log.e("Check","_firstCheckLogin call");
        if (DriveServiceHelper.isSignedIn(getActivity())) {
            Log.e("Check","_firstCheckLogin isSignedIn call");
            GoogleSignInAccount googleSignIn =
                    GoogleSignIn.getLastSignedInAccount(getActivity());

            if (googleSignIn != null) {


                Collection<String> scopes = new ArrayList<>();
                scopes.add(DriveScopes.DRIVE);
                scopes.add(DriveScopes.DRIVE_FILE);
//                scopes.add(DriveScopes.DRIVE_APPDATA);
//                scopes.add(DriveScopes.DRIVE_SCRIPTS);
                scopes.add(DriveScopes.DRIVE_METADATA);

                credential =
                        GoogleAccountCredential.usingOAuth2(
                                getActivity(), scopes);
                credential.setSelectedAccount(googleSignIn.getAccount());
                Drive googleDriveService =
                        new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName(ClsGlobal.AppName)
                                .build();
            }
        } else {
            Log.e("Check","_firstCheckLogin else call");
            requestSignIn();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK && resultData != null) {
                    handleSignInResult(resultData);
                }
                break;
        }
    }


    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {
        Log.e("Check","handleSignInResult call");
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {


                    Collection<String> scopes = new ArrayList<>();
                    scopes.add(DriveScopes.DRIVE);
                    scopes.add(DriveScopes.DRIVE_FILE);
//                    scopes.add(DriveScopes.DRIVE_APPDATA);
//                    scopes.add(DriveScopes.DRIVE_SCRIPTS);
                    scopes.add(DriveScopes.DRIVE_METADATA);


                    // Use the authenticated account to sign in to the Drive service.
                    credential =
                            GoogleAccountCredential.usingOAuth2(
                                    getActivity(), scopes);

                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName(ClsGlobal.AppName)
                                    .build();


                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
//                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);


                    if (mSwipeRefreshLayout.getVisibility() == View.GONE){
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        txt_link_new.setVisibility(View.GONE);
                    }
                    GetFileFromGDrive();
                })
                .addOnFailureListener(exception ->
                        Log.e("--TEST--", "Unable to sign in.", exception));
        Log.e("Check","addOnSuccessListener end");
    }


    /**
     * Starts a sign-in activity using {@link #REQUEST_CODE_SIGN_IN}.
     */
    private void requestSignIn() {
        Log.d("requestSignIn", "Requesting sign-in");
        Log.e("Check","requestSignIn call");
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE),
                                new Scope(DriveScopes.DRIVE_FILE),
//                                new Scope(DriveScopes.DRIVE_APPDATA),
                                new Scope(DriveScopes.DRIVE_METADATA)
                        )
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(getActivity(), signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);

//        Thread thread = new Thread(() -> {
//            DriveServiceHelper driveServiceHelper = new DriveServiceHelper("");
//            driveServiceHelper.Initialize_DriveServiceHelper(getActivity());
//            driveServiceHelper.Create_if_Folder_Not_exists(getActivity());
//        });

//        thread.start();
    }


    private void GetFileFromGDrive() {
        Log.e("Check","GetFileFromGDrive call");
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void>
                asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                adapter.clearItem();
                mSwipeRefreshLayout.setRefreshing(true);
//                progress_bar_layout.setVisibility(View.VISIBLE);

            }

            @SuppressLint("WrongConstant")
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DriveServiceHelper driveServiceHelper = new DriveServiceHelper("");
                driveServiceHelper.Initialize_DriveServiceHelper(getActivity());
                driveServiceHelper.Create_if_Folder_Not_exists(getContext());
                lstGoogleDriveFileHolders = driveServiceHelper.GetFilesInFolder(getActivity());

                return null;
            }


            @Override
            protected void onPostExecute(Void Void) {
                super.onPostExecute(Void);
//                progress_bar_layout.setVisibility(View.GONE);
                Log.e("Check","onPostExecute call");



                // Close Refreshing.
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (lstGoogleDriveFileHolders != null &&
                        lstGoogleDriveFileHolders.size() != 0) {

                    iv_no_bkp.setVisibility(View.GONE);
                    txt_no_data.setVisibility(View.GONE);

                } else {
                    iv_no_bkp.setVisibility(View.VISIBLE);
                    txt_no_data.setVisibility(View.VISIBLE);

                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstGoogleDriveFileHolders);
                Log.d(TAG, "lstGoogleDriveFileHolders: " + jsonInString);

                adapter.AddItems(lstGoogleDriveFileHolders);

            }
        };
        asyncTask.execute();
    }


}



