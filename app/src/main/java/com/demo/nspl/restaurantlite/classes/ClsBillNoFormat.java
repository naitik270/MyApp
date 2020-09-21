package com.demo.nspl.restaurantlite.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.AppPackageName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Bill_No_Format_ApplyTax_Preferences;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Bill_No_Format_Tax_Not_Apply_Preferences;

public class ClsBillNoFormat {

    String Bill_Start_Form= "1",Prefix= "",Suffix = ""
            ,Bill_no_length= "1",Separator = "",Financial_Year_format=""
            ,Month_format="", Reset_Counter_Name = "",Year_format= "",BillNo_Format = "Counter"
            ,resetCounter = "",current_bill_counter = "0",
            FY_From_Date ="",FY_To_Date ="",FinancialYear=""
            ,Selected_Month_rb ="",Selected_Reset_Counter_rb = ""
            ,Selected_Separator_rd= "",Selected_Financial_Year_rb = ""
            ,Selected_Year_rb = "";


    boolean Financial_Year ,Month,Year,ApplybillFormat = false;

    int Month_rb_id = 0,Reset_Counter_rb_id = 0,
            Separator_rb_id = 0,Financial_Year_rb_id = 0,Year_rb_id = 0;

    public String getSelected_Month_rb() {
        return Selected_Month_rb;
    }

    public void setSelected_Month_rb(String selected_Month_rb) {
        Selected_Month_rb = selected_Month_rb;
    }

    public String getSelected_Reset_Counter_rb() {
        return Selected_Reset_Counter_rb;
    }

    public void setSelected_Reset_Counter_rb(String selected_Reset_Counter_rb) {
        Selected_Reset_Counter_rb = selected_Reset_Counter_rb;
    }

    public String getSelected_Separator_rd() {
        return Selected_Separator_rd;
    }

    public void setSelected_Separator_rd(String selected_Separator_rd) {
        Selected_Separator_rd = selected_Separator_rd;
    }

    public String getSelected_Financial_Year_rb() {
        return Selected_Financial_Year_rb;
    }

    public void setSelected_Financial_Year_rb(String selected_Financial_Year_rb) {
        Selected_Financial_Year_rb = selected_Financial_Year_rb;
    }

    public String getSelected_Year_rb() {
        return Selected_Year_rb;
    }

    public void setSelected_Year_rb(String selected_Year_rb) {
        Selected_Year_rb = selected_Year_rb;
    }

    public String getFY_From_Date() {
        return FY_From_Date;
    }

    public void setFY_From_Date(String FY_From_Date) {
        this.FY_From_Date = FY_From_Date;
    }

    public String getFY_To_Date() {
        return FY_To_Date;
    }

    public void setFY_To_Date(String FY_To_Date) {
        this.FY_To_Date = FY_To_Date;
    }

    public String getFinancialYear() {
        return FinancialYear;
    }

    public void setFinancialYear(String financialYear) {
        FinancialYear = financialYear;
    }

    public String getCurrent_bill_counter() {
        return current_bill_counter;
    }

    public void setCurrent_bill_counter(String current_bill_counter) {
        this.current_bill_counter = current_bill_counter;
    }

    public boolean isApplybillFormat() {
        return ApplybillFormat;
    }

    public void setApplybillFormat(boolean applybillFormat) {
        ApplybillFormat = applybillFormat;
    }

    public String getResetCounter() {
        return resetCounter;
    }

    public void setResetCounter(String resetCounter) {
        this.resetCounter = resetCounter;
    }


    public String getBillNo_Format() {
        return BillNo_Format;
    }

    public void setBillNo_Format(String billNo_Format) {
        BillNo_Format = billNo_Format;
    }

    public int getYear_rb_id() {
        return Year_rb_id;
    }

    public void setYear_rb_id(int year_rb_id) {
        Year_rb_id = year_rb_id;
    }

    public String getYear_format() {
        return Year_format;
    }

    public void setYear_format(String year_format) {
        Year_format = year_format;
    }

    public boolean isYear() {
        return Year;
    }

    public void setYear(boolean year) {
        Year = year;
    }

    public String getBill_Start_Form() {
        return Bill_Start_Form;
    }

    public void setBill_Start_Form(String bill_Start_Form) {
        Bill_Start_Form = bill_Start_Form;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public String getSuffix() {
        return Suffix;
    }

    public void setSuffix(String suffix) {
        Suffix = suffix;
    }

    public String getBill_no_length() {
        return Bill_no_length;
    }

    public void setBill_no_length(String bill_no_length) {
        Bill_no_length = bill_no_length;
    }

    public String getSeparator() {
        return Separator;
    }

    public void setSeparator(String separator) {
        Separator = separator;
    }

    public String getFinancial_Year_format() {
        return Financial_Year_format;
    }

    public void setFinancial_Year_format(String financial_Year_format) {
        Financial_Year_format = financial_Year_format;
    }

    public String getMonth_format() {
        return Month_format;
    }

    public void setMonth_format(String month_format) {
        Month_format = month_format;
    }

    public String getReset_Counter_Name() {
        return Reset_Counter_Name;
    }

    public void setReset_Counter_Name(String reset_Counter_Name) {
        Reset_Counter_Name = reset_Counter_Name;
    }

    public boolean isFinancial_Year() {
        return Financial_Year;
    }

    public void setFinancial_Year(boolean financial_Year) {
        Financial_Year = financial_Year;
    }

    public boolean isMonth() {
        return Month;
    }

    public void setMonth(boolean month) {
        Month = month;
    }

    public int getMonth_rb_id() {
        return Month_rb_id;
    }

    public void setMonth_rb_id(int month_rb_id) {
        Month_rb_id = month_rb_id;
    }

    public int getReset_Counter_rb_id() {
        return Reset_Counter_rb_id;
    }

    public void setReset_Counter_rb_id(int reset_Counter_rb_id) {
        Reset_Counter_rb_id = reset_Counter_rb_id;
    }

    public int getSeparator_rb_id() {
        return Separator_rb_id;
    }

    public void setSeparator_rb_id(int separator_rb_id) {
        Separator_rb_id = separator_rb_id;
    }

    public int getFinancial_Year_rb_id() {
        return Financial_Year_rb_id;
    }

    public void setFinancial_Year_rb_id(int financial_Year_rb_id) {
        Financial_Year_rb_id = financial_Year_rb_id;
    }



    public static ClsBillNoFormat getSettingApplyTaxBillNo(Context c, String mode) {

        ClsBillNoFormat clsBillNoFormat = new ClsBillNoFormat();
        SharedPreferences mPreferences;
        if (mode.equalsIgnoreCase("YES")) { // Tax apply.

            File f = new File(
                    "/data/data/" + AppPackageName + "/shared_prefs/" +
                            Bill_No_Format_ApplyTax_Preferences + ".xml");
            if (f.exists()) {
                mPreferences = c.getSharedPreferences(
                        Bill_No_Format_ApplyTax_Preferences,
                        MODE_PRIVATE);

                clsBillNoFormat = set(mPreferences, clsBillNoFormat);

                Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
            } else {
                Log.d("TAG", "Setup default preferences");

//                clearBillFormat(c,"ApplyTax");
//                mPreferences = c.getSharedPreferences(
//                        Bill_No_Format_ApplyTax_Preferences,
//                        MODE_PRIVATE);
//
//                return set(mPreferences,clsBillNoFormat);

                return clsBillNoFormat;
            }
        } else if (mode.equalsIgnoreCase("No")) { // Tax not apply.
            File f = new File(
                    "/data/data/" + AppPackageName + "/shared_prefs/"
                            + Bill_No_Format_Tax_Not_Apply_Preferences + ".xml");
            if (f.exists()) {
                mPreferences = c.getSharedPreferences(
                        Bill_No_Format_Tax_Not_Apply_Preferences,
                        MODE_PRIVATE);

//            Log.e("Check", mPreferences.getString("Bill Start Form", ""));

//            clsBillNoFormat.setSettingApply(true);

                clsBillNoFormat = set(mPreferences, clsBillNoFormat);


                Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
            } else {
                Log.d("TAG", "Setup default preferences");


                return clsBillNoFormat;
            }
        }

        return clsBillNoFormat;
    }

    public static ClsBillNoFormat set(SharedPreferences mPreferences, ClsBillNoFormat clsBillNoFormat) {

        clsBillNoFormat.setPrefix(mPreferences.getString("Prefix", ""));
        clsBillNoFormat.setSuffix(mPreferences.getString("Suffix", ""));

        clsBillNoFormat.setFinancial_Year(mPreferences.getBoolean("Financial Year", false));
        clsBillNoFormat.setFinancial_Year_rb_id(mPreferences.getInt("Financial Year rb_id",
                1));
        clsBillNoFormat.setFinancial_Year_format(mPreferences.getString("Financial Year format",
                ""));

        clsBillNoFormat.setMonth(mPreferences.getBoolean("Month", false));
        clsBillNoFormat.setMonth_format(mPreferences.getString("Month format", ""));
        clsBillNoFormat.setMonth_rb_id(mPreferences.getInt("Month rb_id", 0));

        clsBillNoFormat.setYear(mPreferences.getBoolean("Year", false));
        clsBillNoFormat.setYear_format(mPreferences.getString("Year format", ""));
        clsBillNoFormat.setYear_rb_id(mPreferences.getInt("Year rb_id", 0));

        clsBillNoFormat.setSeparator(mPreferences.getString("Separator", ""));
        clsBillNoFormat.setSeparator_rb_id(mPreferences.getInt("Separator rb_id", 0));

        clsBillNoFormat.setBill_Start_Form(mPreferences.getString("Bill Start Form", "1"));
        clsBillNoFormat.setBill_no_length(mPreferences.getString("Bill no length", "1"));

        clsBillNoFormat.setReset_Counter_rb_id(
                mPreferences.getInt("Reset Counter rb_id", 1));
        clsBillNoFormat.setReset_Counter_Name(mPreferences.getString("Reset Counter Name", ""));
        clsBillNoFormat.setBillNo_Format(mPreferences.getString("Bill No Format", "Counter"));

        Log.e("Check", "Reset_Counter:- " + mPreferences.getString("Reset_Counter", ""));

        clsBillNoFormat.setResetCounter(mPreferences.getString("Reset_Counter", "0"));

        clsBillNoFormat.setApplybillFormat(mPreferences.getBoolean("ApplyFormat", false));

        clsBillNoFormat.setFY_From_Date(mPreferences.getString("FY_From_Date", ""));
        clsBillNoFormat.setFY_To_Date(mPreferences.getString("FY_To_Date", ""));
        clsBillNoFormat.setFinancialYear(mPreferences.getString("FinancialYear", ""));


        clsBillNoFormat.setSelected_Month_rb(mPreferences.getString(
                "Selected_Month_rb", ""));

        clsBillNoFormat.setSelected_Reset_Counter_rb(mPreferences.getString(
                "Selected_Reset_Counter_rb", ""));
        clsBillNoFormat.setSelected_Separator_rd(mPreferences.getString(
                "Selected_Separator_rd", ""));
        clsBillNoFormat.setSelected_Financial_Year_rb(mPreferences.getString(
                "Selected_Financial_Year_rb", ""));
        clsBillNoFormat.setSelected_Year_rb(mPreferences.getString(
                "Selected_Year_rb", ""));


//        clsBillNoFormat.setFY_To_Date(mPreferences.getBoolean("ApplyFormat", false));
//        clsBillNoFormat.setCurrent_bill_counter(mPreferences.getString("current_bill_counter", "0"));
        return clsBillNoFormat;
    }
}
