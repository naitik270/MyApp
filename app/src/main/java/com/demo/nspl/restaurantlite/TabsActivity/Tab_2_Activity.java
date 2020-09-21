package com.demo.nspl.restaurantlite.TabsActivity;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.MyValueFormatter;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_2_Activity extends Fragment {

    List<ClsExpenseMasterNew> lstClsExpenseMasterNew;
    List<String> previousMonthValue = new ArrayList<String>();
    List<String> previousMonthText = new ArrayList<String>();
    HorizontalBarChart barChart;
    TextView txt_nodata;


    public Tab_2_Activity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "Tab_2_Activity"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_2_, container, false);
        txt_nodata = v.findViewById(R.id.txt_nodata);
        barChart = (HorizontalBarChart) v.findViewById(R.id.barchart);
        HorizontalBarChart();
        return v;
    }

    private void fillmonth() {
        previousMonthText.clear();
        previousMonthValue.clear();

        for (int i = 0; i <= 5; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);

            Log.e("datetime", new SimpleDateFormat("MMM yyyy").format(cal.getTime()));

            String value = new SimpleDateFormat("MM/yyyy").format(cal.getTime());
            String text = new SimpleDateFormat("MMM yy").format(cal.getTime());

            value = "01/".concat(value);
            previousMonthText.add(text);
            previousMonthValue.add(value);

        }

    }

    private void HorizontalBarChart() {

        ArrayList<BarEntry> barEntries;

        ViewTopFiveExp();
        BarData data = new BarData();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawBorders(false);
        barChart.setDescription("");

        barChart.invalidate();
        barChart.animateXY(5000, 5000);
        barChart.setDrawValueAboveBar(true);
        fillmonth();

        ArrayList<String> labels = new ArrayList<String>();
        barEntries = new ArrayList<>();


        int maxlength = 10;
        for (ClsExpenseMasterNew obj : lstClsExpenseMasterNew) {

            if (!obj.getExpense_type_name().equalsIgnoreCase("SALARY")) {
                String _VendorName = obj.getVendor_name();
                if (_VendorName.length() > maxlength) {
                    String _VendorSubName = _VendorName.substring(0, maxlength - 2).concat("..");
                    labels.add(_VendorSubName);
                } else {
                    labels.add(obj.getVendor_name());
                }
                barEntries.add(new BarEntry(Float.parseFloat(String.valueOf(obj.getGRAND_TOTAL())),
                        lstClsExpenseMasterNew.indexOf(obj)));
            } else {
                String _EmployeeName = obj.getEmployee_name();
                if (_EmployeeName.length() > maxlength) {
                    String _EmployeeSubName = _EmployeeName.substring(0, maxlength - 2).concat("..");
                    labels.add(_EmployeeSubName);
                } else {
                    labels.add(obj.getEmployee_name());
                }
                barEntries.add(new BarEntry(Float.parseFloat(String.valueOf(obj.getGRAND_TOTAL())),
                        lstClsExpenseMasterNew.indexOf(obj)));
            }

        }



        BarDataSet barDataSet2 = new BarDataSet(barEntries, "Bar Group 2");
        barDataSet2.setColor((Color.parseColor("#C909535E")));
        barChart.notifyDataSetChanged();
        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSet2);

        data = new BarData(labels, dataSets);
        barDataSet2.setBarSpacePercent(38f);
        barDataSet2.setHighLightAlpha(255);

        barChart.getXAxis().setValues(labels);
        barChart.getXAxis().setTextSize(10f);
        barChart.setData(data);
        barChart.setScaleEnabled(false);

        barChart.getXAxis().setAxisLineColor((Color.parseColor("#C909535E")));
        barChart.setVisibility(View.VISIBLE);
        barChart.getXAxis().setGridLineWidth(20);
        barChart.getXAxis().setAdjustXLabels(true);
        barChart.getXAxis().setAdjustXLabels(false);

        barChart.setScaleEnabled(false);
        barChart.setHighlightEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);

        barChart.setData(data);
        barChart.getXAxis().setTextColor(Color.parseColor("#fda500"));
        barChart.getAxisRight().setTextColor(Color.parseColor("#fda500"));

        data.setValueTextColor(Color.parseColor("#fda500"));
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(12f);

        barChart.getAxisLeft().setTextColor(R.color.red_dark); // left y-axis
        barChart.setGridBackgroundColor(Color.parseColor("#00a8e1"));
        barChart.getLegend().setTextColor(Color.BLUE);
        barChart.setDrawBarShadow(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
    }


    private void ViewTopFiveExp() {

        lstClsExpenseMasterNew = new ArrayList<>();
        lstClsExpenseMasterNew = new ClsExpenseMasterNew(getActivity()).getMostFiveExp();

        if (lstClsExpenseMasterNew != null && lstClsExpenseMasterNew.size() != 0) {
            for (ClsExpenseMasterNew obj : lstClsExpenseMasterNew) {
                Log.e("Gson", "getExpense_type_name-- " + obj.getExpense_type_name());
                lstClsExpenseMasterNew.set(lstClsExpenseMasterNew.indexOf(obj), obj);
            }
        } else {

        }

    }

    private void ViewExpenseType() {

        lstClsExpenseMasterNew = new ArrayList<>();
        lstClsExpenseMasterNew = new ClsExpenseMasterNew(getActivity()).getListNew();


//        Gson gson = new Gson();
//        String jsonInString = gson.toJson(lstClsExpenseMasterNew);
//        Log.e("Gson", "Gson:bEFORE-- " + jsonInString);

        //get last 6 monts here
        //if found than set in result list
        if (lstClsExpenseMasterNew != null && lstClsExpenseMasterNew.size() != 0) {
            for (ClsExpenseMasterNew obj : lstClsExpenseMasterNew) {
                String MonthYear = "";//12 2018 it replace with Dec 18
                String[] dateData = obj.getReceipt_date().split(" ");
                String MonthName = ClsGlobal.getMonthName(Integer.parseInt(dateData[0]));
                String Year = dateData[1].substring(dateData[1].length() - 2);
                MonthYear = MonthName.concat(" ").concat(Year);
                obj.setReceipt_date(MonthYear.trim());
                lstClsExpenseMasterNew.set(lstClsExpenseMasterNew.indexOf(obj), obj);
            }
            barChart.notifyDataSetChanged();
//            jsonInString = gson.toJson(lstClsExpenseMasterNew);
//            Log.e("Gson", "Gson:AFTER-- " + jsonInString);
        } else {

        }

    }

}
