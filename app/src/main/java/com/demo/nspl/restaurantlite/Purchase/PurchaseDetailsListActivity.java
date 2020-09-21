package com.demo.nspl.restaurantlite.Purchase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.AsyncTaskReport.PurchaseDetailAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.MultipleImage.PurchaseListImagePreviewActivity;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PurchaseDetailsListActivity extends AppCompatActivity {

    RecyclerView rv_purchase;
    String monthYear = "";
    TextView txt_month_year;
    Toolbar toolbar;
    List<ClsPurchaseMaster> lstClsPurchaseMasters;
    String _whereSearch = "";
    FrameLayout ll_header;
    public static final int DIALOG_QUEST_CODE = 300;


    PurchaseDetailAdapter purchaseDetailAdapter;
    PurchaseMasterAdapter purchaseMasterAdapter;

    FloatingActionButton fab_filter;
    TextView txt_nodata;
    ProgressBar progress_bar;


    EditText edt_bill_no;
    ImageButton iv_clear_vendor, iv_clear_bill_no, clear_date, bt_close;
    Button btn_search, btn_clear;
    TextView from_date, to_date, txt_vendor_name;
    ArrayList<String> selectedVendorName = new ArrayList<>();
    List<ClsPurchaseMaster> lstClsPurchaseMastersVendor;
    String vendorID = "";
    String _FromDate = "";
    String _ToDate = "";
    String _billNo = "";
    String _whereFilter = "";
    String _purchaseFlag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_details);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        main();
    }

    private void main() {

        Intent intent = getIntent();
        monthYear = intent.getStringExtra("monthYear");
        _purchaseFlag = intent.getStringExtra("_purchaseFlag");

        ll_header = findViewById(R.id.ll_header);
        progress_bar = findViewById(R.id.progress_bar);

        fab_filter = findViewById(R.id.fab_filter);
        fab_filter.setColorFilter(Color.WHITE);
        fab_filter.setAlpha(0.50f);

        txt_month_year = findViewById(R.id.txt_month_year);
        txt_month_year.setText(monthYear);

        rv_purchase = findViewById(R.id.rv_purchase);
        rv_purchase.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        txt_nodata = findViewById(R.id.txt_nodata);

        fillVendorList();

        fab_filter.setOnClickListener(v1 -> {
            applyFilters();
        });


        clickOnAdapter();


        String _where = " AND strftime('%m %Y',P.[PurchaseDate]) ="
                .concat("'").concat(ClsGlobal.getMonthYearDigit(monthYear)).concat("'");


        loadData(_where);

    }

    void loadData(String _where) {

        new PurchaseDetailAsyncTask(_where.concat(" AND strftime('%m %Y',P.[PurchaseDate]) ="
                .concat("'" + ClsGlobal.getMonthYearDigit(monthYear) + "'")
                .concat("")),
                txt_nodata,
                PurchaseDetailsListActivity.this, purchaseDetailAdapter,
                progress_bar, rv_purchase).execute();
    }

    void clickOnAdapter() {

        purchaseDetailAdapter = new PurchaseDetailAdapter(PurchaseDetailsListActivity.this);

        purchaseDetailAdapter.setOnViewImg((objClsLayerItemMaster, position) -> {

            Intent intent12 = new Intent(getApplicationContext(), PurchaseListImagePreviewActivity.class);
            intent12.putExtra("_imgMode", "purchase");
            intent12.putExtra("_purchaseFlag", _purchaseFlag);
            intent12.putExtra("_pID", objClsLayerItemMaster.getPurchaseID());
            startActivity(intent12);

        });

        purchaseDetailAdapter.SetOnClickListener((clsPurchaseMaster, position) -> {

            final Dialog dialog = new Dialog(PurchaseDetailsListActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_vendor_details);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);

            RelativeLayout layout_view_details = dialog.findViewById(R.id.layout_view_details);
            RelativeLayout lyout_edit = dialog.findViewById(R.id.lyout_edit);
            RelativeLayout layout_delete = dialog.findViewById(R.id.layout_delete);

            lyout_edit.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                if (clsPurchaseMaster.getPurchaseID() != 0) {

                    Intent intent = new Intent(getApplicationContext(), RetailPurchaseActivity.class);
                    intent.putExtra("_purchaseFlag", "purchaseUpdate");
                    intent.putExtra("_updatePurchaseID", clsPurchaseMaster.getPurchaseID());
                    intent.putExtra("_updatePurchaseDate", clsPurchaseMaster.getPurchaseDate());
                    intent.putExtra("_updatePurchaseBillNo", clsPurchaseMaster.getBillNO());
                    intent.putExtra("_updatePurchasePoNo", clsPurchaseMaster.getPurchaseNo());
                    intent.putExtra("_updatePurchaseVendorID", clsPurchaseMaster.getVendorID());
                    intent.putExtra("_updatePurchaseVendorName", clsPurchaseMaster.getVendorName());
                    intent.putExtra("_updatePurchaseRemark", clsPurchaseMaster.getRemark());
                    startActivity(intent);
                }
            });

            layout_delete.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PurchaseDetailsListActivity.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);

                alertDialog.setPositiveButton("YES", (dialog1, which) -> {

                    dialog1.dismiss();
                    dialog1.cancel();

                    ClsPurchaseMaster DeleteResult = ClsPurchaseMaster.getPurchaseDetailListDelete(clsPurchaseMaster.getPurchaseID());
                    int _deleteMaster = DeleteResult.get_PurchaseMasterResult();
                    int _deleteDetail = DeleteResult.get_PurchaseDetailResult();

                    if (_deleteMaster > 0 && _deleteDetail > 0) {
                        Toast.makeText(PurchaseDetailsListActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                        lstClsPurchaseMasters.remove(position);
                        purchaseDetailAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("NO", (dialog1, which) -> {
                    dialog1.dismiss();
                    dialog1.cancel();

                });
                // Showing Alert Message
                alertDialog.show();
            });

            layout_view_details.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();
                ll_header.setVisibility(View.INVISIBLE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                PurchaseItemDetailsDialogFragment newFragment = new PurchaseItemDetailsDialogFragment();

                newFragment.setID(clsPurchaseMaster.getPurchaseID());
                newFragment.setTotal(clsPurchaseMaster.getPurchaseVal(), clsPurchaseMaster.get_totalTax());

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();

                newFragment.setOnCallbackResult(requestCode -> {
                    if (requestCode == DIALOG_QUEST_CODE) {
                        Log.e("requestCode", String.valueOf(requestCode));
                        ll_header.setVisibility(View.VISIBLE);
                    }
                });

            });

            dialog.show();
        });

        rv_purchase.setAdapter(purchaseDetailAdapter);
    }


    int mYear, mMonth, mDay;
    Calendar c;

    private void applyFilters() {
        Dialog dialog = new Dialog(PurchaseDetailsListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter_purchase);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        bt_close = dialog.findViewById(R.id.bt_close);

        from_date = dialog.findViewById(R.id.from_date);
        to_date = dialog.findViewById(R.id.to_date);
        clear_date = dialog.findViewById(R.id.clear_date);

        c = Calendar.getInstance();


        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        from_date.setText(ClsGlobal.getChangeDateFormat(_FromDate));
        to_date.setText(ClsGlobal.getChangeDateFormat(_ToDate));


        from_date.setOnClickListener(view -> {
            pickDateFromMonth();
        });

        to_date.setOnClickListener(view -> {
            pickDateToMonth();
        });

        clear_date.setOnClickListener(view -> {

            _FromDate = "";
            _ToDate = "";
            from_date.setText("");
            to_date.setText("");


        });


        txt_vendor_name = dialog.findViewById(R.id.txt_vendor_name);
        iv_clear_vendor = dialog.findViewById(R.id.iv_clear_vendor);

        edt_bill_no = dialog.findViewById(R.id.edt_bill_no);
        iv_clear_bill_no = dialog.findViewById(R.id.iv_clear_bill_no);

        btn_clear = dialog.findViewById(R.id.btn_clear);
        btn_search = dialog.findViewById(R.id.btn_search);


        txt_vendor_name.setHint("SELECT VENDOR");
        txt_vendor_name.setText(TextUtils.join(",", selectedVendorName).toUpperCase());

        txt_vendor_name.setOnClickListener(view -> {
            selectVendor();
        });

        iv_clear_vendor.setOnClickListener(view -> {

            txt_vendor_name.setText("");
            vendorID = "";
            selectedVendorName.clear();
        });

        iv_clear_bill_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_bill_no.setText("");
            }
        });


        edt_bill_no.setText(_billNo);
        from_date.setText(ClsGlobal.getChangeDateFormat(_FromDate));
        to_date.setText(ClsGlobal.getChangeDateFormat(_ToDate));

        btn_search.setOnClickListener(view -> {


            _billNo = edt_bill_no.getText().toString();
//            _FromDate = from_date.getText().toString();
//            _ToDate = to_date.getText().toString();

            _whereFilter = "";
            filtersCondition();


            loadData(_whereFilter);


            dialog.dismiss();
            dialog.hide();

        });


        btn_clear.setOnClickListener(view -> {

            _whereFilter = "";

//Date selection is Clear & reset.
            from_date.setText("");
            to_date.setText("");
            _FromDate = "";
            _ToDate = "";

//Vendor Selection is Clear & reset.
            txt_vendor_name.setText("");
            vendorID = "";
            selectedVendorName.clear();

//Bill No is Clear & reset.
            edt_bill_no.setText("");
            _billNo = "";


        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public void pickDateFromMonth() {


        try {
            String dateStr = ClsGlobal.getFirstDateOfMonth(monthYear);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = sdf.parse(dateStr);

            String _endDate = ClsGlobal.getLastDay(dateObj);
            SimpleDateFormat sdfEndDate = new SimpleDateFormat("dd/MM/yyyy");
            Date lastDateObj = sdfEndDate.parse(_endDate);

            long startDate = dateObj.getTime();
            long endDate = lastDateObj.getTime();

            DatePickerDialog dpd = new DatePickerDialog(PurchaseDetailsListActivity.this,
                    (view12, year, month, day) -> {
                        c.set(year, month, day);
                        _FromDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        Log.e("--Date--", "_FromDate: " + _FromDate);

                        from_date.setText(ClsGlobal.getChangeDateFormat(_FromDate));

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(endDate);

            dpd.getDatePicker().setMinDate(startDate);

            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void pickDateToMonth() {


        try {
            String dateStr = ClsGlobal.getFirstDateOfMonth(monthYear);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = sdf.parse(dateStr);

            String _endDate = ClsGlobal.getLastDay(dateObj);
            SimpleDateFormat sdfEndDate = new SimpleDateFormat("dd/MM/yyyy");
            Date lastDateObj = sdfEndDate.parse(_endDate);

            long startDate = dateObj.getTime();
            long endDate = lastDateObj.getTime();

            DatePickerDialog dpd = new DatePickerDialog(PurchaseDetailsListActivity.this,
                    (view12, year, month, day) -> {
                        c.set(year, month, day);
                        _ToDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());


                        Log.e("--Date--", "_ToDate: " + _ToDate);


                        to_date.setText(ClsGlobal.getChangeDateFormat(_ToDate));

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(endDate);

            dpd.getDatePicker().setMinDate(startDate);

            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    private void fillVendorList() {
        String _where = " AND strftime('%m %Y',P.[PurchaseDate]) ="
                .concat("'").concat(ClsGlobal.getMonthYearDigit(monthYear)).concat("'");
        lstClsPurchaseMastersVendor = new ArrayList<>();
        lstClsPurchaseMastersVendor = new ClsPurchaseMaster(getApplicationContext()).getVendorListByMonthYear(PurchaseDetailsListActivity.this, _where);
    }


    private void selectVendor() {

        final Dialog dialogExpenseType = new Dialog(PurchaseDetailsListActivity.this);
        dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExpenseType.setContentView(R.layout.dialog_multiple_selection);
        dialogExpenseType.setTitle("Select Vendor");


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogExpenseType.getWindow().setAttributes(lp);


        ListView listView = dialogExpenseType.findViewById(R.id.listview);
        Button btn_Select = dialogExpenseType.findViewById(R.id.button);

        dialogExpenseType.setCanceledOnTouchOutside(true);
        dialogExpenseType.setCancelable(true);


        AdapterVendors expAdapter = new AdapterVendors(getApplicationContext(), lstClsPurchaseMastersVendor);

        listView.setAdapter(expAdapter);


        btn_Select.setOnClickListener(v -> {
            boolean[] isSelected = expAdapter.getSelectedFlags();
            selectedVendorName = new ArrayList<>();
            ArrayList<Integer> selectedVendorID = new ArrayList<>();

            for (int i = 0; i < isSelected.length; i++) {
                if (isSelected[i]) {
                    selectedVendorName.add(lstClsPurchaseMastersVendor.get(i).getVendorName());
                    selectedVendorID.add(lstClsPurchaseMastersVendor.get(i).getVendorID());
                }
            }

            txt_vendor_name.setText(TextUtils.join(",", selectedVendorName).toUpperCase());

            vendorID = TextUtils.join(",", selectedVendorID);

            dialogExpenseType.dismiss();
        });
        dialogExpenseType.show();

    }

    private void filtersCondition() {


        if (!_FromDate.equalsIgnoreCase("") && !_ToDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND P.[PurchaseDate] between "
                    .concat("('".concat(_FromDate).concat("')"))
                    .concat(" AND ")
                    .concat("('".concat(_ToDate).concat("')")));

        } else if (!_FromDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND P.[PurchaseDate] = ".concat("('"
                    .concat(_FromDate).concat("')")));

        } else if (!_ToDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND P.[PurchaseDate] = ".concat("('"
                    .concat(_ToDate).concat("')")));
        }

        if (!vendorID.equalsIgnoreCase("")) {


            _whereFilter = _whereFilter.concat(" AND P.[VendorID] IN ")
                    .concat("(")
                    .concat(vendorID)
                    .concat(")");

        }

        if (!_billNo.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND P.[BillNO] = ")
                    .concat("'")
                    .concat(edt_bill_no.getText().toString().trim())
                    .concat("'");
        }

        Log.d("--Filters--", "BillNO: " + _whereFilter);

//        Log.d("--Filters--", "_FromDate: " + ClsGlobal.getFilterDate(_FromDate));
//        Log.d("--Filters--", "_ToDate: " + ClsGlobal.getFilterDate(_ToDate));

        loadData(_whereFilter);
    }


    public class AdapterVendors extends BaseAdapter {

        private List<ClsPurchaseMaster> data = new ArrayList<>();
        private Context context;
        private boolean[] isSelected;

        public AdapterVendors(Context context, List<ClsPurchaseMaster> data) {
            this.context = context;
            this.data = data;
            isSelected = new boolean[data.size()];
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position); //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_listview, null);
                holder = new ViewHolder();
                holder.row_relative_layout = view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(data.get(position).getVendorName().toUpperCase());
//            holder.checkedTextView.setText(String.valueOf(data.get(position).getVendorID()));

            holder.row_relative_layout.setOnClickListener(v -> {
                // set the check text view
                boolean flag = holder.checkedTextView.isChecked();
                holder.checkedTextView.setChecked(!flag);
                isSelected[position] = !isSelected[position];

                if (holder.checkedTextView.isChecked()) {
                    holder.row_relative_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                } else {
                    holder.row_relative_layout.setBackgroundResource(0);
                }
            });

            return view;
        }

        public boolean[] getSelectedFlags() {
            return isSelected;
        }

        private class ViewHolder {
            RelativeLayout row_relative_layout;
            CheckedTextView checkedTextView;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
//        getPurchaseDetails("");

        loadData("");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        rv_purchase.setVisibility(View.VISIBLE);
        ll_header.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                _whereSearch = "";
//                getPurchaseDetails("");

                loadData(_whereSearch);

                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _whereSearch = "";
                if (query != null && !query.isEmpty()) {

                    _whereSearch = " AND (V.[VENDOR_NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR P.[BillNO] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR P.[PurchaseDate] LIKE '%".concat(query).concat("%')"));

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(_whereSearch);
                    Log.e("--Purchase--", "PurchaseFilter: " + jsonInString);

                    loadData(_whereSearch);

                } else {

                    loadData(_whereSearch);

                    rv_purchase.setVisibility(View.VISIBLE);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            _whereSearch = "";
//            getPurchaseDetails("");

            loadData(_whereSearch);

            searchView.setQuery("", false);
            searchView.clearFocus();
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
