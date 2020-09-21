package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 3/21/2018.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.Myviewholder>{
    Context c;
    private View itemview;
    List<ClsExpenseMasterNew> listExpense= new ArrayList<ClsExpenseMasterNew>();
    private SQLiteDatabase db;
    AppCompatActivity activity;

    public ExpenseAdapter(AppCompatActivity ac, Context c, ArrayList<ClsExpenseMasterNew> empMasters) {
        this.c = c;
        this.listExpense = empMasters;
        this.activity = ac;
    }


    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expense_master, parent, false);
        ExpenseAdapter.Myviewholder myViewHolder = new ExpenseAdapter.Myviewholder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, final int position) {
        ClsExpenseMasterNew objExpense= new ClsExpenseMasterNew();
        objExpense= listExpense.get(position);


//        holder.lbl_expense_type.setText(objExpense.getExpense_type_name());
//        holder.lbl_amount.setText(String.valueOf(objExpense.getAmount()));
//        holder.lbl_bill_no.setText(String.valueOf(objExpense.getBill_receipt_no()));
//        holder.lbl_tax1.setText(String.valueOf(objExpense.getTax1()));
//        holder.lbl_tax2.setText(String.valueOf(objExpense.getTax2()));
//        holder.lbl_tax3.setText(String.valueOf(objExpense.getTax3()));
//        holder.lbl_othertax.setText(objExpense.getOthertax());
//        holder.lbl_entry_date.setText(objExpense.getEntry_date());
//        holder.lbl_transaction_date.setText(objExpense.getTransaction_date());
//        holder.lbl_remark.setText(objExpense.getRemark());

//        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                final Dialog dialog = new Dialog(c);
//                // Include dialog.xml file
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dialog);
//                dialog.setCancelable(true);
//                TextView update = (TextView) dialog.findViewById(R.id.txtupdate);
//                TextView delete = (TextView) dialog.findViewById(R.id.txtdelete);
//
//
//
//
//                update.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//                        dialog.cancel();
//
//                        ClsExpense Obj = new ClsExpense();
//                        try {
//                            Obj = listExpense.get(position);
//                            Intent intent = new Intent(c, AddExpenseActivity.class);
//                            intent.putExtra("ID", Obj.getExpense_id());
//                            activity.startActivityForResult(intent, 10001);
//
//                        } catch (Exception e)
//                        {
//                            e.getMessageSales();
//                            Log.e("UPDATE", e.getMessageSales());
//                        }
//                    }
//                });
//                dialog.show();
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        dialog.cancel();
//
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
//                        alertDialog.setTitle("Confirm...");
//                        alertDialog.setMessage("Are you sure you want delete?");
//                        alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
//                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClsExpense itemGetSet = new ClsExpense();
//                                itemGetSet = listExpense.get(position);
//                                int result = 0;
//                                String Action="DELETE";
//                                ClsExpense Obj = new ClsExpense(c);
//                                Obj.setExpense_id(itemGetSet.getExpense_id());
//                                result = ClsExpense.Delete(Obj);
//
//                                Log.e("UpdateTaG", String.valueOf(result));
//                                if (result == 1) {
//
//                                    listExpense.remove(position);
//                                    notifyItemRemoved(position);
//                                    notifyItemRangeChanged(position, getItemCount());
//                                }
//                            }
//                        });
//                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                dialog.cancel();
//                            }
//                        });
//
//                        alertDialog.show();
//
//                    }
//                });
//
//
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listExpense.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView lbl_expense_type,lbl_amount,lbl_bill_no,lbl_tax1,lbl_tax2,lbl_tax3,lbl_othertax,lbl_entry_date
                ,lbl_transaction_date,lbl_remark;


        public Myviewholder(View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv);
            lbl_expense_type=itemView.findViewById(R.id.lbl_expense_type);
            lbl_amount=itemView.findViewById(R.id.lbl_amount);
            lbl_bill_no=itemView.findViewById(R.id.lbl_bill_no);
            lbl_tax1=itemView.findViewById(R.id.lbl_tax1);
            lbl_tax2=itemView.findViewById(R.id.lbl_tax2);
            lbl_tax3=itemView.findViewById(R.id.lbl_tax3);
            lbl_othertax=itemView.findViewById(R.id.lbl_othertax);
            lbl_entry_date=itemView.findViewById(R.id.lbl_entry_date);
            lbl_transaction_date=itemView.findViewById(R.id.lbl_transaction_date);
            lbl_remark=itemView.findViewById(R.id.lbl_remark);

        }
    }
}
