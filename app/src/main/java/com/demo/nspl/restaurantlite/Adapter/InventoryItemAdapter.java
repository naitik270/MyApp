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


import com.demo.nspl.restaurantlite.activity.AddInventoryItemActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryItem;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 3/16/2018.
 */

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsInventoryItem> listInventoryitems= new ArrayList<ClsInventoryItem>();
    private SQLiteDatabase db;
    AppCompatActivity activity;

    public InventoryItemAdapter(AppCompatActivity ac, Context c, ArrayList<ClsInventoryItem> itemMasters) {
        this.c = c;
        this.listInventoryitems = itemMasters;
        this.activity = ac;
    }

    @NonNull
    @Override
    public InventoryItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inventory_item_master, parent, false);
        InventoryItemAdapter.MyViewHolder myViewHolder = new InventoryItemAdapter.MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemAdapter.MyViewHolder holder, final int position) {
        ClsInventoryItem ObjClsInventoryItem= new ClsInventoryItem();
        ObjClsInventoryItem= listInventoryitems.get(position);
        holder.lbl_inventory_item_name.setText(ObjClsInventoryItem.getInventory_item_name());
        holder.lbl_unit_name.setText(ObjClsInventoryItem.getUnit_name());
        holder.lbl_active.setText(String.valueOf(ObjClsInventoryItem.getActive()));
        holder.lbl_remark.setText(ObjClsInventoryItem.getRemark());
        holder.lbl_min.setText(String.valueOf(ObjClsInventoryItem.getMin_qty()));
        holder.lbl_max.setText(String.valueOf(ObjClsInventoryItem.getMax_stock()));

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

                RelativeLayout lyout_update = dialog.findViewById(R.id.lyout_update);
                RelativeLayout lyout_delete = dialog.findViewById(R.id.lyout_delete);


                lyout_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();

                        ClsInventoryItem objInventoryItem = new ClsInventoryItem();
                        try {
                            objInventoryItem = listInventoryitems.get(position);
                            Intent intent = new Intent(c, AddInventoryItemActivity.class);
                            intent.putExtra("ID", objInventoryItem.getInventory_item_id());
                            activity.startActivityForResult(intent, 10001);

                        }
                        catch (Exception e)
                        {
                            e.getMessage();
                            Log.e("UPDATE", e.getMessage());
                        }
                    }
                });
                dialog.show();
                lyout_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                        alertDialog.setTitle("Confirm Delete...");
                        alertDialog.setMessage("Are you sure you want delete?");
                        alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ClsInventoryItem itemGetSet = new ClsInventoryItem();
                                itemGetSet = listInventoryitems.get(position);
                                int result = 0;
                                String Action="DELETE";
                                ClsInventoryItem Obj = new ClsInventoryItem(c);
                                Obj.setInventory_item_id(itemGetSet.getInventory_item_id());
                                result = ClsInventoryItem.Delete(Obj);  //  Delete Query in ClsCategory class
                                if (result == 1) {

                                    listInventoryitems.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                }


                                Log.e("UpdateTaG", String.valueOf(result));

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
        return listInventoryitems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_active,lbl_srno,lbl_remark,lbl_unit_name,lbl_inventory_item_name,lbl_min,lbl_max;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header=itemView.findViewById(R.id.ll_header);
            lbl_unit_name=itemView.findViewById(R.id.lbl_unit_name);
            lbl_inventory_item_name=itemView.findViewById(R.id.lbl_inventory_item_name);
            lbl_active=itemView.findViewById(R.id.lbl_active);
            lbl_srno=itemView.findViewById(R.id.lbl_srno);
            lbl_remark=itemView.findViewById(R.id.lbl_remark);
            lbl_min=itemView.findViewById(R.id.lbl_min);
            lbl_max=itemView.findViewById(R.id.lbl_max);
        }
    }
}
