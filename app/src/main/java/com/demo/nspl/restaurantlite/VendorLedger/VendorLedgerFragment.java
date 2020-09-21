package com.demo.nspl.restaurantlite.VendorLedger;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.UpdateCalling.ClsVendorLedgerUpdate;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class VendorLedgerFragment extends Fragment {


    RecyclerView rv;
    TextView txt_nodata;
    LinearLayout progress_bar_layout;
    List<ClsVendorLedger> lstClsVendorLedgers = new ArrayList<>();
    String _whereSearch = "";
    List<ClsVendor> list_vendors;
    List<ClsPaymentMaster> lstVendorPayment = new ArrayList<>();
    VendorLedgerAdapter vendorLedgerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "VendorLedgerFragment"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vendor_ledger_fragment, container, false);

        ClsGlobal.isFristFragment = true;

        main(v);

        return v;
    }

    private void main(View v) {
        setHasOptionsMenu(true);
        rv = v.findViewById(R.id.rv);
        progress_bar_layout = v.findViewById(R.id.progress_bar_layout);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        txt_nodata = v.findViewById(R.id.txt_nodata);


        ClsVendorLedgerUpdate objUpdate = ClsGlobal.getUpdateVendorLedger(getActivity());


        if (objUpdate.getUpdateVendorLedger() == null ||
                objUpdate.getUpdateVendorLedger().equalsIgnoreCase("")) {

            objUpdate.setUpdateVendorLedger("FirstTime");

            ClsGlobal.setUpdateVendorLedger(objUpdate, getActivity());
        }

        if (objUpdate.getUpdateVendorLedger().equalsIgnoreCase("FirstTime")) {
            updatePayment("");
        }

        viewVendorLedgerList("");
    }


    private void viewVendorLedgerList(String _whereSearch) {


        vendorLedgerAdapter = new VendorLedgerAdapter(getActivity());

        vendorLedgerAdapter.SetOnClickListener((clsVendorLedger, position) -> {

            Intent intent = new Intent(getActivity(), VendorDetailActivity.class);
            intent.putExtra("vendorId", clsVendorLedger.getVENDOR_ID());
            intent.putExtra("bill", clsVendorLedger.getVENDOR_ID());
            intent.putExtra("vendorName", clsVendorLedger.getVENDOR_NAME());
            startActivity(intent);

        });
        rv.setAdapter(vendorLedgerAdapter);

        new LoadAsyncTask("").execute();


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Vendor Ledger");
    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();
//        viewVendorLedgerList("");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                lstClsVendorLedgers.clear();
                lstVendorPayment.clear();
                _whereSearch = "";
                viewVendorLedgerList("");
                rv.setVisibility(View.VISIBLE);
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("--searchManager--", "onQueryTextSubmit: " + query);
                lstClsVendorLedgers.clear();
                lstVendorPayment.clear();
                _whereSearch = "";

                if (query != null && !query.isEmpty()) {

                    _whereSearch = " AND V.[VENDOR_NAME] LIKE '%".concat(query).concat("%'");
//                    viewVendorLedgerList(_whereSearch);

                    new LoadAsyncTask("Search").execute();

                } else {
//                    viewVendorLedgerList("");
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

        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            lstClsVendorLedgers.clear();
            lstVendorPayment.clear();
            _whereSearch = "";
            viewVendorLedgerList("");
            rv.setVisibility(View.VISIBLE);

        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePayment(String where) {
        list_vendors = new ArrayList<>();
        list_vendors = new ClsVendor(getActivity()).getList(where);


        for (ClsVendor objClsVendor : list_vendors) {
            ClsPaymentMaster.deleteOpeningBalanceVendor(String.valueOf(objClsVendor.getVendor_id()),
                    getActivity());


            ClsPaymentMaster insertPaymentMaster = new ClsPaymentMaster();
            insertPaymentMaster.setPaymentDate(ClsGlobal.getEntryDate_dd_MM_yyyy());
            insertPaymentMaster.setPaymentMounth(ClsGlobal.getDayMonth(ClsGlobal.getEntryDate_dd_MM_yyyy()));
            insertPaymentMaster.setVendorID(objClsVendor.getVendor_id());
            insertPaymentMaster.setMobileNo(objClsVendor.getContact_no());
            insertPaymentMaster.setCustomerName("");
            insertPaymentMaster.setVendorName(objClsVendor.getVendor_name());
            insertPaymentMaster.setPaymentMode("Opening Balance");
            insertPaymentMaster.setPaymentDetail("Opening Balance");
            insertPaymentMaster.setInvoiceNo("0");

            double openingBal = objClsVendor.getOpeningStock();

            if (openingBal != 0.0) {
                objClsVendor.setOpeningStock(Double.parseDouble(ClsGlobal.round(openingBal, 2)));

                insertPaymentMaster.setAmount(Double.parseDouble(ClsGlobal.round(openingBal, 2)));

            } else {
                insertPaymentMaster.setAmount(0.00);
            }


            insertPaymentMaster.setRemark("Vendor opening balance");
            insertPaymentMaster.setEntryDate(ClsGlobal.getEntryDate());
            insertPaymentMaster.setType("Vendor");
            insertPaymentMaster.setReceiptNo("0");
//            insertPaymentMaster.setOrderId(0);

            ClsPaymentMaster.InsertPaymentVendor(insertPaymentMaster, getActivity());


        }
        ClsVendorLedgerUpdate objUpdate = new ClsVendorLedgerUpdate();
        objUpdate.setUpdateVendorLedger("SecondTime");

        ClsGlobal.setUpdateVendorLedger(objUpdate, getActivity());


    }


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsVendorLedger>> {

        //        int currentPageNo;
        String mode = "";

        @Override
        protected void onPreExecute() {
            progress_bar_layout.setVisibility(View.VISIBLE);
//            progress_bar.setVisibility(View.VISIBLE);
        }

        LoadAsyncTask(String mode) {
            this.mode = mode;
        }


        @Override
        protected List<ClsVendorLedger> doInBackground(Void... voids) {

            if (!mode.equalsIgnoreCase("")) {
                lstClsVendorLedgers.clear();
            }


            lstClsVendorLedgers = new ArrayList<>();
            lstClsVendorLedgers = new ClsPurchaseMaster(getActivity()).getVendorLedgerList(_whereSearch, getActivity());

            Log.e("--Purchase--", "_whereSearch- " + lstClsVendorLedgers.size());

            lstVendorPayment = new ClsPaymentMaster().getVendorPayment("", getActivity());


            Gson gson = new Gson();
            String jsonInString = gson.toJson(lstClsVendorLedgers);
            Log.e("--VendorLedgerList--", "lstClsVendorLedgers: " + jsonInString);

            Gson gson1 = new Gson();
            String jsonInString1 = gson1.toJson(lstVendorPayment);
            Log.e("--VendorLedgerList--", "lstVendorPayment: " + jsonInString1);


            if (lstClsVendorLedgers != null && lstClsVendorLedgers.size() != 0) {
//                txt_nodata.setVisibility(View.GONE);
//                rv.setVisibility(View.VISIBLE);

                if (lstVendorPayment != null && lstVendorPayment.size() != 0) {
                    for (ClsVendorLedger obj : lstClsVendorLedgers) {

                        for (ClsPaymentMaster objVendor : lstVendorPayment) {

                            if (obj.getVENDOR_ID() == objVendor.getVendorID()) {
                                obj.setTotalPaymentAmount(objVendor.getAmount());
                                lstClsVendorLedgers.set(lstClsVendorLedgers.indexOf(obj), obj);
                                break;
                            }

                        }
                    }
                }
            }


            return lstClsVendorLedgers;
        }


        @Override
        protected void onPostExecute(List<ClsVendorLedger> list) {
            super.onPostExecute(list);
//            Log.e("list_Item", "list_Item call " + list.size());
            progress_bar_layout.setVisibility(View.GONE);

            if (list.size() > 0) {
                txt_nodata.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                vendorLedgerAdapter.AddItems(list);

            } else {
                txt_nodata.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }


        }
    }


}
