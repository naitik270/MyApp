package com.demo.nspl.restaurantlite.MultipleImage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sangc on 2015-11-06.
 */
public class ImageAdapterNew extends RecyclerView.Adapter<ImageAdapterNew.ViewHolder> {
    Context context;
    ArrayList<Uri> imagePaths;
    ImageController imageController;

    private OnItemClickListenerClsOrder mOnItemClickListener;
    private OnItemClickListenerClsOrderLong mOnItemClickListenerClsOrderLong;
    private OnRemoveClick mOnRemoveClick;

    public ImageAdapterNew(Context context, ImageController imageController, ArrayList<Uri> imagePaths) {
        this.context = context;
        this.imageController = imageController;
        this.imagePaths = imagePaths;
        Log.d("--path--", "imagePaths_Adapter: " + imagePaths);
    }

    public void setOnClickImg(OnItemClickListenerClsOrder mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    interface OnItemClickListenerClsOrderLong {
        void OnClick(Uri clsImgPath, int position);
    }

    interface OnRemoveClick {
        void OnClick(Uri clsImgPath, int position);
    }

    public void setOnLongClickImg(OnItemClickListenerClsOrderLong mOnItemClickListenerClsOrderLong) {
        this.mOnItemClickListenerClsOrderLong = mOnItemClickListenerClsOrderLong;
    }

    public void setOnRemoveImgClick(OnRemoveClick mOnRemoveClick) {
        this.mOnRemoveClick = mOnRemoveClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Uri imagePath = imagePaths.get(position);

        Picasso.get()
                .load(imagePath)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.Bind(imagePath, mOnItemClickListener, position);
        holder.BindLong(imagePath, mOnItemClickListenerClsOrderLong, position);
        holder.bindRemoveImg(imagePath, mOnRemoveClick, position);
    }

    public void changePath(ArrayList<Uri> imagePaths) {
        this.imagePaths = imagePaths;
        if (imagePaths.size() > 0) {
            imageController.setImgMain(imagePaths.get(0));
            notifyDataSetChanged();
        }
    }

    public void RemoveItem(int position) {
        imagePaths.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    void removeImage(int position) {
        imagePaths.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView iv_remove;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item);
            iv_remove = itemView.findViewById(R.id.iv_remove);

            if (ClsGlobal.imgPreviewMode.equalsIgnoreCase("Preview")) {
                iv_remove.setVisibility(View.GONE);
            } else {
                iv_remove.setVisibility(View.VISIBLE);
            }
        }

        void Bind(final Uri uri, final OnItemClickListenerClsOrder mOnItemClickListener,
                  final int position) {
            imageView.setOnClickListener(v -> {

                mOnItemClickListener.OnClick(uri, position);
            });
        }

        void bindRemoveImg(final Uri uri, final OnRemoveClick mOnRemoveClick,
                           final int position) {
            iv_remove.setOnClickListener(v -> mOnRemoveClick.OnClick(uri, position));
        }

        void BindLong(final Uri uri, final OnItemClickListenerClsOrderLong mOnItemClickListenerClsOrderLong,
                      final int position) {

            imageView.setOnLongClickListener(v -> {

                mOnItemClickListenerClsOrderLong.OnClick(uri, position);

                return true;
            });
        }
    }

}