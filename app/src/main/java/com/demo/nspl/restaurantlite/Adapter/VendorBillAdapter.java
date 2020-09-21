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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import com.demo.nspl.restaurantlite.activity.AddExpenseActivity;
import com.demo.nspl.restaurantlite.activity.VendorBillDescriptionActivity;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Desktop on 4/19/2018.
 */

public class VendorBillAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    Context c;
    List<ClsExpenseMasterNew> list_expense = new ArrayList<ClsExpenseMasterNew>();
    private SQLiteDatabase db;
    AppCompatActivity activity;
    private LayoutInflater inflater = null;
    private Dialog dialog;

    public VendorBillAdapter(AppCompatActivity ac, Context c, List<ClsExpenseMasterNew> stock) {
        this.c = c;
        this.list_expense = stock;
        this.activity = ac;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.card_vendor_bill_header, parent, false);

            holder.txt_date_year = convertView.findViewById(R.id.txt_date_year);
            holder.txt_total = convertView.findViewById(R.id.txt_month_total_amount);

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

//        String headerText = String.valueOf(data.get((int) getHeaderId(position)));

        ClsExpenseMasterNew objClsExpenseMasterNew = list_expense.get(position);

        String _ChangeDateFormat = ClsGlobal.getMonthYear(objClsExpenseMasterNew.getReceipt_date());

        Double finalAmount = 0.0;
        for (ClsExpenseMasterNew _ObjExp : list_expense) {
            if (objClsExpenseMasterNew.getMonthUniqueIndex().equalsIgnoreCase(_ObjExp.getMonthUniqueIndex())) {
                //assign unique index/id/position
                finalAmount = finalAmount + _ObjExp.getGRAND_TOTAL();
            }
        }

        Log.e("Year", _ChangeDateFormat);
        Log.e("Year", objClsExpenseMasterNew.getReceipt_date());

        holder.txt_date_year.setText(_ChangeDateFormat);
        holder.txt_total.setText(String.valueOf(finalAmount).concat(" \u20B9"));


        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

//        String _StickyListHeader = "" + data.get(position).getReceipt_date().subSequence(0, 8);
//        String headerText = "" + _ChangeDateFormat.subSequence(0, 1).charAt(0);
//        return Long.parseLong(_StickyListHeader);
//        return position;
//        ClsExpenseMasterNew objClsExpenseMasterNew = data.get(position);
//
//        String _GLOBAL = ClsGlobalDatabase.getMonthIndex(position);//01-0,01-1,02,02,01,02,02-
//        Toast.makeText(context, "Global--" + _GLOBAL, Toast.LENGTH_SHORT).show();
//        Log.e("_GLOBAL-", "_GLOBAL- " + _GLOBAL);
//
//
//        String _ChangeDateFormat = ClsGlobalDatabase.getChangeDateFormatAllExp(objClsExpenseMasterNew.getReceipt_date());
//        return _ChangeDateFormat.subSequence(0, 1).charAt(0);


        return list_expense.get(position).getMonthUniqueIndex().charAt(0);


    }

    @Override
    public int getCount() {
        return list_expense.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ClsExpenseMasterNew objClsExpenseMasterNew = list_expense.get(i);
        holder = new ViewHolder();
        view = inflater.inflate(R.layout.card_vendorbill_details, viewGroup, false);
        holder.linear_layout = view.findViewById(R.id.linear_layout);

        holder.txt_date = view.findViewById(R.id.txt_date);
        holder.txt_count = view.findViewById(R.id.txt_srno);
        holder.txt_total_amount = view.findViewById(R.id.txt_total_amount);
        holder.txt_amount = view.findViewById(R.id.txt_amount);
        holder.txt_exp_type = view.findViewById(R.id.txt_exp_type);
        holder.txt_receipt_no = view.findViewById(R.id.txt_rec_no);
        holder.txt_remark = view.findViewById(R.id.txt_remark);
        holder.txt_vendor_name = view.findViewById(R.id.txt_vendor_name);
        String _IndexVal = String.valueOf(i + 1).concat(".");
        final ClsExpenseMasterNew finalObjExpense2 = objClsExpenseMasterNew;
        final ClsExpenseMasterNew finalObjExpense = objClsExpenseMasterNew;
        holder.linear_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Dialog dialog = new Dialog(c);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_vendor_details);
                dialog.setCancelable(true);


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);


                RelativeLayout layout_view_details = dialog.findViewById(R.id.layout_view_details);
                RelativeLayout lyout_edit = dialog.findViewById(R.id.lyout_edit);
                RelativeLayout layout_delete = dialog.findViewById(R.id.layout_delete);

                layout_view_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();

                        ClsInventoryStock clsInventoryStock = new ClsInventoryStock();
                        Intent intent = new Intent(c, VendorBillDescriptionActivity.class);
                        intent.putExtra("ID", finalObjExpense.getExpense_id());
                        c.startActivity(intent);
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

                                ClsExpenseMasterNew itemGetSet = new ClsExpenseMasterNew();
                                itemGetSet = list_expense.get(i);
                                int result = 0;
                                String Action = "DELETE";
                                ClsExpenseMasterNew Obj = new ClsExpenseMasterNew(c);
                                Obj.setExpense_id(itemGetSet.getExpense_id());
                                result = ClsExpenseMasterNew.Delete(Obj);
                                Log.e("UpdateTaG", String.valueOf(result));
                                if (result == 1) {

                                    list_expense.remove(i);
                                    notifyDataSetInvalidated();
//                                    notifyItemRemoved(position);
//                                    notifyItemRangeChanged(position, getItemCount());


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


                lyout_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        dialog.cancel();

                        Intent intent = new Intent(c, AddExpenseActivity.class);
                        intent.putExtra("ExpenseId", finalObjExpense2.getExpense_id());
                        intent.putExtra("_TYPE", "InventoryStock");
                        c.startActivity(intent);

                    }
                });


                return false;
            }
        });
        Log.e("Index is", _IndexVal);
        holder.txt_count.setText(_IndexVal);


        String _ChangeDateFormat = ClsGlobal.getChangeDateFormatAllExp(objClsExpenseMasterNew.getReceipt_date());


        holder.txt_date.setText(_ChangeDateFormat);
        holder.txt_total_amount.setText(" " + String.valueOf(objClsExpenseMasterNew.getGRAND_TOTAL()).concat(" \u20B9"));
        holder.txt_vendor_name.setText(objClsExpenseMasterNew.getVendor_name());
        holder.txt_exp_type.setText(objClsExpenseMasterNew.getExpense_type_name());

//        if (!objClsExpenseMasterNew.getExpense_type_name().equalsIgnoreCase("SALARY")) {
//            holder.txt_vendor_name.setText(objClsExpenseMasterNew.getVendor_name());
//        }
//        else {
//            holder.txt_vendor_name.setText(objClsExpenseMasterNew.getEmployee_name());
//        }
        holder.txt_exp_type.setText(objClsExpenseMasterNew.getExpense_type_name());
        holder.txt_receipt_no.setText(objClsExpenseMasterNew.getReceipt_no());
        holder.txt_remark.setText(objClsExpenseMasterNew.getRemark());

        view.setTag(holder);

        return view;
    }

    class ViewHolder {
        TextView txt_date, txt_total_amount, txt_exp_type, txt_vendor_name, txt_amount, txt_receipt_no, txt_remark, txt_count;
        LinearLayout linear_layout;
    }

    class HeaderViewHolder {
        TextView txt_date_year, txt_total;
    }

}
