package com.demo.nspl.restaurantlite.ListOfBkpFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.GoogleDriveFileHolder;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/9/2018.
 */

public class GoogleDriveBkpFileAdapter extends RecyclerView.Adapter<GoogleDriveBkpFileAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<GoogleDriveFileHolder> lstGoogleDriveFileHolders =
            new ArrayList<GoogleDriveFileHolder>();
    onLocalBkpList mOnLocalBkpList;


    public GoogleDriveBkpFileAdapter(Context c, List<GoogleDriveFileHolder> clsGoogleDriveFileHolders) {
        this.c = c;
        this.lstGoogleDriveFileHolders = clsGoogleDriveFileHolders;
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

        GoogleDriveFileHolder ObjCategory = lstGoogleDriveFileHolders.get(position);

        holder.txt_file_name.setText(ObjCategory.getName());
        holder.txt_file_path.setText(String.valueOf(ObjCategory.getCreatedTime()));
        holder.txt_file_size.setText(String.valueOf(ObjCategory.getSize()));


//        Log.d("--path--", "Adapter: " + ObjCategory.get_folderName());
/*

        if (ObjCategory.get_folderName() != null &&
                ObjCategory.get_folderName().contains("fTouchPOSLocalBkp")) {
            Log.d("--path--", "Adapter:  fTouchPOSLocalBkp" );
            holder.txt_manual.setVisibility(View.VISIBLE);
        } else {
            holder.txt_manual.setVisibility(View.GONE);
        }
*/

        holder.Bind(ObjCategory, mOnLocalBkpList, position);
    }

    @Override
    public int getItemCount() {
        return lstGoogleDriveFileHolders.size();
    }

    interface onLocalBkpList {

        void onClick(GoogleDriveFileHolder clsLocalBkpFile, int position);
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


        void Bind(final GoogleDriveFileHolder clsLocalBkpFile, onLocalBkpList mOnLocalBkpList, int position) {
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnLocalBkpList.onClick(clsLocalBkpFile, position);
                }
            });
        }
    }
}
