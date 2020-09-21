package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.QuotationReportAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.RecentQuotationActivity;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;

import java.util.List;

public class QuotationReportFragment extends Fragment {

    RecyclerView rv;
    TextView txt_no_data;
    ProgressBar progress_bar;
    QuotationReportAdapter quotationReportAdapter;

    public QuotationReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Quotation Report");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quotation_reports, container, false);
        ClsGlobal.isFristFragment = true;

        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_no_data = view.findViewById(R.id.txt_no_data);
        progress_bar = view.findViewById(R.id.progress_bar);

        quotationReportAdapter = new QuotationReportAdapter(getActivity(), "QuotationReports");
        rv.setAdapter(quotationReportAdapter);

        quotationReportAdapter.SetOnClickListener((clsInventoryOrderMaster) -> {

            String _monthYear = ClsGlobal.getMonthName(clsInventoryOrderMaster.getMounth())
                    + " " + clsInventoryOrderMaster.getYear();

            Intent intent = new Intent(getActivity(), RecentQuotationActivity.class);
            intent.putExtra("_month", clsInventoryOrderMaster.getMounth());

            intent.putExtra("Year", clsInventoryOrderMaster.getYear());
            intent.putExtra("title", clsInventoryOrderMaster.getYear());

            intent.putExtra("quotType", clsInventoryOrderMaster.getQuotationType());
            intent.putExtra("fragMode", "QuotationReportFragment");
            intent.putExtra("editSource", _monthYear);

            startActivity(intent);
        });


        new LoadAsyncTask("").execute();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
//        new LoadAsyncTask("").execute();
    }

    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsQuotationMaster>> {

        String where = "";

        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);

        }

        LoadAsyncTask(String where) {
            this.where = where;
        }

        @Override
        protected List<ClsQuotationMaster> doInBackground(Void... voids) {
            return new ClsQuotationMaster().getMonthWiseQuotationList(getActivity(), "");
        }

        @Override
        protected void onPostExecute(List<ClsQuotationMaster> lst) {
            super.onPostExecute(lst);
            progress_bar.setVisibility(View.GONE);

            quotationReportAdapter.AddItems(lst);

            if (lst != null && lst.size() != 0) {
                txt_no_data.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            } else {
                txt_no_data.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }

        }
    }


}
