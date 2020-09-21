package com.demo.nspl.restaurantlite.Payment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.PaymentDetailsAdapter;
import com.demo.nspl.restaurantlite.AsyncTaskReport.PaymentReportAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.MultipleImage.DisplayImageNewActivity;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddPaymentActivity;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CustomerPaymentFragment extends Fragment {

    int vendorId;
    String vendorName, monthYear;
    RecyclerView rv;
    TextView txt_nodata;
    LinearLayout ll_header;
    List<ClsPaymentMaster> list = new ArrayList<>();
    PaymentDetailsAdapter paymentDetailsAdapter;
    FloatingActionButton fab_filter;
    String _where = "";
    ProgressBar progress_bar;

    void setValue(int vendorId, String vendorName, String monthYear) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.monthYear = monthYear;

        Log.d("--ID--", "Date: " + monthYear);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.vendor_ledger_tab_list, container, false);

        main(v);

        return v;

    }

    private void main(View v) {

        progress_bar = v.findViewById(R.id.progress_bar);
        fab_filter = v.findViewById(R.id.fab_filter);
        fab_filter.setVisibility(View.VISIBLE);
        fab_filter.setColorFilter(Color.WHITE);

        rv = v.findViewById(R.id.rv);
        ll_header = v.findViewById(R.id.ll_header);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);

        fillCustomerList();

        fab_filter.setOnClickListener(v1 -> applyFilters());

        clickOnAdapter();

        new PaymentReportAsyncTask(" AND [PaymentMounth] =".concat("'" + monthYear + "'").concat(" AND [Type] = 'Customer' "), "CustomerPayment", txt_nodata,
                getActivity(), paymentDetailsAdapter, progress_bar, rv).execute();


    }


    int mYear, mMonth, mDay;
    Calendar c;
    String _FromDate = "";
    String _ToDate = "";
    TextView txt_customer_name;
    String _receiptNo = "";
    String _fromAmt = "";
    String _toAmt = "";


    RadioButton rb_cash, rb_card, rb_other, rb_all;
    EditText edt_amt_from, edt_amt_to, edt_rec_no;
    ImageButton iv_clear_amt, rec_clear, iv_clear_customer, clear_date, bt_close;
    Button btn_search, btn_clear;
    TextView from_date, to_date;


    void applyFilters() {
        Dialog dialogPaymentFilter = new Dialog(getActivity());
        dialogPaymentFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPaymentFilter.setContentView(R.layout.dialog_apply_filters);
        Objects.requireNonNull(dialogPaymentFilter.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogPaymentFilter.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogPaymentFilter.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        from_date = dialogPaymentFilter.findViewById(R.id.from_date);
        to_date = dialogPaymentFilter.findViewById(R.id.to_date);
        clear_date = dialogPaymentFilter.findViewById(R.id.clear_date);
        bt_close = dialogPaymentFilter.findViewById(R.id.bt_close);


        txt_customer_name = dialogPaymentFilter.findViewById(R.id.txt_customer_name);
        iv_clear_customer = dialogPaymentFilter.findViewById(R.id.iv_clear_customer);


        btn_search = dialogPaymentFilter.findViewById(R.id.btn_search);
        btn_clear = dialogPaymentFilter.findViewById(R.id.btn_clear);


        edt_rec_no = dialogPaymentFilter.findViewById(R.id.edt_rec_no);
        rec_clear = dialogPaymentFilter.findViewById(R.id.rec_clear);


        rb_cash = dialogPaymentFilter.findViewById(R.id.rb_cash);
        rb_card = dialogPaymentFilter.findViewById(R.id.rb_card);
        rb_other = dialogPaymentFilter.findViewById(R.id.rb_other);
        rb_all = dialogPaymentFilter.findViewById(R.id.rb_all);


        edt_amt_from = dialogPaymentFilter.findViewById(R.id.edt_amt_from);
        edt_amt_to = dialogPaymentFilter.findViewById(R.id.edt_amt_to);
        iv_clear_amt = dialogPaymentFilter.findViewById(R.id.iv_clear_amt);


        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        from_date.setText(ClsGlobal.getChangeDateFormat(_FromDate));
        to_date.setText(ClsGlobal.getChangeDateFormat(_ToDate));

        from_date.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    (view1, year, month, day) -> {
                        c.set(year, month, day);

                        _FromDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                        from_date.setText(ClsGlobal.getChangeDateFormat(_FromDate));

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        to_date.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    (view12, year, month, day) -> {
                        c.set(year, month, day);
                        _ToDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                        to_date.setText(ClsGlobal.getChangeDateFormat(_ToDate));

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        clear_date.setOnClickListener(view -> {
            _FromDate = "";
            _ToDate = "";
            from_date.setText("");
            to_date.setText("");

//            _where = "";
//            filtersCondition();

        });


        txt_customer_name.setText(TextUtils.join(",", selectedCustomerName).toUpperCase());
        txt_customer_name.setOnClickListener(view -> {
            selectCustomer();

        });

        iv_clear_customer.setOnClickListener(view -> {
            txt_customer_name.setText("");
            _custMobile = "";
            selectedCustomerName.clear();

//            _where = "";
//            filtersCondition();

        });


        rec_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_rec_no.setText("");
//                _where = "";
//                filtersCondition();
            }
        });


        iv_clear_amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_amt_from.setText("");
                edt_amt_to.setText("");

//                _where = "";
//                filtersCondition();

            }
        });

        edt_rec_no.setText(_receiptNo);

        edt_amt_from.setText(_fromAmt);
        edt_amt_to.setText(_toAmt);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPaymentFilter.dismiss();
            }
        });


        btn_search.setOnClickListener(view -> {

            _receiptNo = edt_rec_no.getText().toString();
            _fromAmt = edt_amt_from.getText().toString();
            _toAmt = edt_amt_to.getText().toString();

            _where = "";
            filtersCondition();

            new PaymentReportAsyncTask(_where.concat(" AND [PaymentMounth] =".concat("'" + monthYear + "'").concat(" AND [Type] = 'Customer' ")),
                    "CustomerPayment", txt_nodata,
                    getActivity(), paymentDetailsAdapter, progress_bar, rv).execute();

            dialogPaymentFilter.dismiss();
            dialogPaymentFilter.hide();

        });

        btn_clear.setOnClickListener(view -> {

            _where = "";

//Date selection is Clear & reset.
            from_date.setText("");
            to_date.setText("");
            _FromDate = "";
            _ToDate = "";

//Customer Selection is Clear & reset.
            txt_customer_name.setText("");
            _custMobile = "";
            selectedCustomerName.clear();

//Receipt No is Clear & reset.
            edt_rec_no.setText("");
            _receiptNo = "";

//Amount is Clear & reset.
            edt_amt_from.setText("");
            edt_amt_to.setText("");
            _fromAmt = "";
            _toAmt = "";


        });

//        edt_rec_no.setFocusable(true);
//        edt_rec_no.setFocusableInTouchMode(true);

        dialogPaymentFilter.show();
        dialogPaymentFilter.getWindow().setAttributes(lp);
    }


    private void filtersCondition() {

        if (!_FromDate.equalsIgnoreCase("") && !_ToDate.equalsIgnoreCase("")) {

            _where = _where.concat(" AND [PaymentDate] between "
                    .concat("('".concat(_FromDate).concat("')"))
                    .concat(" AND ")
                    .concat("('".concat(_ToDate).concat("')")));

        } else if (!_FromDate.equalsIgnoreCase("")) {

            _where = _where.concat(" AND [PaymentDate] = ".concat("('"
                    .concat(_FromDate).concat("')")));

        } else if (!_ToDate.equalsIgnoreCase("")) {


            _where = _where.concat(" AND [PaymentDate] = ".concat("('"
                    .concat(_ToDate).concat("')")));
        }


        if (!_custMobile.equalsIgnoreCase("")) {


            _where = _where.concat(" AND [MobileNo] IN ")
                    .concat("(")
                    .concat(_custMobile)
                    .concat(")");
        }

        if (!_receiptNo.equalsIgnoreCase("")) {

            _where = _where.concat(" AND [ReceiptNo] LIKE ")
                    .concat("")
                    .concat(edt_rec_no.getText().toString().trim())
                    .concat("");
        }


        if (!_fromAmt.equalsIgnoreCase("") && !_toAmt.equalsIgnoreCase("")) {

            _where = _where.concat(" AND [Amount] between "
                    .concat("(".concat(edt_amt_from.getText().toString()).concat(")"))
                    .concat(" AND ")
                    .concat("(".concat(edt_amt_to.getText().toString()).concat(")")));

        } else if (!_fromAmt.equalsIgnoreCase("")) {

            _where = _where.concat(" AND [Amount] = ".concat("(".concat(edt_amt_from.getText().toString()).concat(")")));

        } else if (!_toAmt.equalsIgnoreCase("")) {

            _where = _where.concat(" AND [Amount] = ".concat("(".concat(edt_amt_to.getText().toString()).concat(")")));
        }


        if (rb_cash.isChecked()) {
            _where = _where.concat(" AND [PaymentMode] IN ")
                    .concat("('CASH')");
        } else if (rb_card.isChecked()) {
            _where = _where.concat(" AND [PaymentMode] IN ")
                    .concat("('CARD')");
        } else if (rb_other.isChecked()) {
            _where = _where.concat(" AND [PaymentMode] IN ")
                    .concat("('OTHER')");
        } else if (rb_all.isChecked()) {
            _where = _where.concat(" AND [PaymentMode] IN ")
                    .concat("('CASH','CARD','OTHER')");
        }

        Log.d("--_where--", "_where: " + _where);
    }

    ArrayList<String> selectedCustomerName = new ArrayList<>();

    private void selectCustomer() {

        final Dialog dialogExpenseType = new Dialog(getActivity());
        dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExpenseType.setContentView(R.layout.dialog_multiple_selection);
        dialogExpenseType.setTitle("Select Customer");


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogExpenseType.getWindow().setAttributes(lp);


        ListView listView = dialogExpenseType.findViewById(R.id.listview);
        Button btn_Select = dialogExpenseType.findViewById(R.id.button);

        dialogExpenseType.setCanceledOnTouchOutside(true);
        dialogExpenseType.setCancelable(true);


        final AdapterCustomers expAdapter = new AdapterCustomers(getActivity(),
                (ArrayList<ClsCustomerMaster>) list_customer);
        listView.setAdapter(expAdapter);


        btn_Select.setOnClickListener(v -> {
            boolean isSelected[] = expAdapter.getSelectedFlags();
            selectedCustomerName = new ArrayList<>();
            ArrayList<String> selectedCustomerNumber = new ArrayList<>();

            for (int i = 0; i < isSelected.length; i++) {
                if (isSelected[i]) {
                    selectedCustomerName.add(list_customer.get(i).getmName());
                    selectedCustomerNumber.add("'" + list_customer.get(i).getmMobile_No() + "'");
                }
            }

            txt_customer_name.setText(TextUtils.join(",", selectedCustomerName).toUpperCase());
//            txt_customer_name.setTag(TextUtils.join(",", selectedCustomerNumber));

            _custMobile = TextUtils.join(",", selectedCustomerNumber);

            dialogExpenseType.dismiss();
        });
        dialogExpenseType.show();
    }

    String _custMobile = "";

    void clickOnAdapter() {
        paymentDetailsAdapter = new PaymentDetailsAdapter(getActivity(), "CustomerPayment");

        paymentDetailsAdapter.SetOnClickListener((clsPaymentMaster, position) -> {
            final Dialog dialog = new Dialog(getContext());

            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_delete_edit);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);

            LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
            LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);
            LinearLayout ll_view_img = dialog.findViewById(R.id.ll_view_img);

            View vw_image = dialog.findViewById(R.id.vw_image);
            vw_image.setVisibility(View.VISIBLE);
            ll_view_img.setVisibility(View.VISIBLE);

            ll_delete.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog12, int which) {

                        ClsPaymentMaster obj = new ClsPaymentMaster();
                        obj.setReceiptNo(obj.getReceiptNo());
                        obj.setType(obj.getType());

                        int getResult = ClsPaymentMaster.deletePaymentReportData
                                (Integer.parseInt(clsPaymentMaster.getReceiptNo()),
                                        "Customer",
                                        getContext());

                        if (getResult == 1) {
                            Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            paymentDetailsAdapter.RemoveItem(position);
                        } else {
                            Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialog.setNegativeButton("NO", (dialog1, which) -> {
                    dialog1.dismiss();
                    dialog1.cancel();
                });
                // Showing Alert Message
                alertDialog.show();
            });

            ll_update.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                int ReceiptNo = Integer.parseInt(clsPaymentMaster.getReceiptNo());
                Intent intent = new Intent(getActivity(), AddPaymentActivity.class);
                intent.putExtra("type", "Customer");
                intent.putExtra("ReceiptNo", ReceiptNo);
                intent.putExtra("ClsPaymentMaster", clsPaymentMaster);
                startActivity(intent);
            });

            ll_view_img.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                int paymentID = clsPaymentMaster.getPaymentID();
                String customerName = clsPaymentMaster.getCustomerName();


                Log.d("--Image--", "paymentID: " + paymentID);


                Intent intent = new Intent(getActivity(), DisplayImageNewActivity.class);
                intent.putExtra("_type", "Customer Payment");
                intent.putExtra("_id", paymentID);
                intent.putExtra("_customerName", customerName);
                intent.putExtra("imgSave", "Preview");
                startActivity(intent);

            });
            dialog.show();
        });
        rv.setAdapter(paymentDetailsAdapter);
    }

    private List<ClsCustomerMaster> list_customer;

    private void fillCustomerList() {
        list_customer = new ArrayList<>();
        list_customer = new ClsCustomerMaster().getListCustomer("", getActivity());
        Log.e("list_customer", String.valueOf(list_customer.size()));
    }

    public static class AdapterCustomers extends BaseAdapter {

        private ArrayList<ClsCustomerMaster> data = new ArrayList<ClsCustomerMaster>();
        private Context context;
        private boolean isSelected[];

        public AdapterCustomers(Context context, ArrayList<ClsCustomerMaster> data) {
            this.context = context;
            this.data = data;
            isSelected = new boolean[data.size()];
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position); //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_listview, null);
                holder = new ViewHolder();
                holder.row_relative_layout = view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(data.get(position).getmName().toUpperCase()
                    .concat(" - ").concat(data.get(position).getmMobile_No()));

            holder.row_relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set the check text view
                    boolean flag = holder.checkedTextView.isChecked();
                    holder.checkedTextView.setChecked(!flag);
                    isSelected[position] = !isSelected[position];


                    if (holder.checkedTextView.isChecked()) {
                        holder.row_relative_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                    } else {
                        holder.row_relative_layout.setBackgroundResource(0);
                    }
                }
            });

            return view;
        }

        public boolean[] getSelectedFlags() {
            return isSelected;
        }

        private class ViewHolder {
            RelativeLayout row_relative_layout;
            CheckedTextView checkedTextView;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
//        getPaymentDetails("");

        new PaymentReportAsyncTask(" AND [PaymentMounth] =".concat("'" + monthYear + "'").concat(" AND [Type] = 'Customer' "), "CustomerPayment", txt_nodata,
                getActivity(), paymentDetailsAdapter, progress_bar, rv).execute();

    }


}
