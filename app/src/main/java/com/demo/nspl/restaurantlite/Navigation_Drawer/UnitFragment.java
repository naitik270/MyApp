package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Adapter.UnitAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.activity.AddUnitActivity;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnitFragment extends Fragment {
    private List<ClsUnit> list_Units;
    private UnitAdapter cu;
    private RecyclerView rv;
    private TextView empty_title_text;
    String _whereSearch= "";

    public UnitFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClsGlobal.isFristFragment = true;
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "UnitFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Units");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unit, container, false);
        ClsGlobal.isFristFragment = true;
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddUnitActivity.class);
            startActivity(intent);
        });
        setHasOptionsMenu(true);
        rv = v.findViewById(R.id.rv);
        empty_title_text = v.findViewById(R.id.empty_title_text);
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

        list_Units = new ArrayList<>();
        list_Units = new ClsUnit(getActivity()).getList(where);

        if (list_Units.size() == 0){
            empty_title_text.setVisibility(View.VISIBLE);
        }else {
            empty_title_text.setVisibility(View.INVISIBLE);
        }
        cu = new UnitAdapter((AppCompatActivity) getActivity(),getActivity(), (ArrayList<ClsUnit>) list_Units);
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

        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                ViewData("");
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("onQueryTextSubmit","onQueryTextSubmit call");

                if (!query.equals("")){

                    _whereSearch = " AND ([UNIT_NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [ACTIVE] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR [SORT_NO] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR [REMARK] LIKE '%".concat(query).concat("%')"));

                    Log.e("query",_whereSearch);
                    ViewData(_whereSearch);
                }else {
                    ViewData("");
                }

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

            ViewData("");
        });



        super.onCreateOptionsMenu(menu, inflater);
    }


}
