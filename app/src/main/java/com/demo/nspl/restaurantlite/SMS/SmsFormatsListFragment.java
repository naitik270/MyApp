package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getSamplePreview;

public class SmsFormatsListFragment extends Fragment {

    FloatingActionButton fab;
    RecyclerView rv;
    SmsFormatAdapter adapter;
    LinearLayout progress_bar_layout;
    List<ClsMessageFormat> mylist = new ArrayList<>();

    public SmsFormatsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "SmsFormatsListFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("SMS Templates");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sms_formats_list, container, false);
        ClsGlobal.isFristFragment = true;

        // Inflate the layout for this fragment
        fab = view.findViewById(R.id.fab);
        rv = view.findViewById(R.id.rv);
        progress_bar_layout = view.findViewById(R.id.progress_bar_layout);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SmsFormatAdapter(getActivity());
        rv.setAdapter(adapter);

        adapter.SetOnClickListener((clsMessageFormat, position, mode) -> {

            if (mode.equalsIgnoreCase("Image Click")) {

                AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getActivity());
                popupBuilder.setTitle("Message");
                popupBuilder.setMessage(getSamplePreview(clsMessageFormat.getMessage_format()
                        ,getActivity()));
                popupBuilder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                });


                popupBuilder.show();

            } else if (mode.equalsIgnoreCase("delete Click")) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", (dialog, which) -> {

                    int Result = ClsMessageFormat.Delete(getContext(), clsMessageFormat.getId());
                    if (Result > 0) {
                        Toast.makeText(getContext(), "Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        adapter.remove(position);
                    } else {
                        Toast.makeText(getContext(), "Error while Deleting",
                                Toast.LENGTH_SHORT).show();
                    }

                });
                alertDialog.setNegativeButton("NO", (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                });

                // Showing Alert Message
                alertDialog.show();

            } else if (mode.equalsIgnoreCase("edit Click")) {
                Log.e("Check", "Id:- " + String.valueOf(clsMessageFormat.getId()));

                Intent intent = new Intent(getActivity(), Sms_FormatActivity.class);
                intent.putExtra("mode", "Edit");
                intent.putExtra("Id", String.valueOf(clsMessageFormat.getId()));
                startActivity(intent);

            }
        });

        init();

        // Query from database get the list of formats.
        getData();

        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    private void init() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Sms_FormatActivity.class);
            intent.putExtra("mode", "Add");
            intent.putExtra("Id", "0");
            startActivity(intent);
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getData() {

        new AsyncTask<Void, Void, List<ClsMessageFormat>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (mylist.size() == 0) {
                    progress_bar_layout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            protected List<ClsMessageFormat> doInBackground(Void... voids) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ClsMessageFormat.getList(getActivity());
            }

            @Override
            protected void onPostExecute(List<ClsMessageFormat> list) {
                super.onPostExecute(list);
                if (progress_bar_layout.getVisibility() == View.VISIBLE) {
                    progress_bar_layout.setVisibility(View.GONE);
                }
                mylist = list;

                if (list.size() > 0) {
                    adapter.AddItems(list);
                } else {

                }


            }
        }.execute();

    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
