package com.demo.nspl.restaurantlite.ListOfBkpFile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.nspl.restaurantlite.AsyncTask.LocalRestoreAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ftouchLogs_Folder;

public class PhoneStorageListFragment extends Fragment {

    RecyclerView rv;
    LinearLayout ll_no_data;
    TextView txt_no_data;
    TextView txt_link_new;
    List<ClsLocalBkpFile> lstClsLocalBkpFiles = new ArrayList<>();

    String path = Environment.getExternalStorageDirectory().toString()
            + "/ftouchPos AutoLocalBkp";


    String pathEmergBkp = Environment.getExternalStorageDirectory().toString()
            + "/" + ftouchLogs_Folder;

    List<File> allbackup_files = new ArrayList<>();


    File directory = new File(path);

    File[] files = directory.listFiles();

    long _fileSize = 0l;


    String _manualPath = Environment.getExternalStorageDirectory().toString()
            + "/fTouchPOSLocalBkp";

    String _folderName = "";


    File _manualDirectory = new File(_manualPath);

    File[] _manualFiles = _manualDirectory.listFiles();

    LocalBkpFileAdapter adapter;

    long _manualFileSize = 0l;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public PhoneStorageListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fargment_bkp_list,
                container, false);
        main(v);
        return v;
    }


    private void main(View v) {

        rv = v.findViewById(R.id.rv);
        ll_no_data = v.findViewById(R.id.ll_no_data);
        txt_no_data = v.findViewById(R.id.txt_no_data);
        txt_link_new = v.findViewById(R.id.txt_link_new);
        txt_link_new.setVisibility(View.GONE);
        mSwipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new LocalBkpFileAdapter(getActivity());
        rv.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(this::LocalList);

        LocalList();

        adapter.SetOnClickListener((clsLocalBkpFile, position) -> {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Confirm...");
            alertDialog.setMessage("Are you sure! you want to backup now?");
            alertDialog.setIcon(R.drawable.ic_bkp_file);
            alertDialog.setPositiveButton("YES", (dialog, which) -> {

                new LocalRestoreAsyncTask(getActivity(), ClsGlobal.SDPath
                        + "/" + clsLocalBkpFile.get_folderName()
                        + "/" + clsLocalBkpFile.get_fileName()).execute();

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


    // List from folder
    void LocalList() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<ClsLocalBkpFile>> asyncTask =
                new AsyncTask<Void, Void, List<ClsLocalBkpFile>>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        adapter.clearItem();
                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    protected List<ClsLocalBkpFile> doInBackground(Void... voids) {
                        lstClsLocalBkpFiles.clear();
                        if (files != null) {
                            allbackup_files.addAll(Arrays.asList(files));
                        }

                        if (_manualFiles != null) {
                            allbackup_files.addAll(Arrays.asList(_manualFiles));
                        }

                        if (new File(pathEmergBkp).listFiles() != null) {
                            allbackup_files.addAll(Arrays.asList(new File(pathEmergBkp).listFiles()));
                        }

                        for (File file : allbackup_files) {

                            if (file.getName().endsWith(".zip")) {

                                Date lastModDate = new Date(file.lastModified());
                                _folderName = "";

                                if (file.getAbsolutePath().contains(ClsGlobal.AutoLocalBackup)) {
                                    _folderName = "ftouchPos AutoLocalBkp";
                                } else if (file.getAbsolutePath().contains(ClsGlobal.LocalBackup)) {
                                    _folderName = "fTouchPOSLocalBkp";
                                } else if (file.getAbsolutePath().contains(ftouchLogs_Folder)) {
                                    _folderName = ftouchLogs_Folder;
                                }

                                ClsLocalBkpFile obj = new ClsLocalBkpFile();
                                obj.set_filePath(path);
                                obj.set_createDate(ClsGlobal.getLastBkpFileDate(lastModDate));
                                obj.set_fileName(file.getName());
                                obj.set_folderName(_folderName);
                                obj.set_fileSize(ClsGlobal.readableFileSize(file.length()));
                                lstClsLocalBkpFiles.add(obj);
                            }
                        }

                        return lstClsLocalBkpFiles;
                    }


                    @Override
                    protected void onPostExecute(List<ClsLocalBkpFile> list) {
                        super.onPostExecute(list);

                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        if (lstClsLocalBkpFiles != null && lstClsLocalBkpFiles.size() != 0) {
                            ll_no_data.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);

                        } else {
                            ll_no_data.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);
                        }

                        adapter.AddItems(lstClsLocalBkpFiles);

                    }
                }.execute();

    }


}
