package com.demo.nspl.restaurantlite.MultipleImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ImagePreviewActivity extends AppCompatActivity {

    int ID;
    String itemName = "";
    String itemCode = "";
    String _imgMode = "";
    String imgSave = "";
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
        itemCode = getIntent().getStringExtra("itemCode");
        itemName = getIntent().getStringExtra("itemName");
        imgSave = getIntent().getStringExtra("imgSave");
        ID = getIntent().getIntExtra("ID", 0);

        txt_item_name = findViewById(R.id.txt_item_name);

        txt_item_name.setText(itemName);

        img_main = findViewById(R.id.img_main);
        rv_img_block = findViewById(R.id.rv_img_block);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        FishBun.with(ImagePreviewActivity.this)
                .setImageAdapter(new GlideAdapter())
                .setMenuDoneText("DONE")
                .startAlbum();

        mainController = new ImageController(img_main);

        imageAdapterNew = new PreviewImageAdapter(this, mainController, path);
        rv_img_block.setLayoutManager(linearLayoutManager);
        rv_img_block.setAdapter(imageAdapterNew);

        imageAdapterNew.setOnClickImg(new OnItemClickListenerClsOrder() {
            @Override
            public void OnClick(Uri clsImgPath, int position) {


                Log.e("--onClick--", "Uri: " + clsImgPath);


                mainController.setImgMain(clsImgPath);
            }
        });

        imageAdapterNew.setOnRemoveImgClick((clsImgPath, position) -> {

            Log.e("--onClick--", "uri_activity: " + clsImgPath.getPath());

            imageAdapterNew.removeImage(position);


            img_main.setImageBitmap(null);


            if ("content".equals(clsImgPath.getScheme())) {

                srcFilePath.remove(ClsGlobal.getPathFromUri(ImagePreviewActivity.this,
                        clsImgPath));
            } else {
                srcFilePath.remove(clsImgPath.getPath());

            }

        });

    }


    Bitmap bmp;
    String uriString = "";
    List<String> srcFilePath = new ArrayList<>();

    ArrayList<Uri> path = new ArrayList<>();
    ArrayList<String> Databasepath = new ArrayList<>();


    boolean isInsert = false;

    public void InsertMultipleImg(List<String> srcDir) {
        String _folderName = "";

        try {

            if (srcDir != null && srcDir.size() > 0) {
                File _saveLocation = Environment.getExternalStorageDirectory();
                Log.d("camera", "filepath:- " + _saveLocation);


                if (_imgMode.equalsIgnoreCase("purchase")) {
                    _folderName = ClsGlobal.PurchaseImageDirectory;
                } else {
                    _folderName = ClsGlobal.ItemImageDirectory;
                }


                File dir = new File(_saveLocation.getAbsolutePath() + "/"
                        + ClsGlobal.AppFolderName
                        + "/"
                        + _folderName
                        + "/"
                        + itemCode
                        .concat("/"));

                if (!dir.exists()) {
                    dir.mkdirs();
                }


                ArrayList<String> uniques = new ArrayList<String>(srcDir);
                uniques.removeAll(Databasepath);

                List<String> newPath = new ArrayList<>();

                for (String _filePath : uniques) {
                    File src = new File(_filePath);

                    if (src.exists()) {

                        String _copyFileName = ClsGlobal.getRandom() + "_.jpg";
                        String dstFile = "".concat(dir.getAbsolutePath()).concat("/").concat(_copyFileName);
                        File _copy = new File(dstFile);

                        ClsGlobal.copyFileTemp(src.getAbsolutePath(), _copy.getAbsolutePath());
                        newPath.add(_copy.getPath());
                    }
                }


                int result = 0;
                for (String _filePath : newPath) {
                    ClsMultipleImg obj = new ClsMultipleImg(ImagePreviewActivity.this);
                    obj.setDocument_No(String.valueOf(ID));
                    obj.setItem_code(itemCode);
                    obj.setDocument_Name(itemName + "_" + itemCode);
                    obj.setDocument_No(""); // Invoice No#
                    obj.setFile_Path(_filePath);
                    obj.setType("ITEM");
                    obj.setFile_Name(new File(_filePath).getName());
                    result = ClsMultipleImg.Insert(obj);

                }

                if (result == -1) {
                    Toast.makeText(getApplicationContext(), "Technical Error", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Photo save successfully.", Toast.LENGTH_SHORT).show();
                    isInsert = true;
                    finish();
//                    path.clear();
                }

                Log.d("--ClsMultipleImg--", "InsertResult:- " + result);
            }


        } catch (Exception e) {
            Log.d("--path--", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path.addAll(data.getParcelableArrayListExtra(Define.INTENT_PATH));

                    for (Uri uri : path) {

                        if (uri != null) {
                            try {
                                uriString = uri.toString();

                                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                                if ("content".equals(uri.getScheme())) {
                                    srcFilePath.add(ClsGlobal.getPathFromUri(ImagePreviewActivity.this, uri));
                                } else {
                                    srcFilePath.add(uri.getPath());
                                }


                                // Log.d("--path--", "FilePath: " + srcFilePath);

                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }
                    }


                    imageAdapterNew.changePath(path);
                    break;
                }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_plus) {


            FishBun.with(ImagePreviewActivity.this)
                    .setImageAdapter(new GlideAdapter())
                    .setMenuDoneText("DONE")
                    .startAlbum();


            return true;
        }
        if (id == R.id.action_done) {

            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(srcFilePath);
            srcFilePath.clear();
            srcFilePath.addAll(hashSet);

            InsertMultipleImg(srcFilePath);

            return true;
        }

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
