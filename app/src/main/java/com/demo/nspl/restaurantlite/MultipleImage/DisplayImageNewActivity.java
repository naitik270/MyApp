package com.demo.nspl.restaurantlite.MultipleImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsImages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DisplayImageNewActivity extends AppCompatActivity {


    public ArrayList<Uri> path = new ArrayList<>();
    public ArrayList<String> Databasepath = new ArrayList<>();
    ImageView img_main;
    RecyclerView rv_img_block;
    LinearLayoutManager linearLayoutManager;
    DisplayImageAdapterNew imageAdapterNew;
    ImageController mainController;
    Toolbar toolbar;

    int _id=0;
    String _type = "";
    String _customerName = "";
    String imgSave = "";
    TextView txt_title_name;
    TextView txt_no_data;
    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_new);


        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        _customerName = getIntent().getStringExtra("_customerName");
        _type = getIntent().getStringExtra("_type");
        imgSave = getIntent().getStringExtra("imgSave");

        ClsGlobal.imgPreviewMode = imgSave;

        _id = getIntent().getIntExtra("_id", 0);

        Log.d("--Image--", "_id: " + _id);
        Log.d("--Image--", "_type: " + _type);
        Log.d("--Image--", "_customerName: " + _customerName);


        img_main = findViewById(R.id.img_main);
        rv_img_block = findViewById(R.id.rv_img_block);

        txt_title_name = findViewById(R.id.txt_title_name);
        txt_no_data = findViewById(R.id.txt_no_data);

        txt_title_name.setText(_customerName.toUpperCase());


        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        mainController = new ImageController(img_main);


        imageAdapterNew = new DisplayImageAdapterNew(this, mainController, path);
        rv_img_block.setLayoutManager(linearLayoutManager);
        rv_img_block.setAdapter(imageAdapterNew);

        imageAdapterNew.setOnClickImg((clsImgPath, position) -> mainController.setImgMain(clsImgPath));


        if (_type != null) {

            if (_type.equalsIgnoreCase("Customer Payment")) {
                fillCustomerImages();
            } else {
                fillVendorImages();
            }

        }

    }

    List<ClsImages> lstImgList = new ArrayList<>();
    private SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    private void fillCustomerImages() {

        lstImgList = new ArrayList<>();

        String where = " AND [UniqueId] = ".concat("'" + _id + "'")
                .concat(" AND [Type] = ".concat("'" + _type + "'"));

        Log.d("--Image--", "where:- " + where);

        db = DisplayImageNewActivity.this.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        lstImgList = new ClsImages().getImageByPaymentID(where, db, DisplayImageNewActivity.this);
        db.close();

        if (lstImgList != null && lstImgList.size() != 0) {
            txt_no_data.setVisibility(View.GONE);
            for (ClsImages filePath : lstImgList) {
                File file = new File(filePath.getFilePath());

                if (file.exists()) {
                    path.add(Uri.fromFile(new File(filePath.getFilePath())));
                    Databasepath.add(filePath.getFilePath());
                    Log.d("--Image--", "filepath:- " + filePath.getFilePath());
                }

            }
        } else {
            txt_no_data.setVisibility(View.VISIBLE);
        }
        imageAdapterNew.changePath(path);
    }


    @SuppressLint("WrongConstant")
    private void fillVendorImages() {

        lstImgList = new ArrayList<>();

        String where = " AND [UniqueId] = ".concat("'" + _id + "'")
                .concat(" AND [Type] = ".concat("'" + _type + "'"));

        Log.d("--Image--", "where:- " + where);

        db = DisplayImageNewActivity.this.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        lstImgList = new ClsImages().getImageByVendorPaymentID(where, db, DisplayImageNewActivity.this);
        db.close();

        if (lstImgList != null && lstImgList.size() != 0) {
            txt_no_data.setVisibility(View.GONE);
            for (ClsImages filePath : lstImgList) {
                File file = new File(filePath.getFilePath());

                if (file.exists()) {
                    path.add(Uri.fromFile(new File(filePath.getFilePath())));
                    Databasepath.add(filePath.getFilePath());
                    Log.d("--Image--", "filepath:- " + filePath.getFilePath());
                }

            }
        } else {
            txt_no_data.setVisibility(View.VISIBLE);
        }
        imageAdapterNew.changePath(path);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        path.clear();
        Databasepath.clear();
    }

}
