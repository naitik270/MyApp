package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.nspl.restaurantlite.Adapter.AllSmsLogsAdapter;
import com.demo.nspl.restaurantlite.Adapter.SingleSelectionCustSmsAdapter;
import com.demo.nspl.restaurantlite.Adapter.SingleSelectionSmsStatusAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.EndlessRecyclerViewScrollListener;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsSMSLogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CheckSalesSmsStatus;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getSamplePreview;


public class QuotationSmsLogsFragment extends Fragment {

    private RecyclerView rv;
    private TextView txt_nodata;
    private AllSmsLogsAdapter adapter;
    private LinearLayout progress_bar_layout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SQLiteDatabase db;
    List<ClsSMSLogs> list = new ArrayList<>();
    FloatingActionButton fab_filter;


    public QuotationSmsLogsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quotation_sms_logs, container, false);
        // Inflate the layout for this fragment

        rv = view.findViewById(R.id.rv);
        txt_nodata = view.findViewById(R.id.txt_nodata);
        progress_bar_layout = view.findViewById(R.id.progress_bar_layout);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);

        fab_filter = view.findViewById(R.id.fab_filter);
        fab_filter.setColorFilter(Color.WHITE);
        fab_filter.setAlpha(0.50f);


        adapter = new AllSmsLogsAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                ViewData(++page);

            }
        });

        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

        adapter.SetOnClickListener((obj, position) -> {
            AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getActivity());
            popupBuilder.setTitle("Message");
            popupBuilder.setMessage(getSamplePreview(obj.getMessage(), getActivity()));
            popupBuilder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                dialog.cancel();
            });
            popupBuilder.show();
        });

        fab_filter.setOnClickListener(v1 -> applyFilters());
        fillCustomerList();
        fillStatusList();


        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            adapter.clear();
            ViewData(0);

        });

//            Data data = new Data.Builder()
//                    .putString(CheckSmsStatusTask.EXTRA_MODE, "SalesSms")
//                    .build();
//
//            OneTimeWorkRequest oneTimeWorkRequest =
//                    new OneTimeWorkRequest.Builder(CheckSmsStatusTask.class)
//                            .setInputData(data)
//                            .build();
//
//            WorkManager.getInstance().enqueueUniqueWork("CheckSmsStatus"
//                    ,ExistingWorkPolicy.KEEP, oneTimeWorkRequest);
//
//            WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
//                    .observe(this, workInfo -> {
//
//                        if (workInfo != null && workInfo.getState().isFinished()) {
//                            adapter.clear();
//                            ViewData(0);
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        } else {
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }
//
//
//                    });


        ViewData(0);

        return view;
    }


    private List<ClsSMSLogs> lstClsBulkSMSLogs;

    @SuppressLint("WrongConstant")
    private void fillCustomerList() {

        db = getActivity().openOrCreateDatabase(ClsGlobal.Database_Name,
                Context.MODE_APPEND, null);

        lstClsBulkSMSLogs = new ArrayList<>();
        lstClsBulkSMSLogs = new ClsSMSLogs().getSmsLogCustomerList
                (" AND [Type] = 'Quotation'", db);

        db.close();
    }


    private List<ClsSMSLogs> lstClsBulkSMSStatus;

    @SuppressLint("WrongConstant")
    private void fillStatusList() {

        db = getActivity().openOrCreateDatabase(ClsGlobal.Database_Name,
                Context.MODE_APPEND, null);

        lstClsBulkSMSStatus = new ArrayList<>();
        lstClsBulkSMSStatus = new ClsSMSLogs().getSmsLogForStatus
                (" AND [Type] = 'Quotation'", db);

        db.close();
    }


    TextView txt_customer_name;
    TextView txt_select_status;
    ImageButton iv_clear_status, iv_clear_customer;
    ArrayList<String> selectedCustomerName = new ArrayList<>();
    ArrayList<String> selectedCustomerMob = new ArrayList<>();
    ArrayList<String> selectedSmsStatus = new ArrayList<>();

    void applyFilters() {

        Dialog dialogPaymentFilter = new Dialog(getActivity());
        dialogPaymentFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPaymentFilter.setContentView(R.layout.dialog_apply_filters_for_sms);
        Objects.requireNonNull(dialogPaymentFilter.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogPaymentFilter.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogPaymentFilter.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        txt_customer_name = dialogPaymentFilter.findViewById(R.id.txt_customer_name);
        iv_clear_customer = dialogPaymentFilter.findViewById(R.id.iv_clear_customer);

        txt_select_status = dialogPaymentFilter.findViewById(R.id.txt_select_status);
        iv_clear_status = dialogPaymentFilter.findViewById(R.id.iv_clear_status);

        ImageButton bt_close = dialogPaymentFilter.findViewById(R.id.bt_close);
        Button btn_search = dialogPaymentFilter.findViewById(R.id.btn_search);
        Button btn_clear_filters = dialogPaymentFilter.findViewById(R.id.btn_clear_filters);

        txt_select_status.setText(TextUtils.join(",", selectedSmsStatus).toUpperCase().replace("'", ""));
        txt_select_status.setOnClickListener(view -> {
            selectStatus();
        });

        iv_clear_customer.setOnClickListener(view -> {
            Check_UnCheckListForCust(false);
            txt_customer_name.setText("");
            selectedCustomerName.clear();
            _custName = "";
        });

        iv_clear_status.setOnClickListener(view -> {
            Check_UnCheckList(false);
            txt_select_status.setText("");
            selectedSmsStatus.clear();
            _smsStatus = "";
        });

        txt_customer_name.setText(TextUtils.join(",",
                selectedCustomerName).toUpperCase().replace("'", ""));
        txt_customer_name.setOnClickListener(view -> {
            selectCustomer();
        });

        bt_close.setOnClickListener(view -> {
            dialogPaymentFilter.dismiss();
            dialogPaymentFilter.cancel();

        });

        btn_search.setOnClickListener(view -> {
            _where = "";
            filtersCondition();

            ViewData(0);

            dialogPaymentFilter.dismiss();
            dialogPaymentFilter.hide();
        });

        btn_clear_filters.setOnClickListener(view -> {
            _where = "";

            Check_UnCheckListForCust(false);
            txt_customer_name.setText("");
            Check_UnCheckList(false);
            txt_select_status.setText("");

            selectedCustomerName.clear();
            selectedSmsStatus.clear();

            _custName = "";
            _smsStatus = "";

        });

        dialogPaymentFilter.show();
        dialogPaymentFilter.getWindow().setAttributes(lp);
    }

    private void Check_UnCheckList(boolean check_uncheck) {
        for (ClsSMSLogs Obj : lstClsBulkSMSStatus) {
            Obj.setSelected(check_uncheck);
            lstClsBulkSMSStatus.set(lstClsBulkSMSStatus.indexOf(Obj), Obj);
        }
        expAdapterStatus.notifyDataSetChanged();
    }


    private void filtersCondition() {

        if (!_custName.equalsIgnoreCase("")) {


            _where = _where.concat(" AND UPPER([Customer_Name]) IN ")
                    .concat("(")
                    .concat(_custName.trim())
                    .concat(")");
        }

        if (!_custMob.equalsIgnoreCase("")) {


            _where = _where.concat(" OR [mobileNo] IN ")
                    .concat("(")
                    .concat(_custMob.trim())
                    .concat(")");
        }


        if (!_smsStatus.equalsIgnoreCase("")) {


            _where = _where.concat(" AND [Status] IN ")
                    .concat("(")
                    .concat(_smsStatus.trim())
                    .concat(")");
        }


        Log.d("--_where--", "_where: " + _where);
    }


    private void selectStatus() {

        final Dialog dialogExpenseType = new Dialog(getActivity());
        dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExpenseType.setContentView(R.layout.dialog_multiple_selection_sms_cust);
        dialogExpenseType.setTitle("Select Status");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogExpenseType.getWindow().setAttributes(lp);

        LinearLayout ll_search = dialogExpenseType.findViewById(R.id.ll_search);
        ll_search.setVisibility(View.GONE);
        rv_select_customer = dialogExpenseType.findViewById(R.id.rv_select_customer);
        rv_select_customer.setLayoutManager(new LinearLayoutManager(getActivity()));
        ll_bottomDialog = dialogExpenseType.findViewById(R.id.ll_bottom);

        Button btn_select_item = dialogExpenseType.findViewById(R.id.btn_select_item);
        Button btn_clear_list = dialogExpenseType.findViewById(R.id.btn_clear_list);

        progress_bar = dialogExpenseType.findViewById(R.id.progress_bar);
        edit_search = dialogExpenseType.findViewById(R.id.edit_search);

        dialogExpenseType.setCanceledOnTouchOutside(true);
        dialogExpenseType.setCancelable(true);

        expAdapterStatus = new SingleSelectionSmsStatusAdapter(getActivity());
        expAdapterStatus.AddItems(lstClsBulkSMSStatus);//FILL ADP
        expAdapterStatus.OnCharacterClick((clsCustomerMaster, position, holder) -> {

            flag = !holder.txt_label.isChecked();
            holder.txt_label.setChecked(flag);

            if (holder.txt_label.isChecked()) {

                holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                        R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                        R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.linear_layout.setBackgroundResource(0);
            }

            //UPDATE CHECKED LIST
            for (ClsSMSLogs obj : lstClsBulkSMSStatus) {
                if (obj.getStatus().equalsIgnoreCase(clsCustomerMaster.getStatus())) {
                    obj.setSelected(flag);
                    lstClsBulkSMSStatus.indexOf(obj);
                    break;
                }
            }

        });
        rv_select_customer.setAdapter(expAdapterStatus);

        btn_select_item.setOnClickListener(v -> {

            selectedSmsStatus = new ArrayList<>();

            for (ClsSMSLogs model : lstClsBulkSMSStatus) {

                if (model.isSelected()) {
                    selectedSmsStatus.add("'".concat(model.getStatus()).concat("'"));
                }
            }

            txt_select_status.setText(TextUtils.join(",", selectedSmsStatus).toUpperCase().replace("'", ""));
            _smsStatus = TextUtils.join(",", selectedSmsStatus);

            dialogExpenseType.dismiss();
        });

        btn_clear_list.setOnClickListener(v -> {

            Check_UnCheckList(false);
            edit_search.setText("");
            txt_select_status.setText("");
        });

        dialogExpenseType.show();
    }

    private void selectCustomer() {

        final Dialog dialogExpenseType = new Dialog(getActivity());
        dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExpenseType.setContentView(R.layout.dialog_multiple_selection_sms_cust);
        dialogExpenseType.setTitle("Select Customer");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogExpenseType.getWindow().setAttributes(lp);

        LinearLayout ll_search = dialogExpenseType.findViewById(R.id.ll_search);
        ll_search.setVisibility(View.VISIBLE);
        rv_select_customer = dialogExpenseType.findViewById(R.id.rv_select_customer);
        rv_select_customer.setLayoutManager(new LinearLayoutManager(getActivity()));
        ll_bottomDialog = dialogExpenseType.findViewById(R.id.ll_bottom);

        Button btn_select_item = dialogExpenseType.findViewById(R.id.btn_select_item);
        Button btn_clear_list = dialogExpenseType.findViewById(R.id.btn_clear_list);

        progress_bar = dialogExpenseType.findViewById(R.id.progress_bar);
        edit_search = dialogExpenseType.findViewById(R.id.edit_search);

        dialogExpenseType.setCanceledOnTouchOutside(true);
        dialogExpenseType.setCancelable(true);

        expAdapter = new SingleSelectionCustSmsAdapter(getActivity());
        expAdapter.AddItems(lstClsBulkSMSLogs);//FILL ADP

        expAdapter.OnCharacterClick((clsCustomerMaster, position, holder) -> {

            boolean flag = !holder.txt_label.isChecked();
            holder.txt_label.setChecked(flag);

            if (holder.txt_label.isChecked()) {

                holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                        R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                        R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.linear_layout.setBackgroundResource(0);
            }

            //UPDATE CHECKED LIST
            for (ClsSMSLogs obj : lstClsBulkSMSLogs) {
                if (obj.getMobileNo().equalsIgnoreCase(clsCustomerMaster.getMobileNo())) {
                    obj.setSelected(flag);
                    lstClsBulkSMSLogs.indexOf(obj);
                    break;
                }
            }

        });
        rv_select_customer.setAdapter(expAdapter);

        edit_search.setOnEditorActionListener((s, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                img_clear.requestFocus();
                ClsGlobal.hideKeyboard(getActivity());
                return true;
            }
            return true;
        });


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

                filterMain(_filterTxt, lstClsBulkSMSLogs);
            }
        });

        btn_select_item.setOnClickListener(v -> {

            selectedCustomerName = new ArrayList<>();
            selectedCustomerMob = new ArrayList<>();
            for (ClsSMSLogs model : lstClsBulkSMSLogs) {

                if (model.isSelected()) {
                    if (model.getCustomer_Name() != null && model.getCustomer_Name().equalsIgnoreCase("")) {

                        selectedCustomerMob.add("'".concat(model.getMobileNo()).concat("'"));
                    } else {
                        selectedCustomerName.add("'".concat(model.getCustomer_Name().toUpperCase())
                                .concat("'"));
                    }
                }
            }

            txt_customer_name.setText(TextUtils.join(",", selectedCustomerMob).replace("'", ",")
                    .concat(TextUtils.join(",", selectedCustomerName).toUpperCase()).replace("'", ""));

            _custName = TextUtils.join(",", selectedCustomerName);
            _custMob = TextUtils.join(",", selectedCustomerMob);


            Log.d("--_where--", "_custName: " + _custName);
            dialogExpenseType.dismiss();
        });

        btn_clear_list.setOnClickListener(v -> {

            Check_UnCheckListForCust(false);
            edit_search.setText("");
            txt_customer_name.setText("");

        });

        dialogExpenseType.show();
    }

    private void Check_UnCheckListForCust(boolean check_uncheck) {
        for (ClsSMSLogs Obj : lstClsBulkSMSLogs) {
            Obj.setSelected(check_uncheck);
            lstClsBulkSMSLogs.set(lstClsBulkSMSLogs.indexOf(Obj), Obj);
        }
        expAdapter.notifyDataSetChanged();
    }

    List<ClsSMSLogs> filterList = new ArrayList();

    void filterMain(String text, List<ClsSMSLogs> lst) {

        filterList = StreamSupport.stream(lst)
                .filter(str -> str.getCustomer_Name().toLowerCase().contains(text.toLowerCase())
                        || str.getMobileNo().contains(text.toLowerCase()))
                .collect(Collectors.toList());


        if (text.isEmpty()) {
            expAdapter.AddItems(lstClsBulkSMSLogs);
        }

        //update recyclerview
        if (filterList.size() != 0) {
            txt_nodata.setVisibility(View.GONE);
            rv_select_customer.setVisibility(View.VISIBLE);
            expAdapter.AddItems(filterList);

        } else {
            txt_nodata.setVisibility(View.VISIBLE);
            rv_select_customer.setVisibility(View.GONE);
        }
    }

    String _custName = "";
    String _custMob = "";
    String _smsStatus = "";
    String _where = "";

    ProgressBar progress_bar;
    EditText edit_search;
    RecyclerView rv_select_customer;

    SingleSelectionCustSmsAdapter expAdapter = new SingleSelectionCustSmsAdapter(getActivity());
    SingleSelectionSmsStatusAdapter expAdapterStatus = new SingleSelectionSmsStatusAdapter(getActivity());

    boolean flag;
    LinearLayout ll_bottomDialog;
    ImageView img_clear;

    private void ViewData(int currentPageNo) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void>
                asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress_bar_layout.setVisibility(View.VISIBLE);
            }

            @SuppressLint("WrongConstant")
            @Override
            protected Void doInBackground(Void... voids) {
                db = getActivity().openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

                CheckSalesSmsStatus(getActivity(), db);

                String _paging = "";

                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int pageSize = 30;
                int skip = pageSize * (currentPageNo - 1);
                _paging = " LIMIT ".concat("" + skip).concat(", ").concat("" + pageSize);


                if (currentPageNo > 0) {
                    if (ClsSMSLogs.getList(_where.concat(" AND [Type] = 'Quotation'"),
                            _paging, db).size() != 0) {
                        list = ClsSMSLogs.getList(_where
                                        .concat(" AND [Type] = 'Quotation'"),
                                _paging, db);
                    }
                } else {
                    if (ClsSMSLogs.getList(_where.concat(" AND [Type] = 'Quotation'"),
                            _paging, db).size() != 0) {
                        list = ClsSMSLogs.getList(_where
                                        .concat(" AND [Type] = 'Quotation'"),
                                _paging, db);
                    }
                }

//                list.addAll(ClsSMSLogs.getList(_where.concat(" AND [Type] = 'Quotation' "), _paging, db));
                db.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                progress_bar_layout.setVisibility(View.GONE);
                if (list != null && list.size() > 0) {
                    txt_nodata.setVisibility(View.GONE);

                    adapter.AddItems(list);

                } else {
                    txt_nodata.setVisibility(View.VISIBLE);
                }
            }
        };
        asyncTask.execute();
    }
}
