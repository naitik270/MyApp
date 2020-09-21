package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.Utils.ItemAnimation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewSmsOffersDashboardAdapter extends RecyclerView.Adapter<ViewSmsOffersDashboardAdapter.MyViewHolder> {

    Context context;
    View itemView;
    List<ClsViewSmsOffersList> data = new ArrayList<>();
    private LayoutInflater inflater = null;
    setOnOfferClick onOfferClick;
    setOnTermsUrlClick onTermsUrlClick;
    private int animation_type = 0;

    public ViewSmsOffersDashboardAdapter(Context context, List<ClsViewSmsOffersList> data) {
        super();
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.animation_type = animation_type;
    }

    public void SetOnClickListener(setOnOfferClick onOfferClick) {
        this.onOfferClick = onOfferClick;
    }

    public void SetOnTermsUrlClick(setOnTermsUrlClick onTermsUrlClick) {
        this.onTermsUrlClick = onTermsUrlClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_sms_offers_bottom_dialog_new, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClsViewSmsOffersList obj = data.get(position);
        holder.txt_offer_title.setText(obj.getOfferTitle());
        holder.txt_descriptions.setText(obj.getOfferDescription().concat("Valid:").concat(obj.getValidityFromDate() + " " + obj.getValidityFromTime() + " TO " + obj.getValidityToDate() + " " + obj.getValidityToTime()));
        holder.txt_offer_code.setText(obj.getOfferCode());

        if (obj.getISDocumentAvailable().equalsIgnoreCase("YES")) {

            holder.ll_webview.setVisibility(View.VISIBLE);
            Picasso
                    .get()
                    .load(obj.getDocumentUrl())
                    .fit()
                    .centerCrop()
                    .into(holder.iv_document);
        } else {
            holder.ll_webview.setVisibility(View.GONE);
        }

        if (position % 2 == 1) {
            holder.card_view.setBackgroundColor(Color.parseColor("#83374F"));
        } else {
            holder.card_view.setBackgroundColor(Color.parseColor("#DA7F97"));
        }

//        holder.Bind(data.get(position), onOfferClick, position);
        holder.TermsClickURL(data.get(position), onTermsUrlClick, position);
        setAnimation(holder.itemView, position);
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

    public interface setOnOfferClick {
        void OnClick(ClsViewSmsOffersList clsViewSmsOffersList, int position);
    }

    public interface setOnTermsUrlClick {
        void OnClick(ClsViewSmsOffersList clsViewSmsOffersList, int position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_offer_title, txt_descriptions, txt_offer_code, txt_terms;
        ImageView iv_document;
        LinearLayout ll_webview;
        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            txt_offer_code = itemView.findViewById(R.id.txt_offer_code);

            iv_document = itemView.findViewById(R.id.iv_document);
            txt_offer_title = itemView.findViewById(R.id.txt_offer_title);
            ll_webview = itemView.findViewById(R.id.ll_webview);
            txt_terms = itemView.findViewById(R.id.txt_terms);
            txt_descriptions = itemView.findViewById(R.id.txt_descriptions);
        }

        void TermsClickURL(final ClsViewSmsOffersList clsViewSmsOffersList,
                           setOnTermsUrlClick onTermsUrlClick, int position) {
            txt_terms.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onTermsUrlClick.OnClick(clsViewSmsOffersList, position));
        }
    }

    public void updateList(List<ClsViewSmsOffersList> list) {
        data = list;
        notifyDataSetChanged();
    }

}
