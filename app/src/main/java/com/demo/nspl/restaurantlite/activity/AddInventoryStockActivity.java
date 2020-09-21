package com.demo.nspl.restaurantlite.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Adapter.InventoryItemAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryItem;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AddInventoryStockActivity extends AppCompatActivity {

    TextView txtdropdown_inventory_item, txt_transaction_date, entry_date, inventorystock_ID, unit;
    EditText edit_remark, edit_qty, edit_amount;
    Button btnSave;
    TextView vendor_ID, inventoryitem_ID, order_ID, txt_type;
    static final int DATE_DIALOG_ID = 0;
    Toolbar toolbar;
//    private ProgressDialog pd;
    private int result;
    private ClsInventoryItem ObjInv = new ClsInventoryItem();
    private List<ClsInventoryStock> list_stock;
    private ClsVendor ObjVendor = new ClsVendor();
    StickyListHeadersListView stickyListview;
    private Dialog dialog;
    private List<ClsInventoryItem> list_inventoryitem;
    InventoryItemAdapter itemAdapter;
    private PopUpPageAdapter adapter;
    int flag = 0;
    String receiptDate = "";
    String radioVal = "", formattedDate, upt = "0:0:0";
    int mYear, mMonth, mDay;
    int _ID;
    private Dialog dialogItem;
    private StickyListHeadersListView lst;
    private RelativeLayout lyout_nodata;
    private int _vendorID;
    private String _VendorName;
    private List<ClsInventoryItem> listItem;
    String type = "";
    String VendorId;
    String VendorName="";
    Integer OrderId=0;
    String Date="";
    String SOURCE="";
    String Amount="";
    View view_amount;
    TextInputLayout input_amount;
    TextView vendor_name;
    private ClsInventoryStock objStock= new ClsInventoryStock();
    private int StockId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory_stock);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vendor_name=findViewById(R.id.vendor_name);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }catch (Exception e){
            Log.e("Exw","Ex call");
        }

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddInventoryStockActivity"));
        }
        type = getIntent().getStringExtra("TYPE");
        VendorId = getIntent().getStringExtra("VendorId");
        VendorName = getIntent().getStringExtra("VendorName");
        OrderId = getIntent().getIntExtra("OrderId",0);
        Date = getIntent().getStringExtra("Date");
        SOURCE = getIntent().getStringExtra("SOURCE");
        Amount = getIntent().getStringExtra("Amount");


        StockId=getIntent().getIntExtra("StockID",0);
        SOURCE=getIntent().getStringExtra("SOURCE");
        Log.e("StockID","IS"+StockId );
        Log.e("StockID","SOURCE"+SOURCE );

        Log.e("ORDERRR","ID--->>"+OrderId );

        TextView title = toolbar.findViewById(R.id.title);
        TextView vendor_name = toolbar.findViewById(R.id.vendor_name);

        TextView typeTxt=toolbar.findViewById(R.id.type);

            title.setText("Item Entry");
            vendor_name.setText(VendorName);
            typeTxt.setText(type);



        Log.e("Types", "Direct-->>" + type);
        Log.e("Types", "From VendorID-->>" + VendorId);
        Log.e("Types", "From VendorName-->>" + VendorName);
        Log.e("Types", "From OrderID-->>" + OrderId);
        Log.e("Types", "From Date-->>" + Date);
        Log.e("Types", "SOURCE-->>" + SOURCE);


        txtdropdown_inventory_item = findViewById(R.id.txtdropdown_inventory_item);
        txt_transaction_date = findViewById(R.id.txt_transaction_date);
        entry_date = findViewById(R.id.entry_date);
        inventorystock_ID = findViewById(R.id.inventorystock_ID);
        edit_amount = findViewById(R.id.edit_amount);
        edit_qty = findViewById(R.id.edit_qty);
        order_ID = findViewById(R.id.order_ID);
        txt_type = findViewById(R.id.txt_type);


        edit_remark = findViewById(R.id.edit_remark);
        input_amount = findViewById(R.id.input_amount);
        btnSave = findViewById(R.id.btnSave);

        vendor_ID = findViewById(R.id.vendor_ID);
        inventoryitem_ID = findViewById(R.id.inventoryitem_ID);
        unit = findViewById(R.id.unit);


        if(StockId!=0)
        {
            objStock.setStock_id(StockId);
            objStock = objStock.getObject(StockId);
            type="IN";
            OrderId = objStock.getOrder_id();
            txt_transaction_date.setText(objStock.getTrasaction_date());
            txtdropdown_inventory_item.setText(objStock.getInventory_item_name());
            edit_amount.setText(String.valueOf(objStock.getAmount()));
            edit_qty.setText(String.valueOf(objStock.getQty()));
            edit_remark.setText(objStock.getRemark());
            vendor_name.setText(objStock.getVendor_name());
            vendor_ID.setText(String.valueOf(objStock.getVendror_id()));


        }


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        if(SOURCE!=null && SOURCE.equalsIgnoreCase("BILL ENTRY"))
        {
            txt_transaction_date.setText(Date);
            input_amount.setVisibility(View.VISIBLE);
            Log.e("Visibility","---"+"Visible" );

            vendor_name.setVisibility(View.VISIBLE);
//            edit_amount.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_transaction_date.setText(formattedDate);
            input_amount.setVisibility(View.GONE);

            vendor_name.setVisibility(View.GONE);
            Log.e("Visibility","---"+"Gone");
//            edit_amount.setVisibility(View.GONE);
        }
        entry_date.setText(formattedDate);

        BeforeDate();


        txtdropdown_inventory_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AddInventoryStockActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sticky_header_popup);
                dialog.setCancelable(true);

                final ClsInventoryItem Obj = new ClsInventoryItem(AddInventoryStockActivity.this);
                stickyListview = (StickyListHeadersListView) dialog.findViewById(R.id.stickyListview);

                list_inventoryitem = new ArrayList<>();
                list_inventoryitem = new ClsInventoryItem(AddInventoryStockActivity.this).getList("");

                adapter = new PopUpPageAdapter(AddInventoryStockActivity.this,list_inventoryitem);
                stickyListview.setAdapter(adapter);
                dialog.show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
                if (validation()) {
//                    pd = new ProgressDialog(AddInventoryStockActivity.this);
//                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    pd.setMessage("Loading..");
//                    pd.setIndeterminate(true);
//                    pd.setCancelable(true);
//                    pd.show();

//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
                         int result;

//                        @Override
//                        public void run() {
                            ClsInventoryStock objInventoryStock = new ClsInventoryStock(AddInventoryStockActivity.this);
                            objInventoryStock.setStock_id(StockId);
                            objInventoryStock.setOrder_id(OrderId);

                            if(SOURCE!=null && SOURCE.equalsIgnoreCase("BILL ENTRY"))
                            {
                                objInventoryStock.setVendor_name(VendorName);
                                objInventoryStock.setAmount(Double.valueOf(edit_amount.getText().toString().trim()));
                                objInventoryStock.setVendror_id(vendor_ID.getText() == null || vendor_ID.getText().toString().isEmpty() ? 0 : Integer.parseInt(vendor_ID.getText().toString()));
                            }
                            else
                            {
                                objInventoryStock.setVendor_name("");
                                objInventoryStock.setAmount(0.0);
                                objInventoryStock.setVendror_id(0);
                            }

                            objInventoryStock.setInventory_item_id(ObjInv.getInventory_item_id());
                            Log.e("Inventory", "getInventory_item_id-- " + String.valueOf(ObjInv.getInventory_item_id()));
                            objInventoryStock.setInventory_item_name(txtdropdown_inventory_item.getText().toString().trim());
                            objInventoryStock.setObjInventoryItem(ObjInv);
                            objInventoryStock.setQty(Double.valueOf(edit_qty.getText().toString().trim()));
                            objInventoryStock.setType(type);
                            objInventoryStock.setEntry_date(entry_date.getText().toString().trim());
                            objInventoryStock.setTrasaction_date(txt_transaction_date.getText().toString().trim());
                            objInventoryStock.setRemark(edit_remark.getText().toString().trim());
                            if (objInventoryStock.getStock_id() != 0) {
                                result = ClsInventoryStock.Update(objInventoryStock);
                                Log.e("result", String.valueOf(result));

                                if(result==1)
                                {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddInventoryStockActivity.this);
                                    alertDialog.setMessage("Inventory updated successfully");
                                    alertDialog.setIcon(R.drawable.confirm);
                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                                    alertDialog.show();

                                }

                            } else {
                                result = ClsInventoryStock.Insert(ObjInv, ObjVendor, objInventoryStock);
                                if (result == 1) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddInventoryStockActivity.this);
                                    final AlertDialog OptionDialog = builder.create();
                                    builder.setMessage("Inventory Item  added successfully");
                                    builder.setIcon(R.drawable.ic_delete_black_24dp);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            if(SOURCE!=null && SOURCE.equalsIgnoreCase("BILL ENTRY"))
                                            {
                                                Toast.makeText(AddInventoryStockActivity.this, "Bill Activity...", Toast.LENGTH_SHORT).show();
                                                OptionDialog.dismiss();
                                                OptionDialog.cancel();
                                               // txt_transaction_date.setText("");
                                                txtdropdown_inventory_item.setText("Select Item");
                                                edit_qty.setText("");
                                                edit_amount.setText("");
                                                edit_remark.setText("");
//                                                pd.dismiss();
//                                                finish();


                                            }

                                            else {
                                                OptionDialog.dismiss();
                                                OptionDialog.cancel();
//                                                pd.dismiss();
                                                finish();
                                            }
                                        }
                                    });
                                    builder.show();

                                }
                            }
//                        }
//                    }, 3000);
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    void filter(String text) {
        if (!text.isEmpty()) {

            List<ClsInventoryItem> temp = new ArrayList();
            for (ClsInventoryItem objvClsVendorMaster : list_inventoryitem) {
                if (objvClsVendorMaster.getInventory_item_name().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(objvClsVendorMaster);
                }
            }

            list_inventoryitem = temp;

        } else {
//            list_inventoryitem = l;
        }

        adapter = new PopUpPageAdapter(AddInventoryStockActivity.this, list_inventoryitem);
        lst.setAdapter(adapter);

        if (list_inventoryitem != null && list_inventoryitem.size() != 0) {
            lyout_nodata.setVisibility(View.GONE);
        } else {
            lyout_nodata.setVisibility(View.VISIBLE);
        }
    }

    private boolean validation() {
        Boolean result = true;
        if (txtdropdown_inventory_item.getText().toString().equalsIgnoreCase("Select Item")) {
            txtdropdown_inventory_item.setError("Please Select Item");
//            txtdropdown_inventory_item.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            txtdropdown_inventory_item.requestFocus();
            return false;
        } else {
            txtdropdown_inventory_item.setError(null);
        }

        if (edit_qty.getText() == null || edit_qty.getText().toString().isEmpty()) {
            edit_qty.setError("Please Enter Quantity");
          //  edit_qty.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_qty.requestFocus();
            return false;
        } else {
            edit_qty.setError(null);
        }


        if(input_amount.getVisibility()==View.VISIBLE)
        {
            if (edit_amount.getText() == null || edit_amount.getText().toString().isEmpty()) {
                edit_amount.setError("Please Enter Amount");
              //  edit_amount.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                edit_amount.requestFocus();
                return false;
            } else {
                edit_amount.setError(null);
            }
        }

//        if(txt_transaction_date.getVisibility()==View.VISIBLE)
//        {
//            if (txt_transaction_date.getText() == null || edit_amount.getText().toString().isEmpty()) {
//                txt_transaction_date.setError("Please Select Date");
//                txt_transaction_date.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
//                txt_transaction_date.requestFocus();
//                return false;
//            } else {
//                txt_transaction_date.setError(null);
//            }
//        }


        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void BeforeDate() {


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txt_transaction_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(AddInventoryStockActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(year, month, day);
                                String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
                                txt_transaction_date.setText(date);

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

    }



    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            updateLabel();

        }

        private void updateLabel() {

            String myFormat = "dd/MM/yyyy"; // In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            if (flag == 1) {
                receiptDate = sdf.format(myCalendar.getTime());
                Log.d("--TotalValue-- ", "receiptDate: " + receiptDate);
                txt_transaction_date.setText(receiptDate);

            }
        }
    };
    Calendar myCalendar = Calendar.getInstance();

    class PopUpPageAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        Context context;

        List<ClsInventoryItem> list_inventoryitems = new ArrayList<>();
        private LayoutInflater inflater;

        public PopUpPageAdapter(Context context, List<ClsInventoryItem> list) {
            this.list_inventoryitems = list;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public long getHeaderId(int position) {
            return list_inventoryitems.get(position).getInventory_item_name().subSequence(0, 1).charAt(0);
        }

        @Override
        public int getCount() {
            return list_inventoryitems.size();
        }

        @Override
        public Object getItem(int position) {
            return list_inventoryitems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.pagename);

                holder.pageid = (TextView) convertView.findViewById(R.id.pageid);
                holder.pagelink = (TextView) convertView.findViewById(R.id.pagelink);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ClsInventoryItem objInventoryItem = list_inventoryitems.get(position);

            stickyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int Popid = (int) id;
                    txtdropdown_inventory_item.setText(list_inventoryitems.get(position).getInventory_item_name());
                    unit.setText(list_inventoryitems.get(position).getUnit_name());
//                    edit_amount.setText(String.valueOf(list_Emp.get(position).getSalary()));
                    ObjInv = list_inventoryitems.get(position);
                    dialog.dismiss();
                    Log.e("PopID", String.valueOf(Popid));


                }
            });

            holder.text.setText(objInventoryItem.getInventory_item_name());
            return convertView;

        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.header_row, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.rowName);

                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            ClsInventoryItem clsPageMaster = list_inventoryitems.get(position);
            String headerText = "" + clsPageMaster.getInventory_item_name().subSequence(0, 1).charAt(0);
            holder.text.setText(headerText);
            return convertView;
        }


        class ViewHolder {
            TextView text, pageid, pagelink;
        }

        class HeaderViewHolder {
            TextView text;
        }

    }




}
