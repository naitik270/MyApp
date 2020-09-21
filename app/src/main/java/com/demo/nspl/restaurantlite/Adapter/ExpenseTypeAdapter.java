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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddExpenseTypeActivity;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/14/2018.
 */

public class ExpenseTypeAdapter extends RecyclerView.Adapter<ExpenseTypeAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsExpenseType> listExpenseTypes = new ArrayList<ClsExpenseType>();
    private SQLiteDatabase db;
    AppCompatActivity activity;

    public ExpenseTypeAdapter(Context c, List<ClsExpenseType> expenseMasters) {
        this.c = c;
        this.listExpenseTypes = expenseMasters;
         }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expense_type_master, parent, false);
        ExpenseTypeAdapter.MyViewHolder myViewHolder = new ExpenseTypeAdapter.MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ClsExpenseType ObjExpenseType= new ClsExpenseType();
        ObjExpenseType= listExpenseTypes.get(position);

//        holder.id.setText((String.valueOf(itemGetSet.getId())));   // Take Id Here with .xml
        holder.lbl_expense_type.setText(ObjExpenseType.getExpense_type_name());
        holder.lbl_active.setText(String.valueOf(ObjExpenseType.getActive()));
        holder.lbl_srno.setText(String.valueOf(ObjExpenseType.getSort_no()));
        holder.lbl_remark.setText(ObjExpenseType.getRemark());

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



                TextView update = (TextView) dialog.findViewById(R.id.txtupdate);
                TextView delete = (TextView) dialog.findViewById(R.id.txtdelete);




                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        dialog.cancel();

                        ClsExpenseType ObjExpenseType = new ClsExpenseType();
                        try {
                            ObjExpenseType = listExpenseTypes.get(position);
                            Intent intent = new Intent(c, AddExpenseTypeActivity.class);
                            intent.putExtra("ID", ObjExpenseType.getExpense_type_id());
                            activity.startActivityForResult(intent, 10001);

                        } catch (Exception e)
                        {
                            e.getMessage();
                            Log.e("UPDATE", e.getMessage());
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
                                ClsExpenseType itemGetSet = new ClsExpenseType();
                                itemGetSet = listExpenseTypes.get(position);
                                int result = 0;
                                String Action="DELETE";
                                ClsExpenseType Obj = new ClsExpenseType(c);
                                Obj.setExpense_type_id(itemGetSet.getExpense_type_id());
                                result = ClsExpenseType.Delete(Obj);

                                Log.e("UpdateTaG", String.valueOf(result));
                                if (result == 1) {

                                    listExpenseTypes.remove(position);
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
        return listExpenseTypes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_expense_type,lbl_active,lbl_srno,lbl_remark;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header=itemView.findViewById(R.id.ll_header);
            lbl_expense_type=itemView.findViewById(R.id.lbl_expense_type);
            lbl_active=itemView.findViewById(R.id.lbl_active);
            lbl_srno=itemView.findViewById(R.id.lbl_srno);
            lbl_remark=itemView.findViewById(R.id.lbl_remark);

        }
    }
}
