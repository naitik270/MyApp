package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.ViewModel.SharedViewModel;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_APPEND;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.hideKeyboard;

public class SmsFormat_Fragment extends Fragment implements
        KeywordFragment.PassDataBackListener {

    EditText title, message_format, edt_remark;

    TextView message_length;
    LinearLayout linear_keywords;
    private SharedViewModel mSharedviewModel;
    private Spinner spinner_type, spinner_default;
    private String Spinner_Type_Selected = "", Spinner_Default_Selected = "";
    String Current_id = "", mode = "";
    ClsMessageFormat clsMessageFormat;
    int result;

    KeywordFragment fragment = new KeywordFragment();

    public void setData(String mode, String id) {
        this.mode = mode;
        this.Current_id = id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ViewModel for preview text.
        mSharedviewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // for setting menu in fragument.
        setHasOptionsMenu(true);
    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sms_format, container, false);
        // Inflate the layout for this fragment
        title = view.findViewById(R.id.title);
        message_format = view.findViewById(R.id.message_format);

        message_length = view.findViewById(R.id.message_length);
        spinner_type = view.findViewById(R.id.spinner_type);
        spinner_default = view.findViewById(R.id.spinner_default);
        edt_remark = view.findViewById(R.id.edt_remark);
        linear_keywords = view.findViewById(R.id.linear_keywords);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_cust_group);
        toolbar.setOnMenuItemClickListener(item -> {

            hideKeyboard(getActivity());
            int id = item.getItemId();

            if (id == R.id.itm_done) {
//                Toast.makeText(getActivity(),"Done",Toast.LENGTH_LONG).show();
//                if(!title.getText().toString().isEmpty())
                if (validation()) {
                    ClsMessageFormat insert = new ClsMessageFormat();
                    insert.setTitle(title.getText().toString().replace("'", "''").trim());
                    insert.setMessage_format(message_format.getText().toString().replace("'", "''").trim());
                    insert.setDefault(Spinner_Default_Selected);
                    insert.setType(Spinner_Type_Selected);
                    insert.setRemark(edt_remark.getText().toString());
                    if (mode.equalsIgnoreCase("Add")) {

                        result = ClsMessageFormat.Insert(insert, getActivity());

                        if (result > 0) {
                            Toast.makeText(getActivity(), "Message Format Added Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to Add Message Format!", Toast.LENGTH_LONG).show();
                        }
                        getActivity().finish();
                    } else if (mode.equalsIgnoreCase("Edit")) {
                        insert.setId(Integer.parseInt(Current_id));

                        @SuppressLint("WrongConstant")
                        SQLiteDatabase db = getActivity().openOrCreateDatabase(ClsGlobal.Database_Name,
                                MODE_APPEND, null);
                        result = ClsMessageFormat.Update(insert,"Update All",db);

                        if (result > 0) {
                            Toast.makeText(getActivity(), "Message Format Updated Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to Update Message Format!", Toast.LENGTH_LONG).show();
                        }
                        getActivity().finish();

                        db.close();
                    }

                }


            }

            return true;
        });

//        getActivity().setSupportActionBar(toolbar);

//        toolbar.setNavigationOnClickListener(view1 ->
//                getActivity().onBackPressed());

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // set Pass Data Back Listener.
        fragment.setPassDataBackListener(this);

        init();
        SetupSpinner();

        if (mode != null && mode.equalsIgnoreCase("Edit")) {

            Observable.just(ClsMessageFormat.QueryById(getActivity(), Current_id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        clsMessageFormat = result;
                        title.setText(clsMessageFormat.getTitle());
                        message_format.setText(clsMessageFormat.getMessage_format());
                        edt_remark.setText(clsMessageFormat.getRemark());

                        spinner_type.setSelection(Arrays.asList(getResources()
                                .getStringArray(R.array.array_type_options)).indexOf(clsMessageFormat.getType()));

                        spinner_default.setSelection(Arrays.asList(getResources()
                                .getStringArray(R.array.array_default_options)).indexOf(clsMessageFormat.getDefault()));

                    });

//            clsMessageFormat = ClsMessageFormat.QueryById(getActivity(), Current_id);

        }

        return view;
    }

    private void init() {

        message_format.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                message_length.setText(String.valueOf(message_format.getText().length()));
                mSharedviewModel.setDate(message_format.getText().toString());

                Log.e("Check", " != null t" + message_format.getText().toString());

            }
        });

        linear_keywords.setOnClickListener(v -> {
            // display first sheet
            fragment.show(getActivity().getSupportFragmentManager(), "dialog");
        });

    }


    /**
     * SetupSpinner.
     */
    private void SetupSpinner() {

        ArrayAdapter TypeSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_type_options, android.R.layout.simple_spinner_item);

        TypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner_type.setAdapter(TypeSpinnerAdapter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.none_type))) {

                        Spinner_Type_Selected = getString(R.string.none_type);

                    } else if (selection.equals(getString(R.string.purchase_type))) {

                        Spinner_Type_Selected = getString(R.string.purchase_type);

                    } else if (selection.equals(getString(R.string.offer_type))) {

                        Spinner_Type_Selected = getString(R.string.offer_type);

                    }else if (selection.equals(getString(R.string.sales_type))) {
                        Spinner_Type_Selected = getString(R.string.sales_type);
                    }else if (selection.equals(getString(R.string.quotation_type))) {
                        Spinner_Type_Selected = getString(R.string.quotation_type);
                    }

                    else {
                        Spinner_Type_Selected = getString(R.string.none_type);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Spinner_Type_Selected = getString(R.string.none_type);
            }
        });


        ArrayAdapter DefaultSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_default_options, android.R.layout.simple_spinner_item);

        DefaultSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner_default.setAdapter(DefaultSpinnerAdapter);
        spinner_default.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.yes_default))) {
                        Spinner_Default_Selected = getString(R.string.yes_default);

                    } else if (selection.equals(getString(R.string.No_default))) {
                        Spinner_Default_Selected = getString(R.string.No_default);
                    } else {
                        Spinner_Default_Selected = getString(R.string.yes_default);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Spinner_Default_Selected = getString(R.string.No_default);
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Check", "onDestroyView");
        hideKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Check", "onResume");
        hideKeyboard(getActivity());
    }

    @Override
    public void passOnKeywordClick(ClsKeywordDescription clsKeywordDescription, int position) {
        Log.e("Check", "onResume" + clsKeywordDescription + ", " + position);

        message_format.getText().insert(message_format.getSelectionStart(),
                "##" + clsKeywordDescription.getKeyword() +"##");

    }

    private boolean validation() {
        boolean result = true;

        if (title.getText() == null || title.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Title Required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (message_format.getText() == null || message_format.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Message Format Required!", Toast.LENGTH_SHORT).show();
            return false;
        }


        return result;
    }


}
