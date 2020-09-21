package com.demo.nspl.restaurantlite.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.demo.nspl.restaurantlite.activity.AddCategoryActivity;
import com.demo.nspl.restaurantlite.classes.ClsCategory;

import java.util.ArrayList;
import java.util.List;
import com.demo.nspl.restaurantlite.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 3/9/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsCategory> listCategoty = new ArrayList<ClsCategory>();
    private SQLiteDatabase db;
    AppCompatActivity activity;


    public CategoryAdapter(AppCompatActivity ac, Context c, ArrayList<ClsCategory> catMasters) {
        this.c = c;
        this.listCategoty = catMasters;
        this.activity = ac;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category_master, parent, false);
        CategoryAdapter.MyViewHolder myViewHolder = new CategoryAdapter.MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ClsCategory ObjCategory= new ClsCategory();
        ObjCategory= listCategoty.get(position);

//        holder.id.setText((String.valueOf(itemGetSet.getId())));   // Take Id Here with .xml
        holder.lbl_category_name.setText(ObjCategory.getCat_name());
        holder.lbl_active.setText(String.valueOf(ObjCategory.getActive()));
        holder.lbl_srno.setText(String.valueOf(ObjCategory.getSort_no()));
        holder.lbl_remark.setText(ObjCategory.getRemark());

        holder.ll_header.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(c);
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);



                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);

                RelativeLayout layout_delete =  dialog.findViewById(R.id.lyout_delete);
                RelativeLayout layout_update = dialog.findViewById(R.id.lyout_update);


                layout_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        dialog.cancel();

                        ClsCategory ObjCategory = new ClsCategory();
                        try {
                            ObjCategory = listCategoty.get(position);
                            Intent intent = new Intent(c, AddCategoryActivity.class);
                            intent.putExtra("ID", ObjCategory.getCat_id());
                            activity.startActivityForResult(intent, 10001);

                        } catch (Exception e)
                        {
                            e.getMessage();
                            Log.e("UPDATE", e.getMessage());
                        }
                    }
                });
                dialog.show();
                layout_delete.setOnClickListener(new View.OnClickListener() {
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
                                ClsCategory itemGetSet = new ClsCategory();
                                itemGetSet = listCategoty.get(position);
                                int result = 0;
                                String Action="DELETE";
                                ClsCategory Obj = new ClsCategory(c);
                                Obj.setCat_id(itemGetSet.getCat_id());
                                result = ClsCategory.Delete(Obj);

                                Log.e("UpdateTaG", String.valueOf(result));
                                if (result == 1) {

                                    listCategoty.remove(position);
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

                        // Showing Alert Message
                        alertDialog.show();

                    }
                });


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCategoty.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_category_name,lbl_active,lbl_srno,lbl_remark;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header=itemView.findViewById(R.id.ll_header);
                lbl_category_name=itemView.findViewById(R.id.lbl_category_name);
                lbl_active=itemView.findViewById(R.id.lbl_active);
                lbl_srno=itemView.findViewById(R.id.lbl_srno);
                lbl_remark=itemView.findViewById(R.id.lbl_remark);

        }
    }
}
