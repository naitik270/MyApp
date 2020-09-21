package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.VendorAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddVendorActivity;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorFragment extends Fragment {

    private VendorAdapter vendorAdapter;
    RecyclerView rv;
    ProgressBar progress_bar;
    private TextView empty_title_text;
    String _whereSearch = "";

    public VendorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "VendorFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Vendors");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vendor, container, false);
        ClsGlobal.isFristFragment = true;

        rv = v.findViewById(R.id.rv);
        progress_bar = v.findViewById(R.id.progress_bar);
        empty_title_text = v.findViewById(R.id.empty_title_text);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setHasOptionsMenu(true);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddVendorActivity.class);
            startActivity(intent);
        });

        onVendorClickDialog();
        new LoadAsyncTask(_whereSearch).execute();

        return v;
    }

    void onVendorClickDialog() {

        vendorAdapter = new VendorAdapter(getActivity());
        rv.setAdapter(vendorAdapter);

        vendorAdapter.SetOnClickListener((clsVendor, position) -> {

            if (!clsVendor.getVendor_name().equalsIgnoreCase("OTHER")) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.dialog);
                dialog.setContentView(R.layout.dialog_delete_edit);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);

                LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
                LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);

                ll_update.setOnClickListener(v1 -> {

                    dialog.dismiss();
                    dialog.cancel();

                    Intent intent = new Intent(getActivity(), AddVendorActivity.class);
                    intent.putExtra("ID", clsVendor.getVendor_id());
                    startActivity(intent);

                });

                ll_delete.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    dialog.cancel();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Confirm...");
                    alertDialog.setMessage("Are you sure you want delete?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", (dialog1, which) -> {
                        ClsVendor obj = new ClsVendor(getActivity());
                        obj.setVendor_id(clsVendor.getVendor_id());
                        int result = ClsVendor.Delete(obj, getActivity());

                        if (result == 1) {
                            Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            vendorAdapter.RemoveItem(position);
                        } else {
                            Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
                        }

                    });
                    alertDialog.setNegativeButton("NO", (dialog1, which) -> {
                        dialog1.dismiss();
                        dialog1.cancel();
                    });
                    alertDialog.show();

                });

                dialog.show();
            } else {
                Toast.makeText(getActivity(), "This vendor is default.", Toast.LENGTH_SHORT).show();
            }


        });

    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadAsyncTask(_whereSearch).execute();

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
                new LoadAsyncTask(_whereSearch).execute();
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!query.equals("")) {
                    _whereSearch = " AND ([VENDOR_NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [CONTACT_NO] LIKE '%".concat(query).concat("%')"));
                    new LoadAsyncTask(_whereSearch).execute();
                } else {
                    new LoadAsyncTask(_whereSearch).execute();
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

            new LoadAsyncTask(_whereSearch).execute();
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsVendor>> {


        String where = "";


        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);
        }

        LoadAsyncTask(String where) {
            this.where = where;
        }


        @Override
        protected List<ClsVendor> doInBackground(Void... voids) {
            return new ClsVendor(getActivity()).getList(where);
        }


        @Override
        protected void onPostExecute(List<ClsVendor> list) {
            super.onPostExecute(list);

            progress_bar.setVisibility(View.GONE);


            if (list.size() == 0) {
                empty_title_text.setVisibility(View.VISIBLE);
            } else {
                empty_title_text.setVisibility(View.INVISIBLE);
            }

            vendorAdapter.AddItems(list);


        }
    }

}
