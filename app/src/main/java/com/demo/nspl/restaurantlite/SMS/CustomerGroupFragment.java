package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class CustomerGroupFragment extends Fragment {

    RecyclerView rv;
    TextView txt_nodata;
    FloatingActionButton fab;
    DisplayGroupAdapter displayGroupAdapter;
    ProgressBar progress_bar;
    String _whereSearch = "";


    public CustomerGroupFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Customer Group");

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "CustomerGroupFragment"));
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
        fab.setAlpha(0.50f);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);

        displayGroupAdapter = new DisplayGroupAdapter(getActivity());
        rv.setAdapter(displayGroupAdapter);


        displayGroupAdapter.SetOnClickListener((clsSmsCustomerGroup, position) -> {
            EditDeleteDialog(clsSmsCustomerGroup, position);
        });

        new LoadAsyncTask("").execute();

        fab.setOnClickListener(v1 -> {

            Intent intent = new Intent(getActivity(), AddCustomerGroupActivityNew.class);
            intent.putExtra("_ID", 0);
            startActivity(intent);

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        new LoadAsyncTask("").execute();
    }


    void EditDeleteDialog(ClsSmsCustomerGroup clsSmsCustomerGroup, int position) {
        Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_edit);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
        LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);

        ll_update.setOnClickListener(v -> {

            dialog.dismiss();
            dialog.cancel();


            Log.d("--Group--", "GroupID: " + clsSmsCustomerGroup.getGroupId());


            Intent intent = new Intent(getActivity(), AddCustomerGroupActivityNew.class);
            intent.putExtra("_ID", clsSmsCustomerGroup.getGroupId());
            startActivity(intent);
        });

        ll_delete.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("CONFIRM DELETE...");
            alertDialog.setMessage("Sure to delete group ?");

            alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
            alertDialog.setPositiveButton("YES", (dialog1, which) -> {

                int currentObj = clsSmsCustomerGroup.getGroupId();
                Log.e("--getId--", "getGroupId: " + currentObj);

                ClsSmsCustomerGroup DeleteCurrent = new ClsSmsCustomerGroup();
                DeleteCurrent.setGroupId(currentObj);

                int getResult = ClsSmsCustomerGroup.DeleteGroupAndCustomersByGroupId(DeleteCurrent, getContext());

                if (getResult > 0) {
                    Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    displayGroupAdapter.remove(position);
                } else {
                    Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
                }

            });
            alertDialog.setNegativeButton("NO", (dialog12, which) -> {
                dialog12.dismiss();
                dialog12.cancel();
            });
            alertDialog.show();
        });


        dialog.show();


    }

    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsSmsCustomerGroup>> {


        String where = "";


        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);

        }


        LoadAsyncTask(String where) {
            this.where = where;
        }

        @Override
        protected List<ClsSmsCustomerGroup> doInBackground(Void... voids) {
            return new ClsSmsCustomerGroup(getActivity()).getGroupList(getActivity(), where);
        }


        @Override
        protected void onPostExecute(List<ClsSmsCustomerGroup> lst) {
            super.onPostExecute(lst);
            progress_bar.setVisibility(View.GONE);

            displayGroupAdapter.AddItems(lst);

            if (lst.size() == 0) {
                txt_nodata.setVisibility(View.VISIBLE);
            } else {
                txt_nodata.setVisibility(View.INVISIBLE);
            }

        }
    }

    void openCustomerListDialog() {

    }

    @SuppressLint("StaticFieldLeak")
    class LoadCustomerListAsyncTask extends AsyncTask<Void, Void, List<ClsSmsCustomerGroup>> {


        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);

        }


        @Override
        protected List<ClsSmsCustomerGroup> doInBackground(Void... voids) {
            return new ClsSmsCustomerGroup(getActivity()).getCustomerListByID(getActivity(), "");
        }


        @Override
        protected void onPostExecute(List<ClsSmsCustomerGroup> lst) {
            super.onPostExecute(lst);
            progress_bar.setVisibility(View.GONE);

            if (lst.size() == 0) {
                txt_nodata.setVisibility(View.VISIBLE);
            } else {
                txt_nodata.setVisibility(View.INVISIBLE);
            }

            displayGroupAdapter.AddItems(lst);

        }
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
                new LoadAsyncTask("").execute();
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if (!query.equals("")) {

                    _whereSearch = " AND (SCG.[GroupName] LIKE '%".concat(query).concat("%')");
                    new LoadAsyncTask(_whereSearch).execute();
                } else {
                    new LoadAsyncTask("").execute();
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

            new LoadAsyncTask("").execute();
        });


        super.onCreateOptionsMenu(menu, inflater);
    }


}
