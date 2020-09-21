package com.demo.nspl.restaurantlite.SMS;

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

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class SmsIdSettingListFragment extends Fragment {


    RecyclerView rv;
    ProgressBar progress_bar;

    TextView txt_nodata;
    FloatingActionButton fab;

    SmsIdSettingsListAdapter smsIdSettingsListAdapter;
    String _whereSearch = "";

    public SmsIdSettingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Sms Id Setting");

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "CustomerGroupFragment"));
        }

        ClsGlobal.isFristFragment = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sms_id_setting, container, false);
        ClsGlobal.isFristFragment = true;
        setHasOptionsMenu(true);
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
        rv.setLayoutManager(new LinearLayoutManager(getContext()));


        smsIdSettingsListAdapter = new SmsIdSettingsListAdapter(getActivity());
        rv.setAdapter(smsIdSettingsListAdapter);


        new LoadAsyncTask("").execute();

        smsIdSettingsListAdapter.SetOnClickListener((clsSmsIdSetting, position) -> {

            EditDeleteDialog(clsSmsIdSetting, position);
        });

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SmsIdSettingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadAsyncTask("").execute();
    }

    void EditDeleteDialog(ClsSmsIdSetting clsSmsIdSetting, int position) {
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

            Intent intent = new Intent(getActivity(), SmsIdSettingActivity.class);
            intent.putExtra("_ID", clsSmsIdSetting.getId());
            startActivity(intent);
        });


        ll_delete.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("CONFIRM DELETE...");
            alertDialog.setMessage("Delete Sms Id Setting?");

            alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
            alertDialog.setPositiveButton("YES", (dialog1, which) -> {

//                int currentObj = clsSmsIdSetting.getId();
//                Log.e("--getId--", "getGroupId: " + currentObj);
//
//                ClsSmsIdSetting clsSmsIdSetting1 = new ClsSmsIdSetting();
//                clsSmsIdSetting1.setGroupId(currentObj);

                int getResult = ClsSmsIdSetting.Delete(clsSmsIdSetting.getId());

                if (getResult > 0) {
                    Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    smsIdSettingsListAdapter.remove(position);
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

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsSmsIdSetting>> {

        String where = "";

        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);

        }

        LoadAsyncTask(String where) {
            this.where = where;
        }

        @Override
        protected List<ClsSmsIdSetting> doInBackground(Void... voids) {
            return new ClsSmsIdSetting(getActivity()).getList(getActivity(), where);
        }

        @Override
        protected void onPostExecute(List<ClsSmsIdSetting> lst) {
            super.onPostExecute(lst);
            progress_bar.setVisibility(View.GONE);
            smsIdSettingsListAdapter.AddItems(lst);

            if (lst.size() == 0) {
                txt_nodata.setVisibility(View.VISIBLE);
            } else {
                txt_nodata.setVisibility(View.INVISIBLE);
            }

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
                    _whereSearch = " AND ([sms_id] LIKE '%".concat(query).concat("%')");
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
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();

            new LoadAsyncTask("").execute();
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}
