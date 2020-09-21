package com.demo.nspl.restaurantlite.Purchase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickProductItem;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class ItemSelectAdapter extends RecyclerView.Adapter<ItemSelectAdapter.MyViewHolder> {


    List<ClsPurchaseDetail> data = new ArrayList<ClsPurchaseDetail>();
    View itemView;
    Context context;
    private LayoutInflater inflater = null;
    AlertDialog.Builder builder;
    private OnClickProductItem mOnItemClickListener;


    public ItemSelectAdapter(Context context, List<ClsPurchaseDetail> data) {
        super();
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void SetOnClickListener(OnClickProductItem onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_selection, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClsPurchaseDetail objClsProductsList = data.get(position);


        String _IndexVal = String.valueOf(position + 1).concat(".");
        holder.txt_sr_no.setText(_IndexVal);


        holder.txt_name.setText(objClsProductsList.getItemName());
//        holder.txt_count.setText("qty:" + objClsProductsList.getQuantity() + " x rate:"
//                + objClsProductsList.getRate());

        holder.txt_count.setText("(rate) \u20B9 " + objClsProductsList.getRate()
                + " x (qty) " + objClsProductsList.getQuantity());

        holder.txt_amt.setText("\u20B9 " + ClsGlobal.round(objClsProductsList.getGrandTotal(), 2));


        holder.iv_delete.setOnClickListener(v -> {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Delete ?");
            alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
            alertDialog.setMessage("Do you really want to delete this product ?");

            alertDialog.setPositiveButton("YES", (dialog, which) -> {

                ClsPurchaseDetail item = new ClsPurchaseDetail();
                item = data.get(position);
                int result = 1;

                if (result == 0) {
                    //NO RECORD DELETED
                    Toast.makeText(context, "NO RECORD DELETED", Toast.LENGTH_LONG).show();
                } else if (result == 1) {

                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    Toast.makeText(context, "RECORD DELETED SUCCESSFULLY", Toast.LENGTH_LONG).show();

                } else {
                    //error
                    Toast.makeText(context, "ERROR FOR DELETE RECORD", Toast.LENGTH_LONG).show();
                }
            });
            alertDialog.setNegativeButton("NO", (dialog, which) -> {
                dialog.dismiss();
                dialog.cancel();
            });
            // Showing Alert Message
            alertDialog.show();

        });


        holder.Bind(objClsProductsList, mOnItemClickListener, position);


    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_name, txt_count, txt_amt, txt_sr_no;

        ImageView iv_delete, iv_edit;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_header = itemView.findViewById(R.id.ll_header);
            txt_sr_no = itemView.findViewById(R.id.txt_sr_no);
            txt_name = itemView.findViewById(R.id.txt_name);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_amt = itemView.findViewById(R.id.txt_amt);
        }


        void Bind(ClsPurchaseDetail clsProductsList, OnClickProductItem onClickProductItem, int position) {

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickProductItem.OnClick(clsProductsList, position);
                }
            });
        }

    }


}
