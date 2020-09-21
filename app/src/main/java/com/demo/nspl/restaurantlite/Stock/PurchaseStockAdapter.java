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
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseDetail;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class PurchaseStockAdapter extends RecyclerView.Adapter<PurchaseStockAdapter.MyViewHolder> {

    List<ClsPurchaseDetail> data = new ArrayList<ClsPurchaseDetail>();
    View itemView;
    Context context;
    private LayoutInflater inflater = null;


    PurchaseStockAdapter(Context context) {
        super();

        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsPurchaseDetail> data) {

        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_list_display, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClsPurchaseDetail objClsProductsList = data.get(position);

        holder.txt_name.setText("VENDOR: " + objClsProductsList.getVENDOR_NAME());
        holder.txt_grand_total.setText("TOTAL: \u20B9 " + ClsGlobal.round(objClsProductsList.getGrandTotal(), 2));
        holder.txt_apply_tax.setText("APPLY TAX: " + objClsProductsList.getApplyTax());

        if (objClsProductsList.getApplyTax().equalsIgnoreCase("YES")) {
            holder.txt_igst.setText("\u20B9 " + ClsGlobal.round(objClsProductsList.getIGST(), 2));
            holder.txt_cgst.setText("\u20B9 " + ClsGlobal.round(objClsProductsList.getCGST(), 2));
            holder.txt_sgst.setText("\u20B9 " + ClsGlobal.round(objClsProductsList.getSGST(), 2));
            holder.txt_total_tax_amt.setText("TOTAL TAX: \u20B9 " + ClsGlobal.round(objClsProductsList.getTotalTaxAmount(), 2));
        } else {
            holder.txt_igst.setText("\u20B9 0.00");
            holder.txt_cgst.setText("\u20B9 0.00");
            holder.txt_sgst.setText("\u20B9 0.00");
            holder.txt_total_tax_amt.setText("TOTAL TAX: \u20B9 0.00");
        }

        holder.txt_item_amount.setText("TOTAL: \u20B9 " + ClsGlobal.round(objClsProductsList.getTotalAmount(), 2));
        holder.txt_discount.setText("DISCOUNT: \u20B9 " + ClsGlobal.round(objClsProductsList.getDiscount(), 2));
        holder.txt_unit_price.setText("RATE: \u20B9 " + ClsGlobal.round(objClsProductsList.getRate(), 2));
        holder.txt_code.setText("DATE: " + ClsGlobal.getDDMMYYYY(objClsProductsList.getPurchaseDate()));
        holder.txt_qty.setText("QUANTITY: " + ClsGlobal.round(objClsProductsList.getQuantity(), 2));
        holder.txt_unit.setText("UNIT: " + objClsProductsList.getUnit());
        holder.txt_unit.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_name, txt_grand_total, txt_total_tax_amt, txt_igst, txt_cgst, txt_sgst,
                txt_apply_tax, txt_item_amount, txt_discount, txt_unit_price, txt_qty, txt_unit, txt_code;


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

        }


    }
}
