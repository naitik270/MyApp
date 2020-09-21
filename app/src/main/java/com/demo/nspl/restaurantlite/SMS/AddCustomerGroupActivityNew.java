package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.A_Test.ClsGetValue;
import com.demo.nspl.restaurantlite.A_Test.MultipleSelectionAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class AddCustomerGroupActivityNew extends AppCompatActivity {

    Toolbar toolbar;
    //    TextView txt_add_customer;
    RecyclerView rv_display_customer;
    TextView img_customer;
    ImageView img_clear_main;
    TextView txt_total_count;
    TextView txt_grp_id;
    EditText edit_search_main;

    private RecyclerView rv_select_customer;

    private LinearLayout ll_bottom;


    private List<ClsCustomerMaster> itemList = new ArrayList<>();
    public List<ClsGetValue> listCustomerMain = new ArrayList<>();

    DisplayCustomerNameAdapter adapter;
    MultipleSelectionAdapter mCv = new MultipleSelectionAdapter(AddCustomerGroupActivityNew.this);

    String _whereFilter = "";
    EditText edt_grp_name;
    EditText edt_group_remark;
    RadioButton rb_yes, rb_no;

    int _ID, result;
    int mainListPosition = 0;
    ProgressBar progress_bar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_group);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddCustomerGroupActivityNew"));
        }

        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main();

        adapter = new DisplayCustomerNameAdapter(AddCustomerGroupActivityNew.this);
        rv_display_customer.setAdapter(adapter);


        adapter.SetOnClickListener((cls, position) -> {
            ShowDeleteAlertDialog(cls, "Do you want to Delete ?", position);
        });


        if (_ID != 0) {
            fillValue();
        }

        fillCustomerList(_whereFilter);

    }

    private void main() {
        _ID = getIntent().getIntExtra("_ID", 0);

        Log.d("--Group--", "_ID: " + _ID);


        rv_display_customer = findViewById(R.id.rv_display_customer);
        txt_total_count = findViewById(R.id.txt_total_count);
        rv_display_customer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        img_clear_main = findViewById(R.id.img_clear_main);
        img_customer = findViewById(R.id.img_customer);
        edit_search_main = findViewById(R.id.edit_search_main);
        edt_grp_name = findViewById(R.id.edt_grp_name);
        edt_group_remark = findViewById(R.id.edt_group_remark);
        rb_yes = findViewById(R.id.rb_yes);
        rb_no = findViewById(R.id.rb_no);
        txt_grp_id = findViewById(R.id.txt_grp_id);


        img_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomerDialog();
            }
        });

        img_clear_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_search_main.setText("");

            }
        });


        edit_search_main.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.e("--Text--", "--AFTER--" + s.toString());


                filterMain(edit_search_main.getText().toString().trim(), listCustomerMain);

                txt_total_count.setText(listCustomerMain.size() + " CUSTOMERS");
            }
        });


    }


    void fillValue() {

        ClsReturnListObj obj = ClsSmsCustomerGroup.getCustomerGroupValue(_ID, getApplicationContext());
        ClsSmsCustomerGroup clsSmsCustomerGroup = obj.getObj();

        txt_grp_id.setText(String.valueOf(_ID));
        edt_grp_name.setText(clsSmsCustomerGroup.getGroupName());

        if (clsSmsCustomerGroup.getActive().contains("YES")) {
            rb_yes.setChecked(true);
        } else {
            rb_no.setChecked(true);
        }

        edt_group_remark.setText(clsSmsCustomerGroup.getRemark());
        listCustomerMain = obj.getLstClsSmsCustomerGroups();

        if (listCustomerMain != null && listCustomerMain.size() != 0) {
            adapter.AddItems(listCustomerMain);

            txt_total_count.setText(listCustomerMain.size() + " CUSTOMERS");
        }
    }


    void fillCustomerList(String _whereFilter) {

        itemList = new ArrayList<>();
        itemList = new ClsCustomerMaster().getAsyncTaskListCustomers(_whereFilter, AddCustomerGroupActivityNew.this);
        //fill adp here
    }

    void openCustomerDialog() {

        Dialog dialogCustomer = new Dialog(AddCustomerGroupActivityNew.this);
        dialogCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomer.setContentView(R.layout.multiple_selection_with_search);
        dialogCustomer.setTitle("Select Customer");
        dialogCustomer.setCanceledOnTouchOutside(true);
        dialogCustomer.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCustomer.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        EditText edit_search = dialogCustomer.findViewById(R.id.edit_search);
        ImageView img_clear = dialogCustomer.findViewById(R.id.img_clear);
        rv_select_customer = dialogCustomer.findViewById(R.id.rv_select_customer);
        rv_select_customer.setLayoutManager(new LinearLayoutManager(AddCustomerGroupActivityNew.this));
        ll_bottom = dialogCustomer.findViewById(R.id.ll_bottom);

        progress_bar = dialogCustomer.findViewById(R.id.progress_bar);

        Button btn_all = dialogCustomer.findViewById(R.id.btn_all);
        Button btn_clear = dialogCustomer.findViewById(R.id.btn_clear);
        Button btn_select = dialogCustomer.findViewById(R.id.btn_select);
        Button btn_close = dialogCustomer.findViewById(R.id.btn_close);

        if (itemList != null && itemList.size() != 0) {
            ll_bottom.setVisibility(View.GONE);
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
        }

        mCv = new MultipleSelectionAdapter(getApplicationContext());
        mCv.AddItems(itemList);//FILL ADP

        mCv.OnCharacterClick((clsCustomerMaster, position, holder) -> {

            boolean flag = !holder.txt_label.isChecked();
            holder.txt_label.setChecked(flag);

            if (holder.txt_label.isChecked()) {

                holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.linear_layout.setBackgroundResource(0);
            }

            //UPDATE CHECKED LIST
            for (ClsCustomerMaster obj : itemList) {
                if (obj.getmMobile_No().equalsIgnoreCase(clsCustomerMaster.getmMobile_No())) {
                    obj.setSelected(flag);
                    itemList.indexOf(obj);
                    break;
                }
            }

        });
        rv_select_customer.setAdapter(mCv);

        btn_all.setOnClickListener(v -> {
            Check_UnCheckList(true);
            mCv.AddItems(itemList);
        });

        btn_close.setOnClickListener(v -> {
            dialogCustomer.dismiss();
            dialogCustomer.cancel();
        });

        btn_select.setOnClickListener(v -> {
            fillItemDisplayAdapter();
            dialogCustomer.dismiss();
        });

        btn_clear.setOnClickListener(v -> {
            edit_search.setText("");
            Check_UnCheckList(false);
        });

        img_clear.setOnClickListener(v -> {
            edit_search.setText("");
        });

//
//        edit_search.setOnEditorActionListener((s, actionId, event) -> {
//
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                img_clear.requestFocus();
//                ClsGlobal.hideKeyboard(AddCustomerGroupActivityNew.this);
//                return true;
//            }
//            return true;
//        });


        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String _filterTxt = "";

                if (s != null && s.length() != 0)
                    _filterTxt = s.toString();

                filterDialog(_filterTxt);
            }
        });

        dialogCustomer.show();
        dialogCustomer.getWindow().setAttributes(lp);
    }


    void fillItemDisplayAdapter() {

        for (ClsCustomerMaster Obj : itemList) {
            if (Obj.isSelected()
                    && !containsMobile(listCustomerMain, Obj.getmMobile_No())) {
                listCustomerMain.add(new ClsGetValue(Obj.getmName(), Obj.getmMobile_No()));
            }
        }
        adapter.AddItems(listCustomerMain);
        // adapter.notifyDataSetChanged();
        txt_total_count.setText(listCustomerMain.size() + " CUSTOMERS");
    }


    private void ShowDeleteAlertDialog(ClsGetValue cls, String message, int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog OptionDialog = builder.create();
        builder.setMessage(message);

        builder.setPositiveButton("YES", (dialog, id) -> {

            String _mobile = cls.getmMobile();

            for (ClsGetValue _obj : listCustomerMain) {
                if (_mobile.equalsIgnoreCase(_obj.getmMobile())) {
                    listCustomerMain.remove(listCustomerMain.indexOf(_obj));
                    break;
                }
            }

            filterMain(edit_search_main.getText().toString(), listCustomerMain);

            adapter.notifyItemRemoved(position);

            OptionDialog.dismiss();
            OptionDialog.cancel();

            txt_total_count.setText(listCustomerMain.size() + " CUSTOMERS");
        });

        builder.setNegativeButton("NO", (dialogInterface, i) -> {
            OptionDialog.dismiss();
            OptionDialog.cancel();
        });
        builder.show();
    }

    //filter for dialogPOPUP
    void filterDialog(String text) {
        List<ClsCustomerMaster> filterList = new ArrayList();
//        if (text != null && text != "") {


        Log.d("--list--", "filterDialog_IFIFIFIF");


        ll_bottom.setVisibility(View.VISIBLE);

        for (ClsCustomerMaster obj : itemList) {
            if (obj.getmMobile_No().contains(text.toLowerCase())
                    || (obj.getmName() != null && !obj.getmName().isEmpty() && obj.getmName().toLowerCase().contains(text.toLowerCase()))) {
                filterList.add(obj);
            }
        }

        if (filterList.size() != 0) {
            mCv.updateList(filterList);
            ll_bottom.setVisibility(View.GONE);
            rv_select_customer.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            rv_select_customer.setVisibility(View.GONE);
        }
//        } else {
//            mCv.updateList(itemList);
//            ll_bottom.setVisibility(View.GONE);
//            rv_select_customer.setVisibility(View.VISIBLE);
//        }
    }

    List<ClsGetValue> filterList = new ArrayList();

    void filterMain(String text, List<ClsGetValue> lst) {

        filterList = StreamSupport.stream(lst)
                .filter(str -> str.getmName().toLowerCase().contains(text.toLowerCase())
                        || str.getmMobile().contains(text.toLowerCase()))
                .collect(Collectors.toList());

        adapter.AddItems(filterList);

        if (text.isEmpty()) {
            adapter.AddItems(listCustomerMain);
        }
    }

    private void Check_UnCheckList(boolean check_uncheck) {
        for (ClsCustomerMaster Obj : itemList) {
            Obj.setSelected(check_uncheck);
            itemList.set(itemList.indexOf(Obj), Obj);
        }
        mCv.notifyDataSetChanged();
    }


    public boolean containsMobile(final List<ClsGetValue> list, final String mobile) {
        for (ClsGetValue current : list) {
            if (current.getmMobile().contains(mobile)) {
                return true;
            }
        }
        return false;
    }


    private Boolean Validation() {

        if (edt_grp_name.getText() == null || edt_grp_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
            edt_grp_name.requestFocus();
            return false;
        }

        if (listCustomerMain.size() <= 0) {
            Toast.makeText(getApplicationContext(), "Select customer", Toast.LENGTH_SHORT).show();
            edt_grp_name.requestFocus();
            return false;
        }

        if (listCustomerMain.size() >= 5000) {
            Toast.makeText(getApplicationContext(), "You Can't Add Customer More Than 5000!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    void SaveValue() {

        ClsSmsCustomerGroup obj = new ClsSmsCustomerGroup(AddCustomerGroupActivityNew.this);


        String where = " AND  [GroupName] = "
                .concat("'")
                .concat(edt_grp_name.getText().toString().toUpperCase())
                .concat("' ");

        if (_ID != 0) {
            where = where.concat(" AND [GroupId] <> ").concat(String.valueOf(_ID));
        }


        @SuppressLint("WrongConstant") SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        boolean exists = obj.checkExists(where, db);
        if (!exists) {
            if (_ID != 0) {
                UpdateCustomerAsyncTask();
            } else {
                InsertCustomerAsyncTask();
            }
            db.close();
        } else {
            Toast toast = Toast.makeText(AddCustomerGroupActivityNew.this, "Group already exists....", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }

    ProgressDialog pd;

    private void InsertCustomerAsyncTask() {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Integer> asyncTask =
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pd = ClsGlobal._prProgressDialog(AddCustomerGroupActivityNew.this,
                                "Waiting for save records...", true);
                        pd.show();
                    }

                    @SuppressLint("WrongConstant")
                    @Override
                    protected Integer doInBackground(Void... voids) {

                        int Sendresult = 0;

                        ClsSmsCustomerGroup obj = new ClsSmsCustomerGroup(AddCustomerGroupActivityNew.this);
                        obj.setGroupId(_ID);
                        obj.setGroupName(edt_grp_name.getText().toString());
                        obj.setActive(rb_yes.isChecked() ? "YES" : "NO");
                        obj.setRemark(edt_group_remark.getText().toString());

                        Sendresult = ClsSmsCustomerGroup.Insert(obj, listCustomerMain);
                        return Sendresult;
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        super.onPostExecute(result);
                        pd.cancel();

                        if (result > 0) {
                            Toast.makeText(getApplicationContext(), "Record save successfully", Toast.LENGTH_SHORT).show();
                            listCustomerMain.clear();
                            finish();
                        } else if (result == -1) {
                            Toast.makeText(getApplicationContext(), "Technical Error in insert", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
        asyncTask.execute();

    }

    private void UpdateCustomerAsyncTask() {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Integer> asyncTask =
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pd = ClsGlobal._prProgressDialog(AddCustomerGroupActivityNew.this,
                                "Waiting for update records...", true);
                        pd.show();
                    }

                    @SuppressLint("WrongConstant")
                    @Override
                    protected Integer doInBackground(Void... voids) {

                        int Sendresult = 0;

                        ClsSmsCustomerGroup obj = new ClsSmsCustomerGroup(AddCustomerGroupActivityNew.this);
                        obj.setGroupId(_ID);
                        obj.setGroupName(edt_grp_name.getText().toString());
                        obj.setActive(rb_yes.isChecked() ? "YES" : "NO");
                        obj.setRemark(edt_group_remark.getText().toString());

                        Sendresult = ClsSmsCustomerGroup.Update(obj, listCustomerMain);

                        return Sendresult;
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        super.onPostExecute(result);
                        pd.cancel();

                        if (result > 0) {
                            Toast.makeText(getApplicationContext(), "Record Update successfully", Toast.LENGTH_SHORT).show();
                            listCustomerMain.clear();
                            finish();
                        } else if (result == -1) {
                            Toast.makeText(getApplicationContext(), "Technical Error in insert", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Not Updated", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
        asyncTask.execute();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        listCustomerMain.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cust_group, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            listCustomerMain.clear();
            finish();
        }

        if (id == R.id.itm_done) {


            boolean validation = Validation();

            if (validation == true) {

                SaveValue();

            }

        }

        return super.onOptionsItemSelected(item);
    }


}
