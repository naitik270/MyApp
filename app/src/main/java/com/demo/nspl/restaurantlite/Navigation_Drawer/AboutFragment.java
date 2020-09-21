package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;


public class AboutFragment extends Fragment {

    TextView txt_version, txt_link;


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_about, container, false);

        ClsGlobal.isFristFragment = true;

        main(v);

        return v;
    }


    private void main(View v) {
        txt_version = v.findViewById(R.id.txt_version);
        txt_link = v.findViewById(R.id.txt_link);
        txt_link.setOnClickListener(v1 -> {

            String url = "http://www.nathanisoftware.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });
        txt_version.setText("v" + ClsGlobal.getApplicationVersion(getActivity()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("About us");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }









}
