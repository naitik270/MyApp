package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.InputFilterMinMax;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsBillNoFormat;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.MonthFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.formatsFinancialYear;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getSelectedIndexRadioGroup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.get_Current_Day_Month_Yearly;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.setCheckIndexRadioGroup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.yearFormat;


public class Bill_No_Format_No_TaxFragment extends Fragment implements TextWatcher {


    private TextView txt_preview, id_separator, from_date, to_date;
    private RadioGroup rg_Financial_Year, rg_Year, rg_Separator, rg_Reset_Counter, rg_month;
    private EditText edt_Prefix, edt_Suffix, edt_bill_start_form, edt_bill_no_length;
    private CheckBox cb_FY, cb_Year, cb_Month;
    private RadioButton rbCCYYdashCCnYY, rbYYdashnYY, rbCCYYnYY, rbYYnYY,
            rbCCYYdashnYY, rbCCYYCCnYY, rbCCYY, rbYY, rb_allow_zero, rb_No_zero,rb_forward_slash
            ,rb_underscore,rb_minus,rb_dot,rb_back_slash,rb_none,
            rb_never,rb_Daily,rb_monthly,rb_Yearly;

    Calendar c = Calendar.getInstance();
    int year, month, cday;
    String BillNo_preview = "";
    ClsBillNoFormat clsBillNoFormat = new ClsBillNoFormat();

    public Bill_No_Format_No_TaxFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill__no__format__no__tax,
                container, false);

        // TextView
        txt_preview = view.findViewById(R.id.txt_preview);
//        id_month = view.findViewById(R.id.id_month);
        id_separator = view.findViewById(R.id.id_separator);

        // EditText
        edt_Prefix = view.findViewById(R.id.edt_Prefix);
        edt_Suffix = view.findViewById(R.id.edt_Suffix);
        edt_bill_start_form = view.findViewById(R.id.edt_bill_start_form);
        edt_bill_no_length = view.findViewById(R.id.edt_bill_no_length);

        // RadioGroup
        rg_Financial_Year = view.findViewById(R.id.rg_Financial_Year);
        rg_Separator = view.findViewById(R.id.rg_Separator);
        rg_Year = view.findViewById(R.id.rg_Year);
        rg_Reset_Counter = view.findViewById(R.id.rg_Reset_Counter);
        rg_month = view.findViewById(R.id.rg_month);

        // RadioButton
        rbCCYYdashCCnYY = view.findViewById(R.id.rbCCYYdashCCnYY); //2019-2020
        rbYYdashnYY = view.findViewById(R.id.rbYYdashnYY);  //19-20
        rbCCYYnYY = view.findViewById(R.id.rbCCYYnYY); //201920
        rbYYnYY = view.findViewById(R.id.rbYYnYY);  // 1920
        rbCCYYdashnYY = view.findViewById(R.id.rbCCYYdashnYY); // 2019-20
        rbCCYYCCnYY = view.findViewById(R.id.rbCCYYCCnYY); //20192020
        // Year RadioButton
        rbCCYY = view.findViewById(R.id.rbCCYY); // 2019
        rbYY = view.findViewById(R.id.rbYY); // 19
        rb_allow_zero = view.findViewById(R.id.rb_allow_zero); // 19
        rb_No_zero = view.findViewById(R.id.rb_No_zero); // 19


        // CheckBox
        cb_FY = view.findViewById(R.id.cb_FY);
        cb_Year = view.findViewById(R.id.cb_Year);
        cb_Month = view.findViewById(R.id.cb_Month);

        from_date = view.findViewById(R.id.from_date);
        to_date = view.findViewById(R.id.to_date);


//        rb_forward_slash = view.findViewById(R.id.rb_forward_slash);
//        rb_underscore = view.findViewById(R.id.rb_underscore);
//        rb_minus = view.findViewById(R.id.rb_minus);
//        rb_dot = view.findViewById(R.id.rb_dot);
//        rb_back_slash = view.findViewById(R.id.rb_back_slash);
//        rb_none = view.findViewById(R.id.rb_none);
//
//        rb_never = view.findViewById(R.id.rb_never);
//        rb_Daily = view.findViewById(R.id.rb_Daily);
//        rb_monthly = view.findViewById(R.id.rb_monthly);
//        rb_Yearly = view.findViewById(R.id.rb_Yearly);


        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        cday = c.get(Calendar.DAY_OF_MONTH);


        rbCCYYdashCCnYY.setTag("CCYYdashCCnYY");
        rbYYdashnYY.setTag("YYdashnYY");
        rbCCYYnYY.setTag("CCYYnYY");
        rbYYnYY.setTag("YYnYY");
        rbCCYYdashnYY.setTag("CCYYdashnYY");
        rbCCYYCCnYY.setTag("CCYYCCnYY");

        rbCCYY.setText(yearFormat("YYYY"));
        rbCCYY.setTag("CCYY");

        rbYY.setText(yearFormat("YY"));
        rbYY.setTag("YY");

        rb_allow_zero.setText(MonthFormat("MM"));
        rb_allow_zero.setTag("MM");

        rb_No_zero.setText(MonthFormat("M"));
        rb_No_zero.setTag("M");


//        rb_forward_slash.setTag("/");
//        rb_underscore.setTag("_");
//        rb_minus.setTag("-");
//        rb_dot.setTag(".");
//        rb_back_slash.setTag("\\\\");
//        rb_none.setTag("None");
//
//        rb_never.setTag("Never");
//        rb_Daily.setTag("Daily");
//        rb_monthly.setTag("Monthly");
//        rb_Yearly.setTag("Yearly");
//        id_month.setText(String.valueOf(month));

        edt_bill_no_length.setFilters(new InputFilter[]{new InputFilterMinMax("1", "6")});



        getSettingApplyTax();



        main();

        SetBillNoPreview();


        return view;
    }


    private void main() {

        if (!cb_FY.isChecked()) {
            to_date.setEnabled(false);
            from_date.setEnabled(false);

            HideShowFY(false);
//            Disable_Or_Enable_RG_Button(rg_Financial_Year,
//                    false);


        }

        if (!cb_Year.isChecked()) {
            Disable_Or_Enable_RG_Button(rg_Year, false);
        }

        if (!cb_Month.isChecked()) {
//            id_month.setEnabled(false);
            Disable_Or_Enable_RG_Button(rg_month, false);
        }

        cb_FY.setOnCheckedChangeListener((buttonView, isChecked) -> {

            to_date.setEnabled(isChecked);
            from_date.setEnabled(isChecked);

            HideShowFY(isChecked);

//            Disable_Or_Enable_RG_Button(rg_Financial_Year, isChecked);

            SetBillNoPreview();
        });

        cb_Year.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Disable_Or_Enable_RG_Button(rg_Year, isChecked);
            SetBillNoPreview();
        });

        edt_Prefix.addTextChangedListener(this);
        edt_Suffix.addTextChangedListener(this);
        edt_bill_start_form.addTextChangedListener(this);
        edt_bill_no_length.addTextChangedListener(this);


        rg_Financial_Year.setOnCheckedChangeListener((group, checkedId) -> {

            SetBillNoPreview();

        });

        rg_Year.setOnCheckedChangeListener((group, checkedId) -> {

            SetBillNoPreview();
        });

        cb_Month.setOnCheckedChangeListener((buttonView, isChecked) -> {

            Disable_Or_Enable_RG_Button(rg_month, isChecked);
//            id_month.setEnabled(isChecked);
            SetBillNoPreview();
        });

        rg_Separator.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SetBillNoPreview();
        });

        rg_month.setOnCheckedChangeListener((group, checkedId) -> {
            SetBillNoPreview();
        });

        from_date.setOnClickListener(v -> {

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    (view, year, month, day) -> {
                        c.set(year, month, day);
                        String date = new SimpleDateFormat("dd/MM/yyyy")
                                .format(c.getTime());
                        from_date.setText(date);
                        HideShowFY(true);
                        SetBillNoPreview();
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        cday = c.get(Calendar.DAY_OF_MONTH);
                    }, year, month, cday);


            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });

        to_date.setOnClickListener(v -> {

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    (view, year, month, day) -> {
                        c.set(year, month, day);
                        String date = new SimpleDateFormat("dd/MM/yyyy")
                                .format(c.getTime());
                        to_date.setText(date);
                        HideShowFY(true);
                        SetBillNoPreview();
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        cday = c.get(Calendar.DAY_OF_MONTH);
                    }, year, month, cday);


            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR),
                    d.get(Calendar.MONTH),
                    d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });
        Set_FY_Radio_button_Text();

    }


    private void Disable_Or_Enable_RG_Button(RadioGroup radioGroup,
                                             boolean enable_or_disable) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enable_or_disable);
        }
    }

    private String GetSelected_rg_value(RadioGroup radioGroup, String mode) {
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            int id = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(id);
            if (radioButton != null) {
                int radioId = radioGroup.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                String selection = (String) btn.getText();
                Log.e("selection", selection);

                if (mode.equalsIgnoreCase("")) {
                    return selection;
                } else if (mode.equalsIgnoreCase("GetTag")) {
                    return btn.getTag().toString();
                }
            }

        }
        return "";
    }


    private void SetBillNoPreview() {

        StringBuilder stringBuilder = new StringBuilder();

        if (!getPrefix().equalsIgnoreCase("")) {
            stringBuilder.append(getPrefix());
            stringBuilder.append(getSeparator());
        }

        if (!getFinancialYear().equalsIgnoreCase("")) {
            stringBuilder.append(getFinancialYear());
            stringBuilder.append(getSeparator());
        }


        if (!getYear().equalsIgnoreCase("")) {
            stringBuilder.append(getYear());
            stringBuilder.append(getSeparator());
        }

        if (!getMonth().equalsIgnoreCase("")) {
            stringBuilder.append(getMonth());
            stringBuilder.append(getSeparator());
        }

        if (!getBill_Counter().equalsIgnoreCase("")
                && !getSuffix().equalsIgnoreCase("")) {
            stringBuilder.append(getBill_Counter());
            stringBuilder.append(getSeparator());
        } else {
            stringBuilder.append(getBill_Counter());
        }


        if (!getSuffix().equalsIgnoreCase("")) {
            stringBuilder.append(getSuffix());
        }

        BillNo_preview = stringBuilder.toString();

        Log.e("selection", "BillNo_preview:- " + BillNo_preview);
        txt_preview.setText(BillNo_preview);

    }

    private String getPrefix() {
        if (!edt_Prefix.getText().toString().equalsIgnoreCase("")
                && edt_Prefix.getText().toString().length() > 0) {
            return edt_Prefix.getText().toString().trim();
        } else {
            return "";
        }

    }

    private String getSeparator() {
        return GetSelected_rg_value(rg_Separator, "")
                .equalsIgnoreCase("") || GetSelected_rg_value(rg_Separator, "")
                .equalsIgnoreCase("None") ? "" : GetSelected_rg_value(rg_Separator, "");

    }

    private String getFinancialYear() {
        if (cb_FY.isChecked()) {
            return GetSelected_rg_value(rg_Financial_Year, "");
        } else {
            return "";
        }

    }

    private String getYear() {
        if (cb_Year.isChecked()) {
            return GetSelected_rg_value(rg_Year, "");
        } else {
            return "";
        }
    }

    private String getMonth() {
        if (cb_Month.isChecked()) {
//            return id_month.getText().toString();
            return GetSelected_rg_value(rg_month, "");
        } else {
            return "";
        }
    }

    private String getBill_Counter() {

        String string = "";

        // Set length edt_bill_start_form edit text.
        if (!edt_bill_no_length.getText().toString().equalsIgnoreCase("")
                && edt_bill_no_length.getText().toString().length() > 0) {
            edt_bill_start_form.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(edt_bill_no_length.getText()
                            .toString().trim().equalsIgnoreCase("") ? 1 :
                            Integer.valueOf(edt_bill_no_length.getText().toString().trim()))});

            string = String.format("%0" + (edt_bill_no_length.getText()
                            .toString().trim()) + "d",
                    Integer.parseInt(edt_bill_start_form.getText().toString().trim().equalsIgnoreCase("") ? "1"
                            : edt_bill_start_form.getText().toString().trim()));
        }


        if (!edt_bill_start_form.getText().toString().equalsIgnoreCase("")
                && edt_bill_start_form.getText().toString().length() > 0) {
            return string;

        } else {
            return string;
        }
    }

    private String getSuffix() {

        if (!edt_Suffix.getText().toString().equalsIgnoreCase("")
                && edt_Suffix.getText().toString().length() > 0) {
            return edt_Suffix.getText().toString().trim();
        } else {
            return "";
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bill_no_format_menu_at, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.recent_order) {
            saveSettingApplyTax();
        }

        if (id == R.id.clear) {
            ClsGlobal.clearBillFormat(getActivity(), "Tax Not Apply");
            Toast.makeText(getActivity(), "Bill No format clear successfully", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }


        return super.onOptionsItemSelected(item);
    }


    private void saveSettingApplyTax() {

        if (txt_preview.getText().toString().trim().length() > 16) {
            Toast.makeText(getActivity(), "Bill No should not be Greater than 16.", Toast.LENGTH_LONG).show();
        }else {
            if (!edt_bill_start_form.getText().toString().trim().equalsIgnoreCase("")) {
                ClsBillNoFormat clsBillNoFormat = new ClsBillNoFormat();

                clsBillNoFormat.setPrefix(edt_Prefix.getText().toString().trim());
                clsBillNoFormat.setSuffix(edt_Suffix.getText().toString().trim());

                if (cb_FY.isChecked()) {
                    if (!from_date.getText().toString().equalsIgnoreCase("FROM DATE") &&
                            !to_date.getText().toString().equalsIgnoreCase("TO DATE")) {

                        clsBillNoFormat.setFinancial_Year(cb_FY.isChecked());
                        clsBillNoFormat.setFinancial_Year_format(
                                GetSelected_rg_value(rg_Financial_Year, "GetTag"));
//                        clsBillNoFormat.setFinancial_Year_rb_id(
//                                rg_Financial_Year.getCheckedRadioButtonId());

                        clsBillNoFormat.setSelected_Financial_Year_rb(
                                getSelectedIndexRadioGroup(rg_Financial_Year));
                    }else {
                        Toast.makeText(getActivity(),"Select Financial Year!",Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                clsBillNoFormat.setMonth(cb_Month.isChecked());
                clsBillNoFormat.setMonth_format(GetSelected_rg_value(rg_month, "GetTag"));
//                clsBillNoFormat.setMonth_rb_id(rg_month.getCheckedRadioButtonId());
                clsBillNoFormat.setSelected_Month_rb(getSelectedIndexRadioGroup(rg_month));

                clsBillNoFormat.setYear(cb_Year.isChecked());
                clsBillNoFormat.setYear_format(GetSelected_rg_value(rg_Year, "GetTag"));
//                clsBillNoFormat.setYear_rb_id(rg_Year.getCheckedRadioButtonId());
                clsBillNoFormat.setSelected_Year_rb(getSelectedIndexRadioGroup(rg_Year));

                clsBillNoFormat.setSeparator(GetSelected_rg_value(rg_Separator, ""));
//                clsBillNoFormat.setSeparator_rb_id(rg_Separator.getCheckedRadioButtonId());
                clsBillNoFormat.setSelected_Separator_rd(getSelectedIndexRadioGroup(rg_Separator));

                clsBillNoFormat.setBill_Start_Form(edt_bill_start_form.getText().toString().trim());
                clsBillNoFormat.setBill_no_length(edt_bill_no_length.getText().toString().trim()
                        .equalsIgnoreCase("") ? "1" : edt_bill_no_length.getText().toString().trim());

                clsBillNoFormat.setReset_Counter_Name(GetSelected_rg_value(rg_Reset_Counter, ""));
//                clsBillNoFormat.setReset_Counter_rb_id(rg_Reset_Counter.getCheckedRadioButtonId());
                clsBillNoFormat.setSelected_Reset_Counter_rb(getSelectedIndexRadioGroup(rg_Reset_Counter));

                // We are saving current day,month,year or never any one.
                // For ex:- Current Day == 19 we will only save 19 to ResetCounter.
                // For ex:- Current month == 12 we will only save 12 to ResetCounter.
                // For ex:- Current year == 2019 we will only save 2019 to ResetCounter.
                // For ex:- Never means the user have not set reset counter.And we will save Never.
                clsBillNoFormat.setResetCounter(
                        get_Current_Day_Month_Yearly(
                                GetSelected_rg_value(rg_Reset_Counter, "")));


                // Saving bill no format.
                StringBuilder stringBuilder = new StringBuilder();

                if (!getPrefix().equalsIgnoreCase("")) {
                    stringBuilder.append(getPrefix());
                    stringBuilder.append(getSeparator());
                }

                if (!getFinancialYear().equalsIgnoreCase("")) {
                    stringBuilder.append(GetSelected_rg_value(rg_Financial_Year, "GetTag"));
                    stringBuilder.append(getSeparator());
                }

                if (!getYear().equalsIgnoreCase("")) {
                    stringBuilder.append(GetSelected_rg_value(rg_Year, "GetTag"));
                    stringBuilder.append(getSeparator());
                }

                if (!getMonth().equalsIgnoreCase("")) {
                    stringBuilder.append(GetSelected_rg_value(rg_month, "GetTag"));
                    stringBuilder.append(getSeparator());
                }

                if (!getBill_Counter().equalsIgnoreCase("")
                        && !getSuffix().equalsIgnoreCase("")) {
                    stringBuilder.append("Counter");
                    stringBuilder.append(getSeparator());
                } else {
                    stringBuilder.append("Counter");
                }

                if (!getSuffix().equalsIgnoreCase("")) {
                    stringBuilder.append(getSuffix());
                }

                clsBillNoFormat.setBillNo_Format(stringBuilder.toString());

                clsBillNoFormat.setApplybillFormat(true);


                if (!from_date.getText().toString().equalsIgnoreCase("FROM DATE") &&
                        !to_date.getText().toString().equalsIgnoreCase("TO DATE")) {

                    Log.e("Check", "from_date to_date: "+from_date.getText().toString());

                    clsBillNoFormat.setFY_From_Date(from_date.getText().toString());
                    clsBillNoFormat.setFY_To_Date(to_date.getText().toString());

                    clsBillNoFormat.setFinancialYear(
                            from_date.getText().toString().split("/")[2] + "-"
                                    + to_date.getText().toString().split("/")[2]);
                }


                ClsGlobal.saveSettingApplyTax_Bill_No(clsBillNoFormat, getActivity(),
                        "Tax Not Apply");


                int result = ClsInventoryOrderMaster.UpdateOrderSequence(getActivity()
                        , String.valueOf(Integer.valueOf(
                                edt_bill_start_form.getText().toString().trim()) - 1),
                        "NO");

                if (result > 0){
                    Toast.makeText(getActivity(), "Bill No format applied successfully", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(), "Bill No format Not applied.", Toast.LENGTH_LONG).show();
                }


            } else {
                edt_bill_start_form.setError("Must not be empty!");
                Toast.makeText(getActivity(), "Bill start form must not be empty!", Toast.LENGTH_LONG).show();
            }
        }

    }


    private void getSettingApplyTax() {
        clsBillNoFormat = ClsGlobal.getSettingApplyTaxBillNo(getActivity(),
                "No");
        if (clsBillNoFormat != null) {

            Log.e("Check", "!= null");
            Gson gson = new Gson();
            String jsonInString = gson.toJson(clsBillNoFormat);

            Log.e("--Sales--", "SalesDetailsFragment: " + jsonInString);
//            Log.e("Check", "!= null: - " + clsBillNoFormat.getPrefix());

            // if the current data is change then change the
            // current date and year save and display.
            formatsFinancialYear(clsBillNoFormat.getFinancial_Year_format(),
                    clsBillNoFormat, "NO", getActivity());


            if (!clsBillNoFormat.getFY_From_Date().equalsIgnoreCase("")
                    && !clsBillNoFormat.getFY_From_Date().equalsIgnoreCase("")) {
                Log.e("Check", "clsBillNoFormat: getFY_From_Date " + clsBillNoFormat.getFY_From_Date());
                from_date.setText(clsBillNoFormat.getFY_From_Date());
                to_date.setText(clsBillNoFormat.getFY_To_Date());
            }

            Set_FY_Radio_button_Text();
            edt_Prefix.setText(clsBillNoFormat.getPrefix());
            edt_Suffix.setText(clsBillNoFormat.getSuffix());
            cb_FY.setChecked(clsBillNoFormat.isFinancial_Year());


            setCheckIndexRadioGroup(rg_Financial_Year,clsBillNoFormat);


            cb_Month.setChecked(clsBillNoFormat.isMonth());

//            rg_month.check(clsBillNoFormat.getMonth_rb_id());
            setCheckIndexRadioGroup(rg_month,clsBillNoFormat);



            cb_Year.setChecked(clsBillNoFormat.isYear());

            setCheckIndexRadioGroup(rg_Year,clsBillNoFormat);


            setCheckIndexRadioGroup(rg_Separator,clsBillNoFormat);


            edt_bill_start_form.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(Integer.valueOf(clsBillNoFormat.getBill_no_length()
                            .equalsIgnoreCase("0") ? "1" : clsBillNoFormat.getBill_no_length()))});

            edt_bill_start_form.setText(clsBillNoFormat.getBill_Start_Form().equalsIgnoreCase("0")
                    ? "1" : clsBillNoFormat.getBill_Start_Form());

            edt_bill_no_length.setText(clsBillNoFormat.getBill_no_length());

            setCheckIndexRadioGroup(rg_Reset_Counter,clsBillNoFormat);

        } else {
            Log.e("Check", "== null");
        }

//        if (mPreferences.getString("Prefix",null) != null){
//            Log.e("Check", mPreferences.getString("Bill Start Form",""));
//
//            edt_Prefix.setText(mPreferences.getString("Prefix",""));
//            edt_Suffix.setText(mPreferences.getString("Suffix",""));
//
//            cb_FY.setChecked(mPreferences.getBoolean("Financial Year",false));
//            rg_Financial_Year.check(mPreferences.getInt("Financial Year rb_id",0));
//            cb_Month.setChecked(mPreferences.getBoolean("Month",false));
//            rg_Year.check(mPreferences.getInt("Month rb_id",1));
//            rg_Separator.check(mPreferences.getInt("Separator rb_id",1));
//
//
//            edt_bill_start_form.setFilters(new InputFilter[] {
//                    new InputFilter.LengthFilter(Integer.valueOf(mPreferences.getString("Bill no length", "")))});
//
//            edt_bill_start_form.setText(mPreferences.getString("Bill Start Form","1"));
//
//            edt_bill_no_length.setText(mPreferences.getString("Bill no length","1"));
//
//            rg_Reset_Counter.check(mPreferences.getInt("Reset Counter rb_id",1));
//
//        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (getActivity().getCurrentFocus() == edt_Prefix) {

            edt_bill_start_form.removeTextChangedListener(this);
            edt_bill_no_length.removeTextChangedListener(this);
            edt_Suffix.removeTextChangedListener(this);
            SetBillNoPreview();

            edt_bill_start_form.addTextChangedListener(this);
            edt_bill_no_length.addTextChangedListener(this);
            edt_Suffix.addTextChangedListener(this);

        }

        if (getActivity().getCurrentFocus() == edt_Suffix) {

            edt_bill_start_form.removeTextChangedListener(this);
            edt_bill_no_length.removeTextChangedListener(this);
            edt_Prefix.removeTextChangedListener(this);
            SetBillNoPreview();

            edt_bill_start_form.addTextChangedListener(this);
            edt_bill_no_length.addTextChangedListener(this);
            edt_Prefix.addTextChangedListener(this);

        }

        if (getActivity().getCurrentFocus() == edt_bill_start_form) {

            edt_bill_no_length.removeTextChangedListener(this);
            edt_Suffix.removeTextChangedListener(this);
            edt_Prefix.removeTextChangedListener(this);
            SetBillNoPreview();


            edt_bill_no_length.addTextChangedListener(this);
            edt_Prefix.addTextChangedListener(this);
            edt_Suffix.addTextChangedListener(this);

        }


        if (getActivity().getCurrentFocus() == edt_bill_no_length) {

            edt_bill_start_form.removeTextChangedListener(this);
            edt_Suffix.removeTextChangedListener(this);
            edt_Prefix.removeTextChangedListener(this);

            edt_bill_start_form.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(
                            edt_bill_no_length.getText().toString().equalsIgnoreCase("") ? 1
                                    : Integer.valueOf(edt_bill_no_length.getText().toString().trim()))});
            edt_bill_start_form.setText("1");
            SetBillNoPreview();

            edt_bill_start_form.addTextChangedListener(this);
            edt_Prefix.addTextChangedListener(this);
            edt_Suffix.addTextChangedListener(this);

        }


    }


    private void HideShowFY(boolean isCheck) {

        if (!isCheck) {
            Disable_Or_Enable_RG_Button(rg_Financial_Year,
                    false);

        } else {
            if (!from_date.getText().toString().equalsIgnoreCase("FROM DATE") &&
                    !to_date.getText().toString().equalsIgnoreCase("TO DATE")) {
                Log.e("Check", "From Date");
                Disable_Or_Enable_RG_Button(rg_Financial_Year,
                        isCheck);

                Set_FY_Radio_button_Text();

            } else {
                Disable_Or_Enable_RG_Button(rg_Financial_Year,
                        false);
            }
        }

    }

    private void Set_FY_Radio_button_Text() {
        String[] from = from_date.getText().toString().split("/");
        String[] toDate = to_date.getText().toString().split("/");

        if (from.length >= 3 && toDate.length >= 3) {
            rbCCYYdashCCnYY.setText(from[2] +
                    "-" + toDate[2]);


            rbYYdashnYY.setText(from[2].substring(2, 4) + "-"
                    + toDate[2].substring(2, 4));


            rbCCYYnYY.setText(from[2] + toDate[2].substring(2, 4));

            rbYYnYY.setText(from[2].substring(2, 4)
                    + toDate[2].substring(2, 4));


            rbCCYYdashnYY.setText(from[2] + "-" + toDate[2].substring(2, 4));


            rbCCYYCCnYY.setText(from[2] + toDate[2]);

        }


    }
}
