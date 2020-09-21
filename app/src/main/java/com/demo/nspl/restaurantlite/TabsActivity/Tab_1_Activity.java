package com.demo.nspl.restaurantlite.TabsActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.DisplayBarDataActivity;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_1_Activity extends Fragment {

    BarChart barChart;
    List<String> previousMonthText = new ArrayList<String>();
    List<ClsExpenseMasterNew> lstClsExpenseMasterNew;
    List<String> previousMonthValue = new ArrayList<String>();
    List<FirstAndLastDate> lstFirstAndLastDates = new ArrayList<FirstAndLastDate>();


    public Tab_1_Activity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "Tab_1_Activity"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_1_, container, false);
        fillmonth();

        barChart = v.findViewById(R.id.barchart);
        BarChart();
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                FirstAndLastDate obj = new FirstAndLastDate();
                obj = lstFirstAndLastDates.get(e.getXIndex());
                Intent intent = new Intent(getActivity(), DisplayBarDataActivity.class);
                intent.putExtra("startDate", obj.get_FirstDate());
                intent.putExtra("lastDate", obj.get_LastDate());
                intent.putExtra("month", String.valueOf(previousMonthText.get(e.getXIndex())));
                intent.putExtra("totalVal", String.valueOf(e.getVal()));
                startActivity(intent);

            }

            @Override
            public void onNothingSelected() {

            }
        });

        return v;

    }

    private void BarChart() {
        ArrayList<BarEntry> barEntries;
        ViewExpenseType();

        // FILL DATA FROM DATABASE

        BarData data = new BarData();
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawBorders(false);
        barChart.setDescription("");
        barChart.animateXY(1500, 1500);


        ArrayList<String> labels = new ArrayList<String>();
        barEntries = new ArrayList<>();
        for (String s : previousMonthText) {
            labels.add(s);
            boolean found = false;
            for (ClsExpenseMasterNew _ObjData : lstClsExpenseMasterNew) {
                if (s.equalsIgnoreCase(_ObjData.getReceipt_date())) {
                    //May 18, May 18
                    barEntries.add(new BarEntry(Float.parseFloat(String.valueOf(_ObjData.getGRAND_TOTAL())), previousMonthText.indexOf(s)));
                    found = true;
                    break;
                }
            }
            if (!found) {
                barEntries.add(new BarEntry(Float.valueOf(0f), previousMonthText.indexOf(s)));
            }
        }

        BarDataSet barDataSet2 = new BarDataSet(barEntries, "Bar Group 2");
        barDataSet2.setColor((Color.parseColor("#C909535E")));


        barChart.notifyDataSetChanged();

        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSet2);

        data = new BarData(labels, dataSets);

        barChart.getXAxis().setValues(labels);
        barChart.getXAxis().setTextSize(10f);
        barChart.setScaleEnabled(false);

//        barChart.getXAxis().setGridColor(Color.YELLOW);
        barChart.getXAxis().setAxisLineColor((Color.parseColor("#C909535E")));
        barChart.getXAxis().setAdjustXLabels(false);


        barChart.setScaleEnabled(false);
        barChart.setHighlightEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);


        barChart.setData(data);
        data.setValueTextColor(Color.parseColor("#fda500"));
        barChart.getXAxis().setTextColor(Color.parseColor("#fda500"));
        barChart.getAxisRight().setTextColor(Color.parseColor("#fda500"));
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(12f);

        barChart.getAxisLeft().setTextColor(R.color.red_dark);  // left y-axis
        barChart.setGridBackgroundColor(R.color.colorPrimary);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.setDrawBarShadow(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();
//        ViewExpenseType();
        BarChart();  //refresh/fill data into chart from database

    }

    private void ViewExpenseType() {

        lstClsExpenseMasterNew = new ArrayList<>();
        lstClsExpenseMasterNew = new ClsExpenseMasterNew(getActivity()).getListNew();


        //get last 6 monts here
        //if found than set in result list

        if (lstClsExpenseMasterNew != null && lstClsExpenseMasterNew.size() != 0) {
            for (ClsExpenseMasterNew obj : lstClsExpenseMasterNew) {
                if (obj.getReceipt_date() != null && !obj.getReceipt_date().isEmpty()) {
                    String MonthYear = ""; //12 2018 it replace with Dec 18
                    String[] dateData = obj.getReceipt_date().split(" ");
                    String MonthName = ClsGlobal.getMonthName(Integer.parseInt(dateData[0]));
                    String Year = dateData[1].substring(dateData[1].length() - 2);
                    MonthYear = MonthName.concat(" ").concat(Year);
                    obj.setReceipt_date(MonthYear.trim());
                    lstClsExpenseMasterNew.set(lstClsExpenseMasterNew.indexOf(obj), obj);
                }


            }
            barChart.notifyDataSetChanged();

        } else {

        }

    }


    private void fillmonth() {
        previousMonthText.clear();
        previousMonthValue.clear();


        lstFirstAndLastDates = new ArrayList<FirstAndLastDate>();

        for (int i = 5; i >= 0; i--) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);

            Log.e("datetime", new SimpleDateFormat("MMM yyyy").format(cal.getTime()));

            String _FirstDate = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
            String _LastDate = new SimpleDateFormat("yyyy-MM").format(cal.getTime());

            String text = new SimpleDateFormat("MMM yy").format(cal.getTime());

            _FirstDate = _FirstDate.concat("-01");//2018-04-20

            int lastDate = cal.getActualMaximum(Calendar.DATE);
            _LastDate = _LastDate.concat("-").concat(String.valueOf(lastDate));


            previousMonthText.add(text);
//            previousMonthValue.add(value);


            FirstAndLastDate obj = new FirstAndLastDate();
            obj.set_FirstDate(_FirstDate);
            obj.set_LastDate(_LastDate);
            lstFirstAndLastDates.add(obj);

            Log.e("Date--", obj.get_FirstDate() + "--".concat(obj.get_LastDate()));

        }

    }

    public class FirstAndLastDate {

        String _FirstDate = "";
        String _LastDate = "";

        public String get_FirstDate() {
            return _FirstDate;
        }

        public void set_FirstDate(String _FirstDate) {
            this._FirstDate = _FirstDate;
        }

        public String get_LastDate() {
            return _LastDate;
        }

        public void set_LastDate(String _LastDate) {
            this._LastDate = _LastDate;
        }


    }

}
