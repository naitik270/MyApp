package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
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
import com.demo.nspl.restaurantlite.Interface.OnItemClickListener;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 6/2/2018.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    Context c;
    private View itemview;
    List<ClsInventoryOrderDetail> listOrder = new ArrayList<ClsInventoryOrderDetail>();
    private SQLiteDatabase db;
    AppCompatActivity activity;
    private String mode = "";
    boolean _applyTax = false;
    private OnItemClickListener mOnItemClickListener;


    public OrderDetailAdapter(Context c, List<ClsInventoryOrderDetail> catMasters, String mode, boolean _applyTax) {
        this.c = c;
        this.listOrder = catMasters;
        this.mode = mode;
        this._applyTax = _applyTax;
    }

    public void SetOnClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_details, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ClsInventoryOrderDetail current = listOrder.get(position);


        holder.txt_srno.setText(String.valueOf(position + 1).concat("."));
        holder.item_name.setText(current.getItem().toUpperCase());
        holder.txt_item_code.setText("code: " + current.getItemCode().toUpperCase());
        holder.txt_display_tax_amount.setText("SGST:(" + ClsGlobal.round(current.getSGST(), 2) + "%) CGST:(" +
                ClsGlobal.round(current.getCGST(), 2) +
                "%) IGST:(" + ClsGlobal.round(current.getIGST(), 2) + "%)");
        holder.qty.setText("(qty.) " + ClsGlobal.round(current.getQuantity(), 2));

        double _totalTax = current.getTotalTaxAmount();
        holder.txt_display_tax.setText("Total Tax: \u20B9 " + ClsGlobal.round(_totalTax, 2));

        holder.txt_discount.setText("Discount(" + ClsGlobal.round(current.getDiscount_per(), 2) + "%)" + " \u20B9 " + ClsGlobal.round(current.getDiscount_amt(), 2));
        double _itemAmount = 0.0;

        if (_applyTax) {

            holder.txt_item_rate.setText("(rate) \u20B9 " + ClsGlobal.round(current.getSaleRateWithoutTax(), 2));
            _itemAmount = (current.getSaleRateWithoutTax() * current.getQuantity()) - current.getDiscount_amt();
            holder.qty_amount.setText("\u20B9 " + ClsGlobal.round(_itemAmount, 2));

        } else {

            holder.txt_item_rate.setText("(rate) \u20B9 " + ClsGlobal.round(current.getSaleRate(), 2));
            _itemAmount = (current.getSaleRate() * current.getQuantity()) - current.getDiscount_amt();
            holder.qty_amount.setText("\u20B9 " + ClsGlobal.round(_itemAmount, 2));

        }

        holder.Bind(listOrder.get(position), mOnItemClickListener, position);

    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public void remove(int position) {
        listOrder.remove(position);
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

            if (mode.equalsIgnoreCase("OrderActivity")) {
                btn_remove_item.setVisibility(View.VISIBLE);
            } else if (mode.equalsIgnoreCase("OrderDetailInfoActivity")) {
                btn_remove_item.setVisibility(View.GONE);
            }


        }

        void Bind(final ClsInventoryOrderDetail clsClsInventoryOrderDetail, OnItemClickListener onItemClickListener, int position) {
            btn_remove_item.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnClick(clsClsInventoryOrderDetail, position));
        }

    }


}
