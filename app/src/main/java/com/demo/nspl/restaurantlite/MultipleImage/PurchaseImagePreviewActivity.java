package com.demo.nspl.restaurantlite.MultipleImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.gson.Gson;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;
import java.util.List;

public class PurchaseImagePreviewActivity extends AppCompatActivity {

    String _imgMode = "";
    String _purchaseFlag = "";
    ImageView img_main;
    RecyclerView rv_img_block;
    LinearLayoutManager linearLayoutManager;
    PreviewImageAdapter imageAdapterNew;

    ImageController mainController;

    TextView txt_item_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_img);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _imgMode = getIntent().getStringExtra("_imgMode");
        _purchaseFlag = getIntent().getStringExtra("_purchaseFlag");

        img_main = findViewById(R.id.img_main);

        txt_item_name = findViewById(R.id.txt_item_name);

        txt_item_name.setText("Purchase Bill Image");

        rv_img_block = findViewById(R.id.rv_img_block);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);

        mainController = new ImageController(img_main);

       /* if (_purchaseFlag.equalsIgnoreCase("AddNewPurchase")) {
            ClsGlobal.purchaseImgPathLst.clear();
        }
*/
        imageAdapterNew = new PreviewImageAdapter(this, mainController,
                ClsGlobal.purchaseImgPathLst);
        rv_img_block.setLayoutManager(linearLayoutManager);
        rv_img_block.setAdapter(imageAdapterNew);

        Log.d("--static--", "Before: " + ClsGlobal.purchaseImgPathLst.size());

        Gson gson1 = new Gson();
        String jsonInString1 = gson1.toJson(ClsGlobal.purchaseImgPathLst);
        Log.d("--static--", "Next_List: " + jsonInString1);

        Log.d("--static--", "Next_hashCode: " + ClsGlobal.purchaseImgPathLst.hashCode());
        Log.d("--static--", "_purchaseFlag: " + _purchaseFlag);

        imageAdapterNew.changePath(ClsGlobal.purchaseImgPathLst);


        imageAdapterNew.setOnClickImg((clsImgPath, position) -> {

            Log.e("--onClick--", "Uri: " + clsImgPath);
            mainController.setImgMain(clsImgPath);
        });

        imageAdapterNew.setOnRemoveImgClick((clsImgPath, position) -> {

            Log.e("--onClick--", "uri_activity: " + clsImgPath.getPath());

            imageAdapterNew.removeImage(position);
            img_main.setImageBitmap(null);

            if ("content".equals(clsImgPath.getScheme())) {
                srcFilePath.remove(ClsGlobal.getPathFromUri(PurchaseImagePreviewActivity.this,
                        clsImgPath));
            } else {
                srcFilePath.remove(clsImgPath.getPath());
            }
        });

    }


    Bitmap bmp;
    String uriString = "";
    List<String> srcFilePath = new ArrayList<>();


    ArrayList<String> Databasepath = new ArrayList<>();
    boolean isInsert = false;

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
                                    srcFilePath.add(ClsGlobal.getPathFromUri(PurchaseImagePreviewActivity.this, uri));
                                } else {
                                    srcFilePath.add(uri.getPath());
                                }


                                // Log.d("--path--", "FilePath: " + srcFilePath);

                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }
                    }

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(ClsGlobal.purchaseImgPathLst);
                    Log.e("--Dialog--", "onActivityResult ImagePreview : " + jsonInString);
                    imageAdapterNew.changePath(ClsGlobal.purchaseImgPathLst);

                    break;
                }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_multi_img, menu);
        MenuItem item = menu.findItem(R.id.action_done);

        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_plus) {


            FishBun.with(PurchaseImagePreviewActivity.this)
                    .setImageAdapter(new GlideAdapter())
                    .setMenuDoneText("DONE")
                    .startAlbum();


            return true;
        }


        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
