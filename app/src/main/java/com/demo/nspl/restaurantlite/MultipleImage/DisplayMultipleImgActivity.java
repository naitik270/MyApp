package com.demo.nspl.restaurantlite.MultipleImage;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DisplayMultipleImgActivity extends AppCompatActivity {


    public ArrayList<Uri> path = new ArrayList<>();
    public ArrayList<String> Databasepath = new ArrayList<>();
    ImageView img_main;
    RecyclerView rv_img_block;
    LinearLayoutManager linearLayoutManager;
    ImageAdapterNew imageAdapterNew;
    ImageController mainController;
    Toolbar toolbar;

    int ID;
    String itemName = "";
    String itemCode = "";
    String _imgMode = "";
    String imgSave = "";
    TextView txt_item_name;
    MenuItem item;

    boolean isInsert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_img);


        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCode = getIntent().getStringExtra("itemCode");
        itemName = getIntent().getStringExtra("itemName");
        _imgMode = getIntent().getStringExtra("_imgMode");
        imgSave = getIntent().getStringExtra("imgSave");

        ClsGlobal.imgPreviewMode = imgSave;

        ID = getIntent().getIntExtra("ID", 0);

        img_main = findViewById(R.id.img_main);
        rv_img_block = findViewById(R.id.rv_img_block);

        txt_item_name = findViewById(R.id.txt_item_name);

        txt_item_name.setText(itemName);


        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        mainController = new ImageController(img_main);

        Log.e("--Dialog--", "DisplayMultipleImgActivity ");

        imageAdapterNew = new ImageAdapterNew(this, mainController, path);
        rv_img_block.setLayoutManager(linearLayoutManager);
        rv_img_block.setAdapter(imageAdapterNew);

        imageAdapterNew.setOnClickImg((clsImgPath, position) -> mainController.setImgMain(clsImgPath));


        imageAdapterNew.setOnLongClickImg((clsImgPath, position) -> {

            deleteDialog(position);

        });


        imageAdapterNew.setOnRemoveImgClick((clsImgPath, position) -> {
            deleteDialog(position);
        });

        fillItemPhotos();
    }

    Bitmap bmp;
    String uriString = "";
    public List<String> srcFilePath = new ArrayList<>();


    void deleteDialog(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayMultipleImgActivity.this);
        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Are you sure you want delete?");
        alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int result = 0;

                if (!isInsert) {

                    result = ClsMultipleImg.Delete(lstImgList.get(position).getID(),
                            DisplayMultipleImgActivity.this);

                    Log.d("--ClsMultipleImg--", "DeleteResult:- " + result);

                    if (result > 0) {

                        File fdelete = new File(lstImgList.get(position).getFile_Path());


                        Log.d("--ClsMultipleImg--", "getFile_Path: " + lstImgList.get(position).getFile_Path());

                        imageAdapterNew.removeImage(position);
                        img_main.setImageBitmap(null);


                        if (fdelete.exists()) {
                            if (fdelete.delete()) {

                                Log.d("--ClsMultipleImg--", "File Deleted");

                            } else {
                                Log.d("--ClsMultipleImg--", "File Not Deleted");
                            }
                        }
                    }

                } else {

                    imageAdapterNew.removeImage(position);
                    img_main.setImageBitmap(null);

                    Log.d("--ClsMultipleImg--", "isInsert: " + isInsert);

                }
            }
        });
        alertDialog.setNegativeButton("NO", (dialog, which) -> {
            dialog.dismiss();
            dialog.cancel();
        });


        // Showing Alert Message
        alertDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Define.ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getParcelableArrayListExtra(Define.INTENT_PATH).size() > 0) {

                    ClsGlobal.imgPreviewMode = imgSave = "AddImg";
                    path.addAll(data.getParcelableArrayListExtra(Define.INTENT_PATH));
                    if (item != null) {
                        item.setVisible(true);
                    }

                } else {
                    if (item != null) {
                        item.setVisible(false);
                    }
                }


//                    path.addAll(data.getParcelableArrayListExtra(Define.INTENT_PATH));

                Log.e("--Dialog--", "onActivityResult: " + path.size());


                for (Uri uri : path) {

                    if (uri != null) {
                        try {
                            uriString = uri.toString();

                            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                            if ("content".equals(uri.getScheme())) {
                                srcFilePath.add(ClsGlobal.getPathFromUri(DisplayMultipleImgActivity.this, uri));
//                                    Databasepath.add(ClsGlobal.getPathFromUri(DisplayMultipleImgActivity.this, uri));
                            } else {
                                srcFilePath.add(uri.getPath());
//                                    Databasepath.add(uri.getPath());
                            }


                            // Log.d("--path--", "FilePath: " + srcFilePath);

                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    }
                }

                Log.e("--Dialog--", "onActivityResult: " + path.size());
                imageAdapterNew.changePath(path);
                InsertMultipleImg(srcFilePath);

            }
        }
    }

    void deleteLastFolder() {
        String _path = "";
        for (Uri filePath : ClsGlobal.purchaseImgPathLst) {
            if (filePath != null) {
                _path = filePath.toString();
            }

            File file = new File(_path);

            String getDirectoryPath = file.getParent().replace("file:", "");

            deleteRecursive(new File(getDirectoryPath));

            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    new File(getDirectoryPath, children[i]).delete();
                }
            }
        }
    }



    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }


    List<ClsMultipleImg> lstImgList = new ArrayList<>();


    private void fillItemPhotos() {
        lstImgList = new ArrayList<>();
        lstImgList = new ClsMultipleImg().getImageByItemCode(itemCode, DisplayMultipleImgActivity.this);

        for (ClsMultipleImg filePath : lstImgList) {
            File file = new File(filePath.getFile_Path());

            if (file.exists()) {
                path.add(Uri.fromFile(new File(filePath.getFile_Path())));
                Databasepath.add(filePath.getFile_Path());
                Log.d("--Fill--", "filepath:- " + filePath.getFile_Path());
            }
            Gson gson = new Gson();
            String jsonInString = gson.toJson(path);
            Log.e("--Dialog--", "onCreate: " + jsonInString);

            imageAdapterNew.changePath(path);
        }

        //        DisplayImgAdapter displayImgAdapter = new DisplayImgAdapter(DisplayMultipleImgActivity.this, lstImgList);
        //        rv_img_block.setAdapter(displayImgAdapter);


    }

    public void InsertMultipleImg(List<String> srcDir) {
        String _folderName = "";

        try {

            if (srcDir != null && srcDir.size() > 0) {
                File _saveLocation = Environment.getExternalStorageDirectory();
                Log.d("camera", "filepath:- " + _saveLocation);

                Log.d("--Dialog--", "InsertMultipleImg Display:- ");
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
                    ClsMultipleImg obj = new ClsMultipleImg(DisplayMultipleImgActivity.this);
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
                }

                Log.d("--ClsMultipleImg--", "InsertResult:- " + result);
            } else {
                Toast.makeText(getApplicationContext(), "There are no New Image to Save.", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Log.d("--path--", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_multi_img, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_plus) {
            FishBun.with(DisplayMultipleImgActivity.this)
                    .setImageAdapter(new GlideAdapter())
                    .setMenuDoneText("DONE")
                    .startAlbum();


            return true;
        }
//        if (id == R.id.action_done) {
//            InsertMultipleImg(srcFilePath);
//            return true;
//        }

        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        srcFilePath.clear();
        path.clear();
        Databasepath.clear();
    }

}
