package com.demo.nspl.restaurantlite.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddExpenseActivity;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 20-04-2018.
 */

public class DisplayBarDataAdapter extends RecyclerView.Adapter<DisplayBarDataAdapter.MyViewHolder> {
    View itemView;
    Context context;
    List<ClsExpenseMasterNew> data = new ArrayList<ClsExpenseMasterNew>();
    private LayoutInflater inflater = null;

    public DisplayBarDataAdapter(Context context, List<ClsExpenseMasterNew> data) {
        super();
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_bardata_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ClsExpenseMasterNew objClsExpenseMasterNew = data.get(position);


        String _ChangeDateFormat = ClsGlobal.getChangeDateFormat(objClsExpenseMasterNew.getReceipt_date());
        String _IndexVal = String.valueOf(position + 1).concat(".");
        holder.txt_count.setText(_IndexVal);
        holder.txt_date.setText(_ChangeDateFormat);


        Double _Amount = 0.0;
        _Amount = Double.valueOf(String.valueOf(objClsExpenseMasterNew.getGRAND_TOTAL()));

//        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        holder.txt_amount.setText("\u20B9 " + ClsGlobal.round(_Amount, 2));

        if (!objClsExpenseMasterNew.getExpense_type_name().equalsIgnoreCase("SALARY")) {
            holder.txt_name.setText("To: " + objClsExpenseMasterNew.getVendor_name());
        } else {
            holder.txt_name.setText("To: " + objClsExpenseMasterNew.getEmployee_name());
        }
        holder.txt_exp_type.setText("Type: " + objClsExpenseMasterNew.getExpense_type_name());
        holder.txt_remark.setText("Remark: " + objClsExpenseMasterNew.getRemark());

        holder.ll_header.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);
                dialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);

                TextView txtupdate = dialog.findViewById(R.id.txtupdate);
                TextView txtdelete = dialog.findViewById(R.id.txtdelete);

                txtupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        dialog.cancel();
                        ClsExpenseMasterNew itemClsExpenseMasterNew = new ClsExpenseMasterNew();
                        try {
                            itemClsExpenseMasterNew = data.get(position);
                            Intent intent = new Intent(context, AddExpenseActivity.class);
                            intent.putExtra("_ID", itemClsExpenseMasterNew.getExpense_id());
                            intent.putExtra("_TYPE", "update");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }
                });
                txtdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        dialog.cancel();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Confirm Delete...");
                        alertDialog.setMessage(Html.fromHtml("<b>Expense Type-</b>" + objClsExpenseMasterNew.getExpense_type_name()));

                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ClsExpenseMasterNew itemClsExpenseMasterNew = new ClsExpenseMasterNew();
                                itemClsExpenseMasterNew = data.get(position);
                                int result = 1;
                                if (result == 0) {
                                    //NO RECORD DELETED
                                    Toast.makeText(context, "NO RECORD DELETED", Toast.LENGTH_LONG).show();
                                } else if (result == 1) {

                                    ClsExpenseMasterNew obj = new ClsExpenseMasterNew();
                                    obj.setExpense_id(itemClsExpenseMasterNew.getExpense_id());
                                    result = ClsExpenseMasterNew.Delete(obj);
                                    Log.e("DELETE---- ", String.valueOf(result));

                                    data.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());

                                    Toast.makeText(context, "RECORD DELETED", Toast.LENGTH_LONG).show();
                                } else {
                                    //error
                                    Toast.makeText(context, "ERROR FOR DELETE RECORD", Toast.LENGTH_LONG).show();
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


                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date, txt_amount, txt_exp_type, txt_name, txt_remark, txt_count;
        LinearLayout ll_header;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_exp_type = itemView.findViewById(R.id.txt_exp_type);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_remark = itemView.findViewById(R.id.txt_remark);

        }
    }
}
