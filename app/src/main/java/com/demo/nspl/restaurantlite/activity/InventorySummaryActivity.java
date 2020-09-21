package com.demo.nspl.restaurantlite.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.InventorySummaryAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.classes.ClsVendor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class InventorySummaryActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    InventorySummaryAdapter cu;
    private List<ClsInventoryStock> list_inventorystock;
    private String Id;
    String formattedDate;
    String Unit;
    ImageButton filter;
    int mYear, mMonth, mDay;
    private ClsVendor ObjVendor = new ClsVendor();
    private Dialog dialogvendor;
    Dialog dialog;
    private StickyListHeadersListView lst;
    private RelativeLayout lyout_nodata;
    String _VendorName;
    int _vendorID;
    TextView txt_vendor;
    private List<ClsVendor> listVendorSearch;
    private List<ClsVendor> list_vendor;
    TextView selected_vendor, vendor_id;
    private CustomAdapter adapter;
    LinearLayout nodata_layout;
    private TextView empty_title_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_summary);
        dialog = new Dialog(InventorySummaryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filters);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);



        final TextView to_date = dialog.findViewById(R.id.to_date);
        final TextView from_date = dialog.findViewById(R.id.from_date);
        final RadioGroup rg = dialog.findViewById(R.id.rg);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "InventorySummaryActivity"));
        }
        ImageButton cancel_vendor = dialog.findViewById(R.id.cancel_vendor);
        txt_vendor = dialog.findViewById(R.id.txt_vendor);
        selected_vendor = dialog.findViewById(R.id.selected_vendor);
        vendor_id = dialog.findViewById(R.id.vendor_id);
        empty_title_text = findViewById(R.id.empty_title_text);

        final RadioButton rbAll = dialog.findViewById(R.id.rbAll);
        final RadioButton rbIn = dialog.findViewById(R.id.rbIn);
        final RadioButton rbOut = dialog.findViewById(R.id.rbOut);
        Button btn_search = dialog.findViewById(R.id.btn_search);
        Button btn_clear = dialog.findViewById(R.id.btn_clear);
        lyout_nodata = findViewById(R.id.lyout_nodata);
        nodata_layout = findViewById(R.id.nodata_layout);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(InventorySummaryActivity.this,
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

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(InventorySummaryActivity.this,
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

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromdate = from_date.getText().toString();
                String todate = to_date.getText().toString();

                String vendor = vendor_id.getText().toString();//
                String type = rbAll.isChecked() ? "All" : rbIn.isChecked() ? "IN" : "OUT";


                Log.e("FilterData", "Todate" + todate);
                Log.e("FilterData", "Fromdate" + fromdate);
                Log.e("FilterData", "Vendor" + vendor);
                Log.e("FilterData", "type" + type);

                //tblstock
                //VM
                String _where = "";

                if (!fromdate.isEmpty() && !todate.isEmpty()) {
                    _where = _where.concat(" AND tblstock.[TRANSACTION_DATE] between "
                            .concat("('".concat(fromdate).concat("')"))
                            .concat(" AND ")
                            .concat("('".concat(todate).concat("')")));
                } else if (!fromdate.isEmpty()) {
                    _where = _where.concat(" AND tblstock.[TRANSACTION_DATE] = ".concat("('".concat(fromdate).concat("')")));
                } else if (!todate.isEmpty()) {
                    _where = _where.concat(" AND tblstock.[TRANSACTION_DATE] = ".concat("('".concat(todate).concat("')")));
                }
//
                if (!vendor.isEmpty()) {
                    _where = _where.concat(" AND tblstock.[VENDOR_ID] IN ")
                            .concat("(")
                            .concat(vendor.trim())
                            .concat(")");
                }

                if (!type.isEmpty() && !type.equals("All")) {
                    _where = _where.concat(" AND tblstock.[TYPE] = ".concat("('".concat(type).concat("')")));
                    Log.e("ALLL", "if");
                } else if (!type.isEmpty() && type.equals("All")) {
                    _where = _where;
                    Log.e("ALLL", "Else if");
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
                rbAll.setChecked(true);

                Log.e("Clear", "Date" + to_date.getText().toString());
                Log.e("Clear", "FromDate" + from_date.getText().toString());
                Log.e("Clear", "SelectedVendor" + selected_vendor.getText().toString());
                Log.e("Clear", "Radio" + rbAll.getText().toString());
            }
        });

        txt_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogvendor = new Dialog(InventorySummaryActivity.this);
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
                adapter = new CustomAdapter(InventorySummaryActivity.this, (ArrayList<ClsVendor>) list_vendor);
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

                        Toast.makeText(InventorySummaryActivity.this, "Selected: " + selectedItems.toString(), Toast.LENGTH_SHORT).show();
                        dialogvendor.dismiss();
                    }
                });

                dialogvendor.show();


            }

        });
        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        filter = findViewById(R.id.filter);
//      dialog = new Dialog(InventorySummaryActivity.this);

        Id = getIntent().getStringExtra("ItemId");
        String ItemName = getIntent().getStringExtra("ItemName");
        Unit = getIntent().getStringExtra("Unit");

        Log.e("Summary", "Id-->>" + Id);
        Log.e("Summary", "Name-->>" + ItemName);


        TextView title = toolbar.findViewById(R.id.title);
        TextView unit_name = toolbar.findViewById(R.id.unit_name);

        title.setText(ItemName);
        unit_name.setText(Unit);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillVendorList();
                dialog.show();
            }
        });


        //startDate
        //endDate

        ViewData("");

    }


    @Override
    public void onResume() {
        super.onResume();
        ViewData("");

    }

    private void fillVendorList() {
        list_vendor = new ArrayList<>();
        listVendorSearch = list_vendor = new ClsVendor(getApplicationContext()).getList(" AND [ACTIVE]='YES' ORDER BY [VENDOR_NAME]");
    }


    private void ViewData(String _w) {
        String _where = _w;


        if (getIntent().getStringExtra("whereCondition") != null) {
            _where = _where.concat(getIntent().getStringExtra("whereCondition"));
        }

        if (Id != null && Id != "" && Id != "0") {
            _where = " AND tblstock.[INVENTORY_ITEM_ID] = "
                    .concat(Id)
                    .concat(" ")
                    .concat(_w);
        }

        rv.setLayoutManager(new LinearLayoutManager(InventorySummaryActivity.this));
        list_inventorystock = new ArrayList<>();
        list_inventorystock = new ClsInventoryStock(InventorySummaryActivity.this).getList(_where, "general");
        if (list_inventorystock.size() == 0) {
            nodata_layout.setVisibility(View.VISIBLE);
            // empty_title_text.setVisibility(View.VISIBLE);
        } else {
            nodata_layout.setVisibility(View.GONE);
            //empty_title_text.setVisibility(View.INVISIBLE);
        }

        cu = new InventorySummaryAdapter(InventorySummaryActivity.this, InventorySummaryActivity.this, (ArrayList<ClsInventoryStock>) list_inventorystock);
        rv.setAdapter(cu);
        cu.notifyDataSetChanged();
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


}
