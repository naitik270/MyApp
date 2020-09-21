package com.demo.nspl.restaurantlite.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.BuildConfig;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmployeeDocuments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/24/2018.
 */

public class EmployeeDocumentAdapter extends RecyclerView.Adapter<EmployeeDocumentAdapter.MyViewHolder> {
    Context c;
    private View itemview;
    List<ClsEmployeeDocuments> list_doc = new ArrayList<ClsEmployeeDocuments>();
    //    private SQLiteDatabase db;
    AppCompatActivity activity;
    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".myfileprovider";
    String _flag = "";

    String _folderName = "";


    public EmployeeDocumentAdapter(AppCompatActivity ac, Context c, ArrayList<ClsEmployeeDocuments> catMasters, String _flag) {
        this.c = c;
        this.list_doc = catMasters;
        this.activity = ac;
        this._flag = _flag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_document_details, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ClsEmployeeDocuments ObjEmpDoc = list_doc.get(position);

        holder.lbl_document_name.setText(ObjEmpDoc.getDoc_name().toUpperCase());
        holder.lbl_document_no.setText(ObjEmpDoc.getDoc_no().toUpperCase());


        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(c);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_emp_doc);
                dialog.setCancelable(true);


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);



                TextView txtviewdetails = (TextView) dialog.findViewById(R.id.txtviewdetails);
                TextView delete = (TextView) dialog.findViewById(R.id.txtdelete);

                txtviewdetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        dialog.cancel();

                        File _saveLocation = Environment.getExternalStorageDirectory();
                        Log.d("camera", "filepath:- " + _saveLocation);


                        if (_flag.equalsIgnoreCase("customer")) {
                            _folderName = "/CustomerDocument/";

                        } else {
                            _folderName = "/EmployeeDocument/";

                        }

                        File dir = new File(_saveLocation.getAbsolutePath() + "/" +
                                ClsGlobal.AppFolderName +
                                _folderName.concat(String.valueOf(ObjEmpDoc.getEmployee_id())).concat("/"));

                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        Log.e("dir-", String.valueOf(dir));

                        File filetoSave = new File(dir.getAbsolutePath() + "/".concat(ObjEmpDoc.getFilename()));
                        Log.d("camera", "filepath:- " + filetoSave.getAbsolutePath());
                        Uri outputUri = FileProvider.getUriForFile(c, AUTHORITY, filetoSave);

                        if (filetoSave.exists()) {

                            Intent viewFile = new Intent(Intent.ACTION_VIEW);
                            viewFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            viewFile.setDataAndType(outputUri, "image/*");
                            c.startActivity(viewFile);

                        } else {
                            Toast.makeText(c, "File does not exists", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
                dialog.show();
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                        alertDialog.setTitle("Confirm...");
                        alertDialog.setMessage("Are you sure you want delete?");
                        alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ClsEmployeeDocuments itemGetSet = new ClsEmployeeDocuments();
                                itemGetSet = list_doc.get(position);
                                int result = 0;
                                String Action = "DELETE";
                                ClsEmployeeDocuments Obj = new ClsEmployeeDocuments(c);
                                Obj.setDoc_id(itemGetSet.getDoc_id());
                                result = ClsEmployeeDocuments.Delete(Obj);

                                Log.e("UpdateTaG", String.valueOf(result));
                                if (result == 1) {

                                    list_doc.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                }
                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                });

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_doc.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cv;
        TextView lbl_document_name, lbl_document_no;

        public MyViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            lbl_document_name = itemView.findViewById(R.id.lbl_document_name);
            lbl_document_no = itemView.findViewById(R.id.lbl_document_no);
        }
    }
}
