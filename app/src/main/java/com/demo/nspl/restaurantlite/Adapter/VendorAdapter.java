package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsVendor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/14/2018.
 */

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder> {

    Context context;
    private View itemview;
    List<ClsVendor> listVendor = new ArrayList<ClsVendor>();
    private SQLiteDatabase db;
    OnVendorClick onVendorClick;


    public VendorAdapter(Context context) {
        this.context = context;
    }


    public void AddItems(List<ClsVendor> listVendor) {
        this.listVendor = listVendor;
        notifyDataSetChanged();
    }

    public void RemoveItem(int position) {
        listVendor.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vendor_master, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    public void SetOnClickListener(OnVendorClick onVendorClick) {
        this.onVendorClick = onVendorClick;
    }

    public interface OnVendorClick {
        void OnClick(ClsVendor clsVendor, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        ClsVendor objVendor = listVendor.get(position);

        holder.lbl_vendor_name.setText(objVendor.getVendor_name());
        holder.lbl_address.setText(objVendor.getAddress());
        holder.lbl_gst_no.setText(objVendor.getGst_no());
        holder.lbl_active.setText(String.valueOf(objVendor.getActive()));
        holder.lbl_srno.setText(String.valueOf(objVendor.getSort_no()));
        holder.lbl_remark.setText(objVendor.getRemark());

        holder.txt_balance_type.setText(objVendor.getBalanceType());

        if (objVendor.getBalanceType().equalsIgnoreCase("TO PAY")) {
            holder.txt_balance_type.setTextColor(Color.parseColor("#225A25"));
        } else {
            holder.txt_balance_type.setTextColor(Color.parseColor("#c40000"));
        }

        holder.lbl_opening_bal.setText(String.valueOf(objVendor.getOpeningStock()));

        SpannableString content = new SpannableString(objVendor.getContact_no());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.lbl_contact_no.setText(content);
/*

        holder.lbl_contact_no.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                ClsVendor itemGetSet = new ClsVendor();
                itemGetSet = listVendor.get(position);
                String number = itemGetSet.getContact_no();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                c.startActivity(intent);

            }
        });

*/


        holder.Bind(objVendor, onVendorClick, position);

    }

    @Override
    public int getItemCount() {
        return listVendor.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_vendor_name, lbl_contact_no, lbl_address, lbl_gst_no, lbl_active, lbl_srno, lbl_opening_bal, lbl_remark;
        TextView txt_balance_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            lbl_vendor_name = itemView.findViewById(R.id.lbl_vendor_name);
            lbl_contact_no = itemView.findViewById(R.id.lbl_contact_no);
            lbl_address = itemView.findViewById(R.id.lbl_address);
            lbl_gst_no = itemView.findViewById(R.id.lbl_gst_no);
            lbl_active = itemView.findViewById(R.id.lbl_active);
            lbl_srno = itemView.findViewById(R.id.lbl_srno);
            lbl_remark = itemView.findViewById(R.id.lbl_remark);
            lbl_opening_bal = itemView.findViewById(R.id.lbl_opening_bal);
            txt_balance_type = itemView.findViewById(R.id.txt_balance_type);


        }


        void Bind(ClsVendor clsVendor, OnVendorClick onVendorClick, int position) {
            ll_header.setOnClickListener(v -> onVendorClick.OnClick(clsVendor, position));
        }

    }
}
