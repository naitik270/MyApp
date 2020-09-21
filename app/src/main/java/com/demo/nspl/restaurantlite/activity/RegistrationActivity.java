package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.Country.ClsCityList;
import com.demo.nspl.restaurantlite.Country.ClsCityListParams;
import com.demo.nspl.restaurantlite.Country.ClsCountryList;
import com.demo.nspl.restaurantlite.Country.ClsCountrySuccess;
import com.demo.nspl.restaurantlite.Country.ClsStateList;
import com.demo.nspl.restaurantlite.Country.ClsStateListParams;
import com.demo.nspl.restaurantlite.Country.InterfaceCityList;
import com.demo.nspl.restaurantlite.Country.InterfaceCountry;
import com.demo.nspl.restaurantlite.Country.InterfaceStateList;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.AppLocationService;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsRegistrationParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsRegistrationTermsList;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsRegistrationTermsParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceRegistration;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceRegistrationTerms;
import com.demo.nspl.restaurantlite.classes.StringWithTag;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class RegistrationActivity extends AppCompatActivity {

    // TextInputLayout txt_input_edt_uname,txt_restaurant_name,txt_psw,txt_confirm_psw,txt_email,txt_contact_number;
    EditText edit_shop_name, edt_address, edt_pincode, edit_email, edit_mobile_no, edt_contact_person;
    TextView terms_conditions, already_user;
    CheckBox chk_agree;
    Button btn_register;
    //    private Toolbar mToolbar;
    AppLocationService appLocationService;
    Double lati = 0.0, longi = 0.0;
    String address = "", reg_terms = "";

    List<ClsRegistrationTermsList> lstClsRegistrationTermsLists;

    TextView txt_premium_link;

    private static final Pattern MobileNo_Pattern
            = Pattern.compile(ClsGlobal.MobileNo_Pattern);

    Spinner sp_country;
    String _CountryName;
    int _countryID;

    Spinner sp_state;
    String _stateName;
    int _stateID;

    Spinner sp_city;
    String _cityName;
    int _cityID;


    List<ClsStateList> lstClsStateLists = new ArrayList<>();
    List<ClsCityList> lstClsCityLists = new ArrayList<>();

    List<ClsCountryList> lstClsCountryLists = new ArrayList<>();
    List<StringWithTag> lstStringWithTags = new ArrayList<StringWithTag>();

    private RadioGroup radio_group;
    private RadioButton rb_free, rb_premium;
    String _selectLicenseType = "", type = "";
    String _resCustomerID = "", _resCustomerStatus = "", _resLicenseType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrartion);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "RegistrationActivity"));
        }

        edit_shop_name = findViewById(R.id.edit_Shop_name);
        edt_address = findViewById(R.id.edit_address);

        edt_pincode = findViewById(R.id.edt_pincode);
        edit_email = findViewById(R.id.edit_email);


        edit_mobile_no = findViewById(R.id.edit_mobile_no);


        terms_conditions = findViewById(R.id.txt_terms);
        edt_contact_person = findViewById(R.id.edt_contact_person);

        chk_agree = findViewById(R.id.chk_agree);

        btn_register = findViewById(R.id.btn_register);
        appLocationService = new AppLocationService(
                RegistrationActivity.this);

        already_user = findViewById(R.id.already_user);

        ClsPermission.checkpermission(RegistrationActivity.this);
        getLatitudeLongitude();
        getRegistrationTermsAPI();


        radio_group = findViewById(R.id.radio_group);
        rb_free = findViewById(R.id.rb_free);
        rb_premium = findViewById(R.id.rb_premium);

        txt_premium_link = findViewById(R.id.txt_premium_link);


        radio_group.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = radio_group.getCheckedRadioButtonId();
            // find the radio button by returned id
            RadioButton radioButton = findViewById(selectedId);
            _selectLicenseType = radioButton.getText().toString();

        });

        already_user.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        already_user.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LogInActivity.class);
            startActivity(intent);
        });

        txt_premium_link.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_premium_link.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PremiumActivity.class);
            intent.putExtra("flag", "Outside");
            startActivity(intent);
        });

        terms_conditions.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        terms_conditions.setOnClickListener(view -> {
            Intent intent = new Intent(getApplication(), DisplayTAndCActivity.class);
            intent.putExtra("terms", reg_terms);
            startActivity(intent);
        });

        sp_country = findViewById(R.id.sp_country);
        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                Log.e("--URL--", "STEP-10" + position);


                ClsCountryList objClsEmployeeMaster = lstClsCountryLists.get(position);
                _CountryName = adapterView.getItemAtPosition(position).toString();
                Log.e("--URL--", "_CountryName: " + _CountryName);

                _countryID = objClsEmployeeMaster.getCountryID();
                Log.e("--URL--", "_countryID: " + _countryID);


                if (_countryID == 0) {
                    lstClsStateLists.clear();
                    lstClsCityLists.clear();
                    sp_state.setSelection(0);
                    sp_state.setEnabled(false);
                    sp_state.setVisibility(View.GONE);
                    sp_city.setVisibility(View.GONE);
                } else {
                    sp_state.setEnabled(true);
                    sp_state.setVisibility(View.VISIBLE);
                    sp_city.setVisibility(View.GONE);
                    getSateListNew(_countryID);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        getCountryListSpinner();


        sp_state = findViewById(R.id.sp_state);

        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                ClsStateList objClsStateList = lstClsStateLists.get(position);
                _stateName = adapterView.getItemAtPosition(position).toString();
                Log.e("--URL--", "_stateName: " + _stateName);
                _stateID = objClsStateList.getStateID();
                Log.e("--URL--", "_stateID: " + _stateID);


                if (_stateID == 0) {
                    lstClsCityLists.clear();
                    sp_city.setSelection(0);
                    sp_city.setEnabled(false);
                    sp_city.setVisibility(View.GONE);
                } else {
                    sp_city.setEnabled(true);
                    sp_city.setVisibility(View.VISIBLE);
                }
                getCityListSpinner(_stateID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        sp_city = findViewById(R.id.sp_city);

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                Log.e("--URL--", "City--position: " + position);
                ClsCityList objClsCityList = lstClsCityLists.get(position);
                _cityName = adapterView.getItemAtPosition(position).toString();
                Log.e("--URL--", "_cityName: " + _stateName);
                _cityID = objClsCityList.getCityID();
                Log.e("--URL--", "_cityID: " + _stateID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        btn_register.setOnClickListener(view -> {

            boolean validation = RegistrationValidation();
            if (validation == true) {
                getRegistrationAPI();
            }
//            getRegistrationAPI();
        });


    }

    private Boolean RegistrationValidation() {

        if (edit_shop_name.getText() == null || edit_shop_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Restaurant Name Required", Toast.LENGTH_SHORT).show();
            edit_shop_name.requestFocus();
            return false;
        }
        if (edt_contact_person.getText() == null || edt_contact_person.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Person Name Required", Toast.LENGTH_SHORT).show();
            edt_contact_person.requestFocus();
            return false;
        }
        if (edit_mobile_no.getText() == null || edit_mobile_no.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Contact Number Required", Toast.LENGTH_SHORT).show();
            edit_mobile_no.requestFocus();
            return false;
        }

        String email = edit_email.getText().toString().trim();
        if (edit_email.getText() == null || email.isEmpty() || !ClsGlobal.isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Email Id Required", Toast.LENGTH_SHORT).show();
            edit_email.requestFocus();
            return false;
        }

        if (edt_address.getText() == null || edt_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Address Required", Toast.LENGTH_SHORT).show();
            edt_address.requestFocus();
            return false;
        }

        Matcher matcher = MobileNo_Pattern.matcher(edit_mobile_no.getText().toString());
        if (!matcher.matches()) {
            Toast.makeText(getApplicationContext(), "Invalid Mobile No!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (_countryID == 0) {
            Toast.makeText(getApplicationContext(), R.string.err_msg_country, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (_stateID == 0) {
            Toast.makeText(getApplicationContext(), R.string.err_msg_state, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (_cityID == 0) {
            Toast.makeText(getApplicationContext(), R.string.err_msg_city, Toast.LENGTH_SHORT).show();
            return false;
        }

        int checkLicenseType = radio_group.getCheckedRadioButtonId();

        if (checkLicenseType == -1) {
            Toast.makeText(this, "Select License Type", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!chk_agree.isChecked()) {
            Toast.makeText(RegistrationActivity.this, "Please accept terms & conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ClsGlobal.CheckInternetConnection(RegistrationActivity.this)) {
            Toast.makeText(RegistrationActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void getRegistrationAPI() {
        String ProductName = ClsGlobal.AppName;
        String AppType = ClsGlobal.AppType;
        String AlternetMobileNo = "";
        String latitude = String.valueOf(lati);
        String longitude = String.valueOf(longi);
        String StatusRemark = "";

        ClsRegistrationParams objClsRegistrationParams = new ClsRegistrationParams();

        objClsRegistrationParams.setProductName(ProductName);
        objClsRegistrationParams.setAppType(AppType);
        objClsRegistrationParams.setCompanyName(edit_shop_name.getText().toString().trim());
        objClsRegistrationParams.setCINNo("");
        objClsRegistrationParams.setGSTNo("");
        objClsRegistrationParams.setAddress(edt_address.getText().toString().trim());
        objClsRegistrationParams.setPinCode(edt_pincode.getText().toString().trim());
        objClsRegistrationParams.setState(_stateName);
        objClsRegistrationParams.setCity(_cityName);
        objClsRegistrationParams.setMobileNo(edit_mobile_no.getText().toString().trim());
        objClsRegistrationParams.setEmail(edit_email.getText().toString().trim());
        objClsRegistrationParams.setAlternetMobileNo(AlternetMobileNo);
        objClsRegistrationParams.setRegistredDeviceInfo(ClsGlobal.getDeviceInfo(RegistrationActivity.this));
        objClsRegistrationParams.setApplicationVersion(ClsGlobal.getApplicationVersion(RegistrationActivity.this));
        objClsRegistrationParams.setMACAddress(ClsGlobal.getMacAddr());
        objClsRegistrationParams.setCapturedAddress(address);
        objClsRegistrationParams.setLatitude(latitude);
        objClsRegistrationParams.setLongitude(longitude);
        objClsRegistrationParams.setContactPerson(edt_contact_person.getText().toString().trim());
        objClsRegistrationParams.setContactPersonMobileNo(edit_mobile_no.getText().toString().trim());
        objClsRegistrationParams.setCountryID(_countryID);
        objClsRegistrationParams.setStateID(_stateID);
        objClsRegistrationParams.setCityID(_cityID);
        objClsRegistrationParams.setLicenseType("Free");
//        objClsRegistrationParams.setLicenseType(rb_premium.isChecked() ? "Premium" : "Free");

        objClsRegistrationParams.setStatusRemark(StatusRemark);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsRegistrationParams);
        Log.d("--URL--", "Params: " + jsonInString);

        InterfaceRegistration interfaceDesignation = ApiClient.getRetrofitInstance().create(InterfaceRegistration.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceDesignation.toString());

        Call<ClsRegistrationParams> call = interfaceDesignation.postRegistration(objClsRegistrationParams);
        Log.e("--URL--", "ClsRegistrationParams: " + call.request().url());


        ProgressDialog pd =
                ClsGlobal._prProgressDialog(RegistrationActivity.this, "Waiting for registration...", true);
        pd.show();


        call.enqueue(new Callback<ClsRegistrationParams>() {
            @Override
            public void onResponse(Call<ClsRegistrationParams> call, Response<ClsRegistrationParams> response) {
                pd.dismiss();
                String _response = response.body().getSuccess();

                if (_response.equals("1")) {
                    _resCustomerID = response.body().getCustomerid();
                    _resCustomerStatus = response.body().getCustomerstatus();
                    _resLicenseType = response.body().getLicensetype();
                    callingIntent("NEW", _resLicenseType);
                    Toast.makeText(RegistrationActivity.this,
                            "Record saved successfully", Toast.LENGTH_SHORT).show();

                } else if (_response.equals("2")) {
                    _resCustomerID = response.body().getCustomerid();
                    _resCustomerStatus = response.body().getCustomerstatus();
                    _resLicenseType = response.body().getLicensetype();
                    callingIntent("RENEW", _resLicenseType);
                    Toast.makeText(RegistrationActivity.this,
                            "Record already exists", Toast.LENGTH_SHORT).show();

                } else if (_response.equals("3")) {
                    Toast.makeText(RegistrationActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                } else if (_response.equals("0")) {
                    Toast.makeText(RegistrationActivity.this, "fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "No response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsRegistrationParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

                Log.d("--State--", "RegiThrow: " + t.getMessage());
            }
        });
    }

    private void callingIntent(String mode, String _resLicenseType) {

        ClsGlobal.hideKeyboard(RegistrationActivity.this);
        Intent intent = new Intent(RegistrationActivity.this,
                OtpVerificationActivity.class);

        intent.putExtra("_mobileNo", edit_mobile_no.getText().toString().trim());
        intent.putExtra("_customerId", _resCustomerID);
        intent.putExtra("_resCustomerStatus", _resCustomerStatus);
        intent.putExtra("_resLicenseType", _resLicenseType);


        intent.putExtra("mode", mode);

        startActivity(intent);


        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLatitudeLongitude();

        getRegistrationTermsAPI();

    }

    private void getLatitudeLongitude() {

        Location nwLocation = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);

        if (nwLocation != null) {
            try {
                lati = nwLocation.getLatitude();
                longi = nwLocation.getLongitude();
                Geocoder geocoder;
                List<Address> addresses = new ArrayList<>();
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(lati, longi, 1);

                if (addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showSettingsAlert("NETWORK");
        }
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RegistrationActivity.this);
        alertDialog.setTitle(provider + " SETTINGS");
        alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        RegistrationActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, RegistrationActivity.this);
                return;
            }
        }
    }

    private void getCountryListSpinner() {
        lstClsCountryLists = new ArrayList<>();
        ClsCountryList objClsCountryList = new ClsCountryList();
        objClsCountryList.setCountryID(0);
        objClsCountryList.setCountryName("Select Country");
        lstClsCountryLists.add(0, objClsCountryList);

        InterfaceCountry interfaceCountry = ApiClient.getRetrofitInstance().create(InterfaceCountry.class);
        Log.e("--URL--", "interfaceCountry: " + interfaceCountry);
        Call<ClsCountrySuccess> obj = interfaceCountry.getCountryList();

        Log.e("--URL--", "getCountryList: " + obj.request().url());
        Log.e("--URL--", "STEP-1");

        final ProgressDialog pd =
                ClsGlobal._prProgressDialog(RegistrationActivity.this, "Waiting for country...", true);
        pd.show();

        obj.enqueue(new Callback<ClsCountrySuccess>() {
            @Override
            public void onResponse(Call<ClsCountrySuccess> call,
                                   Response<ClsCountrySuccess> response) {
                pd.dismiss();

                if (response.body() != null) {
                    Log.e("--URL--", "STEP-6");
                    String success = response.body().getSuccess();
                    Log.e("--URL--", "STEP-7");

                    if (success.equals("1")) {
                        try {
                            List<ClsCountryList> liveCountryList = response.body().getData();
                            if (liveCountryList != null && liveCountryList.size() != 0) {
                                lstClsCountryLists.addAll(liveCountryList);
                            }
                            fillCountryAdp();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "No country found", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.e("--URL--", "Step--ELSE");
                    Log.e("--URL--", "Step--ELSE" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ClsCountrySuccess> call, Throwable t) {
                Log.d("--State--", "CountryThrow: " + t.getMessage());
            }
        });

    }

    private void getSateListNew(int countryID) {
        lstClsStateLists = new ArrayList<>();
        ClsStateList obClsStateList = new ClsStateList();
        obClsStateList.setStateID(0);
        obClsStateList.setStateName("Select State");
        lstClsStateLists.add(0, obClsStateList);


        InterfaceStateList interfaceStateList = ApiClient.getRetrofitInstance().create(InterfaceStateList.class);
        Call<ClsStateListParams> objState = interfaceStateList.interfaceCall(countryID);

        Log.e("--URL--", "objState: " + objState.request().url());


        final ProgressDialog pd =
                ClsGlobal._prProgressDialog(RegistrationActivity.this, "Waiting for state...", true);
        pd.show();


        objState.enqueue(new Callback<ClsStateListParams>() {
            @Override
            public void onResponse(Call<ClsStateListParams> call, Response<ClsStateListParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    try {

                        List<ClsStateList> liveStateList = response.body().getData();

                        if (liveStateList != null && liveStateList.size() != 0) {
                            lstClsStateLists.addAll(liveStateList);
                        }
                        fillStateAdp();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(RegistrationActivity.this, "Terms and Condition not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsStateListParams> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong...Internet issue!", Toast.LENGTH_SHORT).show();
                Log.d("--State--", "Throw: " + t.getMessage());

            }
        });
    }

    private void getCityListSpinner(int stateID) {
        lstClsCityLists = new ArrayList<>();
        ClsCityList objClsCityList = new ClsCityList();
        objClsCityList.setCityID(0);
        objClsCityList.setCityName("Select City");
        lstClsCityLists.add(0, objClsCityList);


        InterfaceCityList interfaceCityList = ApiClient.getRetrofitInstance().create(InterfaceCityList.class);
        Call<ClsCityListParams> objState = interfaceCityList.interfaceCall(stateID);

        Log.e("--URL--", "objState: " + objState.request().url());


        final ProgressDialog pd =
                ClsGlobal._prProgressDialog(RegistrationActivity.this, "Waiting for city...", true);
        pd.show();


        objState.enqueue(new Callback<ClsCityListParams>() {
            @Override
            public void onResponse(Call<ClsCityListParams> call, Response<ClsCityListParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    try {

                        List<ClsCityList> liveCityList = response.body().getData();

                        if (liveCityList != null && liveCityList.size() != 0) {
                            lstClsCityLists.addAll(liveCityList);
                        }

                        fillCityAdp();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(RegistrationActivity.this, "Terms and Condition not found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ClsCityListParams> call, Throwable t) {

            }
        });

    }

    void fillCountryAdp() {
        if (lstClsCountryLists != null && lstClsCountryLists.size() != 0) {
            lstStringWithTags = new ArrayList<>();
            for (ClsCountryList obj : lstClsCountryLists) {
                lstStringWithTags.add(new StringWithTag(obj.getCountryName()
                        , String.valueOf(obj.getCountryID())));
            }

            ArrayAdapter<StringWithTag> dataAdapter = new ArrayAdapter<StringWithTag>(
                    RegistrationActivity.this, R.layout.spinner_signup,
                    lstStringWithTags) {

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);

                    return view;
                }
            };
            dataAdapter.setDropDownViewResource(R.layout.spinner_signup);
            sp_country.setAdapter(dataAdapter);

        }
    }

    void fillStateAdp() {
        if (lstClsStateLists != null && lstClsStateLists.size() != 0) {
            lstStringWithTags = new ArrayList<>();
            for (ClsStateList obj : lstClsStateLists) {
                lstStringWithTags.add(new StringWithTag(obj.getStateName()
                        , String.valueOf(obj.getStateID())));
            }

            ArrayAdapter<StringWithTag> stateAdapter = new ArrayAdapter<StringWithTag>(
                    RegistrationActivity.this, R.layout.spinner_signup,
                    lstStringWithTags) {
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;

                    return view;
                }
            };
            stateAdapter.setDropDownViewResource(R.layout.spinner_signup);
            sp_state.setAdapter(stateAdapter);

        }
    }

    void fillCityAdp() {
        if (lstClsCityLists != null && lstClsCityLists.size() != 0) {
            lstStringWithTags = new ArrayList<>();
            for (ClsCityList obj : lstClsCityLists) {
                lstStringWithTags.add(new StringWithTag(obj.getCityName()
                        , String.valueOf(obj.getCityName())));
            }

            ArrayAdapter<StringWithTag> cityAdapter = new ArrayAdapter<StringWithTag>(
                    RegistrationActivity.this, R.layout.spinner_signup,
                    lstStringWithTags) {
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);


                    return view;
                }
            };
            cityAdapter.setDropDownViewResource(R.layout.spinner_signup);
            sp_city.setAdapter(cityAdapter);

        }
    }


    private void getRegistrationTermsAPI() {


        InterfaceRegistrationTerms interfaceRegistrationTerms =
                ApiClient.getRetrofitInstance().create(InterfaceRegistrationTerms.class);

        Call<ClsRegistrationTermsParams> obj = interfaceRegistrationTerms.value(ClsGlobal.AppName);


        Log.e("--URL--", "Terms: " + obj.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(RegistrationActivity.this, "Waiting for terms...", true);
        pd.show();


        obj.enqueue(new Callback<ClsRegistrationTermsParams>() {
            @Override
            public void onResponse(Call<ClsRegistrationTermsParams> call, Response<ClsRegistrationTermsParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    lstClsRegistrationTermsLists = new ArrayList<ClsRegistrationTermsList>();
                    lstClsRegistrationTermsLists = response.body().getData();
                    String _response = response.body().getSuccess();

                    if (_response.equals("1")) {
                        for (ClsRegistrationTermsList obj : lstClsRegistrationTermsLists) {
                            reg_terms = obj.getTermsFileUrl();
                            Log.e("reg_terms", reg_terms);
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsRegistrationTermsParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();

                Log.d("--State--", "TermsThrow: " + t.getMessage());
            }
        });
    }

}