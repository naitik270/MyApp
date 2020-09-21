package com.demo.nspl.restaurantlite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.EmployeeDocumentAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmployeeDocuments;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class Document_ListActivity extends AppCompatActivity {


    private List<ClsEmployeeDocuments> list_documents;
    private EmployeeDocumentAdapter cu;
    RecyclerView rv;
    Toolbar toolbar;
    private TextView empty_title_text;


    int cust_id = 0;
    String cust_name = "";
    String cust_mob = "";
    String _flag = "";
    int EMP_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "Document_ListActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.rv);
        EMP_ID = getIntent().getIntExtra("employee_id", 0);
        final String EMP_Name = getIntent().getStringExtra("employee_name");
        final LinearLayoutManager llm = new LinearLayoutManager(Document_ListActivity.this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        empty_title_text = findViewById(R.id.empty_title_text);

        cust_id = getIntent().getIntExtra("cust_id", 0);
        cust_name = getIntent().getStringExtra("cust_name");
        cust_mob = getIntent().getStringExtra("cust_mob");
        _flag = getIntent().getStringExtra("_flag");


        if (_flag.equalsIgnoreCase("customer")) {
            toolbar.setTitle("Customer Document");
        } else {
            toolbar.setTitle("Employee Document");
        }

        ViewData();

        ClsPermission.checkpermission(Document_ListActivity.this);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Document_ListActivity.this, AddDocumentsActivity.class);

            intent.putExtra("EMP_ID", EMP_ID);
            intent.putExtra("Name", EMP_Name);
            intent.putExtra("cust_name", cust_name);
            intent.putExtra("cust_mob", cust_mob);
            intent.putExtra("_flag", _flag);
            intent.putExtra("cust_id", cust_id);
            intent.putExtra("ID", 0);
            startActivity(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData();

    }

    private void ViewData() {

        list_documents = new ArrayList<>();
        if (_flag.equalsIgnoreCase("customer")) {

            list_documents = new ClsEmployeeDocuments(Document_ListActivity.this).getList(" AND [EMP_ID] = ".concat(String.valueOf(cust_id)) + " AND IFNULL([TYPE],'employee') =".concat("'").concat(_flag).concat("'"));
        } else {
            list_documents = new ClsEmployeeDocuments(Document_ListActivity.this).getList(" AND [EMP_ID] = ".concat(String.valueOf(EMP_ID)) + " AND IFNULL([TYPE],'employee') =".concat("'").concat(_flag).concat("'"));
        }


        if (list_documents.size() == 0) {
            empty_title_text.setVisibility(View.VISIBLE);
        } else {
            empty_title_text.setVisibility(View.INVISIBLE);
        }

        cu = new EmployeeDocumentAdapter(Document_ListActivity.this, Document_ListActivity.this, (ArrayList<ClsEmployeeDocuments>) list_documents, _flag);
        rv.setAdapter(cu);
        cu.notifyDataSetChanged();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, Document_ListActivity.this);
                return;
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
