package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Abhishek on 02-05-2018.
 */

public class DisplayAllExpensesAdapterNew extends BaseAdapter implements StickyListHeadersAdapter {


    Context context;
    List<ClsExpenseMasterNew> data = new ArrayList<ClsExpenseMasterNew>();
    private LayoutInflater inflater = null;


    public DisplayAllExpensesAdapterNew(List<ClsExpenseMasterNew> data, Context context) {

        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_row_allexp, parent, false);
            holder.txt_header_month = convertView.findViewById(R.id.txt_header_month);
            holder.txt_total = convertView.findViewById(R.id.txt_total);

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

//        String headerText = String.valueOf(data.get((int) getHeaderId(position)));

        ClsExpenseMasterNew objClsExpenseMasterNew = data.get(position);

        String _ChangeDateFormat = ClsGlobal.getMonthYear(objClsExpenseMasterNew.getReceipt_date());//

        Double finalAmount = 0.0;
        String _FinalAmount = "";

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        for (ClsExpenseMasterNew _ObjExp : data) {
            if (objClsExpenseMasterNew.getMonthUniqueIndex().equalsIgnoreCase(_ObjExp.getMonthUniqueIndex())) {
                //assign unique index/id/position
                finalAmount = finalAmount + _ObjExp.getGRAND_TOTAL();
                _FinalAmount = decimalFormat.format(finalAmount);
            }
        }

//        String _GLOBAL = ClsGlobalDatabase.getMonthIndex(data.get(position));//01-0,01-1,02,02,01,02,02-
//        Log.e("headerText", "_GLOBAL : " + "" + _GLOBAL);
        //String headerText = "" + _ChangeDateFormat.subSequence(0, 1).charAt(0);
//        String headerText = "" + objClsExpenseMasterNew.getExpense_type_name().subSequence(0, 1).charAt(0);
        //Log.e("headerText", "Type : " + "" + _ChangeDateFormat.subSequence(0, 1).charAt(0));

        holder.txt_header_month.setText(_ChangeDateFormat);
        Log.e("getReceipt_date", "getReceipt_date : " + objClsExpenseMasterNew.getReceipt_date());
        holder.txt_total.setText("".concat("\u20B9 ") + String.valueOf(_FinalAmount));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return data.get(position).getMonthUniqueIndex().charAt(0);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ClsExpenseMasterNew objClsExpenseMasterNew = data.get(i);
        try {
//            Gson gson = new Gson();
//            String jsonInString = gson.toJson(objClsExpenseMasterNew);
//            Log.d("Result", "All Exp:- " + jsonInString);
        } catch (Exception e) {
            e.getMessage();
        }

//        Log.e("ExpenseName", "getView: " + "outside view-" + objClsExpenseMasterNew.getExpense_type_name());
//        Log.e("ExpenseName", "getView: " + "outside view-" + " " + String.valueOf(objClsExpenseMasterNew.getGRAND_TOTAL()));
//        Log.e("ExpenseName", "getView: " + "outside view-" + "Receipt#: " + objClsExpenseMasterNew.getReceipt_no());
//        Log.e("ExpenseName", "getView: " + "outside view-" + "Remark: " + objClsExpenseMasterNew.getRemark());


//            Log.e("ExpenseName", "getView: "+"inside view" + objClsExpenseMasterNew.getExpense_type_name() );

        holder = new ViewHolder();
        view = inflater.inflate(R.layout.display_all_expenses_row, viewGroup, false);
        holder.ll_header = view.findViewById(R.id.ll_header);

        holder.txt_count = view.findViewById(R.id.txt_count);


        holder.txt_name = view.findViewById(R.id.txt_name);


        holder.txt_date_month = view.findViewById(R.id.txt_date_month);
        holder.txt_amount = view.findViewById(R.id.txt_amount);
        holder.txt_exp_type = view.findViewById(R.id.txt_exp_type);
        holder.txt_receipt_no = view.findViewById(R.id.txt_receipt_no);
        holder.txt_remark = view.findViewById(R.id.txt_remark);


        String _ChangeDateFormat = ClsGlobal.getChangeDateFormatAllExp(objClsExpenseMasterNew.getReceipt_date());

        String _IndexVal = String.valueOf(i + 1).concat(". ");
        holder.txt_count.setText(_IndexVal);


        int _Index = 0;
        for (ClsExpenseMasterNew _ObjExp : data) {
            if (objClsExpenseMasterNew.getMonthUniqueIndex().equalsIgnoreCase(_ObjExp.getMonthUniqueIndex())) {
                //assign unique index/id/position
                _Index = _Index + 1;
                Log.e("INDEX", "No" + _Index);
            }
            _Index = 0;
        }


        holder.txt_date_month.setText(_ChangeDateFormat);

        double _Amount = 0.0;

        _Amount = Double.valueOf(String.valueOf(objClsExpenseMasterNew.getGRAND_TOTAL()));

        holder.txt_amount.setText("\u20B9 " + ClsGlobal.round(_Amount, 2));

        if (!objClsExpenseMasterNew.getExpense_type_name().equalsIgnoreCase("SALARY")) {
            holder.txt_name.setText("TO: " + objClsExpenseMasterNew.getVendor_name());
        } else {
            holder.txt_name.setText("To: " + objClsExpenseMasterNew.getEmployee_name());
        }

        holder.txt_exp_type.setText("TYPE: " + objClsExpenseMasterNew.getExpense_type_name());
        holder.txt_receipt_no.setText("RECEIPT#: " + objClsExpenseMasterNew.getReceipt_no());

        if (objClsExpenseMasterNew.getRemark() != null && !objClsExpenseMasterNew.getRemark().equalsIgnoreCase("")) {
            holder.txt_remark.setVisibility(View.VISIBLE);
            holder.txt_remark.setText("REMARK: " + objClsExpenseMasterNew.getRemark());
        } else {
            holder.txt_remark.setVisibility(View.GONE);
        }

        view.setTag(holder);

        return view;
    }

    class ViewHolder {
        TextView txt_month, txt_total_amt, txt_date_month, txt_exp_type, txt_name, txt_amount, txt_receipt_no, txt_remark, txt_count;
        LinearLayout ll_header;
    }

    class HeaderViewHolder {
        TextView txt_header_month, txt_total;
    }

}
