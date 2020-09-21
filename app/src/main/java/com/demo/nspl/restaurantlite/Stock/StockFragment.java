package com.demo.nspl.restaurantlite.Stock;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.ExportData.ClsExportStockReportData;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsClsStockList;
import com.demo.nspl.restaurantlite.classes.ClsExportToExcel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StockFragment extends Fragment {

    RecyclerView rv;
    TextView txt_nodata;
    LinearLayout progress_bar_layout;
    List<ClsStock> lstClsStocks;
    List<ClsStock> lstClsStocksOut;
    String _whereSearch = "";
    StockAdapter stockAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Stock Reports");

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "StockFragment"));
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        // Inflate the layout for this fragment

        Log.e("onCreateView", "onCreateView call");
        ClsGlobal.isFristFragment = true;
        main(view);

        return view;
    }


    private void main(View v) {
        setHasOptionsMenu(true);
        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        txt_nodata = v.findViewById(R.id.txt_nodata);
//        progress_bar_layout = v.findViewById(R.id.progress_bar_layout);

        viewStockList();
    }

    private void viewStockList() {

        stockAdapter = new StockAdapter(getActivity());
        stockAdapter.SetOnClickListener(new OnStockClick() {
            @Override
            public void OnClick(ClsStock clsStock, int position) {

                Intent intent = new Intent(getActivity(), StockDetailActivity.class);
                intent.putExtra("itemCode", clsStock.getITEM_CODE());
                intent.putExtra("itemName", clsStock.getITEM_NAME().toUpperCase());
                startActivity(intent);

            }
        });
        rv.setAdapter(stockAdapter);

        new LoadAsyncTask(getActivity()).execute();

//        stockAdapter.notifyDataSetChanged();


    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();

//        viewStockList("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
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
                lstClsStocks.clear();
                lstClsStocksOut.clear();
                viewStockList();
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
                lstClsStocks.clear();
                lstClsStocksOut.clear();

                if (query != null && !query.isEmpty()) {

                    _whereSearch = " AND [ITM].[ITEM_NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [ITM].[ITEM_CODE] LIKE '%".concat(query).concat("%'"));
                    Log.e("--Stock--", "_whereSearch- " + _whereSearch);
                    Log.e("--Stock--", "IN_whereSearch- " + lstClsStocks.size());

                    new LoadAsyncTask(getActivity()).execute();
//                    viewStockList(_whereSearch);
                } else {
                    viewStockList();
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

        searchView.setQueryHint("Search by name or code");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            _whereSearch = "";
            lstClsStocks.clear();
            lstClsStocksOut.clear();

            searchView.setQuery("", false);
            searchView.clearFocus();
            viewStockList();
            rv.setVisibility(View.VISIBLE);

        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    ProgressDialog loading;
    boolean _checkIfNull = false;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            case R.id.itm_excel:

                @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> asyncTask =
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {

                                return exportToExcel();
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
                                Log.d("--customerReport--", "aVoid: " + aVoid);

                                if (loading.isShowing()) {
                                    loading.dismiss();
                                }

                                if (_checkIfNull) {

                                    Toast.makeText(getActivity(), "Export to excel successfully",
                                            Toast.LENGTH_SHORT).show();

                                    ClsGlobal.openSnackBarExcelFile(Objects.requireNonNull(getActivity()), aVoid, "Stock Report");
                                } else {
                                    Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }.execute();

                return true;

            case R.id.itm_pdf:
//                exportToPdf();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<ClsExportStockReportData> lstClsExportGetSets = new ArrayList<>();

    private String exportToExcel() {
        List<String> lstStrings = new ArrayList<>();

//        lstClsExportGetSets = ClsExportStockReportData.getStockList(getActivity());


        if (lstClsStocks != null && lstClsStocks.size() != 0) {
            _checkIfNull = true;

            lstStrings.add("SR#");
            lstStrings.add("ITEM");
            lstStrings.add("UNIT");
            lstStrings.add("MIN");
            lstStrings.add("MAX");
            lstStrings.add("AVG.PURCHASE RATE");
            lstStrings.add("AVG.SALE RATE");
            lstStrings.add("IN");
            lstStrings.add("OUT");
            lstStrings.add("STOCK");
            lstStrings.add("AVG.STOCK VALUE");

        } else {
            _checkIfNull = false;
        }

        return ClsExportToExcel.createExcelSheet("StockReportPath", lstStrings,
                "Stock Report", lstClsStocks, null, null);
    }


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, ClsClsStockList> {


        Context context;
        ProgressDialog pd;

        LoadAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
//            progress_bar_layout.setVisibility(View.VISIBLE);
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Loading..");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

        }


        @Override
        protected ClsClsStockList doInBackground(Void... voids) {
            lstClsStocks = new ArrayList<>();

            lstClsStocks = new ClsStock(getActivity()).getStockList(_whereSearch, getActivity());

            Gson gson1 = new Gson();
            String jsonInString1 = gson1.toJson(lstClsStocks);
            Log.e("--ViewStock--", "First:--- " + jsonInString1);
            Log.e("--ViewStock--", "First- " + lstClsStocks.size());

            lstClsStocksOut = new ArrayList<>();
            lstClsStocksOut = new ClsStock(getActivity()).getStockOutList("", getActivity());

            ClsClsStockList clsClsStockList = new ClsClsStockList();
            clsClsStockList.setLstClsStocks(lstClsStocks);
            clsClsStockList.setLstClsStocksOut(lstClsStocksOut);

            Gson gson2 = new Gson();
            String jsonInString2 = gson2.toJson(clsClsStockList);
            Log.e("--ViewStock--", "First:--- " + jsonInString2);

            return clsClsStockList;
        }


        @Override
        protected void onPostExecute(ClsClsStockList clsClsStockList) {
            super.onPostExecute(clsClsStockList);

            pd.cancel();
            lstClsStocks = clsClsStockList.getLstClsStocks();
            lstClsStocksOut = clsClsStockList.getLstClsStocksOut();

            if (lstClsStocks != null && lstClsStocks.size() != 0) {
                txt_nodata.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

                if (lstClsStocksOut != null && lstClsStocksOut.size() != 0) {
                    for (ClsStock obj : lstClsStocks) {

                        for (ClsStock objClsStockOut : lstClsStocksOut) {

                            if (obj.getITEM_CODE().equalsIgnoreCase(objClsStockOut.getITEM_CODE())) {
                                obj.setOUT(objClsStockOut.getOUT());
                                obj.setAverageSaleRate(objClsStockOut.getAverageSaleRate());
                                lstClsStocks.set(lstClsStocks.indexOf(obj), obj);
                                break;
                            }

                        }
                    }

                    Gson gson2 = new Gson();
                    String jsonInString2 = gson2.toJson(lstClsStocks);
                    Log.e("--ViewStock--", "Final:--- " + jsonInString2);
                }

                stockAdapter.AddItems(lstClsStocks);

            } else {
                txt_nodata.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);

            }


        }
    }


}
