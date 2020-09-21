package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.HintSelectionAdapter;
import com.demo.nspl.restaurantlite.BarcodeClasses.BarcodeReaderActivity;
import com.demo.nspl.restaurantlite.BarcodeClasses.BarcodeReaderFragment;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsSelectionModel;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class FilterDialogFragment extends DialogFragment implements
        BarcodeReaderFragment.BarcodeReaderListener, TextWatcher {

    private int request_code = 0;
    private ClsLayerItemMaster getCurrentObj;

    private ImageButton bt_close;
    private Double counter = 1.0;
    ImageView iv_clear_comments, iv_clear_rate, iv_clear_discount;
    private Double total_Amount = 0.0;
    private Double _includingTotalAmount = 0.0;
    private Double total_Amount_With_Tax = 0.0;


    private TextView txt_item_code, txt_Title, txt_without_tax_total,
            txt_final_amount, txt_Total_tax;
    private EditText edt_without_tax_rate, edt_qty, edit_Comments, edt_with_tax_rate;
    private ImageButton ib_add_to_order;

    private RadioGroup rg;
    private RadioButton rbYES, rbNO;
    private LinearLayout hide_layout, hide_layout1;

    private Button Btn_Add, Btn_Min;
    String mode = "";
    String _barcodeResult = "";
    String entryMode = "";
    String _orderNO = "";
    TextView txt_note;
    TextView txt_last_purchase_price;
    ImageView iv_hint;

    List<ClsSelectionModel> lstHint = new ArrayList<>();
    TextView txt_done;
    RecyclerView rv_hint_list;
    Dialog dialog;
    HintSelectionAdapter mAdapter;


    List<String> _layerList = new ArrayList<>();
    TextView txt_with_tax_total;
    TextView txt_total_tax_amount;
    TextView txt_cgst;
    TextView txt_sgst;
    TextView txt_igst;
    TextView txt_cgst_val;
    TextView txt_sgst_val;
    TextView txt_igst_val;
    TextView txt_discount_per;
    TextView txt_discount_val;
    TextView txt_net_amount;
    Double _edtValue = 0.0;
    EditText edt_discount_value;
    LinearLayout ll_tax;
    TextView txt_no_tax_apply;

    ImageView iv_clear_qty;
    EditText edt_discount_percentage;

    private boolean[] checkedItems;


    int QuotationID = 0;
    String QuotationNo = "";

    TextView txt_tax_name;


    public int getQuotationID() {
        return QuotationID;
    }

    public void setQuotationID(int quotationID) {
        QuotationID = quotationID;
    }

    public String getQuotationNo() {
        return QuotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        QuotationNo = quotationNo;
    }

    public FilterDialogFragment(String mode, String _barcodeResult, String entryMode, String _orderNO) {
        this.mode = mode;
        this._barcodeResult = _barcodeResult;
        this.entryMode = entryMode;
        this._orderNO = _orderNO;

        // Required empty public constructor
    }

    private CallbackResult callbackResult;
    private OnUpdateFooterValue onUpdateFooterValue;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    public void setRequestCode(ClsLayerItemMaster currentObj) {
        this.getCurrentObj = currentObj;


    }

    public void setOnUpdateFooterValue(OnUpdateFooterValue onUpdateFooterValue) {
        this.onUpdateFooterValue = onUpdateFooterValue;

        Log.e("--OnClick--", "itemCount: " + onUpdateFooterValue);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        // Inflate the layout for this fragment

        bt_close = view.findViewById(R.id.bt_close);
        iv_hint = view.findViewById(R.id.iv_hint);
        txt_note = view.findViewById(R.id.txt_note);
        txt_item_code = view.findViewById(R.id.txt_item_code);
        txt_Title = view.findViewById(R.id.txt_Title);
        Btn_Add = view.findViewById(R.id.Btn_Add);
        Btn_Min = view.findViewById(R.id.Btn_Min);
        edt_qty = view.findViewById(R.id.edt_qty);
        edt_without_tax_rate = view.findViewById(R.id.edt_without_tax_rate);
        edt_without_tax_rate.setTag(0.0);
        txt_without_tax_total = view.findViewById(R.id.txt_without_tax_total);
        txt_final_amount = view.findViewById(R.id.txt_final_amount);
        edit_Comments = view.findViewById(R.id.edit_Comments);
        hide_layout = view.findViewById(R.id.hide_layout);
        hide_layout1 = view.findViewById(R.id.hide_layout1);
        iv_clear_comments = view.findViewById(R.id.iv_clear_comments);
        iv_clear_rate = view.findViewById(R.id.iv_clear_rate);
        txt_tax_name = view.findViewById(R.id.txt_tax_name);

        rg = view.findViewById(R.id.rg);
        rbYES = view.findViewById(R.id.rbYES);
        rbNO = view.findViewById(R.id.rbNO);
        ib_add_to_order = view.findViewById(R.id.ib_add_to_order);
        txt_Total_tax = view.findViewById(R.id.txt_Total_tax);

        txt_with_tax_total = view.findViewById(R.id.txt_with_tax_total);
        txt_with_tax_total.setTag(0.0);
        edt_with_tax_rate = view.findViewById(R.id.edt_with_tax_rate);
        edt_with_tax_rate.setTag(0.0);

        txt_total_tax_amount = view.findViewById(R.id.txt_total_tax_amount);
        txt_total_tax_amount.setTag(0.0);

        txt_cgst = view.findViewById(R.id.txt_cgst);
        txt_cgst_val = view.findViewById(R.id.txt_cgst_val);
        txt_sgst = view.findViewById(R.id.txt_sgst);
        txt_sgst_val = view.findViewById(R.id.txt_sgst_val);
        txt_igst = view.findViewById(R.id.txt_igst);
        txt_igst_val = view.findViewById(R.id.txt_igst_val);
        txt_discount_val = view.findViewById(R.id.txt_discount_val);
        txt_net_amount = view.findViewById(R.id.txt_net_amount);
        txt_net_amount.setTag(0.0);
        txt_no_tax_apply = view.findViewById(R.id.txt_no_tax_apply);

        edt_discount_value = view.findViewById(R.id.edt_discount_value);
        txt_discount_per = view.findViewById(R.id.txt_discount_per);
        ll_tax = view.findViewById(R.id.ll_tax);
        iv_clear_discount = view.findViewById(R.id.iv_clear_discount);

        edt_discount_percentage = view.findViewById(R.id.edt_discount_percentage);
        iv_clear_qty = view.findViewById(R.id.iv_clear_qty);

        txt_last_purchase_price = view.findViewById(R.id.txt_last_purchase_price);

        if (getCurrentObj.getLastPurchasePrice() != 0.0) {
            txt_last_purchase_price.setText("LAST PURCHASE PRICE: \u20B9 " + ClsGlobal.round(getCurrentObj.getLastPurchasePrice(), 2));
        } else {
            txt_last_purchase_price.setText("THIS ITEM NOT PURCHASE YET.");
        }


        edt_without_tax_rate.addTextChangedListener(this);
        edt_with_tax_rate.addTextChangedListener(this);

        edt_discount_percentage.addTextChangedListener(this);
        edt_discount_value.addTextChangedListener(this);
        edt_qty.addTextChangedListener(this);

        hide_layout.setVisibility(View.GONE);
        hide_layout1.setVisibility(View.GONE);

        double saleRateWithTax = 0.0;

        if (getCurrentObj != null
                && !getCurrentObj.getITEM_CODE().equalsIgnoreCase("")
                && !getCurrentObj.getITEM_NAME().equalsIgnoreCase("")) {

            txt_Title.setText(getCurrentObj.getITEM_NAME().toUpperCase());
            txt_item_code.setText("CODE: " + getCurrentObj.getITEM_CODE());

            if (mode.equalsIgnoreCase("Sale")) {

                double SaleRate = getCurrentObj.getRATE_PER_UNIT();

                if (getCurrentObj.getTAX_APPLY() != null &&
                        getCurrentObj.getTAX_APPLY().equalsIgnoreCase("YES")) {

                    txt_tax_name.setVisibility(View.VISIBLE);
                    txt_tax_name.setText("TAX: " + getCurrentObj.getSLAB_NAME().toUpperCase());

                    if (getCurrentObj.getTAX_TYPE().equalsIgnoreCase("INCLUSIVE")) {
                        txt_note.setText("RATE IS INCLUSIVE TAX");
                        double _value = SaleRate / (100 + getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST()) * 100;//amount without tax

                        edt_without_tax_rate.setText(String.valueOf(ClsGlobal.round(_value, 2)));
                        edt_with_tax_rate.setText(String.valueOf(SaleRate));

                        edt_without_tax_rate.setTag(_value);

                    } else {
                        txt_note.setText("RATE IS EXCLUDING TAX");
                        edt_with_tax_rate.setEnabled(false);
                        Log.e("--SaleRate--", "SaleRateELSE: " + SaleRate);

                        saleRateWithTax = (SaleRate * (getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST())) / 100;

                        edt_with_tax_rate.setText(String.valueOf(ClsGlobal.round(SaleRate + saleRateWithTax, 2)));
                        edt_without_tax_rate.setText(String.valueOf(SaleRate));
                        edt_without_tax_rate.setTag(SaleRate);

                    }

                } else {
                    txt_note.setText("NO TAX APPLY");
//                    txt_tax_name.setText("NO TAX APPLY");
                    txt_tax_name.setVisibility(View.GONE);
                    edt_with_tax_rate.setEnabled(false);
                    edt_without_tax_rate.setText(String.valueOf(getCurrentObj.getRATE_PER_UNIT()));
                    edt_without_tax_rate.setTag(getCurrentObj.getRATE_PER_UNIT());

                    ll_tax.setVisibility(View.GONE);
                    txt_no_tax_apply.setVisibility(View.VISIBLE);
                }

            } else if (mode.equalsIgnoreCase("Wholesale")) {

                if (getCurrentObj.getTAX_APPLY().equalsIgnoreCase("YES")) {
                    double WholeSaleRate = getCurrentObj.getWHOLESALE_RATE();

                    if (getCurrentObj.getTAX_TYPE().equalsIgnoreCase("INCLUSIVE")) {
                        txt_note.setText("RATE IS INCLUSIVE TAX");
//                        txt_note.setText("INCLUSIVE");

                        double _value = WholeSaleRate / (100 + getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST()) * 100;//amount without tax

                        edt_without_tax_rate.setText(String.valueOf(ClsGlobal.round(_value, 2)));
                        edt_with_tax_rate.setText(String.valueOf(WholeSaleRate));
                        edt_without_tax_rate.setTag(_value);

                        txt_with_tax_total.setText(String.valueOf(getCurrentObj.getWHOLESALE_RATE()));
                        txt_with_tax_total.setTag(getCurrentObj.getWHOLESALE_RATE());

                    } else {

                        txt_note.setText("RATE IS EXCLUDING TAX");
                        edt_with_tax_rate.setEnabled(false);

                        saleRateWithTax = (WholeSaleRate * (getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST())) / 100;

                        edt_with_tax_rate.setText(String.valueOf(ClsGlobal.round(WholeSaleRate + saleRateWithTax, 2)));
                        edt_without_tax_rate.setText(String.valueOf(ClsGlobal.round(WholeSaleRate, 2)));
                        edt_without_tax_rate.setTag(WholeSaleRate);

                        txt_with_tax_total.setText(String.valueOf(ClsGlobal.round(WholeSaleRate + saleRateWithTax, 2)));
                        txt_with_tax_total.setTag(WholeSaleRate + saleRateWithTax);

                    }
                } else {
                    txt_note.setText("NO TAX APPLY");

                    edt_with_tax_rate.setEnabled(false);

                    edt_without_tax_rate.setText(String.valueOf(getCurrentObj.getWHOLESALE_RATE()));
                    edt_without_tax_rate.setTag(getCurrentObj.getWHOLESALE_RATE());

                    ll_tax.setVisibility(View.GONE);
                    txt_no_tax_apply.setVisibility(View.VISIBLE);
                }
            } else if (mode.equalsIgnoreCase("Retail Quotation")) {
                double SaleRate = getCurrentObj.getRATE_PER_UNIT();

                if (getCurrentObj.getTAX_APPLY() != null &&
                        getCurrentObj.getTAX_APPLY().equalsIgnoreCase("YES")) {

                    if (getCurrentObj.getTAX_TYPE().equalsIgnoreCase("INCLUSIVE")) {

                        txt_note.setText("RATE IS INCLUSIVE TAX");

                        double _value = SaleRate / (100 + getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST()) * 100;//amount without tax

                        edt_without_tax_rate.setText(String.valueOf(ClsGlobal.round(_value, 2)));
                        edt_with_tax_rate.setText(String.valueOf(SaleRate));

                        edt_without_tax_rate.setTag(_value);

                    } else {
                        txt_note.setText("RATE IS EXCLUDING TAX");
                        edt_with_tax_rate.setEnabled(false);

                        saleRateWithTax = (SaleRate * (getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST())) / 100;

                        edt_with_tax_rate.setText(String.valueOf(ClsGlobal.round(SaleRate + saleRateWithTax, 2)));
                        edt_without_tax_rate.setText(String.valueOf(SaleRate));
                        edt_without_tax_rate.setTag(SaleRate);

                    }
                } else {
                    txt_note.setText("NO TAX APPLY");

                    edt_with_tax_rate.setEnabled(false);
                    edt_without_tax_rate.setText(String.valueOf(getCurrentObj.getRATE_PER_UNIT()));
                    edt_without_tax_rate.setTag(getCurrentObj.getRATE_PER_UNIT());

                    ll_tax.setVisibility(View.GONE);
                    txt_no_tax_apply.setVisibility(View.VISIBLE);
                }
            } else if (mode.equalsIgnoreCase("Wholesale Quotation")) {
                if (getCurrentObj.getTAX_APPLY().equalsIgnoreCase("YES")) {
                    double WholeSaleRate = getCurrentObj.getWHOLESALE_RATE();

                    if (getCurrentObj.getTAX_TYPE().equalsIgnoreCase("INCLUSIVE")) {
                        txt_note.setText("RATE IS INCLUSIVE TAX");

                        double _value = WholeSaleRate / (100 + getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST()) * 100;//amount without tax

                        edt_without_tax_rate.setText(String.valueOf(ClsGlobal.round(_value, 2)));
                        edt_with_tax_rate.setText(String.valueOf(WholeSaleRate));
                        edt_without_tax_rate.setTag(_value);

                        txt_with_tax_total.setText(String.valueOf(getCurrentObj.getWHOLESALE_RATE()));
                        txt_with_tax_total.setTag(getCurrentObj.getWHOLESALE_RATE());

                    } else {
                        txt_note.setText("RATE IS EXCLUDING TAX");
                        edt_with_tax_rate.setEnabled(false);

                        saleRateWithTax = (WholeSaleRate * (getCurrentObj.getCGST() +
                                getCurrentObj.getSGST() +
                                getCurrentObj.getIGST())) / 100;

                        edt_with_tax_rate.setText(String.valueOf(ClsGlobal.round(WholeSaleRate + saleRateWithTax, 2)));
                        edt_without_tax_rate.setText(String.valueOf(ClsGlobal.round(WholeSaleRate, 2)));
                        edt_without_tax_rate.setTag(WholeSaleRate);

                        txt_with_tax_total.setText(String.valueOf(ClsGlobal.round(WholeSaleRate + saleRateWithTax, 2)));
                        txt_with_tax_total.setTag(WholeSaleRate + saleRateWithTax);

                    }
                } else {
                    txt_note.setText("NO TAX APPLY");
                    edt_with_tax_rate.setEnabled(false);

                    edt_without_tax_rate.setText(String.valueOf(getCurrentObj.getWHOLESALE_RATE()));
                    edt_without_tax_rate.setTag(getCurrentObj.getWHOLESALE_RATE());

                    ll_tax.setVisibility(View.GONE);
                    txt_no_tax_apply.setVisibility(View.VISIBLE);
                }
            }

            edt_qty.setText(String.valueOf(1.0));
            total_Amount = Double.valueOf(ClsGlobal.round(Double.parseDouble(txt_without_tax_total.getText().toString()), 2));
        }
        rateCalculation("withoutTax");

        iv_clear_rate.setOnClickListener(v -> {

            edt_without_tax_rate.removeTextChangedListener(this);
            edt_with_tax_rate.removeTextChangedListener(this);

            edt_without_tax_rate.setText("");
            edt_without_tax_rate.setTag(0.0);
            edt_without_tax_rate.requestFocus();

            edt_with_tax_rate.setText("");
            edt_with_tax_rate.setTag(0.0);

            edt_without_tax_rate.addTextChangedListener(this);
            edt_with_tax_rate.addTextChangedListener(this);

            edt_qty.setText(String.valueOf(1.0));
            edt_qty.setTag(1.0);

            edt_discount_percentage.setText("");
            edt_discount_percentage.setTag(0.0);

            edt_discount_value.setText("");
            edt_discount_value.setTag(0.0);

            rateCalculation("");


        });

        iv_clear_discount.setOnClickListener(v -> {

            edt_discount_percentage.removeTextChangedListener(this);
            edt_discount_value.removeTextChangedListener(this);

            edt_discount_percentage.setText("");
            edt_discount_percentage.setTag(0.0);
            edt_discount_percentage.requestFocus();

            edt_discount_value.setText("");
            edt_discount_value.setTag(0.0);

            edt_discount_percentage.addTextChangedListener(this);
            edt_discount_value.addTextChangedListener(this);


            rateCalculation("");

        });


        iv_clear_qty.setOnClickListener(v -> {


            edt_qty.setText(String.valueOf(1.0));
            edt_qty.setTag(1.0);
            edt_qty.requestFocus();


        });

        iv_clear_comments.setOnClickListener(v -> {

            edit_Comments.setText("");
            edit_Comments.requestFocus();
        });


        Btn_Add.setOnClickListener(v -> {
            Double _qty = 0.0;
            if (counter >= 0.0) {
                if (edt_qty.getText() != null && !edt_qty.getText().toString().isEmpty()) {
                    _qty = Double.valueOf(ClsGlobal.round(Double.valueOf(edt_qty.getText().toString().equalsIgnoreCase("")
                            || edt_qty.getText().toString().equalsIgnoreCase(".")
                            ? "0.00" : edt_qty.getText().toString()), 2));//11.45
                    _qty += 1.0;
                    counter = Double.valueOf(ClsGlobal.round(_qty, 2));
                    edt_qty.setText(String.valueOf(ClsGlobal.round(_qty, 2)));
                } else {
                    edt_qty.setText("0.00");
                    counter = 0.0;
                }
                Count();
            }

            hidekeyboard();

        });

        Btn_Min.setOnClickListener(v -> {

            double _qty = 0.0;

            if (counter >= 0.0) {
                if (edt_qty.getText() != null && !edt_qty.getText().toString().isEmpty()) {
                    _qty = Double.parseDouble(ClsGlobal.round(Double.parseDouble(edt_qty.getText().toString().equalsIgnoreCase("")
                            || edt_qty.getText().toString().equalsIgnoreCase(".")
                            ? "0.00" : edt_qty.getText().toString()), 2));//11.45
                    _qty -= 1.0;

                    if (_qty <= 0.0) {
                        _qty = 0.0;
                        Log.e("_qty", "_qty call");
                    }
                    counter = Double.valueOf(ClsGlobal.round(_qty, 2));

                    Log.e("_qty", String.valueOf(_qty));
                    edt_qty.setText(String.valueOf(ClsGlobal.round(_qty, 2)));
                } else {
                    edt_qty.setText("0.00");
                    counter = 0.0;
                }

                Count();
                hidekeyboard();

            }
        });

        bt_close.setOnClickListener(v -> {
            hidekeyboard();
            dismiss();
            callbackResult.sendResult(300);
        });

        Log.e("--Purchase--", "Obj: " + getCurrentObj.getLAYERITEM_ID());

        _layerList = ClsLayerItemMaster.getLayerValuesWithLayerName(getActivity(),
                " AND [ITEM_ID] =".concat(String.valueOf(getCurrentObj.getLAYERITEM_ID())));

        for (String _layer : _layerList) {
            ClsSelectionModel obj = new ClsSelectionModel();
            obj.set_character(_layer);
            obj.setSelected(false);
            lstHint.add(obj);
        }

        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_recyclerview);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        rv_hint_list = dialog.findViewById(R.id.rv_hint_list);
        txt_done = dialog.findViewById(R.id.txt_done);


        rv_hint_list.setHasFixedSize(true);
        rv_hint_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String[] array = new String[lstHint.size()];
        for (ClsSelectionModel _objRoom : lstHint) {
            array[lstHint.indexOf(_objRoom)] = _objRoom.get_character();
        }

        checkedItems = new boolean[array.length];

        mAdapter = new HintSelectionAdapter(getActivity(), lstHint);
        rv_hint_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        dialogHint();

        addToOrder();

        return view;
    }

    void addToOrder() {
        ib_add_to_order.setOnClickListener(v -> {

            Double _SaleRateWithoutTax = 0.0;
            if (edt_without_tax_rate.getText() != null && edt_without_tax_rate.getText().toString().length() != 0) {
                String txtVal = edt_without_tax_rate.getText().toString();
                if (txtVal.equalsIgnoreCase(".")) {
                    txtVal = "0";
                }
                _SaleRateWithoutTax = Double.valueOf(txtVal);
            }

            Double _SaleRateWithTax = 0.0;
            if (edt_with_tax_rate.getText() != null && edt_with_tax_rate.getText().toString().length() != 0) {
                String txtValWithTax = edt_with_tax_rate.getText().toString();
                if (txtValWithTax.equalsIgnoreCase(".")) {
                    txtValWithTax = "0";
                }
                _SaleRateWithTax = Double.valueOf(txtValWithTax);
            }

            if (_SaleRateWithoutTax <= 0) {//1.0
//error MSG
                Toast.makeText(getContext(), "Rate is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            Double Qty = 0.0;
            if (edt_qty.getText() != null && edt_qty.getText().toString().length() != 0) {
                String txtVal = edt_qty.getText().toString();
                if (txtVal.equalsIgnoreCase(".")) {
                    txtVal = "0";
                }
                Qty = Double.valueOf(txtVal);
            }

            if (Qty <= 0) {
                //1.0
                //error MSG
                Toast.makeText(getContext(), "Quantity is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!edt_qty.getText().toString().equalsIgnoreCase("")
                    || !edt_qty.getText().toString().equalsIgnoreCase(".")) {

                ClsInventoryOrderDetail Obj = new ClsInventoryOrderDetail();
                ClsQuotationOrderDetail objClsQuotationOrderDetail = new ClsQuotationOrderDetail();


                if (mode.equalsIgnoreCase("Sale")) {

                    if (entryMode.equalsIgnoreCase("edit")) {
                        Obj.setOrderNo(ClsGlobal._OrderNo);
                        Obj.setOrderID(ClsGlobal.editOrderID);

                    } else {
                        if (ClsGlobal._OrderNo == null || ClsGlobal._OrderNo.isEmpty()
                                || ClsGlobal._OrderNo.matches("")) {

                            ClsGlobal._OrderNo = String.valueOf(ClsInventoryOrderMaster
                                    .getTemporaryOrderNo("OrderNo"));//ClsSales.getNextOrderNo();
                        }
                        Obj.setOrderNo(ClsGlobal._OrderNo);
                        Obj.setOrderID("0");
                    }

                    Obj.setRate(getCurrentObj.get_saleRateIncludingTax());

                } else if (mode.equalsIgnoreCase("Wholesale")) {
                    if (entryMode.equalsIgnoreCase("edit")) {
                        Obj.setOrderNo(ClsGlobal._WholeSaleOrderNo);
                        Obj.setOrderID(ClsGlobal.editOrderID);
                        Log.e("--Order--", "editOrderID:- " + ClsGlobal.editOrderID);
                    } else {
                        if (ClsGlobal._WholeSaleOrderNo == null || ClsGlobal._WholeSaleOrderNo.isEmpty()
                                || ClsGlobal._WholeSaleOrderNo.matches("")) {
                            ClsGlobal._WholeSaleOrderNo = String.valueOf(
                                    ClsInventoryOrderMaster.getTemporaryOrderNo("WholeSaleOrderNo"));

                        }
                        Obj.setOrderNo(ClsGlobal._WholeSaleOrderNo);
                        Obj.setOrderID("0");
                    }
                    Obj.setRate(getCurrentObj.get_wholesaleRateIncludingTax());
                } else if (mode.equalsIgnoreCase("Retail Quotation")) {


                    if (entryMode.equalsIgnoreCase("edit")) {
                        objClsQuotationOrderDetail.setQuotationNo(ClsGlobal._QuotationSaleOrderNo);
                        objClsQuotationOrderDetail.setQuotationID(ClsGlobal.editQuotationID);
                    } else {
                        if (ClsGlobal._QuotationSaleOrderNo == null || ClsGlobal._QuotationSaleOrderNo.isEmpty()
                                || ClsGlobal._QuotationSaleOrderNo.matches("")) {
                            ClsGlobal._QuotationSaleOrderNo = String.valueOf(
                                    ClsQuotationMaster.getTemporaryOrderNoQuotation("QuotationSaleOrderNo"));
                        }
                        objClsQuotationOrderDetail.setQuotationNo(ClsGlobal._QuotationSaleOrderNo);
                        objClsQuotationOrderDetail.setQuotationID(0);
                    }
                    objClsQuotationOrderDetail.setRate(getCurrentObj.get_saleRateIncludingTax());


                } else if (mode.equalsIgnoreCase("Wholesale Quotation")) {

                    if (entryMode.equalsIgnoreCase("edit")) {
                        objClsQuotationOrderDetail.setQuotationNo(ClsGlobal._QuotationWholesaleOrderNo);
                        objClsQuotationOrderDetail.setQuotationID(ClsGlobal.editQuotationID);
                    } else {
                        if (ClsGlobal._QuotationWholesaleOrderNo == null || ClsGlobal._QuotationWholesaleOrderNo.isEmpty()
                                || ClsGlobal._QuotationWholesaleOrderNo.matches("")) {
                            ClsGlobal._QuotationWholesaleOrderNo = String.valueOf(
                                    ClsQuotationMaster.getTemporaryOrderNoQuotation("QuotationWholesaleOrderNo"));

                        }
                        objClsQuotationOrderDetail.setQuotationNo(ClsGlobal._QuotationWholesaleOrderNo);
                        objClsQuotationOrderDetail.setQuotationID(0);
                    }
                    objClsQuotationOrderDetail.setRate(getCurrentObj.get_wholesaleRateIncludingTax());
                }

                int result = 0;
                if (mode.equalsIgnoreCase("Sale") ||
                        mode.equalsIgnoreCase("Wholesale")) {

                    Obj.setItemCode(getCurrentObj.getITEM_CODE());
                    Obj.setItem(getCurrentObj.getITEM_NAME());
                    Obj.setSaleRate(_SaleRateWithTax);
                    Obj.setQuantity(Double.valueOf(ClsGlobal.round(Qty, 2)));
                    Obj.setAmount(Double.valueOf(ClsGlobal.round(Double.valueOf(txt_without_tax_total.getText().toString()), 2)));
                    Obj.setCGST(getCurrentObj.getCGST());
                    Obj.setSGST(getCurrentObj.getSGST());
                    Obj.setIGST(getCurrentObj.getIGST());
                    Obj.setHSN_SAC_CODE(getCurrentObj.getHSN_SAC_CODE());

                    Double _totalTaxAmt = 0.0;

                    if (txt_total_tax_amount.getTag() != null
                            && !txt_total_tax_amount.getTag().toString().equalsIgnoreCase("")) {
                        _totalTaxAmt = Double.valueOf(txt_total_tax_amount.getTag().toString());
                    }

                    Obj.setTotalTaxAmount(_totalTaxAmt);
                    Obj.setSaleRateWithoutTax(_SaleRateWithoutTax);

                    //new properties
                    total_Amount_With_Tax = (Double.valueOf(txt_without_tax_total.getText().toString().equalsIgnoreCase("")
                            ? "0.00" : txt_without_tax_total.getText().toString().trim()) * (getCurrentObj.getCGST() +
                            getCurrentObj.getSGST() + getCurrentObj.getIGST())) / 100;

                    Obj.setGrandTotal(Double.valueOf(ClsGlobal.round(Double.parseDouble(txt_net_amount.getTag().toString()), 2)));
                    Obj.setItemComment(edit_Comments.getText().toString().equalsIgnoreCase("") ? "" : edit_Comments.getText().toString());

                    if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {
                        Obj.setSaveStatus("EDT");
                    } else {
                        Obj.setSaveStatus("NO");
                    }


                    if (edt_discount_percentage.getText() != null && edt_discount_percentage.getText().toString().length() != 0) {
                        Obj.setDiscount_per(Double.valueOf(edt_discount_percentage.getText().toString()));
                    } else {
                        Obj.setDiscount_per(0.0);
                    }

                    if (edt_discount_value.getText() != null && edt_discount_value.getText().toString().length() != 0) {
                        Obj.setDiscount_amt(Double.valueOf(edt_discount_value.getText().toString()));
                    } else {
                        Obj.setDiscount_amt(0.0);
                    }


                    if (edt_discount_value.getText() != null && edt_discount_value.getText().toString().length() != 0) {
                        Obj.setDiscount_amt(Double.valueOf(edt_discount_value.getText().toString()));
                    } else {
                        Obj.setDiscount_amt(0.0);
                    }


                    Obj.setUnit(getCurrentObj.getUNIT_CODE());


                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(Obj);
                    Log.d("--Dialog--", "OrderDetail: " + jsonInString);


                    result = ClsInventoryOrderDetail.InsertInventoryOrderDetail(Obj, getContext());

                } else if (mode.equalsIgnoreCase("Retail Quotation") ||
                        mode.equalsIgnoreCase("Wholesale Quotation")) {

                    objClsQuotationOrderDetail.setItemCode(getCurrentObj.getITEM_CODE());
                    objClsQuotationOrderDetail.setItem(getCurrentObj.getITEM_NAME());
                    objClsQuotationOrderDetail.setSaleRate(_SaleRateWithTax);
                    objClsQuotationOrderDetail.setQuantity(Double.valueOf(ClsGlobal.round(Qty, 2)));
                    objClsQuotationOrderDetail.setAmount(Double.valueOf(ClsGlobal.round(Double.valueOf(txt_without_tax_total.getText().toString()), 2)));
                    objClsQuotationOrderDetail.setCGST(getCurrentObj.getCGST());
                    objClsQuotationOrderDetail.setSGST(getCurrentObj.getSGST());
                    objClsQuotationOrderDetail.setIGST(getCurrentObj.getIGST());
                    objClsQuotationOrderDetail.setHSN_SAC_CODE(getCurrentObj.getHSN_SAC_CODE());
                    Double _totalTaxAmt = 0.0;

                    if (txt_total_tax_amount.getTag() != null
                            && !txt_total_tax_amount.getTag().toString().equalsIgnoreCase("")) {
                        _totalTaxAmt = Double.valueOf(txt_total_tax_amount.getTag().toString());
                    }
                    objClsQuotationOrderDetail.setTotalTaxAmount(_totalTaxAmt);
                    objClsQuotationOrderDetail.setSaleRateWithoutTax(_SaleRateWithoutTax);

                    //new properties
                    total_Amount_With_Tax = (Double.valueOf(txt_without_tax_total.getText().toString().equalsIgnoreCase("")
                            ? "0.00" : txt_without_tax_total.getText().toString().trim()) * (getCurrentObj.getCGST() +
                            getCurrentObj.getSGST() + getCurrentObj.getIGST())) / 100;

                    objClsQuotationOrderDetail.setGrandTotal(Double.valueOf(ClsGlobal.round(Double.parseDouble(txt_net_amount.getTag().toString()), 2)));
                    objClsQuotationOrderDetail.setItemComment(edit_Comments.getText().toString().equalsIgnoreCase("") ? "" : edit_Comments.getText().toString());

                    if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {
                        objClsQuotationOrderDetail.setSaveStatus("EDT");
                    } else {
                        objClsQuotationOrderDetail.setSaveStatus("NO");
                    }

                    if (edt_discount_percentage.getText() != null && edt_discount_percentage.getText().toString().length() != 0) {
                        objClsQuotationOrderDetail.setDiscount_per(Double.valueOf(edt_discount_percentage.getText().toString()));
                    } else {
                        objClsQuotationOrderDetail.setDiscount_per(0.0);
                    }

                    if (edt_discount_value.getText() != null && edt_discount_value.getText().toString().length() != 0) {
                        objClsQuotationOrderDetail.setDiscount_amt(Double.valueOf(edt_discount_value.getText().toString()));
                    } else {
                        objClsQuotationOrderDetail.setDiscount_amt(0.0);
                    }

                    objClsQuotationOrderDetail.setUnit(getCurrentObj.getUNIT_CODE());
                    result = ClsQuotationOrderDetail.InsertQuotationDetail(objClsQuotationOrderDetail, getContext());
                }

                if (result != 0) {
                    Toast.makeText(getContext(), "Order Added Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Error While Adding Order!", Toast.LENGTH_LONG).show();
                }

                dismiss();
                onUpdateFooterValue.OnClick();
            } else {
                Toast.makeText(getContext(), "There is No Quantity!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void rateCalculation(String mode) {

        edt_without_tax_rate.removeTextChangedListener(this);
        edt_qty.removeTextChangedListener(this);
        edt_with_tax_rate.removeTextChangedListener(this);
        edt_discount_value.removeTextChangedListener(this);
        edt_discount_percentage.removeTextChangedListener(this);

        double _withoutTaxRate = 0.0;
        double _withTaxRate = 0.0;
        double _qty = 0.0;
        double _disPercentage = 0.0;
        double _disAmount = 0.0;
        double _total = 0.0;
        double _netAmount = 0.0;


        if (mode.equalsIgnoreCase("qty")) {
            edt_discount_percentage.setText("");
            edt_discount_percentage.setTag(0.0);
            edt_discount_value.setText("");
            edt_discount_value.setTag(0.0);
        }

        if (edt_without_tax_rate.getText() != null && !edt_without_tax_rate.getText().toString().isEmpty() &&
                !edt_without_tax_rate.getText().toString().equalsIgnoreCase(".")) {

            _withoutTaxRate = Double.valueOf(edt_without_tax_rate.getText().toString());

        }

        if (edt_with_tax_rate.getText() != null && !edt_with_tax_rate.getText().toString().isEmpty() &&
                !edt_with_tax_rate.getText().toString().equalsIgnoreCase(".") &&
                !edt_with_tax_rate.getText().toString().equalsIgnoreCase(" ")) {

            _withTaxRate = Double.valueOf(edt_with_tax_rate.getText().toString());
        }

        if (edt_qty.getText() != null && !edt_qty.getText().toString().isEmpty() &&
                !edt_qty.getText().toString().equalsIgnoreCase(".") &&
                !edt_qty.getText().toString().equalsIgnoreCase(" ")) {

            _qty = Double.valueOf(edt_qty.getText().toString());

        }

        if (edt_discount_percentage.getText() != null && !edt_discount_percentage.getText().toString().isEmpty() &&
                !edt_discount_percentage.getText().toString().equalsIgnoreCase(".") &&
                !edt_discount_percentage.getText().toString().equalsIgnoreCase(" ")) {

            _disPercentage = Double.valueOf(edt_discount_percentage.getText().toString());

        }

        if (edt_discount_value.getText() != null && !edt_discount_value.getText().toString().isEmpty() &&
                !edt_discount_value.getText().toString().equalsIgnoreCase(".") &&
                !edt_discount_value.getText().toString().equalsIgnoreCase(" ")) {

            _disAmount = Double.valueOf(edt_discount_value.getText().toString());

        }


        if (mode.equalsIgnoreCase("withoutTax")) {
            _withTaxRate = (_withoutTaxRate * (getCurrentObj.getCGST() +
                    getCurrentObj.getSGST() +
                    getCurrentObj.getIGST())) / 100;

            edt_with_tax_rate.setText(String.valueOf(ClsGlobal.round(_withoutTaxRate + _withTaxRate, 2)));
            edt_with_tax_rate.setTag(ClsGlobal.round(_withoutTaxRate + _withTaxRate, 2));
        }

        if (mode.equalsIgnoreCase("withTax")) {
            _withoutTaxRate = _withTaxRate / (100 + getCurrentObj.getCGST() +
                    getCurrentObj.getSGST() +
                    getCurrentObj.getIGST()) * 100;//amount without tax

            edt_without_tax_rate.setText(String.valueOf(ClsGlobal.round(_withoutTaxRate, 2)));
            edt_without_tax_rate.setTag(ClsGlobal.round(_withoutTaxRate, 2));
        }

        _total = _withoutTaxRate * _qty;

        txt_without_tax_total.setText(String.valueOf(ClsGlobal.round(_total, 3)));

        if (mode.equalsIgnoreCase("discountPercentage")) {
            _disAmount = (_total * _disPercentage) / 100;
            edt_discount_value.setText(String.valueOf(ClsGlobal.round(_disAmount, 2)));
            edt_discount_value.setTag(ClsGlobal.round(_disAmount, 2));

        }
        if (mode.equalsIgnoreCase("discountAmount")) {
            _disPercentage = (_disAmount / _total) * 100;
            edt_discount_percentage.setText(String.valueOf(ClsGlobal.round(_disPercentage, 2)));
            edt_discount_percentage.setTag(ClsGlobal.round(_disPercentage, 2));
        }


        if (_withoutTaxRate == 0.0) {
            edt_without_tax_rate.setText("");
            edt_without_tax_rate.setTag(0.0);
        }

        if (_withTaxRate == 0.0) {
            edt_with_tax_rate.setText("");
            edt_with_tax_rate.setTag(0.0);
        }

        if (_disPercentage == 0.0) {
            edt_discount_percentage.setText("");
            edt_discount_percentage.setTag(0.0);
        }

        if (_disAmount == 0.0) {
            edt_discount_value.setText("");
            edt_discount_value.setTag(0.0);
        }

        txt_discount_per.setText("DISCOUNT(" + ClsGlobal.round(_disPercentage, 3) + "%)");
        txt_discount_val.setText(String.valueOf(ClsGlobal.round(_disAmount, 2)));

        _netAmount = _total - _disAmount;

        txt_net_amount.setText(String.valueOf(ClsGlobal.round(_netAmount, 2)));
        txt_net_amount.setTag(ClsGlobal.round(_netAmount, 2));


        txt_cgst.setText("CGST(" + ClsGlobal.round(getCurrentObj.getCGST(), 2) + "%)");
        txt_sgst.setText("SGST(" + ClsGlobal.round(getCurrentObj.getSGST(), 2) + "%)");
        txt_igst.setText("IGST(" + ClsGlobal.round(getCurrentObj.getIGST(), 2) + "%)");

        double _cgstVal = (_netAmount * getCurrentObj.getCGST()) / 100;
        double _sgstVal = (_netAmount * getCurrentObj.getSGST()) / 100;
        double _igstVal = (_netAmount * getCurrentObj.getIGST()) / 100;

        txt_cgst_val.setText(String.valueOf(ClsGlobal.round(_cgstVal, 3)));
        txt_sgst_val.setText(String.valueOf(ClsGlobal.round(_sgstVal, 3)));
        txt_igst_val.setText(String.valueOf(ClsGlobal.round(_igstVal, 3)));

        double _totalTax = _cgstVal + _sgstVal + _igstVal;
        txt_total_tax_amount.setText(String.valueOf(ClsGlobal.round(_totalTax, 3)));
        txt_total_tax_amount.setTag(_totalTax);

        txt_with_tax_total.setText(String.valueOf(ClsGlobal.round(_netAmount + _totalTax, 2)));
        txt_with_tax_total.setTag(_netAmount + _totalTax);

        edt_without_tax_rate.addTextChangedListener(this);
        edt_qty.addTextChangedListener(this);
        edt_discount_value.addTextChangedListener(this);
        edt_discount_percentage.addTextChangedListener(this);
        edt_with_tax_rate.addTextChangedListener(this);

    }

    void dialogHint() {

        iv_hint.setOnClickListener(v -> {

            txt_done.setOnClickListener(v1 -> {

                ArrayList<String> selectedItems = new ArrayList<>();

                boolean isSelected[] = mAdapter.getSelectedFlags();
                List<ClsSelectionModel> selectedLayers = new ArrayList<>();

                for (int i = 0; i < isSelected.length; i++) {

                    if (isSelected[i]) {
                        if (!selectedItems.contains(lstHint.get(i).get_character())) {
                            selectedItems.add(lstHint.get(i).get_character());
                            ClsSelectionModel obj;
                            obj = lstHint.get(i);
                            obj.setSelected(true);
                            lstHint.set(lstHint.indexOf(obj), obj);
                            selectedLayers.add(obj);
                        }
                    }
                }

                String comment = TextUtils.join(", ", selectedItems);
                edit_Comments.setText(comment);
                dialog.dismiss();

            });

            if (lstHint != null && lstHint.size() != 0) {
                dialog.show();
            } else {
                Toast.makeText(getActivity(), "No layer found", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return dialog;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callbackResult.sendResult(300);
//        selectedRooms.clear();
        mAdapter.getSelectedFlagsClear();
        lstHint.clear();
    }

    private void Count() {

        if (edt_without_tax_rate.getText().toString().equalsIgnoreCase(".")) {
            total_Amount = counter * Double.valueOf("0.00");
        } else {
            total_Amount = counter * Double.valueOf(edt_without_tax_rate.getText().toString().equalsIgnoreCase("")
                    ? "0.00" : edt_without_tax_rate.getText().toString());
        }

        if (edt_with_tax_rate.getText().toString().equalsIgnoreCase(".")) {
            _includingTotalAmount = counter * Double.valueOf("0.00");
        } else {
            _includingTotalAmount = counter * Double.valueOf(edt_with_tax_rate.getText().toString().equalsIgnoreCase("")
                    ? "0.00" : edt_with_tax_rate.getText().toString());
        }
        rateCalculation("qty");
    }


    @Override
    public void onActivityResult(int _requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(_requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_requestCode == 1208 && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            Toast.makeText(getActivity(), barcode.rawValue, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {


        if (getActivity().getCurrentFocus() == edt_without_tax_rate) {

            rateCalculation("withoutTax");

        } else if (getActivity().getCurrentFocus() == edt_with_tax_rate) {
            rateCalculation("withTax");

        } else if (getActivity().getCurrentFocus() == edt_discount_percentage) {

            rateCalculation("discountPercentage");
        } else if (getActivity().getCurrentFocus() == edt_discount_value) {

            rateCalculation("discountAmount");

        } else if (getActivity().getCurrentFocus() == edt_qty) {

            rateCalculation("qty");
        }

    }

    @Override
    public void onScanned(Barcode barcode) {

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }


    public interface CallbackResult {
        void sendResult(int requestCode);
    }


    public interface OnUpdateFooterValue {

        void OnClick();
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

}
