package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.OrderDetailAdapter;
import com.demo.nspl.restaurantlite.Adapter.QuotationDetailAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class OrdersActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText txt_Discount;
    TextView order_no, grandTotal, total, txt_NetAmount, txt_total_tax, txt_other_discount;
    OrderDetailAdapter cu;
    QuotationDetailAdapter mAdapter;
    RecyclerView rv;
    LinearLayout ll_discount;
    View line;
    List<ClsInventoryOrderDetail> lstCurrentOrder;
    List<ClsQuotationOrderDetail> lstClsQuotationOrderDetails;
    private CheckBox Check_Box_Apply_Tax;
    public static boolean OpenSales = false;
    public Double single_item_price = 0.0;
    public Double _itemTotal = 0.0;
    Double totalTaxAmt = 0.0;
    String saleMode = "";
    String entryMode = "";
    String _currentOrderNo = "";
    String _currentQuotationNo = "";

    String _taxApple = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        toolbar = findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        order_no = toolbar.findViewById(R.id.order_no);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "OrdersActivity"));
        }

        saleMode = getIntent().getStringExtra("saleMode");
        entryMode = getIntent().getStringExtra("entryMode");
        _taxApple = getIntent().getStringExtra("_taxApple");

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));

        total = findViewById(R.id.tv_Total);
        ll_discount = findViewById(R.id.ll_discount);
        line = findViewById(R.id.line);

        txt_Discount = findViewById(R.id.txt_Discount);
        grandTotal = findViewById(R.id.grandTotal);

        txt_NetAmount = findViewById(R.id.txt_NetAmount);
        Check_Box_Apply_Tax = findViewById(R.id.Check_Box_Apply_Tax);

        txt_total_tax = findViewById(R.id.txt_total_tax);
        txt_other_discount = findViewById(R.id.txt_other_discount);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        txt_total_tax.setVisibility(View.GONE);

        if (saleMode.equalsIgnoreCase("Sale")) {
            _currentOrderNo = ClsGlobal._OrderNo;
            order_no.setText(ClsGlobal._OrderNo);
        } else if (saleMode.equalsIgnoreCase("Wholesale")) {
            _currentOrderNo = ClsGlobal._WholeSaleOrderNo;
            order_no.setText(ClsGlobal._WholeSaleOrderNo);
        } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
            _currentQuotationNo = ClsGlobal._QuotationSaleOrderNo;
            order_no.setText(ClsGlobal._QuotationSaleOrderNo);
        } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
            _currentQuotationNo = ClsGlobal._QuotationWholesaleOrderNo;
            order_no.setText(ClsGlobal._QuotationWholesaleOrderNo);
        }

        if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {

            if (_taxApple != null && _taxApple.equalsIgnoreCase("YES")) {
                Check_Box_Apply_Tax.setChecked(true);
                txt_total_tax.setVisibility(View.VISIBLE);
                txt_other_discount.setVisibility(View.GONE);
                txt_Discount.setVisibility(View.GONE);
            } else {
                Check_Box_Apply_Tax.setChecked(false);
                txt_total_tax.setVisibility(View.GONE);
                txt_other_discount.setVisibility(View.VISIBLE);
                txt_Discount.setVisibility(View.VISIBLE);
            }
        }

        Check_Box_Apply_Tax.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                txt_total_tax.setVisibility(View.VISIBLE);
                txt_other_discount.setVisibility(View.GONE);
                txt_Discount.setVisibility(View.GONE);

            } else {
                txt_total_tax.setVisibility(View.GONE);
                txt_other_discount.setVisibility(View.VISIBLE);
                txt_Discount.setVisibility(View.VISIBLE);
            }

            txt_Discount.setText("0");
            txt_Discount.setTag(0.0);

            if (saleMode.equalsIgnoreCase("Sale") ||
                    saleMode.equalsIgnoreCase("Wholesale")) {

                calculateAmount();

            } else if (saleMode.equalsIgnoreCase("Retail Quotation") ||
                    saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                calculateQuotationAmount();

            }
        });

        txt_total_tax.setTag(0.0);
        txt_Discount.setTag(0.0);
        total.setTag(0.0);

        txt_Discount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Double _OtherDis = 0.0;

                if (editable != null && editable.toString().equalsIgnoreCase(".")) {
                    editable.clear();
                    editable.append("0.");
                }

                if (editable != null
                        && !editable.toString().isEmpty()
                        && editable.toString() != ""
                        && !editable.toString().equalsIgnoreCase(".")) {
                    _OtherDis = Double.valueOf(editable.toString());
                }

                txt_Discount.setTag(_OtherDis);

                if (saleMode.equalsIgnoreCase("Sale") ||
                        saleMode.equalsIgnoreCase("Wholesale")) {

                    calculateAmount();

                } else if (saleMode.equalsIgnoreCase("Retail Quotation") ||
                        saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                    calculateQuotationAmount();

                }
            }
        });

        if (saleMode.equalsIgnoreCase("Sale")
                || saleMode.equalsIgnoreCase("Wholesale")) {

            ViewData();

        } else if (saleMode.equalsIgnoreCase("Retail Quotation")
                || saleMode.equalsIgnoreCase("Wholesale Quotation")) {

            viewQuotationData();

        } else {

            Toast.makeText(getApplicationContext(), "NO RECORDS FOUND", Toast.LENGTH_SHORT).show();
        }

    }

    public void ViewData() {

        lstCurrentOrder = new ArrayList<>();
        String _where = " AND [OrderNo] = '" + _currentOrderNo + "' ";


        if (ClsGlobal.editOrderID != null
                && !ClsGlobal.editOrderID.equalsIgnoreCase("")) {
            _where += "".concat(" AND [OrderID] = '" + ClsGlobal.editOrderID + "'")
                    .concat(" AND IFNULL([SaveStatus],'') <> 'DEL' ");
        }

        lstCurrentOrder = new ClsInventoryOrderDetail().getList(_where, OrdersActivity.this);

        if (lstCurrentOrder.size() == 0) {
            txt_Discount.setEnabled(false);
        }

        cu = new OrderDetailAdapter(OrdersActivity.this, lstCurrentOrder,
                "OrderActivity", !Check_Box_Apply_Tax.isChecked());

        cu.SetOnClickListener((clsInventoryOrderDetail, position) ->
                ShowAlertDialog("Do you want to Delete", clsInventoryOrderDetail.getSaveStatus(),
                        clsInventoryOrderDetail.getOrderDetailID(), position));

        rv.setAdapter(cu);
        calculateAmount();
    }

    public void viewQuotationData() {

        lstClsQuotationOrderDetails = new ArrayList<>();
        String _where = " AND [QuotationNo] = '" + _currentQuotationNo + "' ";

        if (ClsGlobal.editQuotationID != 0) {
            _where += "".concat(" AND [QuotationID] = " + ClsGlobal.editQuotationID + "")
                    .concat(" AND IFNULL([SaveStatus],'') <> 'DEL' ");
        }

        @SuppressLint("WrongConstant")
        SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        lstClsQuotationOrderDetails = ClsQuotationOrderDetail.getQuotationDetailList(
                _where, OrdersActivity.this, db);
        db.close();

        if (lstClsQuotationOrderDetails.size() == 0) {
            txt_Discount.setEnabled(false);
        }

        mAdapter = new QuotationDetailAdapter(OrdersActivity.this, lstClsQuotationOrderDetails,
                "OrderActivity", !Check_Box_Apply_Tax.isChecked());

        mAdapter.SetOnClickListener((clsInventoryOrderDetail, position) ->
                deleteAlertDialogQuotation("Do you want to Delete", clsInventoryOrderDetail.getSaveStatus(),
                        clsInventoryOrderDetail.getQuotationDetailID(), position));

        rv.setAdapter(mAdapter);
        calculateQuotationAmount();

    }

    // Calculate Total Amount:
    void calculateAmount() {
        _itemTotal = 0.0;
        if (lstCurrentOrder != null && lstCurrentOrder.size() != 0) {
            for (int i = 0; i < lstCurrentOrder.size(); i++) {
                single_item_price = (lstCurrentOrder.get(i).getSaleRateWithoutTax() * lstCurrentOrder.get(i).getQuantity()) - lstCurrentOrder.get(i).getDiscount_amt();
                _itemTotal += single_item_price;
            }
        }
        total.setTag(ClsGlobal.round(_itemTotal, 2));
        total.setText("Total: " + ClsGlobal.round(_itemTotal, 2));

        //discount -
        getTotalTaxAmountFormList();
        double _totalTax = Double.parseDouble(txt_total_tax.getTag().toString());//15.25

        //apply tax
        double netAmount = 0.0;

        if (Check_Box_Apply_Tax.isChecked()) {
            netAmount = _itemTotal + _totalTax;
        } else {
            netAmount = _itemTotal;
        }
        txt_NetAmount.setText("NET AMOUNT:\u20B9 " + ClsGlobal.round(_itemTotal, 2));//total+tax = netamt
        txt_NetAmount.setTag(ClsGlobal.round(_itemTotal, 2));//total+tax = netamt

        double discount = 0.0;
        if (txt_Discount.getTag() != null && !txt_Discount.getTag().toString().equalsIgnoreCase("")) {
            discount = Double.parseDouble(txt_Discount.getTag().toString());
        }

        double grandTotalAmount = netAmount - discount;
        grandTotal.setTag(ClsGlobal.round(grandTotalAmount, 2));
        grandTotal.setText(" \u20B9 " + ClsGlobal.round(grandTotalAmount, 2));
    }

    // Calculate Total Amount:
    void calculateQuotationAmount() {
        _itemTotal = 0.0;
        if (lstClsQuotationOrderDetails != null && lstClsQuotationOrderDetails.size() != 0) {
            for (int i = 0; i < lstClsQuotationOrderDetails.size(); i++) {
                single_item_price = (lstClsQuotationOrderDetails.get(i).getSaleRateWithoutTax() *
                        lstClsQuotationOrderDetails.get(i).getQuantity()) - lstClsQuotationOrderDetails.get(i).getDiscount_amt();
                _itemTotal += single_item_price;
            }
        }

        total.setTag(ClsGlobal.round(_itemTotal, 2));
        total.setText("Total: " + ClsGlobal.round(_itemTotal, 2));

        //discount -
        getTotalTaxAmountForQuotation();
        double _totalTax = Double.parseDouble(txt_total_tax.getTag().toString());//15.25

        //apply tax
        double netAmount = 0.0;

        if (Check_Box_Apply_Tax.isChecked()) {
            netAmount = _itemTotal + _totalTax;
        } else {
            netAmount = _itemTotal;
        }
        txt_NetAmount.setText("NET AMOUNT:\u20B9 " + ClsGlobal.round(_itemTotal, 2));//total+tax = netamt
        txt_NetAmount.setTag(ClsGlobal.round(_itemTotal, 2));//total+tax = netamt

        double discount = 0.0;
        if (txt_Discount.getTag() != null && !txt_Discount.getTag().toString().equalsIgnoreCase("")) {
            discount = Double.parseDouble(txt_Discount.getTag().toString());
        }

        double grandTotalAmount = netAmount - discount;
        grandTotal.setTag(ClsGlobal.round(grandTotalAmount, 2));
        grandTotal.setText(" \u20B9 " + ClsGlobal.round(grandTotalAmount, 2));

    }


    OnUpdateFooterValueFromOrders onUpdateFooterValueFromOrders;

    public interface OnUpdateFooterValueFromOrders {
        void OnDeleteClick();
    }

    private void ShowAlertDialog(String message, String _saveStatus, int OrderDetailId, int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog OptionDialog = builder.create();
        builder.setMessage(message);

        builder.setPositiveButton("YES", (dialog, id) -> {
            if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {
                if (_saveStatus != null && !_saveStatus.isEmpty() && _saveStatus.equalsIgnoreCase("EDT")) {
                    ClsInventoryOrderDetail.Delete(OrderDetailId, this);
                } else {
                    ClsInventoryOrderDetail.UpdateStatus(OrderDetailId, "DEL", "", this);
                }
            } else {
                ClsInventoryOrderDetail.Delete(OrderDetailId, this);
            }

            cu.remove(position);
            cu.notifyDataSetChanged();
            totalTaxAmt = 0.0;
            _itemTotal = 0.0;

            calculateAmount();

            if (onUpdateFooterValueFromOrders != null) {
                onUpdateFooterValueFromOrders.OnDeleteClick();
            }

            OptionDialog.dismiss();
            OptionDialog.cancel();
        });

        builder.setNegativeButton("NO", (dialogInterface, i) -> {
            OptionDialog.dismiss();
            OptionDialog.cancel();
        });
        builder.show();
    }

    private void deleteAlertDialogQuotation(String message, String _saveStatus, int QuotationDetailID, int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog OptionDialog = builder.create();
        builder.setMessage(message);

        builder.setPositiveButton("YES", (dialog, id) -> {


            if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {


                if (_saveStatus != null && !_saveStatus.isEmpty() && _saveStatus.equalsIgnoreCase("EDT")) {
                    ClsQuotationOrderDetail.deleteQuotationDetailById(QuotationDetailID, this);
                } else {
                    ClsQuotationOrderDetail.UpdateQuotationStatus(QuotationDetailID, "DEL", "", this);
                }


            } else {
                ClsQuotationOrderDetail.deleteQuotationDetailById(QuotationDetailID, this);
            }

            mAdapter.remove(position);
            mAdapter.notifyDataSetChanged();
            totalTaxAmt = 0.0;
            _itemTotal = 0.0;

            calculateQuotationAmount();

            if (onUpdateFooterValueFromOrders != null) {
                onUpdateFooterValueFromOrders.OnDeleteClick();
            }

            OptionDialog.dismiss();
            OptionDialog.cancel();

        });

        builder.setNegativeButton("NO", (dialogInterface, i) -> {
            OptionDialog.dismiss();
            OptionDialog.cancel();

        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_orders_activity, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenSales) {
            Intent intent = new Intent(OrdersActivity.this, SalesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("saleMode", saleMode);
            intent.putExtra("entryMode", entryMode);
            startActivity(intent);
            OpenSales = false;
        }
    }

    void saveOrderRecord() {
        if (lstCurrentOrder.size() != 0) {
            ClsInventoryOrderMaster saveObj = new ClsInventoryOrderMaster();

            if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {
                if (saleMode.equalsIgnoreCase("Sale") ||
                        saleMode.equalsIgnoreCase("Wholesale")) {
                    @SuppressLint("WrongConstant") SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
                    saveObj = ClsInventoryOrderMaster.getOrder(_currentOrderNo, "", OrdersActivity.this, db);
                    db.close();
                }
            } else {
                saveObj.setMobileNo(""); //+
                saveObj.setCustomerName("");
                saveObj.setCompanyName("");
                saveObj.setGSTNo(""); //+
                saveObj.setBillDate("");//yyyy-MM-dd hh
                saveObj.setPaymentMode("");
                saveObj.setPaymentDetail("");
            }
            saveObj.setOrderNo(_currentOrderNo);

            saveObj.setTotalAmount(Double.valueOf(total.getTag().toString()));
            saveObj.setDiscountAmount(txt_Discount.getTag().toString().equalsIgnoreCase("") ? 0.0 : Double.valueOf(txt_Discount.getTag().toString()));
            saveObj.setTotalPaybleAmount(Double.valueOf(grandTotal.getTag().toString()));
            saveObj.setTotalTaxAmount(Double.valueOf(txt_total_tax.getTag().toString()));
            saveObj.setTotalReceiveableAmount(Double.valueOf(grandTotal.getTag().toString()));

            if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {

                saveObj.setPaidAmount(saveObj.getPaidAmount());
            } else {
                saveObj.setPaidAmount(Double.valueOf(grandTotal.getTag().toString()));
            }
            saveObj.setAdjumentAmount(0.0);
            saveObj.setEntryDate(ClsGlobal.getCurruntDateTime());
            saveObj.setApplyTax(Check_Box_Apply_Tax.isChecked() ? "YES" : "NO");

            if (saleMode.equalsIgnoreCase("Sale") ||
                    saleMode.equalsIgnoreCase("Wholesale")) {

                Intent doneIntent = new Intent(OrdersActivity.this, InvoiceInfoActivity.class);
                doneIntent.putExtra("ClsInventoryOrderMaster", saveObj);
                doneIntent.putExtra("Mode", "Save");
                doneIntent.putExtra("Amount", grandTotal.getTag().toString());
                doneIntent.putExtra("saleMode", saleMode);
                doneIntent.putExtra("entryMode", entryMode);
                startActivity(doneIntent);
            }


            Gson gson = new Gson();
            String jsonInString = gson.toJson(saveObj);
            Log.e("--Check--", "SaveData: " + jsonInString);


        } else {
            Toast.makeText(OrdersActivity.this, "THERE ARE NO ITEMS!", Toast.LENGTH_LONG).show();
        }
    }


    void saveQuotationRecord() {
        if (lstClsQuotationOrderDetails.size() != 0) {

            ClsQuotationMaster saveObj = new ClsQuotationMaster();

            if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {

                if (saleMode.equalsIgnoreCase("Retail Quotation") ||
                        saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                    saveObj = ClsQuotationMaster.getQuotationOrder(_currentQuotationNo,
                            OrdersActivity.this);

                }

            } else {
                saveObj.setMobileNo(""); //+
                saveObj.setCustomerName("");
                saveObj.setCompanyName("");
                saveObj.setGSTNo(""); //+
                saveObj.setBillDate("");//yyyy-MM-dd hh
                saveObj.setPaymentMode("");
                saveObj.setPaymentDetail("");
            }

            saveObj.setQuotationNo(_currentQuotationNo);

            saveObj.setTotalAmount(Double.valueOf(total.getTag().toString()));
            saveObj.setDiscountAmount(txt_Discount.getTag().toString().equalsIgnoreCase("") ? 0.0 : Double.valueOf(txt_Discount.getTag().toString()));

            saveObj.setTotalTaxAmount(Double.valueOf(txt_total_tax.getTag().toString()));

            saveObj.setGrandTotal(Double.valueOf(grandTotal.getTag().toString()));
            saveObj.setEntryDate(ClsGlobal.getCurruntDateTime());
            saveObj.setApplyTax(Check_Box_Apply_Tax.isChecked() ? "YES" : "NO");

            if (saleMode.equalsIgnoreCase("Retail Quotation") ||
                    saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                Intent doneIntent = new Intent(OrdersActivity.this,
                        GenerateQuotationActivity.class);
                doneIntent.putExtra("ClsQuotationMaster", saveObj);
                doneIntent.putExtra("Mode", "Save");
                doneIntent.putExtra("amount", txt_NetAmount.getTag().toString());
                doneIntent.putExtra("grandTotal", grandTotal.getTag().toString());
                doneIntent.putExtra("discount", txt_Discount.getTag().toString());
                doneIntent.putExtra("saleMode", saleMode);
                doneIntent.putExtra("entryMode", entryMode);
                startActivity(doneIntent);
            }

        } else {
            Toast.makeText(OrdersActivity.this, "There are No Items!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.recent_order:

                if (saleMode.equalsIgnoreCase("Sale") ||
                        saleMode.equalsIgnoreCase("Wholesale")) {

                    saveOrderRecord();

                } else if (saleMode.equalsIgnoreCase("Retail Quotation") ||
                        saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                    saveQuotationRecord();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("SetTextI18n")
    private void getTotalTaxAmountFormList() {
        Double getTotalAmount = 0.0;
        for (ClsInventoryOrderDetail currentObj : lstCurrentOrder) {
            getTotalAmount += currentObj.getTotalTaxAmount();
        }
        txt_total_tax.setTag(ClsGlobal.round(getTotalAmount, 2));
        txt_total_tax.setText("TOTAL TAX AMT:\u20B9 " + ClsGlobal.formatNumber(2,
                Double.parseDouble(ClsGlobal.round(getTotalAmount, 2))));
    }

    @SuppressLint("SetTextI18n")
    private void getTotalTaxAmountForQuotation() {
        Double getTotalAmount = 0.0;
        for (ClsQuotationOrderDetail currentObj : lstClsQuotationOrderDetails) {
            getTotalAmount += currentObj.getTotalTaxAmount();
        }
        txt_total_tax.setTag(ClsGlobal.round(getTotalAmount, 2));
        txt_total_tax.setText("TOTAL TAX AMT:\u20B9 " + ClsGlobal.formatNumber(2,
                Double.parseDouble(ClsGlobal.round(getTotalAmount, 2))));
    }

}
