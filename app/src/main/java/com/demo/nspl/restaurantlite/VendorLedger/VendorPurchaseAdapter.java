package com.demo.nspl.restaurantlite.VendorLedger;

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
import com.demo.nspl.restaurantlite.Interface.OnProductDetailClick;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class VendorPurchaseAdapter extends RecyclerView.Adapter<VendorPurchaseAdapter.MyViewHolder> {

    Context context;
    List<ClsPurchaseMaster> data = new ArrayList<>();
    OnProductDetailClick onProductDetailClick;
    LayoutInflater inflater = null;
    View itemView;

    public VendorPurchaseAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsPurchaseMaster> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnProductDetailClick onProductDetailClick) {
        this.onProductDetailClick = onProductDetailClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vendor_purchase_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ClsPurchaseMaster objClsPurchaseMaster = data.get(position);

        holder.txt_date.setText(ClsGlobal.getDDMMYYYY(objClsPurchaseMaster.getPurchaseDate()));
        holder.txt_bill_no.setText("Bill No# " + objClsPurchaseMaster.getBillNO());
        holder.txt_remark.setText(objClsPurchaseMaster.getRemark());
        holder.txt_name.setText(objClsPurchaseMaster.getVendorName());
        holder.txt_po_no.setText(String.valueOf(objClsPurchaseMaster.getPurchaseNo()));
        holder.txt_amount.setText("\u20B9 " + ClsGlobal.round(objClsPurchaseMaster.getPurchaseVal(), 2));

        holder.Bind(objClsPurchaseMaster, onProductDetailClick, position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_bill_no, txt_remark, txt_date, txt_po_no, txt_amount, txt_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_bill_no = itemView.findViewById(R.id.txt_bill_no);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_remark = itemView.findViewById(R.id.txt_remark);
            txt_po_no = itemView.findViewById(R.id.txt_po_no);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_name = itemView.findViewById(R.id.txt_name);
        }

        void Bind(ClsPurchaseMaster clsPurchaseMaster, OnProductDetailClick onProductDetailClick, int position) {
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProductDetailClick.OnClick(clsPurchaseMaster, position);
                }
            });

        }
    }
}
