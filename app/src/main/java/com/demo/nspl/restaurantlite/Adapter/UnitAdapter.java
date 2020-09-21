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
import android.widget.Toast;


import com.demo.nspl.restaurantlite.activity.AddUnitActivity;
import com.demo.nspl.restaurantlite.classes.ClsUnit;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 3/14/2018.
 */

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsUnit> listUnits = new ArrayList<ClsUnit>();
    private SQLiteDatabase db;
    AppCompatActivity activity;

    public UnitAdapter(AppCompatActivity ac, Context c, ArrayList<ClsUnit> unitMasters) {
        this.c = c;
        this.listUnits = unitMasters;
        this.activity = ac;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_unit_master, parent, false);
        UnitAdapter.MyViewHolder myViewHolder = new UnitAdapter.MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ClsUnit ObjUnit= new ClsUnit();
        ObjUnit= listUnits.get(position);

//        holder.id.setText((String.valueOf(itemGetSet.getId())));   // Take Id Here with .xml
        holder.lbl_unit_name.setText(ObjUnit.getUnit_name());
        holder.lbl_active.setText(String.valueOf(ObjUnit.getActive()));
        holder.lbl_srno.setText(String.valueOf(ObjUnit.getSort_no()));
        holder.lbl_remark.setText(ObjUnit.getRemark());

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
                        Toast.makeText(c, "Update", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        dialog.cancel();

                        ClsUnit ObjUnit = new ClsUnit();
                        try {
                            ObjUnit = listUnits.get(position);
                            Intent intent = new Intent(c, AddUnitActivity.class);
                            intent.putExtra("ID", ObjUnit.getUnit_id());
                            activity.startActivityForResult(intent, 10001);

                        } catch (Exception e)
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
                        alertDialog.setTitle("Confirm...");
                        alertDialog.setMessage("Are you sure you want delete?");
                        alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ClsUnit itemGetSet = new ClsUnit();
                                itemGetSet = listUnits.get(position);
                                int result = 0;
                                String Action="DELETE";
                                ClsUnit Obj = new ClsUnit(c);
                                Obj.setUnit_id(itemGetSet.getUnit_id());
                                result = ClsUnit.Delete(Obj);

                                Log.e("UpdateTaG", String.valueOf(result));
                                if (result == 1) {

                                    listUnits.remove(position);
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
        return listUnits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_unit_name,lbl_active,lbl_srno,lbl_remark;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header=itemView.findViewById(R.id.ll_header);
            lbl_unit_name=itemView.findViewById(R.id.lbl_unit_name);
            lbl_active=itemView.findViewById(R.id.lbl_active);
            lbl_srno=itemView.findViewById(R.id.lbl_srno);
            lbl_remark=itemView.findViewById(R.id.lbl_remark);

        }
    }
}
