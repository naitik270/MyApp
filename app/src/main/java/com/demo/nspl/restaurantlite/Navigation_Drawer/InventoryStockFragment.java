package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Adapter.InventoryStockAdater;
import com.demo.nspl.restaurantlite.Adapter.RecentStockEntriesAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddExpenseActivity;
import com.demo.nspl.restaurantlite.activity.AddInventoryStockActivity;
import com.demo.nspl.restaurantlite.activity.MonthwiseStockActivity;
import com.demo.nspl.restaurantlite.activity.VendorBillDetailsActivity;
import com.demo.nspl.restaurantlite.classes.ClsDateTime;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsInventoryItem;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryStockFragment extends Fragment implements View.OnClickListener {


    private InventoryStockAdater cu;
    RecentStockEntriesAdapter cu_recent;
    RecyclerView rv, rv_recent;
    TextView current_month, previous_month;

    private Boolean isFabOpen = false;

    private List<ClsInventoryStock> list_inventorystock;
    private List<ClsInventoryStock> list_recent_stock;
    private List<ClsInventoryItem> items;
    FloatingActionButton fab_default, fab_in, fab_out;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    ClsInventoryItem clsInventoryItem = new ClsInventoryItem();
    private Dialog dialogSearch;
    private List<ClsInventoryStock> listItemSearch;
    int mYear, mMonth, mDay;
    LinearLayout nodata_layout, linear;


    public InventoryStockFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "InventoryStockFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("DashBoard");
    }

    //https://www.simplifiedcoding.net/search-functionality-recyclerview/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inventory_stock, container, false);
        ClsGlobal.isFristFragment = true;
        setHasOptionsMenu(true);
        fab_default =  v.findViewById(R.id.fab_default);
        fab_in =  v.findViewById(R.id.fab_in);
        fab_out =  v.findViewById(R.id.fab_out);



        previous_month = v.findViewById(R.id.previous_month);
        current_month = v.findViewById(R.id.current_month);
        nodata_layout = v.findViewById(R.id.nodata_layout);
        linear = v.findViewById(R.id.linear);
        rv = v.findViewById(R.id.rv_stock_list);

        ClsDateTime obj = new ClsDateTime();
        obj = ClsGlobal.getMonth(0);
        current_month.setText(obj.getMonthName().toUpperCase());
        current_month.setTag(obj);

        obj = ClsGlobal.getMonth(-1);
        previous_month.setText(obj.getMonthName().toUpperCase());
        previous_month.setTag(obj);


        current_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClsDateTime _ObjDtTime = (ClsDateTime) current_month.getTag();
                String _whereCondition = "".concat(" AND tblstock.[TRANSACTION_DATE] between "
                        .concat("('".concat(ClsGlobal.getDbDateFormat(_ObjDtTime.getFirstDate())).concat("')"))
                        .concat(" AND ")
                        .concat("('".concat(ClsGlobal.getDbDateFormat(_ObjDtTime.getLastDate())).concat("')")));
                Intent intent = new Intent(getActivity(), MonthwiseStockActivity.class);
                intent.putExtra("whereCondition", _whereCondition);
                intent.putExtra("Month", _ObjDtTime.getMonthName());
                intent.putExtra("Year", String.valueOf(_ObjDtTime.getShortYear()));
                Log.e("ValueIntent", "year" + _ObjDtTime.getShortYear());

                startActivity(intent);
            }
        });

        previous_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClsDateTime _ObjDtTime = (ClsDateTime) previous_month.getTag();
                String _whereCondition = "".concat(" AND tblstock.[TRANSACTION_DATE] between "
                        .concat("('".concat(ClsGlobal.getDbDateFormat(_ObjDtTime.getFirstDate())).concat("')"))
                        .concat(" AND ")
                        .concat("('".concat(ClsGlobal.getDbDateFormat(_ObjDtTime.getLastDate())).concat("') ")));
                Intent intent = new Intent(getActivity(), MonthwiseStockActivity.class);
                intent.putExtra("whereCondition", _whereCondition);
                intent.putExtra("Month", _ObjDtTime.getMonthName());
                intent.putExtra("Year", String.valueOf(_ObjDtTime.getShortYear()));
                startActivity(intent);
            }
        });

        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
        fab_default.setOnClickListener(this);
        fab_in.setOnClickListener(this);
        fab_out.setOnClickListener(this);


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        rv_recent = v.findViewById(R.id.rv_recent);


        rv_recent.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rv_recent.setLayoutManager(layoutManager1);
        rv_recent.setHasFixedSize(true);


        ViewData("");
        ViewRecentData("");


        fab_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Fab In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AddInventoryStockActivity.class);
                intent.putExtra("TYPE", "IN");
//                fab_in.startAnimation(fab_close);
//                fab_out.startAnimation(fab_close);

                startActivity(intent);

            }
        });

        fab_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Fab Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AddInventoryStockActivity.class);
                intent.putExtra("TYPE", "OUT");
//                fab_out.startAnimation(fab_close);
//                fab_in.startAnimation(fab_close);

                startActivity(intent);

            }
        });


        return v;


    }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            if (id == R.id.add_billdetails) {
                Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
                intent.putExtra("FLAG", "InventoryStock");
                startActivity(intent);
                return true;
            }

            if (id == R.id.view_details) {
                Intent intent = new Intent(getActivity(), VendorBillDetailsActivity.class);
                startActivity(intent);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData("");
        ViewRecentData("");

    }

    private void ViewData(String _w) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        list_inventorystock = new ArrayList<>();
        listItemSearch = list_inventorystock = new ClsInventoryStock(getActivity()).getStock("");

//        if (list_inventorystock.size()==0){
//            stock_title.setVisibility(View.VISIBLE);
//            empty_title_text.setVisibility(View.VISIBLE);
//        }else {
//            stock_title.setVisibility(View.INVISIBLE);
//            empty_title_text.setVisibility(View.INVISIBLE);
//        }
//

        cu = new InventoryStockAdater((AppCompatActivity) getActivity(), getActivity(), (ArrayList<ClsInventoryStock>) list_inventorystock);
        rv.setAdapter(cu);
        cu.notifyDataSetChanged();
    }



    private void ViewRecentData(String _w) {
        rv_recent.setLayoutManager(new LinearLayoutManager(getContext()));
        list_recent_stock = new ArrayList<>();
        list_recent_stock = new ClsInventoryStock(getActivity()).getList("", "dashboard");

//
//            if (list_recent_stock.size() == 0) {
//
//                linear.setVisibility(View.VISIBLE);
//                Log.e("ENtries","nodata" );
//
//            } else {
//
//                linear.setVisibility(View.GONE);
//                Log.e("ENtries","data" );
//
//            }


        if (list_recent_stock != null && list_recent_stock.size() != 0) {
            linear.setVisibility(View.VISIBLE);
            cu_recent = new RecentStockEntriesAdapter((AppCompatActivity) getActivity(), getActivity(), (ArrayList<ClsInventoryStock>) list_recent_stock);
            rv_recent.setAdapter(cu_recent);
            cu_recent.notifyDataSetChanged();
        } else {
            linear.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab_default:

                animateFAB();
                break;
            case R.id.fab_in:


                break;
            case R.id.fab_out:


                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab_default.startAnimation(rotate_backward);
            fab_in.startAnimation(fab_close);
            fab_out.startAnimation(fab_close);
            fab_in.setClickable(false);
            fab_out.setClickable(false);
            isFabOpen = false;


        } else {

            fab_default.startAnimation(rotate_forward);
            fab_in.startAnimation(fab_open);
            fab_out.startAnimation(fab_open);
            fab_in.setClickable(true);
            fab_out.setClickable(true);
            isFabOpen = true;


        }
    }
}
