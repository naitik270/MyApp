package com.demo.nspl.restaurantlite.TabsActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


public class Tab_4_Activity extends Fragment {

    private PieChart chart1;
    List<ClsExpenseMasterNew> lstClsExpenseMasterNew;

    BottomSheetDialog dialog;


    public Tab_4_Activity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler))
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "Tab_4_Activity"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_4, container, false);
        setHasOptionsMenu(true);
        main(v);

        return v;
    }

    private void main(View v) {
        chart1 = v.findViewById(R.id.chart1);
        ArrayList<Entry> entries = new ArrayList<>();

        ClsPaymentMaster objClsPaymentMaster = new ClsPaymentMaster(getActivity());
        double SaleAmount = ClsPaymentMaster.getTotalSaleAmount();
        entries.add(new Entry(Float.valueOf(String.valueOf(SaleAmount)), 0));

        double getIncome = ClsPaymentMaster.getTotalAmount(" AND [TYPE]='Customer' ");
        entries.add(new Entry(Float.valueOf(String.valueOf(getIncome)), 1));

        double getPaymentVendor = ClsPaymentMaster.getTotalAmount(" AND [TYPE]='Vendor' ");
        entries.add(new Entry(Float.valueOf(String.valueOf(getPaymentVendor)), 2));


        lstClsExpenseMasterNew = new ArrayList<>();
        lstClsExpenseMasterNew = new ClsExpenseMasterNew(getActivity()).getGrantTotalExp();
        double _GrandTotal = 0.0;

        for (ClsExpenseMasterNew obj : lstClsExpenseMasterNew) {
            _GrandTotal = Double.valueOf(String.valueOf(obj.getGRAND_TOTAL()));
        }
        entries.add(new Entry(Float.valueOf(String.valueOf(_GrandTotal)), 3));

        double TotalPurchaseVal = ClsInventoryOrderMaster.getTotalPurchaseAmt("", getActivity());
        entries.add(new Entry(Float.valueOf(String.valueOf(TotalPurchaseVal)), 4));


        double TotalAvgPurchaseVal = ClsInventoryOrderMaster.getPurchaseAvgValue("", getActivity());
        double _diff = SaleAmount - (_GrandTotal + TotalAvgPurchaseVal);

        entries.add(new Entry(Float.valueOf(String.valueOf(_diff)), 5));


        ArrayList year = new ArrayList();
        year.add("SALE");
        year.add("COLLECT AMT");
        year.add("PAID AMT");

        year.add("EXPENSE");
        year.add("PURCHASE");
        year.add("DIFFERENCE");

        PieDataSet set = new PieDataSet(entries, "");
/*

        set.setSliceSpace(2f);
        set.setValueTextSize(8f);
        set.setValueTextColor(Color.WHITE);
        set.setSelectionShift(10f);
*/


        set.setSliceSpace(2f);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.WHITE);
        set.setSelectionShift(10f);



        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.rgb(66, 133, 244));//blue
        colors.add(Color.rgb(248, 178, 80));//dark yellow
        colors.add(Color.rgb(132, 92, 240));//violet
        colors.add(Color.rgb(16, 211, 141));//Green
        colors.add(Color.rgb(238, 68, 130));//pink
        colors.add(Color.rgb(234, 67, 53));//red

        set.setColors(colors);

        PieData data = new PieData(year, set);
        Legend l = chart1.getLegend();
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(0f);

        chart1.setDescription("DIFFERENCE CALCULATION");
        chart1.setDescriptionTextSize(6f);
        chart1.setData(data);
        chart1.invalidate(); // refresh


        chart1.animateXY(1000, 1000);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_alert, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_alert_dialog) {
            bottomDialog();

        }
        return super.onOptionsItemSelected(item);
    }


    void bottomDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_exp_overview, null);
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();


    }




}
