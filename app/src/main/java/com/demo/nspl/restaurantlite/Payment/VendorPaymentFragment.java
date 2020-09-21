package com.demo.nspl.restaurantlite.Payment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class VendorPaymentFragment extends Fragment {

    int vendorId;
    String vendorName, monthYear;
    RecyclerView rv;
    TextView txt_nodata;
    LinearLayout ll_header;

    List<ClsPaymentMaster> list;
    PaymentDetailsAdapter paymentDetailsAdapter;
    FloatingActionButton fab_filter;
    String _where = "";

    ProgressBar progress_bar;

    void setValue(int vendorId, String vendorName, String monthYear) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.monthYear = monthYear;
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
        fab_filter.setColorFilter(Color.WHITE);
        fab_filter.setVisibility(View.VISIBLE);

        rv = v.findViewById(R.id.rv);
        ll_header = v.findViewById(R.id.ll_header);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);


        fillVendorList();

        fab_filter.setOnClickListener(v1 -> {
            applyFilters();
        });


        clickOnAdapter();

        new PaymentReportAsyncTask(" AND [PaymentMounth] =".concat("'" + monthYear + "'").concat(" AND [Type] = 'Vendor' "), "VendorPayment", txt_nodata,
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
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_apply_filters);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        from_date = dialog.findViewById(R.id.from_date);
        to_date = dialog.findViewById(R.id.to_date);
        clear_date = dialog.findViewById(R.id.clear_date);
        bt_close = dialog.findViewById(R.id.bt_close);


        txt_customer_name = dialog.findViewById(R.id.txt_customer_name);
        iv_clear_customer = dialog.findViewById(R.id.iv_clear_customer);


        btn_search = dialog.findViewById(R.id.btn_search);
        btn_clear = dialog.findViewById(R.id.btn_clear);


        edt_rec_no = dialog.findViewById(R.id.edt_rec_no);
        rec_clear = dialog.findViewById(R.id.rec_clear);


        rb_cash = dialog.findViewById(R.id.rb_cash);
        rb_card = dialog.findViewById(R.id.rb_card);
        rb_other = dialog.findViewById(R.id.rb_other);
        rb_all = dialog.findViewById(R.id.rb_all);


        edt_amt_from = dialog.findViewById(R.id.edt_amt_from);
        edt_amt_to = dialog.findViewById(R.id.edt_amt_to);
        iv_clear_amt = dialog.findViewById(R.id.iv_clear_amt);


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

//            _where = "";

            _FromDate = "";
            _ToDate = "";
            from_date.setText("");
            to_date.setText("");


        });


        txt_customer_name.setHint("SELECT VENDOR");
        txt_customer_name.setText(TextUtils.join(",", selectedVendorName).toUpperCase());
        txt_customer_name.setOnClickListener(view -> {
            selectVendor();
        });

        iv_clear_customer.setOnClickListener(view -> {

            txt_customer_name.setText("");
            vendorID = "";
            selectedVendorName.clear();
        });


        rec_clear.setOnClickListener(view -> {

            edt_rec_no.setText("");
        });


        iv_clear_amt.setOnClickListener(view -> {

            edt_amt_from.setText("");
            edt_amt_to.setText("");
        });

        edt_rec_no.setText(_receiptNo);

        edt_amt_from.setText(_fromAmt);
        edt_amt_to.setText(_toAmt);

        bt_close.setOnClickListener(v -> dialog.dismiss());


        btn_search.setOnClickListener(view -> {


            _receiptNo = edt_rec_no.getText().toString();
            _fromAmt = edt_amt_from.getText().toString();
            _toAmt = edt_amt_to.getText().toString();

            _where = "";
            filtersCondition();

            new PaymentReportAsyncTask(_where.concat(" AND [PaymentMounth] =".concat("'" + monthYear + "'").concat(" AND [Type] = 'Vendor' ")),
                    "VendorPayment", txt_nodata,
                    getActivity(), paymentDetailsAdapter, progress_bar, rv).execute();

            dialog.dismiss();
            dialog.hide();

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
            vendorID = "";
            selectedVendorName.clear();

//Receipt No is Clear & reset.
            edt_rec_no.setText("");
            _receiptNo = "";

//Amount is Clear & reset.
            edt_amt_from.setText("");
            edt_amt_to.setText("");
            _fromAmt = "";
            _toAmt = "";


        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

        if (!vendorID.equalsIgnoreCase("")) {
            _where = _where.concat(" AND [VendorID] IN ")
                    .concat("(")
                    .concat(vendorID)
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

    }


    ArrayList<String> selectedVendorName = new ArrayList<>();
    List<ClsVendor> lstClsVendors;

    private void fillVendorList() {
        lstClsVendors = new ArrayList<>();
        lstClsVendors = new ClsVendor(getActivity()).getList(" AND [ACTIVE] = 'YES' AND [Type] IN ('SUPPLIER','BOTH') ORDER BY [VENDOR_NAME]");

    }

    private void selectVendor() {

        final Dialog dialogExpenseType = new Dialog(getActivity());
        dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExpenseType.setContentView(R.layout.dialog_multiple_selection);
        dialogExpenseType.setTitle("Select Vendor");


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogExpenseType.getWindow().setAttributes(lp);

        ListView listView = dialogExpenseType.findViewById(R.id.listview);
        Button btn_Select = dialogExpenseType.findViewById(R.id.button);

        dialogExpenseType.setCanceledOnTouchOutside(true);
        dialogExpenseType.setCancelable(true);


        final AdapterVendors expAdapter = new AdapterVendors(getActivity(),
                (ArrayList<ClsVendor>) lstClsVendors);

        listView.setAdapter(expAdapter);


        btn_Select.setOnClickListener(v -> {
            boolean isSelected[] = expAdapter.getSelectedFlags();
            selectedVendorName = new ArrayList<>();
            ArrayList<String> selectedVendorID = new ArrayList<>();

            for (int i = 0; i < isSelected.length; i++) {
                if (isSelected[i]) {
                    selectedVendorName.add(lstClsVendors.get(i).getVendor_name());
                    selectedVendorID.add("'" + lstClsVendors.get(i).getVendor_id() + "'");
                }
            }

            txt_customer_name.setText(TextUtils.join(",", selectedVendorName).toUpperCase());
//            txt_customer_name.setTag(TextUtils.join(",", selectedVendorID));
            vendorID = TextUtils.join(",", selectedVendorID);

            dialogExpenseType.dismiss();
        });
        dialogExpenseType.show();

    }

    String vendorID = "";

    public class AdapterVendors extends BaseAdapter {

        private ArrayList<ClsVendor> data = new ArrayList<ClsVendor>();
        private Context context;
        private boolean isSelected[];

        public AdapterVendors(Context context, ArrayList<ClsVendor> data) {
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
                holder = new AdapterVendors.ViewHolder();
                holder.row_relative_layout = view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (AdapterVendors.ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(data.get(position).getVendor_name().toUpperCase());

            holder.row_relative_layout.setOnClickListener(v -> {
                // set the check text view
                boolean flag = holder.checkedTextView.isChecked();
                holder.checkedTextView.setChecked(!flag);
                isSelected[position] = !isSelected[position];


                if (holder.checkedTextView.isChecked()) {
                    holder.row_relative_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                } else {
                    holder.row_relative_layout.setBackgroundResource(0);
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


    void clickOnAdapter() {

        paymentDetailsAdapter = new PaymentDetailsAdapter(getActivity(), "VendorPayment");


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
                alertDialog.setPositiveButton("YES", (dialog12, which) -> {


                    ClsPaymentMaster obj = new ClsPaymentMaster();
                    obj.setReceiptNo(obj.getReceiptNo());
                    obj.setType(obj.getType());

                    int getResult = ClsPaymentMaster.deletePaymentReportData
                            (Integer.parseInt(clsPaymentMaster.getReceiptNo()),
                                    "Vendor",
                                    getContext());

                    if (getResult == 1) {
                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        paymentDetailsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
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
                intent.putExtra("type", "Vendor");
                intent.putExtra("ReceiptNo", ReceiptNo);
                startActivity(intent);

            });


            ll_view_img.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                int paymentID = clsPaymentMaster.getPaymentID();
                String vendorName = clsPaymentMaster.getVendorName();

                Log.d("--Image--", "vendorID: " + vendorID);
                Log.d("--Image--", "vendorName: " + vendorName);

                Intent intent = new Intent(getActivity(), DisplayImageNewActivity.class);
                intent.putExtra("_type", "Vendor Payment");
                intent.putExtra("_id", paymentID);
                intent.putExtra("_customerName", vendorName);
                intent.putExtra("imgSave", "Preview");
                startActivity(intent);

            });

            dialog.show();
        });


        rv.setAdapter(paymentDetailsAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        new PaymentReportAsyncTask(" AND [PaymentMounth] =".concat("'" + monthYear + "'").concat(" AND [Type] = 'Vendor' "), "VendorPayment", txt_nodata,
                getActivity(), paymentDetailsAdapter, progress_bar, rv).execute();

    }
}
