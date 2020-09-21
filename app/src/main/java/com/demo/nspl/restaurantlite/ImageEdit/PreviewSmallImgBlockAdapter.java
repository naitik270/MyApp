package com.demo.nspl.restaurantlite.ImageEdit;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.MultipleImage.ImageController;
import com.demo.nspl.restaurantlite.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sangc on 2015-11-06.
 */
public class PreviewSmallImgBlockAdapter extends RecyclerView.Adapter<PreviewSmallImgBlockAdapter.ViewHolder> {

    Context context;
    ArrayList<Uri> imagePaths = new ArrayList<>();
    ImageController imageController;
    private OnItemImageBlockClick mOnItemClickListener;

    public PreviewSmallImgBlockAdapter(Context context, ImageController imageController, ArrayList<Uri> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.imageController = imageController;

        if (imagePaths.size() > 0) {
            notifyDataSetChanged();
        }
        Log.d("--path--", "imagePaths_Adapter: " + imagePaths);
    }

    public void setOnClickImg(OnItemImageBlockClick mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnItemImageBlockClick {
        void OnClick(Uri uri, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_small_img_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("Check", "onBindViewHolder");
        final Uri imagePath = imagePaths.get(position);

        Picasso.get().load(imagePath).fit().centerCrop().into(holder.imageView);

        holder.Bind(imagePath, mOnItemClickListener, position);

    }

    public void changePath(ArrayList<Uri> imagePaths) {
        this.imagePaths = imagePaths;
        if (imagePaths.size() > 0) {
//            imageController.setImgMain(imagePaths.get(0));
            notifyDataSetChanged();
        }
    }




    public void removeImage(int position) {
        imagePaths.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView iv_remove;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item);
            iv_remove = itemView.findViewById(R.id.iv_remove);
            iv_remove.setVisibility(View.GONE);
        }

        void Bind(final Uri uri, final OnItemImageBlockClick mOnItemClickListener,
                  final int position) {

            imageView.setOnClickListener(v -> {

                Log.d("--Adapter--", "uri: " + uri);
                Log.d("--Adapter--", "position: " + position);

                mOnItemClickListener.OnClick(uri, position);
            });
        }

    }
}