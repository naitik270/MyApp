package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnQuotationClick;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 6/2/2018.
 */

public class QuotationDetailAdapter extends RecyclerView.Adapter<QuotationDetailAdapter.MyViewHolder> {
    Context c;
    private View itemview;
    List<ClsQuotationOrderDetail> data = new ArrayList<ClsQuotationOrderDetail>();
    private SQLiteDatabase db;
    AppCompatActivity activity;
    String mode = "";
    boolean _applyTax = false;
    private OnQuotationClick mOnItemClickListener;

    public QuotationDetailAdapter(Context c, List<ClsQuotationOrderDetail> data,
                                  String mode, boolean _applyTax) {
        this.c = c;
        this.data = data;
        this.mode = mode;
        this._applyTax = _applyTax;
    }

    public void SetOnClickListener(OnQuotationClick onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_details, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ClsQuotationOrderDetail current = data.get(position);

        holder.txt_srno.setText(String.valueOf(position + 1).concat("."));
        holder.item_name.setText(current.getItem().toUpperCase());
        holder.txt_item_code.setText("code: " + current.getItemCode().toUpperCase());
        holder.qty.setText("(qty.) " + ClsGlobal.round(current.getQuantity(), 2));


        holder.txt_display_tax_amount.setText("SGST:(" + ClsGlobal.round(current.getSGST(), 2) + "%) CGST:(" +
                ClsGlobal.round(current.getCGST(), 2) +
                "%) IGST:(" + ClsGlobal.round(current.getIGST(), 2) + "%)");


        double _totalTax = current.getTotalTaxAmount();
        holder.txt_display_tax.setText("Total Tax: \u20B9 " + ClsGlobal.round(_totalTax, 2));

        holder.txt_discount.setText("Discount(" + current.getDiscount_per() + "%)" + " \u20B9 " + current.getDiscount_amt());
        double _itemAmount = 0.0;

        if (_applyTax) {

            holder.txt_item_rate.setText("(rate) \u20B9 " + ClsGlobal.round(current.getSaleRateWithoutTax(), 2));
            _itemAmount = (current.getSaleRateWithoutTax() * current.getQuantity()) - current.getDiscount_amt();
            holder.qty_amount.setText("\u20B9 " + ClsGlobal.formatNumber(2, _itemAmount));

        } else {

            holder.txt_item_rate.setText("(rate) \u20B9 " + ClsGlobal.round(current.getSaleRate(), 2));
            _itemAmount = (current.getSaleRate() * current.getQuantity()) - current.getDiscount_amt();
            holder.qty_amount.setText("\u20B9 " + ClsGlobal.formatNumber(2, _itemAmount));

        }

        holder.Bind(data.get(position), mOnItemClickListener, position);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_srno, item_name, qty, txt_item_rate, qty_amount;
        TextView txt_discount, txt_item_code;
        TextView txt_display_tax;
        TextView txt_display_tax_amount;
        ImageView btn_remove_item;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_srno = itemView.findViewById(R.id.txt_srno);
            item_name = itemView.findViewById(R.id.item_name);
            qty = itemView.findViewById(R.id.qty);
            txt_item_rate = itemView.findViewById(R.id.txt_item_rate);
            qty_amount = itemView.findViewById(R.id.qty_amount);
            btn_remove_item = itemView.findViewById(R.id.btn_remove_item);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            txt_item_code = itemView.findViewById(R.id.txt_item_code);
            txt_display_tax = itemView.findViewById(R.id.txt_display_tax);
            txt_display_tax_amount = itemView.findViewById(R.id.txt_display_tax_amount);

            if (mode.equalsIgnoreCase("OrderList") ||
                    mode.equalsIgnoreCase("QuotationList")) {
                btn_remove_item.setVisibility(View.GONE);
            }


        }

        void Bind(final ClsQuotationOrderDetail clsQuotationOrderDetail, OnQuotationClick onItemClickListener, int position) {
            btn_remove_item.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnClick(clsQuotationOrderDetail, position));
        }

    }


}
