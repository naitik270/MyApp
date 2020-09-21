package com.demo.nspl.restaurantlite.SMS;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayGroupAdapter extends RecyclerView.Adapter<DisplayGroupAdapter.MyViewHolder> {

    private List<ClsSmsCustomerGroup> data = new ArrayList<>();
    private Context context;
    private View itemView;
    private OnItemClickListener onItemClickListener;


    public DisplayGroupAdapter(Context context) {
        this.context = context;

    }

    public void AddItems(List<ClsSmsCustomerGroup> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_display_group_customer, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ClsSmsCustomerGroup obj = data.get(position);

        String _IndexVal = String.valueOf(position + 1).concat(".");
        holder.txt_sr_no.setText(_IndexVal);

        holder.txt_name.setText(obj.getGroupName());
        holder.txt_count.setText(" (" + obj.getTotalCount() + ")");


        if (obj.getActive().equalsIgnoreCase("YES")) {
            holder.card_color.setCardBackgroundColor(Color.parseColor("#225A25"));
        } else {
            holder.card_color.setCardBackgroundColor(Color.parseColor("#C02828"));
        }

        holder.Bind(data.get(position), onItemClickListener, position);

    }


    public interface OnItemClickListener {
        void OnClick(ClsSmsCustomerGroup clsSmsCustomerGroup, int position);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        CardView card_color;
        TextView txt_name;
        TextView txt_sr_no;
        TextView txt_count;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            card_view = itemView.findViewById(R.id.card_view);
            txt_sr_no = itemView.findViewById(R.id.txt_sr_no);
            txt_count = itemView.findViewById(R.id.txt_count);
            card_color = itemView.findViewById(R.id.card_color);

        }

        void Bind(final ClsSmsCustomerGroup clsSmsCustomerGroup,
                  OnItemClickListener onItemClickListener, int position) {
            card_view.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnClick(clsSmsCustomerGroup, position));
        }

    }

}





