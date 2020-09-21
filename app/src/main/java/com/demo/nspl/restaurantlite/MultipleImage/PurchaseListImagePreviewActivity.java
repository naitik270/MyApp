package com.demo.nspl.restaurantlite.MultipleImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurchaseListImagePreviewActivity extends AppCompatActivity {

    String _imgMode = "";
    String _purchaseFlag = "";
    ImageView img_main;
    RecyclerView rv_img_block;
    LinearLayoutManager linearLayoutManager;
    PreviewImageForPurchaseAdapter imageAdapterNew;
    ImageController mainController;
    TextView txt_item_name;
    TextView txt_no_data;
    int _pID = 0;

    RelativeLayout rl_recyclerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_img);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        _imgMode = getIntent().getStringExtra("_imgMode");
        _purchaseFlag = getIntent().getStringExtra("_purchaseFlag");
        _pID = getIntent().getIntExtra("_pID", 0);
        Log.e("--img--", "_pID: " + _pID);


        Log.e("--img--", "purchaseImgPathLst: " + ClsGlobal.purchaseImgPathLst);


        rl_recyclerview = findViewById(R.id.rl_recyclerview);
        txt_no_data = findViewById(R.id.txt_no_data);
        img_main = findViewById(R.id.img_main);

        txt_item_name = findViewById(R.id.txt_item_name);
        txt_item_name.setText("Purchase Bill Image");

        ClsGlobal.purchaseImgPathLst.clear();
        fillItemPhotos(_pID);

        rv_img_block = findViewById(R.id.rv_img_block);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);

        mainController = new ImageController(img_main);

        imageAdapterNew = new PreviewImageForPurchaseAdapter(this, mainController,
                ClsGlobal.purchaseImgPathLst);

        rv_img_block.setLayoutManager(linearLayoutManager);
        rv_img_block.setAdapter(imageAdapterNew);

        imageAdapterNew.changePath(ClsGlobal.purchaseImgPathLst);

        imageAdapterNew.setOnClickImg((clsImgPath, position) -> {
            mainController.setImgMain(clsImgPath);
        });

        imageAdapterNew.setOnRemoveImgClick((clsImgPath, position) -> {

            imageAdapterNew.removeImage(position);
            img_main.setImageBitmap(null);

            if ("content".equals(clsImgPath.getScheme())) {
                srcFilePath.remove(ClsGlobal.getPathFromUri(PurchaseListImagePreviewActivity.this,
                        clsImgPath));
            } else {
                srcFilePath.remove(clsImgPath.getPath());
            }
        });
    }

    List<ClsMultipleImg> lstImgList = new ArrayList<>();

    private void fillItemPhotos(int _pID) {
        lstImgList = new ArrayList<>();
        lstImgList = new ClsMultipleImg().getImageByPurchaseID(_pID, PurchaseListImagePreviewActivity.this);

        if (lstImgList != null && lstImgList.size() != 0) {
            rl_recyclerview.setVisibility(View.VISIBLE);
            txt_no_data.setVisibility(View.GONE);
            for (ClsMultipleImg filePath : lstImgList) {
                File file = new File(filePath.getFile_Path());
                if (file.exists()) {
                    ClsGlobal.purchaseImgPathLst.add(Uri.fromFile(new File(filePath.getFile_Path())));
                }
            }
        } else {
            rl_recyclerview.setVisibility(View.GONE);
            txt_no_data.setVisibility(View.VISIBLE);
        }
    }

    Bitmap bmp;
    String uriString = "";
    List<String> srcFilePath = new ArrayList<>();

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    ClsGlobal.purchaseImgPathLst.addAll(data.getParcelableArrayListExtra(Define.INTENT_PATH));

                    for (Uri uri : ClsGlobal.purchaseImgPathLst) {

                        if (uri != null) {
                            try {
                                uriString = uri.toString();

                                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                                if ("content".equals(uri.getScheme())) {
                                    srcFilePath.add(ClsGlobal.getPathFromUri(PurchaseListImagePreviewActivity.this, uri));
                                } else {
                                    srcFilePath.add(uri.getPath());
                                }
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }
                    }
                    imageAdapterNew.changePath(ClsGlobal.purchaseImgPathLst);
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
