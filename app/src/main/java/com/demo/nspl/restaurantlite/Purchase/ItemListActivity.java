package com.demo.nspl.restaurantlite.Purchase;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.demo.nspl.restaurantlite.Adapter.PurchaseItemListAdapter;
import com.demo.nspl.restaurantlite.Interface.OnClickSalesItem;
import com.demo.nspl.restaurantlite.Navigation_Drawer.FilterDialogFragment;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    TextView empty_title_text;
    List<ClsLayerItemMaster> list;
    // StickyListHeadersListView rv;
    String _where = "";
    PurchaseItemListAdapter mCu;
    ListView list_view;
    String _whereSearch = "";
    public static final int DIALOG_QUEST_CODE = 300;
    //ItemListAdapter mCv;
    Toolbar toolbar;
    int _ID;
    String date = "", vendor_name = "", bill_no = "", remark = "", billNo = "";
    int VendorId = 0;
    String po_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main();
    }

    private void main() {

        Intent intent = getIntent();

        _ID = intent.getIntExtra("_ID", 0);
        VendorId = intent.getIntExtra("VendorId", 0);
        date = intent.getStringExtra("date");
        vendor_name = intent.getStringExtra("vendor_name");
        bill_no = intent.getStringExtra("bill_no");
        po_no = intent.getStringExtra("po_no");
        remark = intent.getStringExtra("remark");
        billNo = intent.getStringExtra("bill_no");


        // rv = findViewById(R.id.rv);
        empty_title_text = findViewById(R.id.empty_title_text);
        list_view = findViewById(R.id.list_view);

        ViewData();

    }


    private void ViewData() {
        list = new ArrayList<>();

        list = ClsLayerItemMaster.getLayerItemList(getApplication(), _where, _whereSearch);

        if (list.size() == 0) {

            empty_title_text.setVisibility(View.VISIBLE);
            list_view.setVisibility(View.GONE);
        } else {
            empty_title_text.setVisibility(View.INVISIBLE);
            list_view.setVisibility(View.VISIBLE);
        }

        Gson gsonOut = new Gson();
        String jsonInString2gsonOut = gsonOut.toJson(list);
        Log.e("--GSON--", "list:-- " + jsonInString2gsonOut);


        list = sortAndAddSections(list);
        mCu = new PurchaseItemListAdapter(this, list);


        list_view.setAdapter(mCu);

        mCu.SetOnClicklistener(new OnClickSalesItem() {
            @Override
            public void OnClick(ClsLayerItemMaster clsLayerItemMaster) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FullScreenDailog newFragment = new FullScreenDailog();
//            newFragment.setRequestCode(clsLayerItemMaster,_ID, date, billNo, VendorId, vendor_name, bill_no, po_no, remark);
                newFragment.setProductList(clsLayerItemMaster, "list");

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
                newFragment.setOnCallbackResult(new FilterDialogFragment.CallbackResult() {
                    @Override
                    public void sendResult(int requestCode) {
                        if (requestCode == DIALOG_QUEST_CODE) {
                            Log.e("requestCode", String.valueOf(requestCode));
                            list_view.setVisibility(View.VISIBLE);
                        }
                    }
                });

                list_view.setVisibility(View.INVISIBLE);

            }
        });

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
                ViewData();
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

                    _whereSearch = " AND ([ITEM_NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [ITEM_CODE] LIKE '%".concat(query).concat("%')"));

                    ViewData();
                }
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
            ViewData();
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
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        list_view.setVisibility(View.VISIBLE);
    }


    private List<ClsLayerItemMaster> sortAndAddSections(List<ClsLayerItemMaster> itemList) {

        List<ClsLayerItemMaster> tempList = new ArrayList<>();
        //First we sort the array

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for (int i = 0; i < itemList.size(); i++) {
            //If it is the start of a new section we create a new listcell and add it to our array
            if (!(header.equals(String.valueOf(itemList.get(i).getITEM_NAME().charAt(0)).toUpperCase()))) {
                ClsLayerItemMaster sectionCell = new ClsLayerItemMaster(String.valueOf(itemList.get(i).getITEM_NAME()
                        .charAt(0)).toUpperCase());
                sectionCell.setHeader(true);
                tempList.add(sectionCell);
                header = String.valueOf(itemList.get(i).getITEM_NAME().charAt(0)).toUpperCase();
                Log.e("header", header);
                Log.e("check", "inside if");
            }

            Log.e("check", "outside if");
            tempList.add(itemList.get(i));

        }

        return tempList;
    }


}
