package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.CityAdapter;
import com.demo.nspl.restaurantlite.Adapter.StateAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCityMaster;
import com.demo.nspl.restaurantlite.classes.ClsStateMaster;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.capitalize_First_Char;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.hideKeyboard;
import static com.demo.nspl.restaurantlite.classes.ClsCityMaster.DeleteById;
import static com.demo.nspl.restaurantlite.classes.ClsCityMaster.getCityList;
import static com.demo.nspl.restaurantlite.classes.ClsStateMaster.getStateList;

public class AddCityActivity extends AppCompatActivity {

    //    private ImageView iv_add_city;
    private Button btn_add_city;
    private RelativeLayout rl_main;
    private ImageView img_clear_query;
    Toolbar mToolbar;
    private int SateId = 0;
    private EditText edit_city_code, ed_city_name_search;
    private RecyclerView rv;
    private TextView txt_state;
    private CityAdapter mCityAdapter;
    StateAdapter adapter = new StateAdapter();
    AsyncTask<Void, Void, Void> asyncTask;
    private LinearLayout progress_bar_layout, no_data_layout, ll_add_city;
    private List<ClsCityMaster> mCityMasterList = new ArrayList<>();
    private List<ClsStateMaster> mStateMasterList = new ArrayList<>();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_state_city);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddStateCityActivity"));
        }

        rl_main = findViewById(R.id.rl_main);
        img_clear_query = findViewById(R.id.img_clear_query);
        txt_state = findViewById(R.id.txt_state);
        progress_bar_layout = findViewById(R.id.progress_bar_layout);
        no_data_layout = findViewById(R.id.no_data_layout);
        ll_add_city = findViewById(R.id.ll_add_city);
        ed_city_name_search = findViewById(R.id.ed_city_name_search);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(
                AddCityActivity.this));

        mCityAdapter = new CityAdapter(AddCityActivity.this);
        rv.setAdapter(mCityAdapter);

        mCityAdapter.SetOnClickListener((clsCityMaster, mode) -> {

            if (mode.equalsIgnoreCase("delete Click")) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCityActivity.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setPositiveButton("YES", (dialog, which) -> {

                    Observable.just(DeleteById(" AND [City_ID] = "
                                    + clsCityMaster.getCity_id(),
                            AddCityActivity.this))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                if (result > 0) {
                                    Toast.makeText(AddCityActivity.this,
                                            "City Deleted Successfully.", Toast.LENGTH_SHORT).show();
                                    LoadCityAndState();
                                } else {
                                    Toast.makeText(AddCityActivity.this,
                                            "Failed to Delete City.", Toast.LENGTH_SHORT).show();
                                }

                                ed_city_name_search.setText("");

                            });

                });

                alertDialog.setNegativeButton("NO", (dialog, which) -> {
                    dialog.dismiss();

                });
                // Showing Alert Message
                alertDialog.show();


            } else if (mode.equalsIgnoreCase("edit Click")) {
                AddEditDialog("Update", clsCityMaster);

            }


        });


        txt_state.setText("All");

        LoadCityAndState();

        txt_state.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(AddCityActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            View dialogView = getLayoutInflater()
                    .inflate(R.layout.dialog_select_state,
                            null);

            dialog.getWindow().setContentView(dialogView);
            dialog.show();

            EditText editText_search = dialogView.findViewById(R.id.editText_search);
            TextView textView_noresult = dialogView.findViewById(R.id.textView_noresult);
            ImageView img_clear_query = dialogView.findViewById(R.id.img_clear_query);
            RecyclerView rv = dialogView.findViewById(R.id.rv);


            rv.setLayoutManager(new
                    LinearLayoutManager(AddCityActivity.this));

            img_clear_query.setOnClickListener(v1 -> {
                editText_search.setText("");
            });

            rv.setAdapter(adapter);

            editText_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null) {

                        List<ClsStateMaster> filterList = StreamSupport
                                .stream(mStateMasterList)
                                .filter(str -> str.getName()
                                        .toLowerCase()
                                        .contains(s.toString().toLowerCase()))
                                .collect(Collectors.toList());

                        if (filterList != null && filterList.size() > 0) {
                            textView_noresult.setVisibility(View.GONE);
                            adapter.AddItem(filterList);
                        } else {
                            adapter.Clear();
                            textView_noresult.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });


            adapter.setOnItemClick(clsStateMaster -> {
                dialog.cancel();
                txt_state.setText(clsStateMaster.getName());
                SateId = clsStateMaster.getSTATE_ID();

                hideKeyboard(AddCityActivity.this);
                LoadCityAndState();
            });


        });


        img_clear_query.setOnClickListener(v -> {
            ed_city_name_search.setText("");
        });

        ed_city_name_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {

                    Observable.just(SearchCity(s.toString()))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(resultList -> {

                                if (resultList != null && resultList.size() > 0) {
                                    no_data_layout.setVisibility(View.GONE);
                                    mCityAdapter.AddItems(resultList);
                                } else {
                                    mCityAdapter.Clear();

                                    if (asyncTask != null
                                            && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {

                                        no_data_layout.setVisibility(View.GONE);
                                    } else {
                                        no_data_layout.setVisibility(View.VISIBLE);
                                    }
                                }


                            });

                }

            }
        });


        ll_add_city.setOnClickListener(v -> {
            if (txt_state.getText() != null && !txt_state.getText().toString().isEmpty()
                    && !txt_state.getText().toString().equalsIgnoreCase("All")) {
                AddEditDialog("Add", new ClsCityMaster());
            } else {
                Toast.makeText(this, "Please Select State", Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @SuppressLint("StaticFieldLeak")
    private void LoadCityAndState() {

        asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mCityAdapter.Clear();
                no_data_layout.setVisibility(View.GONE);
                progress_bar_layout.setVisibility(View.VISIBLE);

            }

            @Override
            protected Void doInBackground(Void... voids) {


                mStateMasterList = getStateList("",
                        AddCityActivity.this);

                if (txt_state.getText() != null &&
                        txt_state.getText().toString().equalsIgnoreCase("All")) {
                    mCityMasterList = getCityList(""
                            , AddCityActivity.this);
                } else {

                    mCityMasterList = getCityList(" AND [StateId] = " + SateId
                            , AddCityActivity.this);
                }


                Log.e("Check", "mCityMasterList: " + mCityMasterList.size());

                return null;
            }

            @SuppressLint("CheckResult")
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                if (progress_bar_layout.getVisibility() == View.VISIBLE) {
                    progress_bar_layout.setVisibility(View.GONE);
                }

                if (mCityMasterList != null && mCityMasterList.size() > 0) {

                    mCityAdapter.AddItems(mCityMasterList);
                    no_data_layout.setVisibility(View.GONE);
                } else {
                    mCityAdapter.Clear();
                    no_data_layout.setVisibility(View.VISIBLE);
                }

                adapter.AddItem(mStateMasterList);
            }
        };

        asyncTask.execute();
    }


    @SuppressLint("CheckResult")
    private void AddEditDialog(String mode,
                               ClsCityMaster updateCityMaster) {


        AlertDialog.Builder mAlertDialogBuilder = new
                AlertDialog.Builder(AddCityActivity.this);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View mView = inflater.inflate(R.layout.dialog_add_city, null);
        EditText ed_city_name = mView.findViewById(R.id.ed_city_name);
        ImageView img_clear_query = mView.findViewById(R.id.img_clear_query);
        TextView dialogTitle = mView.findViewById(R.id.dialogTitle);


        img_clear_query.setOnClickListener(v -> {
            ed_city_name.setText("");
        });

        if (mode.equalsIgnoreCase("Update")) {

            dialogTitle.setText("Edit City");
            ed_city_name.setText(updateCityMaster.getCityName());

            mAlertDialogBuilder.setPositiveButton("Update", null);

        } else if (mode.equalsIgnoreCase("Add")) {

            dialogTitle.setText("Add City");

            mAlertDialogBuilder.setPositiveButton("Add", null);
        }


        mAlertDialogBuilder.setNegativeButton("Cancel", (dialog12, which) -> {
            dialog12.dismiss();
        });

        mAlertDialogBuilder.setView(mView);
        AlertDialog dialog = mAlertDialogBuilder.create();

        // Set positive button click listener.
        dialog.setOnShowListener(dialog1 -> {
            Button button = ((AlertDialog) dialog1)
                    .getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {

                if (mode.equalsIgnoreCase("Update")) {


                    if (txt_state.getText().toString().length() <= 0) {
                        Toast.makeText(AddCityActivity.this, "Select State!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (ed_city_name.getText().length() <= 0) {
                        Toast.makeText(AddCityActivity.this, "City Name Must Not Be Empty!"
                                , Toast.LENGTH_LONG).show();
                        return;
                    }

                    String where = "".concat(" AND [City_Name] = '")
                            .concat(capitalize_First_Char(ed_city_name.getText()
                                    .toString().trim()).concat("'"));
                    if (updateCityMaster.getCity_id() != 0){
                        where = where.concat(" AND [City_ID] <> ").concat(
                                String.valueOf(updateCityMaster.getCity_id()));
                    }

                    Observable.just(ClsCityMaster
                            .checkExists(where, AddCityActivity.this))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {

                                Log.e("Check", "exists Result:" + result);

                                if (!result) {

                                    Log.e("Check", "! Result:" + result);
                                    ClsCityMaster clsCityMaster = new ClsCityMaster();
                                    clsCityMaster.setCity_id(updateCityMaster.getCity_id());
                                    clsCityMaster.setCityName(capitalize_First_Char(
                                            ed_city_name.getText().toString().trim()));
                                    clsCityMaster.setCityCode("");
//                                clsCityMaster.setCityCode(ed_city_code.getText().toString());
                                    clsCityMaster.setStateId(SateId);

                                    Observable.just(ClsCityMaster
                                            .updateCity(clsCityMaster, AddCityActivity.this))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(updatedResult -> {
                                                dialog1.dismiss();
                                                if (updatedResult > 0) {
                                                    Toast.makeText(AddCityActivity.this,
                                                            "City Updated Successfully", Toast.LENGTH_LONG).show();

                                                    LoadCityAndState();

                                                } else {
                                                    Toast.makeText(AddCityActivity.this,
                                                            "Failed to Update!", Toast.LENGTH_LONG).show();
                                                }

                                                ed_city_name_search.setText("");
                                            });
                                } else {
                                    Log.e("Check", "else Result:" + result);
                                    Toast.makeText(AddCityActivity.this,
                                            "City Name already Exists!", Toast.LENGTH_LONG).show();
                                }

                            });

                } else if (mode.equalsIgnoreCase("Add")) {
                    if (txt_state.getText().toString().length() <= 0) {
                        Toast.makeText(AddCityActivity.this, "Select State!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (ed_city_name.getText().length() <= 0) {
                        Toast.makeText(AddCityActivity.this, "City Name Must Not Be Empty!"
                                , Toast.LENGTH_LONG).show();
                        return;
                    }


                    ClsCityMaster clsCityMaster = new ClsCityMaster();
                    clsCityMaster.setCityName(capitalize_First_Char(
                            ed_city_name.getText().toString().trim()));
                    clsCityMaster.setCityCode("");
//                clsCityMaster.setCityCode(ed_city_code.getText().toString());
                    clsCityMaster.setStateId(SateId);


                    if (!clsCityMaster.getCityName().isEmpty()) {

                        String where = "".concat(" AND [City_Name] = '")
                                .concat(capitalize_First_Char(ed_city_name.getText().toString()
                                        .trim()).concat("'"));


                        Observable.just(ClsCityMaster
                                .checkExists(where, AddCityActivity.this))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> {

                                    Log.e("Check", " setPositiveButton Add result: " + result);
                                    if (!result) {
                                        Observable.just(ClsCityMaster
                                                .Insert(clsCityMaster, AddCityActivity.this))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(InsertResult -> {
                                                    dialog1.dismiss();
                                                    Log.e("Check",
                                                            " setPositiveButton Add InsertResult: " + InsertResult);

                                                    if (InsertResult > 0) {
                                                        Toast.makeText(AddCityActivity.this,
                                                                "City Added Successfully", Toast.LENGTH_LONG).show();

                                                        LoadCityAndState();
                                                    } else {
                                                        Toast.makeText(AddCityActivity.this, "Failed to Add City!"
                                                                , Toast.LENGTH_LONG).show();
                                                    }

                                                });

                                    } else {
                                        Toast.makeText(AddCityActivity.this,
                                                "City Name already Exists!", Toast.LENGTH_LONG).show();
                                    }

                                    ed_city_name_search.setText("");
                                });

                    }
                }


            });
        });
        dialog.show();


    }


    private List<ClsCityMaster> SearchCity(String city) {
        return StreamSupport
                .stream(mCityMasterList)
                .filter(str -> str.getCityName()
                        .toLowerCase()
                        .contains(city.toLowerCase()))
                .collect(Collectors.toList());

    }


}
