package com.demo.nspl.restaurantlite.Stock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class SaleStockAdapter extends RecyclerView.Adapter<SaleStockAdapter.MyViewHolder> {

    List<ClsStockSale> data = new ArrayList<ClsStockSale>();
    View itemView;
    Context context;
    private LayoutInflater inflater = null;


    SaleStockAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsStockSale> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sale_list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClsStockSale objClsStockSale = data.get(position);

        holder.txt_name.setText("ORDER# " + objClsStockSale.getOrderNo());
        holder.txt_code.setText("CUSTOMER: " + objClsStockSale.getCustomerName().toUpperCase());
//        holder.txt_grand_total.setText("Amount: " + ClsGlobal.round(objClsStockSale.getAmount(), 2));


        holder.txt_total_tax_amt.setText("TOTAL TAX AMOUNT: \u20B9 " + ClsGlobal.round(objClsStockSale.getTotalTaxAmount(), 2));

        holder.txt_item_amount.setText("QUANTITY: " + ClsGlobal.round(objClsStockSale.getQuantity(),2));
        holder.txt_discount.setText("SALE RATE: \u20B9 " + ClsGlobal.round(objClsStockSale.getSaleRate(), 2));
        holder.txt_unit_price.setText("MOBILE: " + objClsStockSale.getMobileNo());
        double total = objClsStockSale.getQuantity() * objClsStockSale.getSaleRate();
        holder.txt_qty.setText("BILL DATE: " + ClsGlobal.getDDMMYYYY(objClsStockSale.getBillDate()));
        if (objClsStockSale.getApplyTax().equalsIgnoreCase("YES")) {
            holder.txt_Total_tax_Amt.setText("TOTAL TAX: \u20B9 " + ClsGlobal.round(objClsStockSale.getTotalTaxAmount(), 2));
            holder.txt_grand_total.setText("TOTAL: \u20B9 " + ClsGlobal.round(total + objClsStockSale.getTotalTaxAmount(), 2));
        } else {
            holder.txt_Total_tax_Amt.setText("TOTAL TAX: \u20B9 0.00");
            holder.txt_grand_total.setText("TOTAL: \u20B9 " + ClsGlobal.round(total, 2));
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_name, txt_grand_total, txt_total_tax_amt, txt_igst, txt_cgst, txt_sgst,
                txt_apply_tax, txt_item_amount, txt_discount, txt_unit_price, txt_qty, txt_unit, txt_code, txt_Total_tax_Amt;


        public MyViewHolder(View itemView) {
            super(itemView);

            ll_header = itemView.findViewById(R.id.ll_header);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_code = itemView.findViewById(R.id.txt_code);
            txt_unit = itemView.findViewById(R.id.txt_unit);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            txt_unit_price = itemView.findViewById(R.id.txt_unit_price);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            txt_item_amount = itemView.findViewById(R.id.txt_item_amount);
            txt_apply_tax = itemView.findViewById(R.id.txt_apply_tax);
            txt_sgst = itemView.findViewById(R.id.txt_sgst);
            txt_cgst = itemView.findViewById(R.id.txt_cgst);
            txt_igst = itemView.findViewById(R.id.txt_igst);
            txt_total_tax_amt = itemView.findViewById(R.id.txt_total_tax_amt);
            txt_grand_total = itemView.findViewById(R.id.txt_grand_total);
            txt_Total_tax_Amt = itemView.findViewById(R.id.txt_Total_tax_Amt);

        }


    }
}
