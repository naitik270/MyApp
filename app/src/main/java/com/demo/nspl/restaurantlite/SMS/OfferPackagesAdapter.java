package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


class OfferPackagesAdapter extends RecyclerView.Adapter<OfferPackagesAdapter.MyViewHolder> {

    Context context;
    View itemView;
    List<ClsViewSmsOffersList> data = new ArrayList<>();
    private LayoutInflater inflater = null;
    setOnOfferClick onOfferClick;
//    setOnTermsUrlClick onTermsUrlClick;


    OfferPackagesAdapter(Context context) {
        this.context = context;
    }

    public void AddItems(List<ClsViewSmsOffersList> data) {

        this.data = data;
        notifyDataSetChanged();
    }




    public void SetOnClickListener(setOnOfferClick onOfferClick) {
        this.onOfferClick = onOfferClick;
    }
//
//    public void SetOnTermsUrlClick(setOnTermsUrlClick onTermsUrlClick) {
//        this.onTermsUrlClick = onTermsUrlClick;
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer_package, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClsViewSmsOffersList obj = data.get(position);

        holder.txt_offer_title.setText(data.get(position).getOfferCode());
        holder.txt_description.setText(obj.getOfferDescription().concat(" Valid:").concat(obj.getValidityFromDate() + " " + obj.getValidityFromTime()
                + " TO " + obj.getValidityToDate() + " " + obj.getValidityToTime()));

        holder.txt_terms.setText(data.get(position).getTermsFileUrl());

        if (obj.getISDocumentAvailable().equalsIgnoreCase("YES")) {

            holder.iv_document.setVisibility(View.VISIBLE);
            holder.txt_offer_title.setVisibility(View.GONE);
            holder.txt_description.setVisibility(View.GONE);

            Picasso
                    .get()
                    .load(obj.getDocumentUrl())
                    .fit()
                    .centerCrop()
                    .into(holder.iv_document);
        } else {
            holder.iv_document.setVisibility(View.GONE);
            holder.txt_offer_title.setVisibility(View.VISIBLE);
            holder.txt_description.setVisibility(View.VISIBLE);
        }

        holder.Bind(data.get(position), onOfferClick, position);
//        holder.TermsClickURL(data.get(position), onTermsUrlClick, position);
    }

    public interface setOnOfferClick {
        void OnClick(ClsViewSmsOffersList clsViewSmsOffersList, int position);
    }

//    public interface setOnTermsUrlClick {
//        void OnClick(ClsViewSmsOffersList clsViewSmsOffersList, int position);
//    }


    @Override
    public int getItemCount() {
        return data.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_document;
        TextView txt_offer_title;
        TextView txt_description;
        TextView txt_terms;
        TextView txt_doc_url;
        LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            iv_document = itemView.findViewById(R.id.iv_document);
            txt_offer_title = itemView.findViewById(R.id.txt_offer_title);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_terms = itemView.findViewById(R.id.txt_terms);
            txt_doc_url = itemView.findViewById(R.id.txt_doc_url);

        }


        void Bind(final ClsViewSmsOffersList clsViewSmsOffersList,
                  setOnOfferClick onOfferClick, int position) {
            card_view.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onOfferClick.OnClick(clsViewSmsOffersList, position));
        }


//        void TermsClickURL(final ClsViewSmsOffersList clsViewSmsOffersList,
//                           setOnTermsUrlClick onTermsUrlClick, int position) {
//            txt_doc_url.setOnClickListener(v ->
//                    // send current position via Onclick method.
//                    onTermsUrlClick.OnClick(clsViewSmsOffersList, position));
//        }

    }
}