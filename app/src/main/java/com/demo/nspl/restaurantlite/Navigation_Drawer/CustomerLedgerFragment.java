package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.CustomerLedgerAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.activity.CustomerDetailActivity;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.ArrayList;
import java.util.List;

public class CustomerLedgerFragment extends Fragment {

    TextView empty_title_text;
    List<ClsVendorLedger> list = new ArrayList<>();
    RecyclerView rv;
    CustomerLedgerAdapter cu;
    String _whereSearch = "";


    public CustomerLedgerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Customer Ledger");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ClsGlobal.isFristFragment = true;

        View view = inflater.inflate(R.layout.fragment_customer_ledger, container, false);
        rv = view.findViewById(R.id.rv);
        empty_title_text = view.findViewById(R.id.empty_title_text);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        setHasOptionsMenu(true);

        ViewData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    private void ViewData() {


        cu = new CustomerLedgerAdapter( getActivity());

        cu.SetOnClickListener(clsVendorLedger -> {
            Intent intent = new Intent(getActivity(), CustomerDetailActivity.class);
            intent.putExtra("clsVendorLedger", clsVendorLedger);
            startActivity(intent);
        });

        rv.setAdapter(cu);

        new LoadAsyncTask("",getActivity()).execute();
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
                _whereSearch = "";
                list.clear();
                ViewData();
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
                _whereSearch = "";
                list.clear();

                if (query != null && !query.isEmpty()) {

                    _whereSearch = " AND (CM.[NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR CM.[MOBILE_NO] LIKE '%".concat(query).concat("%')"));

                    new LoadAsyncTask("Search",getActivity()).execute();
                } else {
//                    ViewData();
                    rv.setVisibility(View.VISIBLE);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("--searchManager--", "onQueryTextChange: " + newText);
                return true;
            }
        });

        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            _whereSearch = "";
            list.clear();

            ViewData();
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


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsVendorLedger>> {


        String mode = "";
        ProgressDialog  pd;
        Context context;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Loading..");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();


        }

        LoadAsyncTask(String mode ,Context context) {
            this.mode = mode;
            this.context = context;
        }


        @Override
        protected List<ClsVendorLedger> doInBackground(Void... voids) {
            list = new ArrayList<>();

            if (!mode.equalsIgnoreCase("")) {

                list.clear();
            }

            list = ClsPurchaseMaster.getCustomerLedgerList(_whereSearch, getActivity());

            List<ClsPaymentMaster> lstCustomerPayments = new ClsPaymentMaster().getCustomerPayment(""
                    , getActivity());

            if (lstCustomerPayments != null && lstCustomerPayments.size() != 0) {
                for (ClsVendorLedger obj : list) {
                    for (ClsPaymentMaster objCust : lstCustomerPayments) {
                        if (obj.getCustomerMobileNo().equalsIgnoreCase(objCust.getMobileNo())) {
                            obj.setTotalPaymentAmount(objCust.getAmount());
                            list.set(list.indexOf(obj), obj);
                            break;
                        }
                    }
                }
            }


            return list;
        }


        @Override
        protected void onPostExecute(List<ClsVendorLedger> list) {
            super.onPostExecute(list);

            pd.cancel();
            cu.AddItems(list);

            if (list.size() == 0) {
                empty_title_text.setVisibility(View.VISIBLE);
            } else {
                empty_title_text.setVisibility(View.INVISIBLE);
            }


        }
    }

}
