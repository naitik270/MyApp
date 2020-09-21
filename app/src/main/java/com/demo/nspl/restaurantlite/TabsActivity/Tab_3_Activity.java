package com.demo.nspl.restaurantlite.TabsActivity;


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
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_3_Activity extends Fragment {

    List<ClsExpenseMasterNew> lstClsExpenseMasterNew;
    TextView txt_all_total, txt_income, txt_diffrece, txt_sale_amount;

    public Tab_3_Activity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "Tab_3_Activity"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_3_, container, false);
        main(v);
        return v;
    }

    private void main(View v) {

        txt_all_total = v.findViewById(R.id.txt_all_total);
        txt_income = v.findViewById(R.id.txt_income);
        txt_diffrece = v.findViewById(R.id.txt_diffrece);
        txt_sale_amount = v.findViewById(R.id.txt_sale_amount);


        ViewGrandTotalExp();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGrandTotalExp();
    }

    private void ViewGrandTotalExp() {
        lstClsExpenseMasterNew = new ArrayList<>();
        lstClsExpenseMasterNew = new ClsExpenseMasterNew(getActivity()).getGrantTotalExp();
        Double _GrandTotal = 0.0;

        for (ClsExpenseMasterNew obj : lstClsExpenseMasterNew) {
            _GrandTotal = Double.valueOf(String.valueOf(obj.getGRAND_TOTAL()));
//            String _FinalGrandTotal = decimalFormat.format(_GrandTotal);
            txt_all_total.setText(" " + String.valueOf(ClsGlobal.round(_GrandTotal, 2)));
        }


        Double getIncome = ClsPaymentMaster.getTotalAmount("");
        Log.e("getIncome", String.valueOf(getIncome));
        txt_income.setText(" " + String.valueOf(ClsGlobal.round(getIncome, 2)));


        ClsPaymentMaster objClsPaymentMaster = new ClsPaymentMaster(getActivity());
        Double getSaleAmount = ClsPaymentMaster.getTotalSaleAmount();
        Log.e("getIncome", String.valueOf(getSaleAmount));
        txt_sale_amount.setText(" " + String.valueOf(ClsGlobal.round(getSaleAmount, 2)));

        Double _diff = getSaleAmount - _GrandTotal;

//        Double  _diff = AVG.SALE VALUE  - (AVG.PURCHASE VALUE + TOTAL EXPENSE);


        txt_diffrece.setText(" " + String.valueOf(ClsGlobal.round(_diff, 2)));
        Log.e("difference", String.valueOf(getIncome - _GrandTotal));


    }
}