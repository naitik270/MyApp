package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.SaleReportAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.RecentOrderActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class SalesReportsFragment extends Fragment {

    TextView empty_title_text;
    RecyclerView rv;
    List<ClsInventoryOrderMaster> list;
    SaleReportAdapter cu;


    public SalesReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Sale Report");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sales_reports, container, false);
        // Inflate the layout for this fragment
        rv = view.findViewById(R.id.rv);
        ClsGlobal.isFristFragment = true;
        empty_title_text = view.findViewById(R.id.empty_title_text);


        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        ViewData();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData();
    }

    private void ViewData() {
        list = new ArrayList<>();
        list = new ClsInventoryOrderMaster().getMonthWiseOrdersList("", getActivity());

        Gson gsonOut = new Gson();
        String jsonInString2gsonOut = gsonOut.toJson(list);
        Log.e("--SALE--", "GSON: " + jsonInString2gsonOut);


        if (list != null && list.size() != 0) {
            empty_title_text.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);

            cu = new SaleReportAdapter(getActivity(), list, "SalesReports");

            cu.SetOnClickSalesReports(clsInventoryOrderMaster -> {


                String _monthYear = ClsGlobal.getMonthName(clsInventoryOrderMaster.getMounth())
                        + " " + clsInventoryOrderMaster.getYear();


                Intent intent = new Intent(getActivity(), RecentOrderActivity.class);
                intent.putExtra("_month", clsInventoryOrderMaster.getMounth());

                intent.putExtra("Year", clsInventoryOrderMaster.getYear());
                intent.putExtra("title", clsInventoryOrderMaster.getYear());

                intent.putExtra("editSource", _monthYear);
                intent.putExtra("fragMode", "SalesReportsFragment");

                startActivity(intent);

            });

            rv.setAdapter(cu);

        } else {
            empty_title_text.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }


    }


    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


}
