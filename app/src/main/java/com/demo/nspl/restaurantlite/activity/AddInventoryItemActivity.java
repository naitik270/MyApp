package com.demo.nspl.restaurantlite.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryItem;
import com.demo.nspl.restaurantlite.classes.ClsUnit;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AddInventoryItemActivity extends AppCompatActivity {

    Toolbar toolbar;

//    AutoCompleteTextView edit_inventory_item;
    EditText edit_min_qty, edit_remark, edit_max_qty,edit_inventory_item;

    RadioButton rbYES, rbNO;
    Button btnSave;
    static final int DATE_DIALOG_ID = 0;
    private int result;
    private ProgressDialog pd;
    TextView txtdropdown_unit, inventory_item_ID;
    int _ID;
    private Dialog dialog;

    StickyListHeadersListView stickyListview;
    private List<ClsUnit> list_unit;
    private ClsInventoryItem ObjInventoryItem;
    private ClsUnit ObjUnit = new ClsUnit();
    private PopUpPageAdapter adapter;
    List<String> ItemsAutoSuggestion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory_item);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Inventory Item");


        edit_inventory_item = findViewById(R.id.edit_inventory_item);
        edit_min_qty = findViewById(R.id.edit_min_qty);
        edit_max_qty = findViewById(R.id.edit_max_qty);
        edit_remark = findViewById(R.id.edit_remark);
        inventory_item_ID = findViewById(R.id.inventory_item_ID);
        txtdropdown_unit = findViewById(R.id.txtdropdown_unit);

//        ItemsAutoSuggestion = ClsInventoryItem.getItemAutoSuggestionList();
//
//        AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(this,
//                android.R.layout.simple_list_item_1, ItemsAutoSuggestion);
//
//        edit_inventory_item.setAdapter(autoSuggestAdapter);



        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddInventoryItemActivity"));
        }

        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);

        btnSave = findViewById(R.id.btnSave);

        _ID = getIntent().getIntExtra("ID", 0);
        Log.e("jdhgfsghfkg", String.valueOf(_ID));

        if (_ID != 0) {
            ClsInventoryItem objItem = new ClsInventoryItem(AddInventoryItemActivity.this);
            objItem.setInventory_item_id(_ID);
            objItem = objItem.getObject(objItem);

            inventory_item_ID.setText(String.valueOf(objItem.getInventory_item_id()));
            edit_inventory_item.setText(objItem.getInventory_item_name());
            edit_remark.setText(objItem.getRemark());
            edit_max_qty.setText(String.valueOf(objItem.getMax_stock()));
            edit_min_qty.setText(String.valueOf(objItem.getMin_qty()));
            txtdropdown_unit.setText(objItem.getUnit_name());

            Log.e("ACTIVE", objItem.getActive());
            if (objItem.getActive().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (objItem.getActive().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }

        }

        txtdropdown_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(AddInventoryItemActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sticky_header_popup);
                dialog.setCancelable(true);

                final ClsUnit Obj = new ClsUnit(AddInventoryItemActivity.this);
                stickyListview = (StickyListHeadersListView) dialog.findViewById(R.id.stickyListview);

                list_unit = new ArrayList<>();
                list_unit = new ClsUnit(AddInventoryItemActivity.this).getDialogList(" AND [ACTIVE]='YES' ORDER BY [UNIT_NAME] ASC");

                adapter = new PopUpPageAdapter(AddInventoryItemActivity.this, list_unit);
                stickyListview.setAdapter(adapter);
                dialog.show();
            }
        });
        displayListView();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
                pd = new ProgressDialog(AddInventoryItemActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("Loading..");
                pd.setIndeterminate(true);
                pd.setCancelable(true);
                pd.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (validation()) {
                            String where = " AND  [INVENTORY_ITEM_NAME] = "
                                    .concat("'")
                                    .concat(edit_inventory_item.getText().toString())
                                    .concat("' ");

                            if (!inventory_item_ID.getText().toString().isEmpty()) {
                                where = where.concat(" AND [INVENTORY_ITEM_ID] <> ").concat(inventory_item_ID.getText().toString());
                            }

                            Log.e("ItemID", "INV---" + inventory_item_ID);


                            ClsInventoryItem Obj = new ClsInventoryItem(AddInventoryItemActivity.this);
                            boolean exists = Obj.checkExists(where);
                            if (!exists) {
                                Obj.setInventory_item_id(inventory_item_ID.getText() == null || inventory_item_ID.getText().toString().isEmpty() ? 0 : Integer.parseInt(inventory_item_ID.getText().toString()));
                                Obj.setObjUnit(ObjUnit);
                                Obj.setUnit_name(txtdropdown_unit.getText().toString().trim());
                                Obj.setInventory_item_name(edit_inventory_item.getText().toString().trim());
                                Obj.setMin_qty(Double.valueOf(edit_min_qty.getText().toString().trim()));
                                Obj.setMax_stock(Double.valueOf(edit_max_qty.getText().toString().trim()));
                                Obj.setActive(rbYES.isChecked() ? "YES" : "NO");
                                Obj.setRemark(edit_remark.getText().toString().trim());


                                if (Obj.getInventory_item_id() != 0) {

                                    result = ClsInventoryItem.Update(Obj);

                                    if (result == 1) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddInventoryItemActivity.this);
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

                                    result = ClsInventoryItem.Insert(ObjUnit, Obj);
                                    Log.e("Result", String.valueOf(result));
                                    if (result == 1) {
                                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddInventoryItemActivity.this);
                                        alertDialog.setMessage("Inventory added successfully");
                                        alertDialog.setIcon(R.drawable.confirm);
                                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                }


                            } else {
                                Toast toast = Toast.makeText(AddInventoryItemActivity.this, "Inventory Item already exists....", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                                pd.dismiss();
                                return;
                            }

                        }


                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                }, 3000);


            }
        });


    }


    private boolean validation() {
        Boolean result = true;
        //branch validation
        if (edit_inventory_item.getText() == null ||
                edit_inventory_item.getText().toString().isEmpty()) {
            edit_inventory_item.setError("Inventory name is required");
            //     edit_inventory_item.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_inventory_item.requestFocus();
            return false;
        } else {
            edit_inventory_item.setError(null);
        }

        if (txtdropdown_unit.getText().toString().equalsIgnoreCase("Select Unit")) {
            txtdropdown_unit.setError("Please Select Unit");
            // txtdropdown_unit.getBackground().setColorFilter(getReso, PorterDuff.Mode.SRC_ATOP);

            txtdropdown_unit.requestFocus();
            return false;
        } else {
            txtdropdown_unit.setError(null);
        }

        return result;
    }

    private void displayListView() {
        //Code For Update
        String item_Id = getIntent().getStringExtra("ID");
        Log.e("item_Id", "Id is >>>>>" + String.valueOf(item_Id));
        String _where = " AND [EXPENSE_TYPE_ID] = "
                .concat("'")
                .concat(String.valueOf(item_Id)
                        .concat("'"));


        Log.e("Where", _where);
        List<ClsInventoryItem> listItems = new ArrayList<>();
        listItems = new ClsInventoryItem(AddInventoryItemActivity.this).getList(_where);
        Log.e("ITEM_MASTER", "listPageActionMaster-size" + String.valueOf(listItems.size()));

        if (listItems != null && listItems.size() != 0) {

            ObjUnit = new ClsUnit();
            ObjUnit.setUnit_name(listItems.get(0).getUnit_name());


            ObjUnit.setUnit_id(listItems.get(0).getUnit_id());
            txtdropdown_unit.setText(ObjUnit.getUnit_name());


        }


    }


    class PopUpPageAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        Context context;

        List<ClsUnit> list_unit = new ArrayList<>();
        private LayoutInflater inflater;

        PopUpPageAdapter(Context context, List<ClsUnit> list) {
            this.list_unit = list;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public long getHeaderId(int position) {
            return list_unit.get(position).getUnit_name().subSequence(0, 1).charAt(0);
        }

        @Override
        public int getCount() {
            return list_unit.size();
        }

        @Override
        public Object getItem(int position) {
            return list_unit.get(position);
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

            ClsUnit objCategory = list_unit.get(position);


            stickyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int Popid = (int) id;
                    txtdropdown_unit.setText(list_unit.get(position).getUnit_name());
                    ObjUnit = list_unit.get(position);
                    dialog.dismiss();
                    Log.e("PopID", String.valueOf(Popid));


                }
            });
            holder.text.setText(objCategory.getUnit_name());


            return convertView;

        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.header_row, parent, false);
                holder.text = convertView.findViewById(R.id.rowName);

                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            ClsUnit clsPageMaster = list_unit.get(position);
            String headerText = "" + clsPageMaster.getUnit_name().subSequence(0, 1).charAt(0);
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

