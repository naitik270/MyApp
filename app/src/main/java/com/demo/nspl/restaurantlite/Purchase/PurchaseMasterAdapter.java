package com.demo.nspl.restaurantlite.Purchase;

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

public class PurchaseMasterAdapter extends RecyclerView.Adapter<PurchaseMasterAdapter.MyViewHolder> {

    Context context;
    private View itemview;
    List<ClsPurchaseDetail> data = new ArrayList<>();
    OnPurchaseDetailClick onPurchaseDetailClick;
    private LayoutInflater inflater = null;
    View itemView;

    public PurchaseMasterAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    public void AddItems(List<ClsPurchaseDetail> list) {

        this.data = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnPurchaseDetailClick onPurchaseDetailClick) {
        this.onPurchaseDetailClick = onPurchaseDetailClick;

    }


    public interface OnPurchaseDetailClick {


        void OnClick(ClsPurchaseDetail clsPurchaseDetail, int position);

    }


    public void RemoveItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_purchase_details, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ClsPurchaseDetail objClsPurchaseDetail = data.get(position);

        holder.lbl_Date.setText("DATE: " + ClsGlobal.getPurchaseDateDDMMM(objClsPurchaseDetail.getPurchaseDate().toUpperCase()));
        holder.lbl_Vendor.setText("VENDOR: " + objClsPurchaseDetail.getVENDOR_NAME());
        holder.lbl_Payment_Details.setText("BILL NO# " + objClsPurchaseDetail.getBillNO());
        holder.lbl_Mode.setText("PUNCHING: " + ClsGlobal.getEntryDateFormat(objClsPurchaseDetail.getEntryDate()));


        if (objClsPurchaseDetail.getRemark() != null && !objClsPurchaseDetail.getRemark().isEmpty()) {
            holder.lbl_Amount.setVisibility(View.VISIBLE);
            holder.lbl_Amount.setText("REMARK: " + objClsPurchaseDetail.getRemark());
        } else {
            holder.lbl_Amount.setVisibility(View.GONE);
        }

        holder.Bind(objClsPurchaseDetail, onPurchaseDetailClick, position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;

        TextView lbl_Date, lbl_Vendor, lbl_Mode, lbl_Payment_Details, lbl_Amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);

            lbl_Date = itemView.findViewById(R.id.lbl_Date);
            lbl_Amount = itemView.findViewById(R.id.lbl_Amount);
            lbl_Payment_Details = itemView.findViewById(R.id.lbl_Payment_Details);
            lbl_Mode = itemView.findViewById(R.id.lbl_Mode);
            lbl_Vendor = itemView.findViewById(R.id.lbl_Vendor);
        }

        void Bind(ClsPurchaseDetail clsPurchaseDetail,
                  OnPurchaseDetailClick onPurchaseDetailClick,
                  int position) {
            ll_header.setOnClickListener(v -> onPurchaseDetailClick.OnClick(clsPurchaseDetail, position));

        }
    }
}
