package com.demo.nspl.restaurantlite.ListOfBkpFile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.AsyncTask.LocalRestoreAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Activity_ListOfBkp_File extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    String _customerId = "", LicenseType = "";
    private int PICKFILE_RESULT_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_file);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        main();
    }

    private void main() {
        _customerId = getIntent().getStringExtra("_customerId");
        LicenseType = getIntent().getStringExtra("LicenseType");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setTabTextColors(Color.parseColor("#CFCFCF"), Color.parseColor("#FFFFFF"));

        viewPager.setOffscreenPageLimit(2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            viewPager.setPageTransformer(false, (page, position) -> {
                final float normalizedPosition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedPosition / 2 + 0.5f);
                page.setScaleY(normalizedPosition / 2 + 0.5f);
            });
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerBkpFileAdapter adapter = new ViewPagerBkpFileAdapter(getSupportFragmentManager());

        FtouchCloudRestoreFragment ftouchCloudRestoreFragment = new FtouchCloudRestoreFragment();
        ftouchCloudRestoreFragment.SetCustomerId(_customerId);
        adapter.addFrag(ftouchCloudRestoreFragment, "ftouch Cloud");

        PhoneStorageListFragment phone = new PhoneStorageListFragment();
        adapter.addFrag(phone, "Phone storage");

        GoogleDriveListFragment google = new GoogleDriveListFragment();
        adapter.addFrag(google, "Google drive");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_restore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.itm_restore) {
            LocalRestoreFile();
        }
        return super.onOptionsItemSelected(item);
    }

    void LocalRestoreFile() {
        String manufacturer = Build.MANUFACTURER;
        Log.e("manufacturer", manufacturer);

        if (manufacturer.contains("samsung")) {
            Log.e("manufacturer", "samsung call");
            Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        } else {
            Intent chooser = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getDownloadCacheDirectory().getPath().toString());
            chooser.addCategory(Intent.CATEGORY_OPENABLE);
            chooser.setDataAndType(uri, "*/*");

            try {
                PackageManager pm = getPackageManager();
                if (chooser.resolveActivity(pm) != null) {
                    startActivityForResult(chooser, PICKFILE_RESULT_CODE);
                }

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null
                && requestCode != RESULT_CANCELED) {
            Log.e("contains", "requestCode == 1002");

            Uri getUri = data.getData();
            Log.d("--uri--", "getUri_Restore:: " + getUri);

            if (getUri != null) {
                try {
                    Log.e("contains", "data.getData()");
                    String getPathFromFileManager = data.getData().getPath();
                    Log.d("--uri--", "getPathFromFileManager_Restore: " + getPathFromFileManager);

                    String FilePath = "";
                    Log.e("contains", getPathFromFileManager);

                    if ("content".equals(getUri.getScheme())) {
                        FilePath = ClsGlobal.getPathFromUri(Activity_ListOfBkp_File.this, getUri);
                    } else {
                        FilePath = getUri.getPath();
                    }

                    if (getPathFromFileManager != null && getPathFromFileManager.contains("primary:")) {
                        Log.e("contains", "contains inside");

                        if (FilePath != null && FilePath.contains(".zip")) {
                            new LocalRestoreAsyncTask(this, FilePath).execute();
                            Toast.makeText(this, FilePath, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Incorrect file", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Log.e("contains", "contains outside");
                        if (getPathFromFileManager != null && getPathFromFileManager.contains(".zip")
                                || FilePath != null && FilePath.contains(".zip")) {

                            new LocalRestoreAsyncTask(this, FilePath).execute();
                            Toast.makeText(this, FilePath, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Incorrect file", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
