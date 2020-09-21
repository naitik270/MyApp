package com.demo.nspl.restaurantlite.Navigation_Drawer;


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

import com.demo.nspl.restaurantlite.Adapter.EmployeeAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddEmployeeActivity;
import com.demo.nspl.restaurantlite.activity.Document_ListActivity;
import com.demo.nspl.restaurantlite.classes.ClsEmployee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeFragment extends Fragment {

    private EmployeeAdapter employeeAdapter;
    RecyclerView rv;
    ProgressBar progress_bar;
    private TextView empty_title_text;
    String _whereSearch = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "EmployeeFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Employees");

    }


    public EmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employee, container, false);

        ClsGlobal.isFristFragment = true;
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
            startActivity(intent);
        });

        setHasOptionsMenu(true);

        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        progress_bar = v.findViewById(R.id.progress_bar);
        empty_title_text = v.findViewById(R.id.empty_title_text);

        onEmployeeAdpClick();
        new LoadAsyncTask(_whereSearch).execute();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadAsyncTask(_whereSearch).execute();
    }

    void onEmployeeAdpClick() {

        employeeAdapter = new EmployeeAdapter(getActivity());
        rv.setAdapter(employeeAdapter);

        employeeAdapter.SetOnClickListener((clsEmployee, position) -> {

            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_functionality);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);

            LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
            LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);
            LinearLayout ll_upload_doc = dialog.findViewById(R.id.ll_upload_doc);
            LinearLayout ll_customer_ledger = dialog.findViewById(R.id.ll_customer_ledger);
            View ll_view = dialog.findViewById(R.id.ll_view);
            ll_view.setVisibility(View.GONE);
            ll_customer_ledger.setVisibility(View.GONE);

            ll_upload_doc.setOnClickListener(v1 -> {
                dialog.dismiss();
                dialog.cancel();
                Intent intent = new Intent(getActivity(), Document_ListActivity.class);
                intent.putExtra("employee_id", clsEmployee.getEmployee_id());
                intent.putExtra("employee_name", clsEmployee.getEmployee_name());
                intent.putExtra("_flag", "employee");
                startActivity(intent);

            });

            ll_update.setOnClickListener(v1 -> {
                dialog.dismiss();
                dialog.cancel();

                Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
                intent.putExtra("ID", clsEmployee.getEmployee_id());
                Log.d("--Update--", "EmpID: " + clsEmployee.getEmployee_id());

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
                    ClsEmployee Obj = new ClsEmployee(getActivity());
                    Obj.setEmployee_id(clsEmployee.getEmployee_id());
                    int result = ClsEmployee.Delete(Obj, getActivity());

                    if (result == 1) {
                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        employeeAdapter.RemoveItem(position);
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
        });
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
                new LoadAsyncTask(_whereSearch).execute();
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                Log.e("onQueryTextSubmit", "onQueryTextSubmit call");

                if (!query.equals("")) {

                    _whereSearch = " AND ([EMPLOYEE_NAME] LIKE '%".concat(query).concat("%'");
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


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsEmployee>> {


        String where = "";


        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);
        }

        LoadAsyncTask(String where) {
            this.where = where;
        }


        @Override
        protected List<ClsEmployee> doInBackground(Void... voids) {
            return new ClsEmployee(getActivity()).getList(where);
        }


        @Override
        protected void onPostExecute(List<ClsEmployee> list) {
            super.onPostExecute(list);

            progress_bar.setVisibility(View.GONE);

            if (list.size() == 0) {
                empty_title_text.setVisibility(View.VISIBLE);
            } else {
                empty_title_text.setVisibility(View.INVISIBLE);
            }

            employeeAdapter.AddItems(list);


        }
    }

}
