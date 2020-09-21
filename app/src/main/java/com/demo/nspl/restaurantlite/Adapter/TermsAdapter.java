package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Interface.OnItemClickListenerTerms;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsTerms;

import java.util.ArrayList;
import java.util.List;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ClsTerms> mTerms_List = new ArrayList<>();
    private OnItemClickListenerTerms mOnItemClickListenerTerms;

    public TermsAdapter(Context context, List<ClsTerms> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mTerms_List = list;
        this.mContext = context;
    }

    public void SetOnClickListenerTerms(OnItemClickListenerTerms onItemClickListenerTerms) {
        this.mOnItemClickListenerTerms = onItemClickListenerTerms;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_terms, parent, false);
        return new TermsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClsTerms currentObj = mTerms_List.get(position);
        holder.txt_Terms_Value.setText(currentObj.getmTerms().toUpperCase());
        holder.txt_Sort_No.setText("SORT NO: " + currentObj.getmSort_No());
        holder.txt_Active.setText("ACTIVE: " + currentObj.getmActive());


        if (currentObj.getTERM_TYPE() != null && !currentObj.getTERM_TYPE().isEmpty()) {


            holder.txt_terms_type.setText( currentObj.getTERM_TYPE()
                            .replace("[","")
                            .replace("]","").replace("\"",""));

        } else {
            holder.txt_terms_type.setText("");
//             holder.txt_terms_type.setText("SALE INVOICE TERMS");
        }

        holder.Bind(mTerms_List.get(position), mOnItemClickListenerTerms, position);

    }

    @Override
    public int getItemCount() {
        return mTerms_List.size();
    }

    public void UpdateList(int position) {
        mTerms_List.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_header;
        private TextView txt_Terms_Value, txt_Sort_No, txt_Active, txt_terms_type;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.Item_List);

            txt_Terms_Value = itemView.findViewById(R.id.txt_Terms_Value);
            txt_Sort_No = itemView.findViewById(R.id.txt_Sort_No);
            txt_Active = itemView.findViewById(R.id.txt_Active);
            txt_terms_type = itemView.findViewById(R.id.txt_terms_type);

        }

        void Bind(final ClsTerms clsTerms, OnItemClickListenerTerms OnItemClickListenerTerms, int position) {
            ll_header.setOnLongClickListener(v ->
                    // send current position via Onclick method.
                    OnItemClickListenerTerms.OnClick(clsTerms, position));
        }
    }
}
