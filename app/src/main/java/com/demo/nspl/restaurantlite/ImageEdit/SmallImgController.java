package com.demo.nspl.restaurantlite.ImageEdit;

import android.net.Uri;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by sangc on 2015-11-06.
 */
public class SmallImgController {
    CropImageView mCropImageView;

    SmallImgController(CropImageView mCropImageView) {
        this.mCropImageView = mCropImageView;
    }

    void setImgMain(Uri path) {
        Picasso.get().load(path).fit().centerCrop().into((Target) mCropImageView);
    }

}
