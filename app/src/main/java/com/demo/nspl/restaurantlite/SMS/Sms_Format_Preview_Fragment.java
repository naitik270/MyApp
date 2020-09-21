package com.demo.nspl.restaurantlite.SMS;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.ViewModel.SharedViewModel;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getSamplePreview;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.hideKeyboard;


public class Sms_Format_Preview_Fragment extends Fragment {


    TextView message_format_preview, message_length;

//    SmsFormat_Fragment fragment = new SmsFormat_Fragment();
    private SharedViewModel mSharedviewModel;


    public Sms_Format_Preview_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ViewModel for preview text.
        mSharedviewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms__format__priview, container, false);
        setHasOptionsMenu(true);
        message_format_preview = view.findViewById(R.id.message_format_preview);
        message_length = view.findViewById(R.id.message_length);



        mSharedviewModel.getDate().observe(this, preview -> {
            if (preview != null){
                message_format_preview.setText(getSamplePreview(preview,getActivity()));
            }

        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Check", "onResume");
        hideKeyboard(getActivity());
        message_length.setText(String.valueOf(message_format_preview.getText().length()));
    }


}

