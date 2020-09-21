package com.demo.nspl.restaurantlite.ListOfBkpFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.GoogleDriveFileHolder;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class GoogleDriveBkpAdapter extends RecyclerView.Adapter<GoogleDriveBkpAdapter.MyViewHolder> {

    private View itemview;
    Context c;
    List<GoogleDriveFileHolder> lst = new ArrayList<GoogleDriveFileHolder>();
    onGoogleDriveBkp mOnGoogleDriveBkp;

    public GoogleDriveBkpAdapter(Context c) {
        this.c = c;
    }


    public void AddItems(List<GoogleDriveFileHolder> lst){
        this.lst = lst;
        notifyDataSetChanged();
    }

    public void clearItem() {
        this.lst.clear();
        notifyDataSetChanged();
    }

    public void SetOnClickListener(onGoogleDriveBkp mOnGoogleDriveBkp) {
        this.mOnGoogleDriveBkp = mOnGoogleDriveBkp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_local_bkp_file, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GoogleDriveFileHolder obj = lst.get(position);

        holder.txt_file_name.setText(obj.getName());
        holder.iv_icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.ic_local_bkp_list));
        holder.txt_file_size.setText("File Size: " + obj.getSize());
        holder.txt_file_path.setText("Created: " + obj.getCreatedTime());

        holder.Bind(obj, mOnGoogleDriveBkp, position);
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    interface onGoogleDriveBkp {
        void onClick(GoogleDriveFileHolder googleDriveFileHolder, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header,hide_layout;

        TextView txt_file_path, txt_file_name, txt_file_size;
        TextView txt_manual;
        ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            hide_layout = itemView.findViewById(R.id.hide_layout);
            txt_file_path = itemView.findViewById(R.id.txt_file_path);
            txt_file_name = itemView.findViewById(R.id.txt_file_name);
            txt_file_size = itemView.findViewById(R.id.txt_file_size);
            txt_manual = itemView.findViewById(R.id.txt_manual);
        }

        void Bind(final GoogleDriveFileHolder googleDriveFileHolder, onGoogleDriveBkp mOnGoogleDriveBkp
                , int position) {
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnGoogleDriveBkp.onClick(googleDriveFileHolder, position);
                }
            });
        }
    }

}
