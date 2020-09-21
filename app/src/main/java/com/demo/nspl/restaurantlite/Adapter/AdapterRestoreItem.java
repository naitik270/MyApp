package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickBackUp;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsGetBackupDetailsList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class AdapterRestoreItem extends RecyclerView.Adapter<AdapterRestoreItem.MyViewHolder> {

    View itemView;
    Context context;
    List<ClsGetBackupDetailsList> data = new ArrayList<ClsGetBackupDetailsList>();
    private LayoutInflater inflater = null;
    OnClickBackUp mOnClickListener;

    public AdapterRestoreItem(Context context, List<ClsGetBackupDetailsList> data) {
        super();
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsGetBackupDetailsList> data){
        this.data = data;
        notifyDataSetChanged();
    }


    public void clearItem() {
        this.data.clear();
        notifyDataSetChanged();
    }


    public void SetOnClickListener(OnClickBackUp onClickBackUp){
        this.mOnClickListener = onClickBackUp;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        ClsGetBackupDetailsList current = data.get(i);
        holder.txt_backup_date.setText("BackupDate: " + data.get(i).getBackupDate());
        holder.txt_file_size.setText("FileSize: " + data.get(i).getFileSize());
        holder.txt_remark.setText("Remark: " + data.get(i).getRemark());
        holder.Bind(current,mOnClickListener,i);
//        holder.ll_header.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog alertDialog = new AlertDialog.Builder(context,
//                        R.style.AppCompatAlertDialogStyle).create(); //Read Update.
//                alertDialog.setContentView(R.layout.activity_dialog);
//                alertDialog.setTitle("Confirmation");
//                alertDialog.setMessage("sure to start restore?");
//                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                       // Toast.makeText(context, "Id: " + i, Toast.LENGTH_SHORT).show();
//                        String url = data.get(i).getFileUrl();
//
//                        String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//                        String zipPath = SDPath + "/ShapBackup/";
//
//                        Thread thread = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    String zipFilePath = HttpDownloadUtility.downloadFile(url, zipPath);
//
//                                    Log.d("zipFilePath", "run: " + zipFilePath);
//                                    Log.d("zipFilePath", "STEP--1");
//                                    FileZipOperation.unzip(zipFilePath, SDPath + "/" + ClsGlobal.AppFolderName);
//                                    Log.d("zipFilePath", "STEP--2");
//                                    RESTORE();
//                                    Log.d("zipFilePath", "STEP--3");
//                                } catch (IOException ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//                        });
//                        thread.start();
//                    }
//                });
//                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                alertDialog.setCancelable(false);
//                alertDialog.show();
//
//
//            }
//        });

    }


    private void RESTORE() {
        try {

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
               // Toast.makeText(context, "Database Restored successfully", Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(context, "Database File Not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            Log.e("e",e.getMessage());
            //Toast.makeText(context, "Error Exception in Restore", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_backup_date, txt_file_name, txt_extension, txt_file_size, txt_full_file_name, txt_file_url, txt_remark;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_header = itemView.findViewById(R.id.ll_header);
            txt_backup_date = itemView.findViewById(R.id.txt_backup_date);
            txt_file_size = itemView.findViewById(R.id.txt_file_size);
            txt_remark = itemView.findViewById(R.id.txt_remark);

        }

        void Bind(ClsGetBackupDetailsList clsGetBackupDetailsList, OnClickBackUp onClickBackUp,int position){
            ll_header.setOnClickListener(v -> {
                onClickBackUp.OnClick(clsGetBackupDetailsList,position);
            });
        }
    }
}