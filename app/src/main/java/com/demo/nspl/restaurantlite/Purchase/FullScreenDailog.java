package com.demo.nspl.restaurantlite.Purchase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Navigation_Drawer.FilterDialogFragment;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.gson.Gson;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.demo.nspl.restaurantlite.Purchase.PurchaseItemListActivity.lstClsProductListList;

public class FullScreenDailog extends DialogFragment {

    private ClsLayerItemMaster getCurrentObj;

    private ImageButton bt_close;

    private Double total_Amount = 0.0;
    private Double total_Amount_With_Tax = 0.0;
    private Double SGST, CGST, IGST;

    private TextView txt_item_code, txt_Title, txt_final_amt, txt_unit, txt_amount,
            txt_sgst, txt_cgst, txt_igst, txt_sgst_value, txt_cgst_value, txt_igst_value, txt_total_tax;
    private Button Btn_Add_To_Order;

    private LinearLayout hide_layout1;

    CheckBox ckb_apply_tax;
    LinearLayout ll_tax;
    EditText edt_qty, edt_discount, edit_Rate;
    ImageButton btn_done;
    Double total = 0.0;
    Double totalTaxValue = 0.0;
    Double finalAmount = 0.0;
    int _ID, VendorId, po_no;
    String date = "", vendor_name = "", bill_no = "", remark = "", billNo = "", _mode = "";

    boolean applyTax = false;

    public FullScreenDailog() {
        // Required empty public constructor
    }


    public FilterDialogFragment.CallbackResult callbackResult;

    public void setOnCallbackResult(final FilterDialogFragment.CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }


    public void setRequestCode(ClsLayerItemMaster currentObj, int _ID, String date, String billNo, int VendorId, String vendor_name, String bill_no, int po_no, String remark) {
        this.getCurrentObj = currentObj;
        this._ID = _ID;
        this.date = date;
        this.bill_no = bill_no;
        this.VendorId = VendorId;
        this.vendor_name = vendor_name;
        this.billNo = billNo;
        this.po_no = po_no;
        this.remark = remark;
    }

    public void setProductList(ClsLayerItemMaster _getCurrentObj, String _mode) {
        this.getCurrentObj = _getCurrentObj;
        this._mode = _mode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_screen_dialog, container, false);
        // Inflate the layout for this fragment

        bt_close = view.findViewById(R.id.bt_close);
        ll_tax = view.findViewById(R.id.ll_tax);

        txt_sgst = view.findViewById(R.id.txt_sgst);
        txt_cgst = view.findViewById(R.id.txt_cgst);
        txt_igst = view.findViewById(R.id.txt_igst);


        txt_sgst_value = view.findViewById(R.id.txt_sgst_value);
        txt_cgst_value = view.findViewById(R.id.txt_cgst_value);
        txt_igst_value = view.findViewById(R.id.txt_igst_value);
        txt_total_tax = view.findViewById(R.id.txt_total_tax);
        txt_total_tax.setTag(0.0);

        txt_amount = view.findViewById(R.id.txt_amount);
        edt_discount = view.findViewById(R.id.edt_discount);
        ckb_apply_tax = view.findViewById(R.id.ckb_apply_tax);
        txt_item_code = view.findViewById(R.id.txt_item_code);
        edt_qty = view.findViewById(R.id.edt_qty);
        txt_unit = view.findViewById(R.id.txt_unit);
        txt_Title = view.findViewById(R.id.txt_Title);

        edit_Rate = view.findViewById(R.id.edit_Rate);

        Btn_Add_To_Order = view.findViewById(R.id.Btn_Add_To_Order);
        btn_done = view.findViewById(R.id.btn_done);
        txt_final_amt = view.findViewById(R.id.txt_final_amt);
        txt_final_amt.setTag(0.0);
        hide_layout1 = view.findViewById(R.id.hide_layout1);


        hide_layout1.setVisibility(View.GONE);

        CGST = Double.valueOf(ClsGlobal.round(getCurrentObj.getCGST(), 2));
        SGST = Double.valueOf(ClsGlobal.round(getCurrentObj.getSGST(), 2));
        IGST = Double.valueOf(ClsGlobal.round(getCurrentObj.getIGST(), 2));

        txt_Title.setText(getCurrentObj.getITEM_NAME().toUpperCase());
        txt_item_code.setText(getCurrentObj.getITEM_CODE().toUpperCase());
        txt_unit.setText("UNIT: " + getCurrentObj.getUNIT_CODE().toUpperCase());

        txt_sgst.setText("SGST (" + ClsGlobal.round(SGST, 2) + "%)");
        txt_cgst.setText("CGST (" + ClsGlobal.round(CGST, 2) + "%)");
        txt_igst.setText("IGST (" + ClsGlobal.round(IGST, 2) + "%)");

        if (getCurrentObj.getTAX_APPLY() != null) {
            if (getCurrentObj.getTAX_APPLY().equalsIgnoreCase("NO")) {
                calculateTotalAmountWithTax();
            }
        }

        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                calculateAmount();

            }
        });

        edit_Rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateAmount();
            }
        });


        edt_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                calculateAmount();

            }
        });

        bt_close.setOnClickListener(v -> {
            dismiss();
            callbackResult.sendResult(300);
        });


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validation = checkValidation();
                if (validation == true) {
                    InsertPurchaseDetail();
                }
            }
        });


        Btn_Add_To_Order.setOnClickListener(v -> {

            if (!edit_Rate.getText().toString().equalsIgnoreCase("")) {
                if (ClsGlobal._OrderNo == null || ClsGlobal._OrderNo.isEmpty() || ClsGlobal._OrderNo.matches("")) {
                    ClsGlobal._OrderNo = String.valueOf(ClsInventoryOrderMaster.getNextOrderNo(getContext()));//ClsSales.getNextOrderNo();
                    // ClsGlobal._OrderNo = ClsGlobal.getRandom();
                }

                ClsInventoryOrderDetail Obj = new ClsInventoryOrderDetail();
                Obj.setOrderNo(ClsGlobal._OrderNo);
                Obj.setOrderID("0");
                Obj.setItemCode(getCurrentObj.getITEM_CODE());
                Obj.setItem(getCurrentObj.getITEM_NAME());
                Obj.setRate(getCurrentObj.getRATE_PER_UNIT());
                Obj.setUnit(getCurrentObj.getUNIT_CODE());
                Obj.setSaleRate(Double.valueOf(edit_Rate.getText().toString()));
                Obj.setQuantity(Double.valueOf(edt_qty.getText().toString()));
                Obj.setAmount(Double.valueOf(txt_final_amt.getText().toString()));
                Obj.setCGST(CGST);
                Obj.setSGST(SGST);
                Obj.setIGST(IGST);
                Obj.setSaveStatus("NO");


                Gson gson = new Gson();
                String jsonInString = gson.toJson(Obj);
                Log.d("--Obj--", "Obj: " + jsonInString);


                total_Amount_With_Tax = (total_Amount * (CGST + SGST + IGST)) / 100;
                Obj.setTotalTaxAmount(total_Amount_With_Tax);
                Obj.setGrandTotal(total_Amount_With_Tax + total_Amount);

                int result = ClsInventoryOrderDetail.InsertInventoryOrderDetail(Obj, getContext());

                if (result != 0) {
                    Toast.makeText(getContext(), "Order Added Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Error While Adding Order!", Toast.LENGTH_LONG).show();
                }

                getActivity().onBackPressed();

            } else {
                Toast.makeText(getContext(), "There is No Rate!", Toast.LENGTH_SHORT).show();
            }


        });

        ckb_apply_tax.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ll_tax.setVisibility(View.VISIBLE);
                applyTax = true;
            } else {
                ll_tax.setVisibility(View.GONE);
                applyTax = false;
            }
            calculateAmount();
        });

        return view;
    }

    void InsertPurchaseDetail() {

        ClsPurchaseMaster objClsPurchaseMaster = new ClsPurchaseMaster(getActivity());
        objClsPurchaseMaster.setPurchaseID(_ID);
        objClsPurchaseMaster.setPurchaseNo(po_no);
        objClsPurchaseMaster.setBillNO(billNo);
        objClsPurchaseMaster.setPurchaseDate(date);
        objClsPurchaseMaster.setVendorID(VendorId);
        objClsPurchaseMaster.setRemark(remark);
        objClsPurchaseMaster.setEntryDate(ClsGlobal.getEntryDate());

        ClsPurchaseDetail objClsPurchaseDetail = new ClsPurchaseDetail();
        objClsPurchaseDetail.setItemID(getCurrentObj.getLAYERITEM_ID());
        objClsPurchaseDetail.setItemCode(getCurrentObj.getITEM_CODE());
        objClsPurchaseDetail.setUnit(getCurrentObj.getUNIT_CODE());
        objClsPurchaseDetail.setRate(Double.parseDouble(edit_Rate.getText().toString().equalsIgnoreCase("")
                ? "0.00" : edit_Rate.getText().toString()));
        objClsPurchaseDetail.setQuantity(Double.parseDouble(edt_qty.getText().toString().equalsIgnoreCase("")
                ? "0.00" : edt_qty.getText().toString()));
        objClsPurchaseDetail.setTotalAmount(Double.parseDouble(txt_amount.getText().toString().equalsIgnoreCase("")
                ? "0.00" : txt_amount.getText().toString()));
        objClsPurchaseDetail.setNetAmount(Double.parseDouble(txt_amount.getText().toString().equalsIgnoreCase("")
                ? "0.00" : txt_amount.getText().toString()));

        if (edt_discount.getText() != null && !edt_discount.getText().toString().isEmpty()
                && !edt_discount.getText().toString().equalsIgnoreCase(".")) {
            objClsPurchaseDetail.setDiscount(Double.parseDouble(edt_discount.getText().toString()));
        } else {
            objClsPurchaseDetail.setDiscount(0.0);
        }

        objClsPurchaseDetail.setGrandTotal(Double.parseDouble(txt_final_amt.getTag().toString()));
        objClsPurchaseDetail.setApplyTax(ckb_apply_tax.isChecked() ? "Yes" : "No");


        if (applyTax == true) {

            objClsPurchaseDetail.setCGST(Double.valueOf(ClsGlobal.round(Double.parseDouble(txt_cgst_value.getTag().toString()), 2)));
            objClsPurchaseDetail.setSGST(Double.valueOf(ClsGlobal.round(Double.parseDouble(txt_sgst_value.getTag().toString()), 2)));
            objClsPurchaseDetail.setIGST(Double.valueOf(ClsGlobal.round(Double.parseDouble(txt_igst_value.getTag().toString()), 2)));
            objClsPurchaseDetail.setTotalTaxAmount(Double.parseDouble(txt_total_tax.getTag().toString()));
        } else {

            objClsPurchaseDetail.setCGST(0.0);
            objClsPurchaseDetail.setSGST(0.0);
            objClsPurchaseDetail.setIGST(0.0);
            objClsPurchaseDetail.setTotalTaxAmount(0.0);
        }
        objClsPurchaseDetail.setItemName(txt_Title.getText().toString());

        lstClsProductListList.add(objClsPurchaseDetail);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsPurchaseDetail);
        Log.d("--Change--", "InsertPurchaseDetail- " + jsonInString);

        hidekeyboard();

        if (_mode.equalsIgnoreCase("scan")) {
            dismiss();
            getActivity().onBackPressed();
        } else {
            getActivity().finish();
        }
    }

    private Boolean checkValidation() {

        if (edt_qty.getText() == null || edt_qty.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Quantity is required", Toast.LENGTH_SHORT).show();
            edt_qty.requestFocus();
            return false;
        }

        if (edit_Rate.getText() == null || edit_Rate.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Price is required", Toast.LENGTH_SHORT).show();
            edit_Rate.requestFocus();
            return false;
        }
        return true;
    }


    @SuppressLint("SetTextI18n")
    void calculateAmount() {
        Double qty = 0.0;
        Double unitPrice = 0.0;
        Double discount = 0.0;

        if (edt_qty.getText() != null && !edt_qty.getText().toString().isEmpty()
                && edt_qty.getText().toString().length() != 0) {

            String txtVal = edt_qty.getText().toString();
            if (txtVal.equalsIgnoreCase(".")) {
                txtVal = "0";
            }

            qty = Double.valueOf(txtVal);
        }

        if (edit_Rate.getText() != null && !edit_Rate.getText().toString().isEmpty()
                && edit_Rate.getText().toString().length() != 0) {

            String txtVal = edit_Rate.getText().toString();
            if (txtVal.equalsIgnoreCase(".")) {
                txtVal = "0";
            }

            unitPrice = Double.valueOf(txtVal);
        }

        if (edt_discount.getText() != null && !edt_discount.getText().toString().isEmpty()
                && edt_discount.getText().toString().length() != 0) {

            String txtVal = edt_discount.getText().toString();
            if (txtVal.equalsIgnoreCase(".")) {
                txtVal = "0";
            }

            discount = Double.valueOf(txtVal);
        }

        total = (qty * unitPrice) - discount;
        txt_amount.setText(String.valueOf(ClsGlobal.round(total, 2)));

        Double sgstValue = 0.0;
        Double cgstValue = 0.0;
        Double igstValue = 0.0;

        sgstValue = Double.valueOf(ClsGlobal.round(total * getCurrentObj.getSGST() / 100, 2));
        cgstValue = Double.valueOf(ClsGlobal.round(total * getCurrentObj.getCGST() / 100, 2));
        igstValue = Double.valueOf(ClsGlobal.round(total * getCurrentObj.getIGST() / 100, 2));

        txt_sgst_value.setText("\u20B9 " + ClsGlobal.round(sgstValue, 2));
        txt_cgst_value.setText("\u20B9 " + ClsGlobal.round(cgstValue, 2));
        txt_igst_value.setText("\u20B9 " + ClsGlobal.round(igstValue, 2));

        txt_sgst_value.setTag(sgstValue);
        txt_cgst_value.setTag(cgstValue);
        txt_igst_value.setTag(igstValue);

        totalTaxValue = sgstValue + cgstValue + igstValue;

        txt_total_tax.setText("TOTAL TAX: \u20B9 " + ClsGlobal.round(totalTaxValue, 2));
        txt_total_tax.setTag(ClsGlobal.round(totalTaxValue, 2));
        if (ckb_apply_tax.isChecked()) {
            finalAmount = total + totalTaxValue;
        } else {
            finalAmount = total;
        }
        txt_final_amt.setText("TOTAL: \u20B9 " + ClsGlobal.round(finalAmount, 2));
        txt_final_amt.setTag(ClsGlobal.round(finalAmount, 2));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return dialog;
    }

    void hidekeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateTotalAmountWithTax() {
        // calculate Tax.
        total_Amount_With_Tax = (total_Amount * (CGST + SGST + IGST)) / 100;
        // Here we are setting Total Amount + Tax to TextView.
        txt_final_amt.setText(String.valueOf(total_Amount_With_Tax + total_Amount));

    }
}