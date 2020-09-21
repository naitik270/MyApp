package com.demo.nspl.restaurantlite.Purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnProductDetailClick;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailAdapter extends RecyclerView.Adapter<PurchaseDetailAdapter.MyViewHolder> {

    Context context;
    private View itemview;
    List<ClsPurchaseMaster> data = new ArrayList<>();
    OnProductDetailClick onProductDetailClick;
    private LayoutInflater inflater = null;
    View itemView;

    public PurchaseDetailAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    public void AddItems(List<ClsPurchaseMaster> list) {

        this.data = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnProductDetailClick onProductDetailClick) {
        this.onProductDetailClick = onProductDetailClick;

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

        final ClsPurchaseMaster objClsPurchaseMaster = data.get(position);

        holder.lbl_Date.setText("DATE: " + ClsGlobal.getPurchaseDateDDMMM(objClsPurchaseMaster.getPurchaseDate().toUpperCase()));
        holder.lbl_Vendor.setText("VENDOR: " + objClsPurchaseMaster.getVendorName().toUpperCase());
        holder.lbl_Payment_Details.setText("BILL NO# " + objClsPurchaseMaster.getBillNO());
        holder.lbl_Mode.setText("PUNCHING: " + ClsGlobal.getEntryDateFormat(objClsPurchaseMaster.getEntryDate()));
        holder.txt_total_amount.setText("TOTAL AMOUNT: \u20B9 " + ClsGlobal.round(objClsPurchaseMaster.getPurchaseVal(), 2));
        holder.txt_total_tax.setText("TOTAL TAX: \u20B9 " + ClsGlobal.round(objClsPurchaseMaster.get_totalTax(), 2));


        if (objClsPurchaseMaster.getRemark() != null && !objClsPurchaseMaster.getRemark().isEmpty()) {
            holder.lbl_Amount.setVisibility(View.VISIBLE);
            holder.lbl_Amount.setText("REMARK: " + objClsPurchaseMaster.getRemark());
        } else {
            holder.lbl_Amount.setVisibility(View.GONE);
        }

        holder.Bind(objClsPurchaseMaster, onProductDetailClick, position);
        holder.viewImgClick(objClsPurchaseMaster, onItemClick, position);
    }

    private OnItemClick onItemClick;

    public void setOnViewImg(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void onItemClick(ClsPurchaseMaster clsPurchaseMaster, int position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;

        TextView lbl_Date, lbl_Vendor, lbl_Mode, lbl_Payment_Details, lbl_Amount;
        TextView txt_total_amount;
        TextView txt_total_tax;
        ImageView iv_photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);

            lbl_Date = itemView.findViewById(R.id.lbl_Date);
            lbl_Amount = itemView.findViewById(R.id.lbl_Amount);
            lbl_Payment_Details = itemView.findViewById(R.id.lbl_Payment_Details);
            lbl_Mode = itemView.findViewById(R.id.lbl_Mode);
            lbl_Vendor = itemView.findViewById(R.id.lbl_Vendor);
            txt_total_amount = itemView.findViewById(R.id.txt_total_amount);
            txt_total_tax = itemView.findViewById(R.id.txt_total_tax);

            iv_photo = itemView.findViewById(R.id.iv_photo);
        }

        void Bind(ClsPurchaseMaster clsPurchaseMaster,
                  OnProductDetailClick onProductDetailClick,
                  int position) {
            ll_header.setOnClickListener(v -> onProductDetailClick.OnClick(clsPurchaseMaster, position));

        }


        void viewImgClick(ClsPurchaseMaster clsPurchaseMaster,
                          OnItemClick onItemClick, int position) {

            iv_photo.setOnClickListener(v -> onItemClick.onItemClick(clsPurchaseMaster, position));

        }


    }
}
