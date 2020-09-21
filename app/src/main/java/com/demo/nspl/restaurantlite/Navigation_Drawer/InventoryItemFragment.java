package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.Adapter.InventoryItemAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import com.demo.nspl.restaurantlite.activity.AddInventoryItemActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryItemFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private List<ClsInventoryItem> list_inventory_items;
    private InventoryItemAdapter cu;
    RecyclerView rv;
    private TextView empty_title_text;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "InventoryItemFragment"));
        }
    }

    public InventoryItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Inventory Items");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory_item, container, false);
        ClsGlobal.isFristFragment = true;
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddInventoryItemActivity.class);
            startActivity(intent);
        });

        rv = v.findViewById(R.id.rv);
        empty_title_text = v.findViewById(R.id.empty_title_text);
        empty_title_text.setVisibility(View.INVISIBLE);
        setHasOptionsMenu(true);
        ViewData("");

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData("");

    }

    private void ViewData(String where) {

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        list_inventory_items = new ArrayList<>();
        list_inventory_items = new ClsInventoryItem(getActivity()).getList(where);

        if (list_inventory_items.size() == 0){
            empty_title_text.setVisibility(View.VISIBLE);
        }else {
            empty_title_text.setVisibility(View.INVISIBLE);
        }

        cu = new InventoryItemAdapter((AppCompatActivity) getActivity(),getActivity(), (ArrayList<ClsInventoryItem>) list_inventory_items);
        rv.setAdapter(cu);
        cu.notifyDataSetChanged();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.meun_inventory_item, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");


        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String where = "";
        if (newText != "") {
            where = " AND [INVENTORY_ITEM_NAME] like " + "'%" + newText + "%'"
                    + "OR" + "[UNIT_NAME] like"+ "'%" + newText + "%'"
                    + "OR" + "[ACTIVE] like"+ "'%" + newText + "%'" ;
        }
        ViewData(where);

        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }
}
