package com.demo.nspl.restaurantlite.SMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Interface.OnClickSalesItem;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;

import java.util.List;

public class AllContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContactVO> contactVOList;
    private Context mContext;

    private static final int SECTION_VIEW = 0;
    private static final int CONTENT_VIEW = 1;



    public AllContactsAdapter(List<ContactVO> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SECTION_VIEW) {
            return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_orders, parent, false));
        } else {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_contact_list, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                SectionHeaderViewHolder SectionHeaderviewHolder = (SectionHeaderViewHolder) holder;
                SectionHeaderviewHolder.headerTitleTextview.setClickable(false);
                SectionHeaderviewHolder.headerTitleTextview.setText(contactVO.getContactName());

                break;

            case 1:
                ItemViewHolder ItemViewviewHolder = (ItemViewHolder) holder;
                ItemViewviewHolder.tvContactName.setText(contactVO.getContactName());
                ItemViewviewHolder.tvPhoneNumber.setText(contactVO.getContactNumber());

        }


    }


    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerTitleTextview;

        SectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextview = itemView.findViewById(R.id.txt_cat_name);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView tvContactName;
        TextView tvPhoneNumber;


        ItemViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);

        }

        void Bind(final ClsLayerItemMaster clsLayerItemMaster, OnClickSalesItem onClickSalesItem) {
            linearLayout.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onClickSalesItem.OnClick(clsLayerItemMaster));
        }
    }


    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{


        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);

            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}
