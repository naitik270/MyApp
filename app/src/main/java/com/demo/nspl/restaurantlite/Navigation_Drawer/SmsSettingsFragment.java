package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import static android.content.Context.MODE_PRIVATE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.hideKeyboard;


public class SmsSettingsFragment extends Fragment {


    //    private CheckBox checkbox_sales_receiver, checkbox_quotation_receiver;
    private EditText edt_mobile_no_sales_receiver, edt_qsr;
    private TextView txt_quotation_sms_receiver, txt_sales_sms_receiver;
    private Button btn_apply;
    private static SharedPreferences mPreferences;
    private static final String mPreferncesName = "MyPerfernces";

    ImageButton iv_clear_phone_sale, iv_clear_phone_quotation;

    public SmsSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Sms Settings");

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "Sms Settings"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sms_settings, container, false);
        // Inflate the layout for this fragment.

        ClsGlobal.isFristFragment = true;

        edt_mobile_no_sales_receiver = view.findViewById(R.id.edt_mobile_no_sales_receiver);
        edt_qsr = view.findViewById(R.id.edt_qsr);
        txt_quotation_sms_receiver = view.findViewById(R.id.txt_quotation_sms_receiver);
        txt_sales_sms_receiver = view.findViewById(R.id.txt_sales_sms_receiver);
        btn_apply = view.findViewById(R.id.btn_apply);
        iv_clear_phone_sale = view.findViewById(R.id.iv_clear_phone_sale);
        iv_clear_phone_quotation = view.findViewById(R.id.iv_clear_phone_quotation);

        ViewData();
        return view;
    }

    private void ViewData() {
        // get Data from Preferences.
        if (ClsGlobal.getSmsReceiver("Sales", getActivity()) != null
                && !ClsGlobal.getSmsReceiver("Sales", getActivity()).equalsIgnoreCase("")) {
            String sales = ClsGlobal.getSmsReceiver("Sales", getActivity());

            txt_sales_sms_receiver.setEnabled(true);
//            txt_sales_sms_receiver.setTextColor(getResources().getColor(R.color.Txt_list_Colour));
            edt_mobile_no_sales_receiver.setText(sales);

            edt_mobile_no_sales_receiver.getBackground().setColorFilter(getResources()
                            .getColor(R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        if (ClsGlobal.getSmsReceiver("Quotation", getActivity()) != null
                && !ClsGlobal.getSmsReceiver("Quotation", getActivity()).equalsIgnoreCase("")) {
            String Quotation = ClsGlobal.getSmsReceiver("Quotation", getActivity());

            txt_quotation_sms_receiver.setEnabled(true);
//            txt_quotation_sms_receiver.setTextColor(getResources().getColor(R.color.Txt_list_Colour));
            edt_qsr.setText(Quotation);

            edt_qsr.getBackground().setColorFilter(getResources()
                            .getColor(R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }
        // -------------------------------------------------------------------------//

        iv_clear_phone_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_mobile_no_sales_receiver.setText("");
            }
        });

        iv_clear_phone_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_qsr.setText("");
            }
        });

        btn_apply.setOnClickListener(v -> {

            if (edt_mobile_no_sales_receiver.getText().length() > 0
                    && edt_mobile_no_sales_receiver.getText().length() == 10) {

                SavePreferences("Sales Sms Receiver",
                        edt_mobile_no_sales_receiver.getText().toString());

                Toast.makeText(getActivity(),
                        "Settings Apply successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),
                        "Sales Receiver Phone No length Should be 10", Toast.LENGTH_SHORT).show();

            }

            if (edt_qsr.getText().length() > 0
                    && edt_qsr.getText().length() == 10) {

                SavePreferences("Quotation Sms Receiver",
                        edt_qsr.getText().toString());
                Toast.makeText(getActivity(),
                        "Settings Apply successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),
                        "Quotation Receiver Phone No length Should be 10", Toast.LENGTH_SHORT).show();

            }
            // Hide keyboard in fragment.
            hideKeyboard(getActivity());

        });
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences mPreferences = getActivity().getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

}
