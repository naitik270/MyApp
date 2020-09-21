package com.demo.nspl.restaurantlite.Purchase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PurchaseListFragment extends Fragment {

    FloatingActionButton fab;
    TextView empty_title_text;
    RecyclerView rv;
    List<ClsPurchaseDetail> lstClsPurchaseDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "TermsFragment"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.retail_stock_list, container, false);

        ClsGlobal.isFristFragment = true;

        main(v);

        viewPurchaseList();

        return v;
    }


    private void main(View v) {

        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        empty_title_text = v.findViewById(R.id.empty_title_text);
        fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);

        fab.setOnClickListener(v1 -> {

            Intent intent = new Intent(getActivity(), RetailPurchaseActivity.class);
            intent.putExtra("_ID", 0);
            intent.putExtra("_purchaseFlag", "AddNewPurchase");
            startActivity(intent);

        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Purchase List");
    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPurchaseList();
    }

    private void viewPurchaseList() {
        lstClsPurchaseDetails = new ArrayList<>();
        lstClsPurchaseDetails = new ClsPurchaseMaster(getActivity()).getListMonthWise(getActivity());
        if (lstClsPurchaseDetails != null && lstClsPurchaseDetails.size() != 0) {
            rv.setVisibility(View.VISIBLE);
            empty_title_text.setVisibility(View.GONE);
            PurchaseListAdapter purchaseListAdapter = new PurchaseListAdapter(getActivity(), lstClsPurchaseDetails);
            purchaseListAdapter.SetOnClickListener(clsPaymentMaster -> {

                Intent intent = new Intent(getActivity(), PurchaseDetailsListActivity.class);
                intent.putExtra("monthYear", clsPaymentMaster.getMonthYear());
                intent.putExtra("_purchaseFlag", "AddNewPurchase");
                startActivity(intent);

            });
            rv.setAdapter(purchaseListAdapter);
            purchaseListAdapter.notifyDataSetChanged();

        } else {
            empty_title_text.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
    }

}
