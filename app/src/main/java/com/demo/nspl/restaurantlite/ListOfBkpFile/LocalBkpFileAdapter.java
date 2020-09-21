package com.demo.nspl.restaurantlite.ListOfBkpFile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/9/2018.
 */

public class LocalBkpFileAdapter extends RecyclerView.Adapter<LocalBkpFileAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsLocalBkpFile> lstClsLocalBkpFiles = new ArrayList<ClsLocalBkpFile>();
    onLocalBkpList mOnLocalBkpList;


    public LocalBkpFileAdapter(Context c) {
        this.c = c;
    }

    public void AddItems(List<ClsLocalBkpFile> clsLocalBkpFiles){
        this.lstClsLocalBkpFiles = clsLocalBkpFiles;
        notifyDataSetChanged();
    }

    public void clearItem() {
        this.lstClsLocalBkpFiles.clear();
        notifyDataSetChanged();
    }



    public void SetOnClickListener(onLocalBkpList mOnLocalBkpList) {
        this.mOnLocalBkpList = mOnLocalBkpList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_local_bkp_file, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ClsLocalBkpFile ObjCategory = lstClsLocalBkpFiles.get(position);


        holder.txt_file_name.setText(ObjCategory.get_fileName());
        holder.txt_file_path.setText(ObjCategory.get_createDate());
        holder.txt_file_size.setText(ObjCategory.get_fileSize());


//        Log.d("--path--", "Adapter: " + ObjCategory.get_folderName());

        if (ObjCategory.get_folderName() != null &&
                ObjCategory.get_folderName().contains("fTouchPOSLocalBkp")) {
            Log.d("--path--", "Adapter:  fTouchPOSLocalBkp" );
            holder.txt_manual.setVisibility(View.VISIBLE);
        } else {
            holder.txt_manual.setVisibility(View.GONE);
        }

        holder.Bind(ObjCategory, mOnLocalBkpList, position);
    }

    @Override
    public int getItemCount() {
        return lstClsLocalBkpFiles.size();
    }

    interface onLocalBkpList {

        void onClick(ClsLocalBkpFile clsLocalBkpFile, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_file_path, txt_file_name, txt_file_size;
        TextView txt_manual;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_file_path = itemView.findViewById(R.id.txt_file_path);
            txt_file_name = itemView.findViewById(R.id.txt_file_name);
            txt_file_size = itemView.findViewById(R.id.txt_file_size);
            txt_manual = itemView.findViewById(R.id.txt_manual);
        }


        void Bind(final ClsLocalBkpFile clsLocalBkpFile, onLocalBkpList mOnLocalBkpList, int position) {
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnLocalBkpList.onClick(clsLocalBkpFile, position);
                }
            });
        }
    }
}
