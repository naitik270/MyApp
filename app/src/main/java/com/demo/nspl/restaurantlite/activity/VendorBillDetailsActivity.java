package com.demo.nspl.restaurantlite.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Adapter.VendorBillAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;
import com.demo.nspl.restaurantlite.classes.ClsVendor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class VendorBillDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    StickyListHeadersListView rv;
    private List<ClsExpenseMasterNew> list_expense;
    VendorBillAdapter cu;
    LinearLayout nodata_layout;
    TextView toolbar_title;
    ImageButton filter;
    private Dialog dialog;
    int mYear, mMonth, mDay;
    private Dialog dialogvendor,dialogExpenseType;
    TextView txt_vendor,txt_expensetype,empty_title_text;
    private List<ClsVendor> list_vendor;
    List<ClsExpenseType> list_expensetype;
    TextView selected_vendor, vendor_id,expensetype_id;
    private List<ClsVendor> listVendorSearch;
    private List<ClsExpenseType> listExpenseTypeSearch;
    private CustomAdapter adapter;
    private AdapterExpenseType expAdapter;
    EditText edt_rec_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_bill_details);

        empty_title_text = findViewById(R.id.empty_title_text);

            toolbar=findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            dialog = new Dialog(VendorBillDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_vendorbill_details);
            dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);


        rv=findViewById(R.id.rv);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            nodata_layout=findViewById(R.id.nodata_layout);

        toolbar_title=toolbar.findViewById(R.id.toolbar_title);

        filter=toolbar.findViewById(R.id.filter);

        final TextView to_date = dialog.findViewById(R.id.to_date);
        final TextView from_date = dialog.findViewById(R.id.from_date);
        txt_expensetype= dialog.findViewById(R.id.txt_expensetype);
        final TextView selected_expensetype= dialog.findViewById(R.id.selected_expensetype);
         edt_rec_no= dialog.findViewById(R.id.edt_rec_no);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "VendorBillDetailsActivity"));
        }

        ImageButton cancel_vendor = dialog.findViewById(R.id.cancel_vendor);
        ImageButton cancel_expensetype = dialog.findViewById(R.id.cancel_expensetype);
        txt_vendor = dialog.findViewById(R.id.txt_vendor);
        selected_vendor = dialog.findViewById(R.id.selected_vendor);
        vendor_id = dialog.findViewById(R.id.vendor_id);
        expensetype_id= dialog.findViewById(R.id.expensetype_id);


        Button btn_search = dialog.findViewById(R.id.btn_search);
        Button btn_clear = dialog.findViewById(R.id.btn_clear);
        ImageButton rec_clear = dialog.findViewById(R.id.rec_clear);

        //No data layout is remaining......
//        lyout_nodata = findViewById(R.id.lyout_nodata);
        nodata_layout = findViewById(R.id.nodata_layout);

        toolbar_title.setText("Vendor Bill Details");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        rec_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_rec_no.setText("");
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(VendorBillDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(year, month, day);
                                String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
                                to_date.setText(date);

                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

                Calendar d = Calendar.getInstance();
                dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });


        cancel_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_vendor.setText("");
                vendor_id.setText("");
            }
        });


        cancel_expensetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_expensetype.setText("");
                expensetype_id.setText("");
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromdate = from_date.getText().toString();
                String todate = to_date.getText().toString();

                String vendor = vendor_id.getText().toString();
                String expenseType = expensetype_id.getText().toString();
                String RecNo = edt_rec_no.getText().toString();
                Log.e("FilterData", "Todate" + todate);
                Log.e("FilterData", "Fromdate" + fromdate);
                Log.e("FilterData", "Vendor" + vendor);
                Log.e("FilterData", "ExpenseType" + expenseType);
                Log.e("FilterData", "RecNo" + RecNo);


                //tblstock
                //VM
                String _where = "";

                if (!fromdate.isEmpty() && !todate.isEmpty()) {
                    _where = _where.concat(" AND [RECEIPT_DATE] between "
                            .concat("('".concat(fromdate).concat("')"))
                            .concat(" AND ")
                            .concat("('".concat(todate).concat("')")));
                } else if (!fromdate.isEmpty()) {
                    _where = _where.concat(" AND [RECEIPT_DATE] = ".concat("('".concat(fromdate).concat("')")));
                } else if (!todate.isEmpty()) {
                    _where = _where.concat(" AND [RECEIPT_DATE] = ".concat("('".concat(todate).concat("')")));
                }
                if (!vendor.isEmpty()) {
                        _where = _where.concat(" AND [VENDOR_ID] IN ")
                            .concat("(")
                            .concat(vendor.trim())
                            .concat(")");
                }

                if (!expenseType.isEmpty()) {
                    _where = _where.concat(" AND [EXPENSE_TYPE_ID] IN ")
                            .concat("(")
                            .concat(expenseType.trim())
                            .concat(")");
                }


                if (!RecNo.isEmpty()) {
                    _where = _where.concat(" AND [RECEIPT_NO] LIKE ")
                            .concat("'%")
                            .concat(RecNo.trim())
                            .concat("'%");
                }

                ViewData(_where);
                dialog.hide();


            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_date.setText("");
                from_date.setText("");
                selected_vendor.setText("");
                vendor_id.setText("");
                selected_expensetype.setText("");
                expensetype_id.setText("");
                edt_rec_no.setText("");


                Log.e("Clear", "Date" + to_date.getText().toString());
                Log.e("Clear", "FromDate" + from_date.getText().toString());
                Log.e("Clear", "SelectedVendor" + selected_vendor.getText().toString());

            }
        });

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(VendorBillDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(year, month, day);
                                String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
                                from_date.setText(date);
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

                Calendar d = Calendar.getInstance();

                dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        txt_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogvendor = new Dialog(VendorBillDetailsActivity.this);
                dialogvendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogvendor.setContentView(R.layout.dialog_multiple_selection);
                dialogvendor.setTitle("Select Vendor");

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogvendor.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogvendor.getWindow().setAttributes(lp);

                ListView listView = dialogvendor.findViewById(R.id.listview);
                Button btn_Select = dialogvendor.findViewById(R.id.button);
                dialogvendor.setCanceledOnTouchOutside(true);
                dialogvendor.setCancelable(true);
                list_vendor = new ArrayList<>();
                listVendorSearch = list_vendor = new ClsVendor(getApplicationContext()).getList(" AND [ACTIVE]='YES' ORDER BY [VENDOR_NAME]");
                adapter = new CustomAdapter(VendorBillDetailsActivity.this, (ArrayList<ClsVendor>) list_vendor);
                listView.setAdapter(adapter);

                btn_Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelected[] = adapter.getSelectedFlags();
                        ArrayList<String> selectedItems = new ArrayList<>();
                        ArrayList<String> vendorId = new ArrayList<>();

                        for (int i = 0; i < isSelected.length; i++) {
                            if (isSelected[i]) {
                                selectedItems.add(list_vendor.get(i).getVendor_name());
                                vendorId.add(String.valueOf(list_vendor.get(i).getVendor_id()));
                            }
                        }
                        selected_vendor.setText(TextUtils.join(",", selectedItems));
                        vendor_id.setText(TextUtils.join(",", vendorId));

                        Toast.makeText(VendorBillDetailsActivity.this, "Selected: " + selectedItems.toString(), Toast.LENGTH_SHORT).show();
                        dialogvendor.dismiss();
                    }
                });

                dialogvendor.show();


            }

        });


        txt_expensetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogExpenseType = new Dialog(VendorBillDetailsActivity.this);
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
                list_expensetype = new ArrayList<>();
                listExpenseTypeSearch = list_expensetype = new ClsExpenseType(getApplicationContext()).getList(" AND [ACTIVE]='YES' ORDER BY [EXPENSE_TYPE_NAME]");
                expAdapter = new AdapterExpenseType(VendorBillDetailsActivity.this, (ArrayList<ClsExpenseType>) list_expensetype);
                listView.setAdapter(expAdapter);

                btn_Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelected[] = expAdapter.getSelectedFlags();
                        ArrayList<String> selectedItems = new ArrayList<>();
                        ArrayList<String> ExpenseTypeId = new ArrayList<>();

                        for (int i = 0; i < isSelected.length; i++) {
                            if (isSelected[i]) {
                                selectedItems.add(list_expensetype.get(i).getExpense_type_name());
                                ExpenseTypeId.add(String.valueOf(list_expensetype.get(i).getExpense_type_id()));
                            }
                        }
                        selected_expensetype.setText(TextUtils.join(",", selectedItems));
                        expensetype_id.setText(TextUtils.join(",", ExpenseTypeId));

                        Toast.makeText(VendorBillDetailsActivity.this, "Selected: " + selectedItems.toString(), Toast.LENGTH_SHORT).show();
                        dialogExpenseType.dismiss();
                    }
                });

                dialogExpenseType.show();


            }

        });



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        ViewData("");

    }



    @Override
    public void onResume() {
        super.onResume();
        ViewData("");

    }

    private void ViewData(String _w) {
        String _where = _w;


        if (getIntent().getStringExtra("whereCondition") != null) {
            _where  = _where .concat( getIntent().getStringExtra("whereCondition"));
        }

//        rv.setLayoutManager(new LinearLayoutManager(VendorBillDetailsActivity.this));
        list_expense = new ArrayList<>();
        list_expense = new ClsExpenseMasterNew(VendorBillDetailsActivity.this).getListVendor(_where);

        if(list_expense.size() == 0){
            empty_title_text.setVisibility(View.VISIBLE);
        }else {
            empty_title_text.setVisibility(View.INVISIBLE);
        }

//        cu = new VendorBillAdapter(VendorBillDetailsActivity.this,VendorBillDetailsActivity.this, list_expense);
//        rv.setAdapter(cu);




        if (list_expense != null && list_expense.size() != 0) {



            List<String> uniqueList = new ArrayList<>();
            for (ClsExpenseMasterNew _ObjExp : list_expense) {
                if (!uniqueList.contains(ClsGlobal.getMonthYear(_ObjExp.getReceipt_date()))) {
                    uniqueList.add(ClsGlobal.getMonthYear(_ObjExp.getReceipt_date()));
                }
            }

            int srNO = 0;
            for (String monthName : uniqueList) {
                if (srNO > 62) {
                    srNO = 0;
                }
                String uniqueID = ClsGlobal.getMonthIndex(srNO);
                Double finalAmount = 0.0;
                //String uniqueID = ClsGlobalDatabase.getMonthIndex(uniqueList.indexOf(monthName));//a,b
                for (ClsExpenseMasterNew _ObjExp : list_expense) {
                    if (monthName.equalsIgnoreCase(ClsGlobal.getMonthYear(_ObjExp.getReceipt_date()))) {
                        //assign unique index/id/position
                        //  finalAmount = finalAmount + _ObjExp.getGRAND_TOTAL();
                        _ObjExp.setMonthUniqueIndex(uniqueID);
                        //_ObjExp.set_finalAmount(finalAmount);
                        //set in list
                        list_expense.set(list_expense.indexOf(_ObjExp), _ObjExp);
                    }
                }
                srNO++;
            }
            cu = new VendorBillAdapter(VendorBillDetailsActivity.this,VendorBillDetailsActivity.this,(ArrayList<ClsExpenseMasterNew>) list_expense);
            rv.setAdapter(cu);
            cu.notifyDataSetChanged();
        }

        else {
            nodata_layout.setVisibility(View.GONE);
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CustomAdapter extends BaseAdapter {
        private ArrayList<ClsVendor> list_vendor = new ArrayList<ClsVendor>();
        private Context context;
        private boolean isSelected[];

        public CustomAdapter(Context context, ArrayList<ClsVendor> counteryList) {
            this.context = context;
            this.list_vendor = counteryList;
            isSelected = new boolean[counteryList.size()];
        }

        @Override
        public int getCount() {
            return list_vendor.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_listview, null);
                holder = new ViewHolder();
                holder.relativeLayout = (RelativeLayout) view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = (CheckedTextView) view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(String.valueOf(list_vendor.get(position).getVendor_name()));
//            holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set the check text view
                    boolean flag = holder.checkedTextView.isChecked();
                    holder.checkedTextView.setChecked(!flag);
                    isSelected[position] = !isSelected[position];

                    if (holder.checkedTextView.isChecked()) {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                    } else {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundResource(0);
                    }
                }
            });

            return view;
        }

        public boolean[] getSelectedFlags() {
            return isSelected;
        }

        private class ViewHolder {
            RelativeLayout relativeLayout;
            CheckedTextView checkedTextView;
        }

    }

    public class AdapterExpenseType extends BaseAdapter {
        private ArrayList<ClsExpenseType> list_expense_type= new ArrayList<ClsExpenseType>();
        private Context context;
        private boolean isSelected[];

        public AdapterExpenseType(Context context, ArrayList<ClsExpenseType> counteryList) {
            this.context = context;
            this.list_expense_type = counteryList;
            isSelected = new boolean[counteryList.size()];
        }

        @Override
        public int getCount() {
            return list_expense_type.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_listview, null);
                holder = new ViewHolder();
                holder.relativeLayout = (RelativeLayout) view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = (CheckedTextView) view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(String.valueOf(list_expense_type.get(position).getExpense_type_name()));
//            holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set the check text view
                    boolean flag = holder.checkedTextView.isChecked();
                    holder.checkedTextView.setChecked(!flag);
                    isSelected[position] = !isSelected[position];

                    if (holder.checkedTextView.isChecked()) {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                    } else {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundResource(0);
                    }
                }
            });

            return view;
        }

        public boolean[] getSelectedFlags() {
            return isSelected;
        }

        private class ViewHolder {
            RelativeLayout relativeLayout;
            CheckedTextView checkedTextView;
        }

    }



}
