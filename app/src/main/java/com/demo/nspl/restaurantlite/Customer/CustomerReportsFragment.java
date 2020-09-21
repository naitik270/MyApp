package com.demo.nspl.restaurantlite.Customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.AsyncTaskReport.CustomerReportAsyncTask;
import com.demo.nspl.restaurantlite.AsyncTaskReport.ExcelDataInsertPhoneBookAsyncTask;
import com.demo.nspl.restaurantlite.BuildConfig;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.activity.Activity_ImportCustomer;
import com.demo.nspl.restaurantlite.activity.CustomerDetailActivity;
import com.demo.nspl.restaurantlite.activity.Document_ListActivity;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsExportToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CustomerReportsFragment extends Fragment {

    RecyclerView rv;
    TextView txt_nodata;
    List<ClsCustomerMaster> lstClsCustomerMasters;
    FloatingActionButton fab;
    String _whereSearch = "";
    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".myfileprovider";


    ProgressBar progress_bar;

    public CustomerReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Customer Report");

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "CustomerReportsFragment"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer_reports, container, false);
        setHasOptionsMenu(true);
        ClsGlobal.isFristFragment = true;
        main(view);

        return view;

    }


    private void main(View v) {


        progress_bar = v.findViewById(R.id.progress_bar);
        rv = v.findViewById(R.id.rv);
        fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);

        viewCustomerList();

//


        fab.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), ActivityCustomerMaster.class);
            intent.putExtra("_ID", 0);
            startActivity(intent);

//            ClsCustomerMaster.DeleteAll(getActivity());

        });

    }

    CustomerAdapter customerAdapter;

    private void viewCustomerList() {

        customerAdapter = new CustomerAdapter(getActivity());
        rv.setAdapter(customerAdapter);

        customerAdapter.SetOnClickListener((clsCustomerMaster, position) -> {

            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_functionality);
            dialog.setCancelable(true);
            dialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);

            LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
            LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);
            LinearLayout ll_upload_doc = dialog.findViewById(R.id.ll_upload_doc);
            LinearLayout ll_customer_ledger = dialog.findViewById(R.id.ll_customer_ledger);

            ll_upload_doc.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                Intent intent = new Intent(getActivity(), Document_ListActivity.class);

                intent.putExtra("cust_id", clsCustomerMaster.getmId());
                intent.putExtra("cust_name", clsCustomerMaster.getmName());
                intent.putExtra("cust_mob", clsCustomerMaster.getmMobile_No());
                intent.putExtra("_flag", "customer");

                startActivity(intent);
            });

            ll_customer_ledger.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                Intent intent = new Intent(getActivity(), CustomerDetailActivity.class);

                ClsVendorLedger clsVendorLedger = new ClsVendorLedger();
                clsVendorLedger.setCusomterName(clsCustomerMaster.getmName());
                clsVendorLedger.setCompanyName(clsCustomerMaster.getCompany_Name());
                clsVendorLedger.setCustomerMobileNo(clsCustomerMaster.getmMobile_No());
                clsVendorLedger.setGST_NO(clsCustomerMaster.getGST_NO());

                intent.putExtra("clsVendorLedger", clsVendorLedger);

                startActivity(intent);


            });

            ll_update.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                Intent intent = new Intent(getActivity(), ActivityCustomerMaster.class);

                intent.putExtra("_ID", clsCustomerMaster.getmId());
                Log.d("--Update--", "onClick: " + clsCustomerMaster.getmId());

                startActivity(intent);


            });

            ll_delete.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        ClsCustomerMaster Obj = new ClsCustomerMaster(getActivity());
                        Obj.setmId(clsCustomerMaster.getmId());
                        int result = ClsCustomerMaster.Delete(Obj, getActivity());

                        if (result == 1) {
                            Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            customerAdapter.RemoveItem(position);
                        } else {
                            Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                        dialog1.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            });

        });

    }


    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        new CustomerReportAsyncTask(_whereSearch, txt_nodata,
                getActivity(), customerAdapter, progress_bar, rv).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_customer_master, menu);
        MenuItem item = menu.findItem(R.id.search);

        MenuItem menu_excel = menu.findItem(R.id.itm_excel);
        menu_excel.setVisible(true);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                _whereSearch = "";
                new CustomerReportAsyncTask(_whereSearch, txt_nodata,
                        getActivity(), customerAdapter, progress_bar, rv).execute();
                rv.setVisibility(View.VISIBLE);
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _whereSearch = "";

                if (query != null && !query.isEmpty()) {

                    _whereSearch = " AND [NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [MOBILE_NO] LIKE '%".concat(query).concat("%'"));

                    new CustomerReportAsyncTask(_whereSearch, txt_nodata,
                            getActivity(), customerAdapter, progress_bar, rv).execute();

                } else {

                    new CustomerReportAsyncTask(_whereSearch, txt_nodata,
                            getActivity(), customerAdapter, progress_bar, rv).execute();

                    rv.setVisibility(View.VISIBLE);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        searchView.setQueryHint("Search by name or mobile");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            _whereSearch = "";

            searchView.setQuery("", false);
            searchView.clearFocus();

            new CustomerReportAsyncTask(_whereSearch, txt_nodata,
                    getActivity(), customerAdapter, progress_bar, rv).execute();

            rv.setVisibility(View.VISIBLE);

        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    ProgressDialog loading;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            case R.id.itm_excel:
                exportToExcel();
                return true;
            case R.id.itm_pdf:
                exportToPdf();
                return true;
            case R.id.itm_import_customer:
                Intent intent = new Intent(getActivity(), Activity_ImportCustomer.class);
                startActivity(intent);
                return true;
            case R.id.itm_import_contact:
                importPhoneBookContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void exportToExcel() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> asyncTask =
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {

                        List<String> lstCustomerReportExcel = new ArrayList<>();

                        lstCustomerReportExcel.add("MOBILE NO.");
                        lstCustomerReportExcel.add("NAME");
                        lstCustomerReportExcel.add("COMPANY NAME");
                        lstCustomerReportExcel.add("GST NO.");
                        lstCustomerReportExcel.add("ADDRESS");
                        lstCustomerReportExcel.add("EMAIL");
                        lstCustomerReportExcel.add("CREDIT LIMIT");
                        lstCustomerReportExcel.add("OPENING BALANCE");
                        lstCustomerReportExcel.add("BALANCE TYPE");
                        lstCustomerReportExcel.add("NOTE");

                        return ClsExportToExcel.createExcelSheet("CustomerReportPath", lstCustomerReportExcel,
                                "Customer Report", new ClsCustomerMaster().getListCustomer("", getActivity()),
                                null, null);

                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ClsGlobal._prProgressDialog(getActivity()
                                , "Please Wait...", false);
                        loading.show();
                    }

                    @Override
                    protected void onPostExecute(String aVoid) {
                        super.onPostExecute(aVoid);

                        if (loading.isShowing()) {
                            loading.dismiss();
                        }

                        if (aVoid.equalsIgnoreCase("No Record Found")) {
                            Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Export to excel successfully",
                                    Toast.LENGTH_SHORT).show();

                            ClsGlobal.openSnackBarExcelFile(Objects.requireNonNull(getActivity()), aVoid, "Customer Report");
                        }
                    }
                }.execute();

    }

    void exportToPdf() {
//        https://stackoverflow.com/questions/8586691/how-to-open-file-save-dialog-in-android

    }

    void importPhoneBookContact() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission();
            }
        }

        new ExcelDataInsertPhoneBookAsyncTask(getActivity(),
                progress_bar).execute();

        viewCustomerList();

        new CustomerReportAsyncTask(_whereSearch, txt_nodata,
                getActivity(), customerAdapter, progress_bar, rv).execute();
    }


    private static final int PERMISSION_REQUEST_CODE = 1;

    public boolean checkPermission() {

        int ContactPermissionResult = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS);

        return ContactPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE
                }, PERMISSION_REQUEST_CODE);

    }
}
