package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.SalesItemWithHeaderRV;
import com.demo.nspl.restaurantlite.BarcodeClasses.BarcodeReaderActivity;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.EndlessRecyclerViewScrollListener;
import com.demo.nspl.restaurantlite.MultipleImage.DisplayMultipleImgActivity;
import com.demo.nspl.restaurantlite.Navigation_Drawer.FilterDialogFragment;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsItemFilter;
import com.demo.nspl.restaurantlite.classes.ClsItemLayer;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsSelectionModel;
import com.demo.nspl.restaurantlite.classes.Filters;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity implements
        FilterDialogFragment.OnUpdateFooterValue,
        OrdersActivity.OnUpdateFooterValueFromOrders {

    private FloatingActionButton fab_filter, fab_barcode;
    public static final int DIALOG_QUEST_CODE = 300;
    public static final String param = "getSelectedList";
    public static String getSelectedList = "";
    private static final int MAX_ITEMS_PER_REQUEST = 30;
//    private static int NUMBER_OF_ITEMS = 0;


    List<ClsLayerItemMaster> list = new ArrayList<>();

    List<String> Send_list;
    List<String> headerlist = new ArrayList<>();
    private TextView empty_title_text;

    public String _where = "";
    String _whereSearch = "";
    String _whereSearchChar = "";
    String _whereLastPurchase = "";
    Toolbar toolbar;
    //    private ListView list_view;
    private RecyclerView list_rv;
    private SalesItemWithHeaderRV mCu;
    //    private DisplayOrderWithHeaderAdapter mCu;
    ClsLayerItemMaster clsLayerItemMaster;
    // private DiaplayOrderAdapter mCv;
    FrameLayout FrameLayout;
    FilterDialogFragment newFragment;
    ProgressBar progress_bar;
    String _paging = "", getKeepOrderNo = "";
    double updatedAmt = 0.0;
    int clickcount = 0;

    String saleMode = "";
    String entryMode = "";
    String _taxApple = "";
    String _barcodeResult = "";
    String editOrderNo = "";
    String editSource = "";
    String editOrderID = "";
//    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1208;

    LinearLayout ll_item_count;
    TextView txt_footer_value;


    ImageView iv_view, iv_remove;
//    private FilterDialogFragment.OnUpdateFooterValue onUpdateFooterValue;

    RecyclerView lst_letter;
    ImageView txt_all;
    LinearLayout ll_uper_filter;
    String _footerValue = "";

    MultipleSelectionAdapter mAdapter;
    int editQuotationID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        // rv = findViewById(R.id.rv);
        fab_filter = findViewById(R.id.fab_filter);
        fab_filter.setColorFilter(Color.WHITE);
        ll_uper_filter = findViewById(R.id.ll_uper_filter);
        fab_barcode = findViewById(R.id.fab_barcode);
        fab_barcode.setColorFilter(Color.WHITE);

        lst_letter = findViewById(R.id.lst_letter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        lst_letter.setLayoutManager(layoutManager);

        empty_title_text = findViewById(R.id.empty_title_text);
        toolbar = findViewById(R.id.toolbar);

        FrameLayout = findViewById(R.id.FrameLayout);
        progress_bar = findViewById(R.id.progress_bar);
        ll_item_count = findViewById(R.id.ll_item_count);
        txt_footer_value = findViewById(R.id.txt_footer_value);

        iv_view = findViewById(R.id.iv_view);
        iv_remove = findViewById(R.id.iv_remove);

        list_rv = findViewById(R.id.list_rv);
        txt_all = findViewById(R.id.txt_all);

        saleMode = getIntent().getStringExtra("saleMode");


        if (saleMode.equalsIgnoreCase("Sale")
                || saleMode.equalsIgnoreCase("Wholesale")) {
            ClsGlobal.editOrderID = editOrderID = getIntent().getStringExtra("editOrderID");
        } else if (saleMode.equalsIgnoreCase("Retail Quotation")
                || saleMode.equalsIgnoreCase("Wholesale Quotation")) {
            ClsGlobal.editQuotationID = editQuotationID = getIntent().getIntExtra("editQuotationID", 0);
        }

        entryMode = ClsGlobal.GetOrderEditMode(SalesActivity.this);

        editOrderNo = getIntent().getStringExtra("editOrderNo");
        editSource = getIntent().getStringExtra("editSource");

        if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {
            _taxApple = getIntent().getStringExtra("_taxApple");
            ll_item_count.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewDialogTxt));
        } else {
            _taxApple = "";
            ll_item_count.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.uper_filter));
        }

        getAlphabetList();

        txt_all.setOnClickListener(v -> {
            boolean selection = false;
            _whereSearchChar = "";

            if (lstClsAlphabeticFilters != null && lstClsAlphabeticFilters.size() != 0) {
                for (ClsSelectionModel _obj : lstClsAlphabeticFilters) {
                    if (_obj.isSelected()) {

                        if (selection == false) {
                            selection = true;
                        }
                        _obj.setSelected(false);
                        lstClsAlphabeticFilters.set(lstClsAlphabeticFilters.indexOf(_obj), _obj);
                    }
                }
            }

            if (selection) {
                mAdapter.notifyDataSetChanged();
            }

            if (selection == true) {

                headerlist.clear();
                list.clear();
                new LoadAsyncTask(1, 0, "")
                        .execute();
            }
        });

        if (editOrderNo != null && !editOrderNo.isEmpty()) {
            if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
                if (saleMode.equalsIgnoreCase("Sale")) {
                    ClsGlobal._OrderNo = editOrderNo;
                    ClsGlobal.SetKeepOrderNo(SalesActivity.this, editOrderNo, "Sale");
                } else if (saleMode.equalsIgnoreCase("Wholesale")) {
                    ClsGlobal._WholeSaleOrderNo = editOrderNo;
                    ClsGlobal.SetKeepOrderNo(SalesActivity.this, editOrderNo, "Wholesale");
                } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
                    ClsGlobal._QuotationSaleOrderNo = editOrderNo;
                    ClsGlobal.SetKeepOrderNo(SalesActivity.this, editOrderNo, "Retail Quotation");
                } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                    ClsGlobal._QuotationWholesaleOrderNo = editOrderNo;
                    ClsGlobal.SetKeepOrderNo(SalesActivity.this, editOrderNo, "Wholesale Quotation");
                }
            }
        } else {
            Log.e("--Check--", "Main ELSE");
        }

        if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
            getKeepOrderNo = ClsGlobal.GetKeepOrderNo(SalesActivity.this, saleMode);

            if (saleMode.equalsIgnoreCase("Sale") && getKeepOrderNo != null
                    && !getKeepOrderNo.equalsIgnoreCase("")) {
                ClsGlobal._OrderNo = getKeepOrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale")) {
                ClsGlobal._WholeSaleOrderNo = getKeepOrderNo;
            } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
                ClsGlobal._QuotationSaleOrderNo = getKeepOrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                ClsGlobal._QuotationWholesaleOrderNo = getKeepOrderNo;
            }
        }


        if (saleMode == null) {
            saleMode = "";
        }


        Log.e("--Mode--", "Sale Type: " + saleMode);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(saleMode);
        }


        clsLayerItemMaster = new ClsLayerItemMaster();

        getSelectedList = "";
        getSelectedList = getIntent().getStringExtra("SelectedList");


        if (getSelectedList != null && !getSelectedList.equalsIgnoreCase("")) {

            Type listType = new TypeToken<ArrayList<Filters>>() {
            }.getType();


            List<Filters> filtersList = new ArrayList<>();
            filtersList = new Gson().fromJson(getSelectedList, listType);


            if (filtersList != null && filtersList.size() != 0) {


                _where = "";


                List<String> itemIdList = new ArrayList<>();


                //apply filter here

                List<String> listLayer = new ArrayList<>();
                List<String> listValue = new ArrayList<>();
                List<String> listTags = new ArrayList<>();
                List<String> listItems = new ArrayList<>();

                for (Filters ObjFilter : filtersList) {


                    if (ObjFilter.getLAYERNAME() != null && !ObjFilter.getLAYERNAME().isEmpty()) {
                        if (ObjFilter.getLAYERNAME().equalsIgnoreCase("Items")) {
                            listItems.add("".concat("'").concat(ObjFilter.getLAYERVALUE().concat("'")));//'Mouse','Bottel','mi'
                        } else if (ObjFilter.getLAYERNAME().equalsIgnoreCase("Tags")) {
                            listTags.add("".concat("'").concat(ObjFilter.getLAYERVALUE().concat("'")));//'red','samsung','mi'
                        } else {
                            listLayer.add("".concat("'").concat(ObjFilter.getLAYERNAME()).concat("'"));//'color','brand'
                            listValue.add("".concat("'").concat(ObjFilter.getLAYERVALUE().concat("'")));//'red','samsung','mi'
                        }
                    }
                }

                //layer
                //where
                _where = _where.concat(" AND [LAYER_NAME] in (".concat(TextUtils.join(",", listLayer)).concat(")"));
                _where = _where.concat(" AND [LAYER_VALUE] in (".concat(TextUtils.join(",", listValue)).concat(")"));


                ClsLayerItemMaster clsLayerItemMaster = new ClsLayerItemMaster(getApplication());

                itemIdList = ClsLayerItemMaster.getItemsByLayers(_where);//1,2,3,5,7,8,11,5845,3,9,85
                getAlphabetList();

                if (listTags != null && listTags.size() != 0) {
                    String _whereTags = "".concat(" AND [TAGNAME] in (".concat(TextUtils.join(",", listTags)).concat(")"));
                    Log.e("_whereTags", "_whereTags >>>>_where " + _whereTags);

                    List<String> TagItemIDList = ClsLayerItemMaster.getItemIdListByTag(SalesActivity.this, _whereTags);//3,9,85
                    Gson gson = new Gson();
                    String getTaglist = gson.toJson(TagItemIDList);
                    Log.e("getTaglist", getTaglist);

                    if (TagItemIDList != null && TagItemIDList.size() != 0) {
                        itemIdList.addAll(TagItemIDList);
                    }
                }

                if (listItems != null && listItems.size() != 0) {
                    String _whereItems = "".concat(" AND [ITEM_NAME] in (".concat(TextUtils.join(",", listItems)).concat(")"));
                    Log.e("_whereTags", "_whereTags >>>>_where " + _whereItems);

                    List<String> itemIDListByItemName = ClsLayerItemMaster.getItemIdListByItemName(SalesActivity.this, _whereItems);//3,9,85


                    if (itemIDListByItemName != null && itemIDListByItemName.size() != 0) {
                        itemIdList.addAll(itemIDListByItemName);
                    }
                }

                _where = " AND [LAYERITEM_ID] IN (".concat(TextUtils.join(",", itemIdList)).concat(")");
                ViewData();
                getAlphabetList();

            }


        } else {
            ViewData();
        }

        fab_barcode.setOnClickListener(v -> launchBarCodeActivity());

        fab_filter.setOnClickListener(v1 -> {
            onFabButtonClick();

        });

        iv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOrders();
            }
        });

        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAllRecords();

            }
        });
        setFooterDetail();

    }


    List<ClsSelectionModel> lstClsAlphabeticFilters = new ArrayList<>();

    ArrayList<String> selectCharcter = new ArrayList<>();

    private void getAlphabetList() {
        lstClsAlphabeticFilters = ClsLayerItemMaster.getItemCharList(_whereSearch.concat(" ").concat(_where).concat(" "), SalesActivity.this);
        mAdapter = new MultipleSelectionAdapter(SalesActivity.this, lstClsAlphabeticFilters);
        lst_letter.setAdapter(mAdapter);
    }

    public class MultipleSelectionAdapter extends RecyclerView.Adapter<MultipleSelectionAdapter.myViewHolder> {

        Context context;
        List<ClsSelectionModel> mData = new ArrayList<>();

        public MultipleSelectionAdapter(Context context, List<ClsSelectionModel> mData) {
            this.context = context;
            this.mData = mData;
        }

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_alphabet, parent, false);
            return new myViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
            ClsSelectionModel obj = mData.get(position);
            holder.txt_letter.setText(obj.get_character());

            holder.ll_header.setBackgroundColor(obj.isSelected() ? Color.parseColor("#345164") : Color.WHITE);
            holder.txt_letter.setTextColor(obj.isSelected() ? Color.parseColor("#FFFFFF") : Color.BLACK);

            holder.ll_header.setOnClickListener(view -> {

                Log.d("--step--", "Upper: " + obj.isSelected());

                obj.setSelected(!obj.isSelected());
                lstClsAlphabeticFilters.set(position, obj);

                holder.view.setBackgroundColor(obj.isSelected() ? Color.parseColor("#345164") : Color.WHITE);
                holder.txt_letter.setTextColor(obj.isSelected() ? Color.parseColor("#FFFFFF") : Color.BLACK);


                selectCharcter.clear();
                _whereSearchChar = "";


                for (ClsSelectionModel _obJ : lstClsAlphabeticFilters) {
                    if (_obJ.isSelected()) {
                        selectCharcter.add(" [ITEM_NAME] LIKE '".concat(_obJ.get_character()).concat("%' "));
                    }
                }

                if (selectCharcter != null && selectCharcter.size() != 0) {
                    _paging = "";
                    _whereSearchChar = _whereSearchChar.concat(" AND (").concat(TextUtils.join(" OR ", selectCharcter).concat(") "));
//                    list_rv.getRecycledViewPool().clear();

                    list_rv.stopScroll();
                    new LoadAsyncTask(1, 0, "Search").execute();
                } else {
                    _paging = "";
//                    list_rv.getRecycledViewPool().clear();
                    list_rv.stopScroll();
                    new LoadAsyncTask(1, 0, "Search").execute();

                }
            });

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }


        public class myViewHolder extends RecyclerView.ViewHolder {
            TextView txt_letter;
            LinearLayout ll_header;
            View view;


            public myViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                txt_letter = itemView.findViewById(R.id.txt_letter);
                ll_header = itemView.findViewById(R.id.ll_header);
            }

        }
    }


    String orderNO = "";

//    void deleteAllRecords() {
//
//        orderNO = "";
//
//        if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
//            if (saleMode.equalsIgnoreCase("Sale")) {
//                if (!ClsGlobal._OrderNo.equalsIgnoreCase("")) {
//                    orderNO = ClsGlobal._OrderNo;
//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
//                    alertDialog.setTitle("Order is Running...");
//                    alertDialog.setMessage("Sure to delete...?");
//                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
//                    alertDialog.setPositiveButton("YES", (dialog, which) -> {
//
//                        int Result = ClsInventoryOrderDetail.DeleteAllRecords(Integer.valueOf(orderNO)
//                                , SalesActivity.this);
//
//                        if (true) {
//                            ClsGlobal._OrderNo = "";
//                            ClsGlobal.editOrderID = "";
//                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, "", "Sale");
//
//                        }
//                        Log.e("Result", String.valueOf(Result));
//
//                        SalesActivity.this.finish();
//                        dialog.dismiss();
//                        dialog.cancel();
//                    });
//                    alertDialog.setNegativeButton("NO", (dialog, which) -> {
//                        SalesActivity.this.finish();
//
//                        dialog.dismiss();
//                        dialog.cancel();
//                    });
//                    alertDialog.show();
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                if (!ClsGlobal._WholeSaleOrderNo.equalsIgnoreCase("")) {
//
//                    orderNO = ClsGlobal._WholeSaleOrderNo;
//
//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
//                    alertDialog.setTitle("Order is Running...");
//                    alertDialog.setMessage("Sure to delete...?");
//                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
//                    alertDialog.setPositiveButton("YES", (dialog, which) -> {
//
//
//                        int Result = ClsInventoryOrderDetail.DeleteAllRecords(Integer.valueOf(orderNO)
//                                , SalesActivity.this);
//
//                        if (true) {
//                            ClsGlobal._WholeSaleOrderNo = "";
//                            ClsGlobal.editOrderID = "";
//                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, "", "Wholesale");
//
//                        }
//                        Log.e("Result", String.valueOf(Result));
//
//                        SalesActivity.this.finish();
//                        dialog.dismiss();
//                        dialog.cancel();
//
//                    });
//                    alertDialog.setNegativeButton("NO", (dialog, which) -> {
//                        SalesActivity.this.finish();
//
//                        dialog.dismiss();
//                        dialog.cancel();
//
//                    });
//                    alertDialog.show();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        } else {
//            Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
//        }
//    }

    void deleteAllRecords() {

        orderNO = "";

        if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
            if (saleMode.equalsIgnoreCase("Sale")) {
                if (!ClsGlobal._OrderNo.equalsIgnoreCase("")) {
                    orderNO = ClsGlobal._OrderNo;

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Sure to delete...?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", (dialog, which) -> {

                        int Result = ClsInventoryOrderDetail.DeleteAllRecords(Integer.valueOf(orderNO)
                                , SalesActivity.this);

                        if (true) {
                            ClsGlobal._OrderNo = "";
                            ClsGlobal.editOrderID = "";
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, "", "Sale");

                        }
                        Log.e("Result", String.valueOf(Result));

                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.setNegativeButton("NO", (dialog, which) -> {
                        SalesActivity.this.finish();

                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.show();


                } else {
                    Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
                }
            } else if (saleMode.equalsIgnoreCase("Wholesale")) {
                if (!ClsGlobal._WholeSaleOrderNo.equalsIgnoreCase("")) {

                    orderNO = ClsGlobal._WholeSaleOrderNo;


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Sure to delete...?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", (dialog, which) -> {


                        int Result = ClsInventoryOrderDetail.DeleteAllRecords(Integer.valueOf(orderNO)
                                , SalesActivity.this);


                        if (true) {
                            ClsGlobal._WholeSaleOrderNo = "";
                            ClsGlobal.editOrderID = "";
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, "", "Wholesale");

                        }
                        Log.e("Result", String.valueOf(Result));

                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.setNegativeButton("NO", (dialog, which) -> {
                        SalesActivity.this.finish();

                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.show();

                } else {
                    Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
                }
            } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {

                if (!ClsGlobal._QuotationSaleOrderNo.equalsIgnoreCase("")) {

                    orderNO = ClsGlobal._QuotationSaleOrderNo;

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Sure to delete...?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", (dialog, which) -> {


                        int Result = ClsInventoryOrderDetail.DeleteAllRecords(Integer.valueOf(orderNO)
                                , SalesActivity.this);

                        if (true) {
                            ClsGlobal._QuotationSaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    "", "Retail Quotation");

                        }
                        Log.e("Result", String.valueOf(Result));

                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.setNegativeButton("NO", (dialog, which) -> {
                        SalesActivity.this.finish();

                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.show();

                } else {
                    Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
                }


            } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                if (!ClsGlobal._QuotationWholesaleOrderNo.equalsIgnoreCase("")) {

                    orderNO = ClsGlobal._QuotationWholesaleOrderNo;


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Sure to delete...?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", (dialog, which) -> {


                        int Result = ClsInventoryOrderDetail.DeleteAllRecords(Integer.valueOf(orderNO)
                                , SalesActivity.this);

                        if (true) {
                            ClsGlobal._QuotationWholesaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    "", "Wholesale Quotation");

                        }
                        Log.e("Result", String.valueOf(Result));

                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.setNegativeButton("NO", (dialog, which) -> {
                        SalesActivity.this.finish();

                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.show();

                } else {
                    Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "There is no item in current order!", Toast.LENGTH_SHORT).show();
        }
    }

    void setFooterDetail() {

        String orderNum = "";

        if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
            if (saleMode.equalsIgnoreCase("Sale")) {
                orderNum = ClsGlobal._OrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale")) {
                orderNum = ClsGlobal._WholeSaleOrderNo;
            } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
                orderNum = ClsGlobal._QuotationSaleOrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                orderNum = ClsGlobal._QuotationWholesaleOrderNo;
            }
        }

        if (orderNum != null && !orderNum.isEmpty()) {
            String _whereFooter = "".concat(" AND [OrderNo] = ").concat("'")
                    .concat(orderNum).concat("'");


            if (!entryMode.equalsIgnoreCase("New")) {
                _whereFooter += "".concat(" AND [OrderID] = " + editOrderID);
            }

            String _whereFooterQuotation = "".concat(" AND [QuotationNo] = ").concat("'")
                    .concat(orderNum).concat("'");

            if (!entryMode.equalsIgnoreCase("New")) {
                _whereFooterQuotation += "".concat(" AND [QuotationID] = " + editQuotationID);
                Log.e("--Edit--", "_whereFooterQuotation: " + _whereFooterQuotation);
            }

            if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
                if (saleMode.equalsIgnoreCase("Sale")
                        || saleMode.equalsIgnoreCase("Wholesale")) {

                    ClsInventoryOrderDetail.runningOrder order =
                            ClsInventoryOrderDetail.getRunningOrderDetails(_whereFooter,
                                    SalesActivity.this);

                    _footerValue = " ".concat("BILL#:" + orderNum).concat(", ITEMS: " + order.getItemCount())
                            .concat(", TOTAL: \u20B9 " + ClsGlobal.round(order.getTotalAmt(), 2));

                } else if (saleMode.equalsIgnoreCase("Retail Quotation")
                        || saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                    ClsQuotationOrderDetail.runningQuotation order =
                            ClsQuotationOrderDetail.getRunningQuotationDetail(_whereFooterQuotation,
                                    SalesActivity.this);

                    _footerValue = " ".concat("BILL#:" + orderNum).concat(", ITEMS: " + order.getItemCount())
                            .concat(", TOTAL: \u20B9 " + ClsGlobal.round(order.getTotalAmt(), 2));

                }

                txt_footer_value.setText(_footerValue);

            } else {
                _footerValue = "NO ITEMS";
                txt_footer_value.setText(_footerValue);
            }

        } else {
            _footerValue = "NO ITEMS";
            txt_footer_value.setText(_footerValue);
        }
    }


    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(getApplicationContext(), true, false);
        startActivityForResult(launchIntent, 1208);
    }


    @Override
    public void onActivityResult(int _requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(_requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No details found.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_requestCode == 1208 && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            _barcodeResult = barcode.rawValue;

            _whereSearch = _whereSearch.concat(" AND [ITM].[ITEM_CODE] = ").concat("'").concat(_barcodeResult).concat("'");
            clsLayerItemMaster = new ClsLayerItemMaster();
            clsLayerItemMaster = ClsLayerItemMaster.getItem(getApplication(), _where, _whereSearch);
            _whereSearch = "";

        }


        if (clsLayerItemMaster.getITEM_CODE() == null || clsLayerItemMaster.getITEM_CODE().isEmpty()) {
            Toast.makeText(getApplicationContext(), "NO ITEM FOUND", Toast.LENGTH_SHORT).show();
            return;
        } else {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
            mp.start();
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(100);
            itemClick(clsLayerItemMaster);
        }
    }


    private void onFabButtonClick() {

        List<ClsItemFilter.Layer> filterList = new ArrayList<>();

        //BRAND, MODEL, STORAGE
        List<ClsInventoryLayer> listLayer = ClsInventoryLayer.getAllList(getApplication(), " AND [ACTIVE] = 'YES' ");


        List<ClsItemFilter.LayerItem> layerItemList = new ArrayList<>();

        if (listLayer != null && listLayer.size() != 0) {

            Log.e("--Step--", "Step -- 3");
            Log.e("--Step--", String.valueOf(listLayer.size()));

            List<ClsItemLayer> listItems = ClsLayerItemMaster.getLayerItems(getApplication(), "");
            //call list here


            Gson gsonlistItems = new Gson();
            String jsonInStringlistItems = gsonlistItems.toJson(listItems);
            Log.e("--SSSSSS--", "jsonInString:--- " + jsonInStringlistItems);

            Log.e("--Step--", "Step -- 4");

            if (listItems != null && listItems.size() != 0) {

                Log.e("--Step--", "Step -- 5");


                for (ClsInventoryLayer ObjLayer : listLayer) {


                    Log.e("--Step--", "Step -- 6");


                    ClsItemFilter.Layer ObjFilterLayer = new ClsItemFilter.Layer();
                    //layer: sim

                    Log.e("--Step--", "Step -- 7");


                    ObjFilterLayer.setLayerID(ObjLayer.getINVENTORYLAYER_ID());
                    ObjFilterLayer.setLayerName(ObjLayer.getInventoryLayerName());
                    ObjFilterLayer.setSelected(false);

                    Log.e("--Step--", "Step -- 8");

                    //set layer list items:, duel, single

                    layerItemList = new ArrayList<>();
                    for (ClsItemLayer ObjLayerItem : listItems) {


                        Log.e("--Step--", "Step -- 9");

                        if (ObjLayer.getInventoryLayerName().equalsIgnoreCase(ObjLayerItem.getLAYER_NAME())) {


                            Log.e("--Step--", "Step -- 10");


                            boolean isFound = false;
                            for (ClsItemFilter.LayerItem e : layerItemList) {

                                Log.e("--Step--", "Step -- 11");

                                if (e.getItemName().equalsIgnoreCase(ObjLayerItem.getLAYER_VALUE())) {
                                    isFound = true;

                                    Log.e("--Step--", "Step -- 12");

                                    break;
                                }

                                Log.e("--Step--", "Step -- 13");

                            }


                            Log.e("--Step--", "Step -- 14");

                            //add item alu to list
                            if (!isFound) {

                                Log.e("--Step--", "Step -- 15");

                                ClsItemFilter.LayerItem ObjCurruntItem = new ClsItemFilter.LayerItem();
                                ObjCurruntItem.setItemID(ObjLayerItem.getITEM_ID());
                                ObjCurruntItem.setItemName(ObjLayerItem.getLAYER_VALUE());///single,dule,single,single,dule
                                ObjCurruntItem.setSelected(false);
                                ObjCurruntItem.setLayerID(ObjLayer.getINVENTORYLAYER_ID());

                                layerItemList.add(ObjCurruntItem);

                                Log.e("--Step--", "Step -- 16");


                            }
                        }
                    }

                    ObjFilterLayer.setLayerItemList(layerItemList);

                    //get all layer items from item list:
                    filterList.add(ObjFilterLayer);

                }

                layerItemList = new ArrayList<>();

                List<String> tagList = new ArrayList<>();
                tagList = ClsLayerItemMaster.getUniqueTagsList(SalesActivity.this);


                Gson gsontagList = new Gson();
                String jsonInStringtagList = gsontagList.toJson(tagList);
                Log.e("--Step--", "jsonInStringtagList:--- " + jsonInStringtagList);


                if (tagList != null && tagList.size() != 0) {

                    ClsItemFilter.Layer ObjFilterLayer = new ClsItemFilter.Layer();
                    //layer: sim
                    ObjFilterLayer.setLayerID(0);
                    ObjFilterLayer.setLayerName("Tags");
                    ObjFilterLayer.setSelected(false);

                    for (String tag : tagList) {
                        ClsItemFilter.LayerItem ObjCurruntItem = new ClsItemFilter.LayerItem();
                        ObjCurruntItem.setItemID(0);
                        ObjCurruntItem.setItemName(tag);///single,dule,single,single,dule
                        ObjCurruntItem.setSelected(false);
                        ObjCurruntItem.setLayerID(0);
                        layerItemList.add(ObjCurruntItem);
                    }
                    ObjFilterLayer.setLayerItemList(layerItemList);

                    //get all layer items from item list:
                    filterList.add(ObjFilterLayer);

                }

                layerItemList = new ArrayList<>();

                Send_list = new ArrayList<>();

                Send_list = ClsLayerItemMaster.getUniqueItemList(getApplication(), "");

                if (Send_list != null && Send_list.size() != 0) {

                    ClsItemFilter.Layer ObjFilterLayer = new ClsItemFilter.Layer();
                    //layer: sim
                    ObjFilterLayer.setLayerID(0);
                    ObjFilterLayer.setLayerName("Items");
                    ObjFilterLayer.setSelected(false);

                    for (String current : Send_list) {
                        ClsItemFilter.LayerItem ObjCurruntItem = new ClsItemFilter.LayerItem();
                        ObjCurruntItem.setItemID(0);
                        ObjCurruntItem.setItemName(current);///single,dule,single,single,dule
                        ObjCurruntItem.setSelected(false);
                        ObjCurruntItem.setLayerID(0);
                        layerItemList.add(ObjCurruntItem);
                    }
                    ObjFilterLayer.setLayerItemList(layerItemList);

                    //get all layer items from item list:
//                        filterList.add(ObjFilterLayer);
                    filterList.add(0, ObjFilterLayer);
                }

            }

            String jsonInFilterList = new Gson().toJson(filterList);

            Intent intent = new Intent(getApplication(), FilterDialogActivity.class);
            intent.putExtra("FilterList", jsonInFilterList);
            intent.putExtra("saleMode", saleMode);
            intent.putExtra("entryMode", entryMode);
            intent.putExtra("editSource", editSource);
            startActivity(intent);

        } else {
            Toast.makeText(SalesActivity.this, "There are No Filter", Toast.LENGTH_LONG).show();
        }
    }


    public void ViewData() {


        mCu = new SalesItemWithHeaderRV(SalesActivity.this, saleMode);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SalesActivity.this);
        list_rv.setLayoutManager(linearLayoutManager);
        list_rv.setAdapter(mCu);

        list_rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                new LoadAsyncTask(++page, totalItemsCount, "").execute();
                Log.e("onLoadMore", "onLoadMore page:- " + page);
                Log.e("onLoadMore", "onLoadMore totalItemsCount:- " + totalItemsCount);


            }
        });

        new LoadAsyncTask(1, 0, "").execute();


        mCu.SetOnClicklistener(clsLayerItemMaster -> {

            itemClick(clsLayerItemMaster);

        });

        mCu.setOnViewImg((objClsLayerItemMaster, position) -> {


            int currentObjId = objClsLayerItemMaster.getLAYERITEM_ID();
            Intent intent = new Intent(getApplicationContext(), DisplayMultipleImgActivity.class);
            intent.putExtra("ID", currentObjId);
            intent.putExtra("itemName", objClsLayerItemMaster.getITEM_NAME());
            intent.putExtra("itemCode", objClsLayerItemMaster.getITEM_CODE());
            intent.putExtra("_imgMode", "Item");
            intent.putExtra("imgSave", "Preview");
            startActivity(intent);


        });

    }


    void itemClick(ClsLayerItemMaster _itemObj) {

        hidekeyboard();

        FrameLayout.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        newFragment = new FilterDialogFragment(saleMode, _barcodeResult, entryMode, editOrderNo);


        Log.e("--Test--", "SaleActivitySaleMode: " + saleMode);

        Log.e("--Test--", "ITEM_CLICK_EntryMode: " + entryMode);

        newFragment.setRequestCode(_itemObj);
        newFragment.setOnUpdateFooterValue(this);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        newFragment.setOnCallbackResult(requestCode -> {

            if (requestCode == DIALOG_QUEST_CODE) {

                FrameLayout.setVisibility(View.VISIBLE);
//                _where = "";
//                ViewData();
                hidekeyboard();

            }
        });


        FrameLayout.setVisibility(View.INVISIBLE);
    }


    void hidekeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(SalesActivity.this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_sales, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        MenuItem done = menu.findItem(R.id.recent_order);

        if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {
            //hide recent order toolbar menu when order update mode....
            done.setVisible(false);
            //hide delete image view when order update mode....
            iv_remove.setVisibility(View.GONE);
        }


        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                list.clear();
                _whereSearch = "";
                headerlist.clear();
                ViewData();
                getAlphabetList();
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _whereSearch = "";
                _paging = "";
                if (query != null && !query.isEmpty()) {
                    _whereSearch = " AND ([ITEM_NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [ITEM_CODE] LIKE '%".concat(query).concat("%')"));
                    new LoadAsyncTask(1, 0, "Search").execute();

                    getAlphabetList();

                    ClsGlobal.hideKeyboard(SalesActivity.this);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
//            _whereSearch = "";
//            list.clear();
//            headerlist.clear();
//            ViewData();
            searchView.setQuery("", false);
            searchView.clearFocus();
        });

        return true;
    }


    void viewOrders() {



        if (saleMode.equalsIgnoreCase("Sale")) {
            if (!ClsGlobal._OrderNo.equalsIgnoreCase("")) {
                Intent intent = new Intent(SalesActivity.this, OrdersActivity.class);
                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("_taxApple", _taxApple);
                startActivity(intent);
            } else {
                Toast.makeText(this, "There is no item in current order!", Toast.LENGTH_SHORT).show();
            }
        } else if (saleMode.equalsIgnoreCase("Wholesale")) {
            if (!ClsGlobal._WholeSaleOrderNo.equalsIgnoreCase("")) {
                Intent intent = new Intent(SalesActivity.this, OrdersActivity.class);
                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("_taxApple", _taxApple);

                startActivity(intent);
            } else {
                Toast.makeText(this, "There is no item in current order!", Toast.LENGTH_SHORT).show();
            }
        } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
            if (!ClsGlobal._QuotationSaleOrderNo.equalsIgnoreCase("")) {
                Intent intent = new Intent(SalesActivity.this, OrdersActivity.class);
                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("_taxApple", _taxApple);
                startActivity(intent);
            } else {
                Toast.makeText(this, "There is no item in current order!", Toast.LENGTH_SHORT).show();
            }
        } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
            if (!ClsGlobal._QuotationWholesaleOrderNo.equalsIgnoreCase("")) {
                Intent intent = new Intent(SalesActivity.this, OrdersActivity.class);
                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("_taxApple", _taxApple);

                startActivity(intent);
            } else {
                Toast.makeText(this, "There is no item in current order!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.view_orders) {

            viewOrders();

            return true;
        }


        if (id == R.id.recent_order) {

            if (saleMode.equalsIgnoreCase("Sale") ||
                    saleMode.equalsIgnoreCase("Wholesale")) {

                Intent intent = new Intent(SalesActivity.this, RecentOrderActivity.class);
                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("fragMode", "SaleActivity");
                startActivity(intent);

            } else if (saleMode.equalsIgnoreCase("Retail Quotation") ||
                    saleMode.equalsIgnoreCase("Wholesale Quotation")) {


                Intent intent = new Intent(SalesActivity.this, RecentQuotationActivity.class);
                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("fragMode", "SaleActivity");
                startActivity(intent);

                finish();
            }


            return true;
        }


        if (id == R.id.clear) {
            list.clear();
            headerlist.clear();
            _where = "";
            ViewData();

//            new LoadAsyncTask(1, 0, "").execute();
            getAlphabetList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        if (saleMode.equalsIgnoreCase("Sale") ||
                saleMode.equalsIgnoreCase("Wholesale")) {

            if (ClsGlobal._quotationStatusID != null && ClsGlobal._quotationStatusID != "") {

                ClsGlobal._quotationStatusID = "";
                ClsGlobal._quotationMobileNO = "";
//delete

                ClsInventoryOrderDetail.DeleteFromOrdermaster(ClsGlobal._OrderNo
                        , "", "DeleteById", SalesActivity.this);

                ClsInventoryOrderDetail.DeleteFromOrdermaster(ClsGlobal._WholeSaleOrderNo
                        , "", "DeleteById", SalesActivity.this);

                ClsGlobal._OrderNo = "";
                ClsGlobal._WholeSaleOrderNo = "";
                ClsGlobal.editOrderID = "";

                ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                        "", "Sale");
                ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                        "", "Wholesale");


//                    SalesActivity.this.finish();
            }
        }


        if (entryMode.equalsIgnoreCase("NEW")) {

            if (saleMode.equalsIgnoreCase("Sale")) {

                if (!ClsGlobal._OrderNo.equals("") && FrameLayout.getVisibility() == View.VISIBLE) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Please choose option for Running Order!");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("DELETE", (dialog, which) -> {

                        int Result = ClsInventoryOrderDetail.DeleteFromOrdermaster(ClsGlobal._OrderNo
                                , "", "DeleteById", SalesActivity.this);
                        if (true) {
                            ClsGlobal._OrderNo = "";
                            ClsGlobal.editOrderID = "";
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, "", "Sale");
                        }
                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.setNegativeButton("KEEP", (dialog, which) -> {
                        SalesActivity.this.finish();
                        if (!ClsGlobal._OrderNo.equalsIgnoreCase("")) {
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, ClsGlobal._OrderNo, "Sale");
                        }
                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.show();
                } else {
                    if (FrameLayout.getVisibility() == View.VISIBLE) {
                        SalesActivity.this.finish();
                    } else {
                        newFragment.dismiss();
                    }
                }
            } else if (saleMode.equalsIgnoreCase("Wholesale")) {

//
                if (!ClsGlobal._WholeSaleOrderNo.equals("") && FrameLayout.getVisibility() == View.VISIBLE) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Please choose option for Running Order!");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("DELETE", (dialog, which) -> {

                        int Result = ClsInventoryOrderDetail.DeleteFromOrdermaster(ClsGlobal._WholeSaleOrderNo
                                , "", "DeleteById", SalesActivity.this);

                        if (true) {
                            ClsGlobal._WholeSaleOrderNo = "";
                            ClsGlobal.editOrderID = "";
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, "", "Wholesale");
                        }

                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.setNegativeButton("KEEP", (dialog, which) -> {
                        SalesActivity.this.finish();

                        if (!ClsGlobal._WholeSaleOrderNo.equalsIgnoreCase("")) {
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, ClsGlobal._WholeSaleOrderNo, "Wholesale");
                        }
                        dialog.dismiss();
                        dialog.cancel();

                    });
                    alertDialog.show();
                } else {
                    if (FrameLayout.getVisibility() == View.VISIBLE) {
                        SalesActivity.this.finish();
                    } else {
                        newFragment.dismiss();
                    }
                }


            } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {

                if (!ClsGlobal._QuotationSaleOrderNo.equals("") && FrameLayout.getVisibility() == View.VISIBLE) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Please choose option for Running Order!");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("DELETE", (dialog, which) -> {

                        int Result = ClsInventoryOrderDetail.DeleteFromOrdermaster
                                (ClsGlobal._OrderNo
                                        , "", "DeleteById", SalesActivity.this);
                        if (true) {
                            ClsGlobal._QuotationSaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, "",
                                    "Retail Quotation");
                        }

                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.setNegativeButton("KEEP", (dialog, which) -> {
                        SalesActivity.this.finish();
                        if (!ClsGlobal._QuotationSaleOrderNo.equalsIgnoreCase("")) {
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    ClsGlobal._QuotationSaleOrderNo,
                                    "Retail Quotation");
                        }
                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.show();
                } else if (ClsGlobal._quotationStatusID != null && ClsGlobal._quotationStatusID != "") {

                    int Result = ClsInventoryOrderDetail.DeleteFromOrdermaster
                            (ClsGlobal._OrderNo
                                    , "", "DeleteById", SalesActivity.this);
                    if (true) {
                        ClsGlobal._QuotationSaleOrderNo = "";
                        ClsGlobal.editQuotationID = 0;
                        ClsGlobal.SetKeepOrderNo(SalesActivity.this, "",
                                "Retail Quotation");
                    }
                    SalesActivity.this.finish();

                } else {
                    if (FrameLayout.getVisibility() == View.VISIBLE) {
                        SalesActivity.this.finish();
                    } else {
                        newFragment.dismiss();
                    }
                }

            } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                if (!ClsGlobal._QuotationWholesaleOrderNo.equals("") && FrameLayout.getVisibility() == View.VISIBLE) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                    alertDialog.setTitle("Order is Running...");
                    alertDialog.setMessage("Please choose option for Running Order!");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("DELETE", (dialog, which) -> {

                        int Result = ClsInventoryOrderDetail.DeleteFromOrdermaster(ClsGlobal._OrderNo
                                , "", "DeleteById", SalesActivity.this);
                        if (true) {
                            ClsGlobal._QuotationWholesaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    "", "Wholesale Quotation");
                        }
                        SalesActivity.this.finish();
                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.setNegativeButton("KEEP", (dialog, which) -> {
                        SalesActivity.this.finish();
                        if (!ClsGlobal._QuotationWholesaleOrderNo.equalsIgnoreCase("")) {
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this, ClsGlobal._QuotationWholesaleOrderNo,
                                    "Wholesale Quotation");
                        }
                        dialog.dismiss();
                        dialog.cancel();
                    });
                    alertDialog.show();
                } else if (ClsGlobal._quotationStatusID != null && ClsGlobal._quotationStatusID != "") {

                    int Result = ClsInventoryOrderDetail.DeleteFromOrdermaster(ClsGlobal._OrderNo
                            , "", "DeleteById", SalesActivity.this);
                    if (true) {
                        ClsGlobal._QuotationWholesaleOrderNo = "";
                        ClsGlobal.editQuotationID = 0;
                        ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                "", "Wholesale Quotation");
                    }
                    SalesActivity.this.finish();

                } else {
                    if (FrameLayout.getVisibility() == View.VISIBLE) {
                        SalesActivity.this.finish();
                    } else {
                        newFragment.dismiss();
                    }
                }
            }

        } else {
//                newFragment.isVisible()
            if (FrameLayout.getVisibility() == View.VISIBLE) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
                alertDialog.setTitle("Order is update mode...");
                alertDialog.setMessage("What you like to do?");
                alertDialog.setIcon(R.drawable.ic_edit);

                alertDialog.setPositiveButton("CANCEL", (dialog, which) -> {

                    ClsGlobal.SetOrderEditMode(SalesActivity.this, "");

                    if (saleMode != null && !saleMode.equalsIgnoreCase("")) {

                        if (saleMode.equalsIgnoreCase("Sale")) {
                            ClsGlobal._OrderNo = "";
                            ClsGlobal.editOrderID = "";
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    "", "Sale");

                        } else if (saleMode.equalsIgnoreCase("Wholesale")) {
                            ClsGlobal._WholeSaleOrderNo = "";
                            ClsGlobal.editOrderID = "";
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    "", "Wholesale");

                        } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
                            ClsGlobal._QuotationSaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    "", "Retail Quotation");

                        } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                            ClsGlobal._QuotationWholesaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;
                            ClsGlobal.SetKeepOrderNo(SalesActivity.this,
                                    "", "Wholesale Quotation");
                        }
                    }
                    ClsInventoryOrderMaster.deletePendingSaveItems(SalesActivity.this);
                    ClsQuotationOrderDetail.deletePendingSaveQuotation(SalesActivity.this);

                    finish();
                    dialog.dismiss();
                    dialog.cancel();

                });
                alertDialog.setNegativeButton("CONTINUE", (dialog, which) -> {

                    dialog.dismiss();
                    dialog.cancel();

                });
                alertDialog.show();
            } else {
                if (FrameLayout.getVisibility() == View.VISIBLE) {
                    SalesActivity.this.finish();
                } else {
                    newFragment.dismiss();
                }
            }

        }

    }


    private List<ClsLayerItemMaster> sortAndAddSections(List<ClsLayerItemMaster> itemList) {

        List<ClsLayerItemMaster> tempList = new ArrayList<>();

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for (int i = 0; i < itemList.size(); i++) {
            //If it is the start of a new section we create a new listcell and add it to our array

            if (itemList.get(i).getITEM_NAME() != null) {
                //if (!itemList.get(i).isHeader()) {
                if (!(header.equals(String.valueOf(itemList.get(i).getITEM_NAME().charAt(0)).toUpperCase()))) {
                    ClsLayerItemMaster sectionCell = new ClsLayerItemMaster(String.valueOf(itemList.get(i).getITEM_NAME()
                            .charAt(0)).toUpperCase());
                    sectionCell.setHeader(true);
                    //A CHECK IN ALL ARRAY
                    if (!headerlist.contains(String.valueOf(itemList.get(i)
                            .getITEM_NAME().charAt(0)).toUpperCase())) {

                        tempList.add(sectionCell);

                        headerlist.add(String.valueOf(itemList.get(i).getITEM_NAME()
                                .charAt(0)).toUpperCase());
                    }
                }
                //}
            }

            Log.e("check", "outside if");
            tempList.add(itemList.get(i));
        }
        return tempList;
    }
    @Override
    public void OnClick() {

        String orderNum = "";

        if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
            if (saleMode.equalsIgnoreCase("Sale")) {
                orderNum = ClsGlobal._OrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale")) {
                orderNum = ClsGlobal._WholeSaleOrderNo;
            } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
                orderNum = ClsGlobal._QuotationSaleOrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                orderNum = ClsGlobal._QuotationWholesaleOrderNo;
            }
        } else {
            Log.e("--Check--", "setFooterDetail Step 10");
            Log.e("--Check--", "setFooterDetail ELSE");
        }

        if (orderNum != null && !orderNum.isEmpty()) {
            String _whereFooter = "".concat(" AND [OrderNo] = ").concat("'")
                    .concat(orderNum).concat("'");

            String _whereFooterQuotation = "".concat(" AND [QuotationNo] = ").concat("'")
                    .concat(orderNum).concat("'");

            if (!entryMode.equalsIgnoreCase("New")) {
                _whereFooter += "".concat(" AND [OrderID] = " + editOrderID);
            }

//            if (!entryMode.equalsIgnoreCase("New")) {
//                _whereFooterQuotation += "".concat(" AND [QuotationID] = " + editOrderID);
//            }


            if (!entryMode.equalsIgnoreCase("New")) {
                _whereFooterQuotation += "".concat(" AND [QuotationID] = " + editQuotationID);
                Log.e("--Edit--", "_whereFooterQuotation: " + _whereFooterQuotation);
            }


            if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
                if (saleMode.equalsIgnoreCase("Sale")
                        || saleMode.equalsIgnoreCase("Wholesale")) {

                    ClsInventoryOrderDetail.runningOrder order =
                            ClsInventoryOrderDetail.getRunningOrderDetails(_whereFooter,
                                    SalesActivity.this);

                    _footerValue = " ".concat("BILL#:" + orderNum).concat(", ITEMS: " + order.getItemCount())
                            .concat(", TOTAL: \u20B9 " + ClsGlobal.round(order.getTotalAmt(), 2));

                } else if (saleMode.equalsIgnoreCase("Retail Quotation")
                        || saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                    ClsQuotationOrderDetail.runningQuotation order =
                            ClsQuotationOrderDetail.getRunningQuotationDetail(_whereFooterQuotation,
                                    SalesActivity.this);

                    _footerValue = " ".concat("BILL#:" + orderNum).concat(", ITEMS: " + order.getItemCount())
                            .concat(", TOTAL: \u20B9 " + ClsGlobal.round(order.getTotalAmt(), 2));
                }
                txt_footer_value.setText(_footerValue);

            } else {
                _footerValue = "NO ITEMS";
                txt_footer_value.setText(_footerValue);
            }

        } else {
            _footerValue = "NO ITEMS";
            txt_footer_value.setText(_footerValue);
        }
    }


    @Override
    public void OnDeleteClick() {
        String orderNum = "";
        if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
            if (saleMode.equalsIgnoreCase("Sale")) {
                orderNum = ClsGlobal._OrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale")) {
                orderNum = ClsGlobal._WholeSaleOrderNo;
            } else if (saleMode.equalsIgnoreCase("Retail Quotation")) {
                orderNum = ClsGlobal._QuotationSaleOrderNo;
            } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                orderNum = ClsGlobal._QuotationWholesaleOrderNo;
            }
        } else {
            Log.e("--Check--", "setFooterDetail Step 10");
            Log.e("--Check--", "setFooterDetail ELSE");
        }

        if (orderNum != null && !orderNum.isEmpty()) {
            String _whereFooter = "".concat(" AND [OrderNo] = ").concat("'")
                    .concat(orderNum).concat("'");

            String _whereFooterQuotation = "".concat(" AND [QuotationNo] = ").concat("'")
                    .concat(orderNum).concat("'");

            if (!entryMode.equalsIgnoreCase("New")) {
                _whereFooter += "".concat(" AND [OrderID] = " + editOrderID);
                _whereFooterQuotation += "".concat(" AND [QuotationID] = " + editQuotationID);
            }

            if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
                if (saleMode.equalsIgnoreCase("Sale")
                        || saleMode.equalsIgnoreCase("Wholesale")) {

                    ClsInventoryOrderDetail.runningOrder order =
                            ClsInventoryOrderDetail.getRunningOrderDetails(_whereFooter,
                                    SalesActivity.this);

                    _footerValue = " ".concat("BILL#:" + orderNum).concat(", ITEMS: " + order.getItemCount())
                            .concat(", TOTAL: \u20B9 " + ClsGlobal.round(order.getTotalAmt(), 2));

                } else if (saleMode.equalsIgnoreCase("Retail Quotation")
                        || saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                    ClsQuotationOrderDetail.runningQuotation order =
                            ClsQuotationOrderDetail.getRunningQuotationDetail(_whereFooterQuotation,
                                    SalesActivity.this);

                    _footerValue = " ".concat("BILL#:" + orderNum).concat(", ITEMS: " + order.getItemCount())
                            .concat(", TOTAL: \u20B9 " + ClsGlobal.round(order.getTotalAmt(), 2));
                }
                txt_footer_value.setText(_footerValue);

            } else {
                _footerValue = "NO ITEMS";
                txt_footer_value.setText(_footerValue);
            }

        } else {
            _footerValue = "NO ITEMS";
            txt_footer_value.setText(_footerValue);
        }
    }


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsLayerItemMaster>> {

        int currentPageNo;
        int totalItemsCount;
        String mode = "";

        @Override
        protected void onPreExecute() {
//            progress_bar.setVisibility(View.VISIBLE);
//            if (mode.equalsIgnoreCase("")) {
////                Toast.makeText(SalesActivity.this, "LOAD MORE ITEMS", Toast.LENGTH_SHORT).show();
//
//            }

//            Toast.makeText(SalesActivity.this, "Start loading", Toast.LENGTH_SHORT).show();
        }

        LoadAsyncTask(int currentPageNo, int totalItemsCount, String mode) {
            this.currentPageNo = currentPageNo;
            this.totalItemsCount = totalItemsCount;
            this.mode = mode;


//            Toast.makeText(SalesActivity.this, "Page:" + this.currentPageNo +
//                    ", Total items:" + this.totalItemsCount, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ClsLayerItemMaster> doInBackground(Void... voids) {

            if (mode.equalsIgnoreCase("")) {
                Log.e("paging", "Page no:- " + currentPageNo);
                int skip = MAX_ITEMS_PER_REQUEST * (currentPageNo - 1);
//            offset = limit * page;
                Log.e("paging skip:-", String.valueOf(skip));
                _paging = "LIMIT ".concat("" + skip).concat(", ").concat("" + MAX_ITEMS_PER_REQUEST);
                Log.e("paging qurey:- ", _paging);
            } else {
                headerlist.clear();
                list.clear();
            }


            List<ClsLayerItemMaster> listnew = new ArrayList<>();

//            _whereLastPurchase = " AND PD.[PurchaseID] =".concat(String.valueOf(19));
            _whereLastPurchase = "";

            listnew = ClsLayerItemMaster.getLayerItemListNew(getApplication(),
                    _where, _whereSearch.concat(" ").concat(_whereSearchChar).concat(" "), _paging);

            Log.e("add", "listnew:-" + listnew.size());

            for (ClsLayerItemMaster current : listnew) {

                ClsLayerItemMaster clsLayerItemMaster = new ClsLayerItemMaster();

                clsLayerItemMaster.setLAYERITEM_ID(current.getLAYERITEM_ID());
                clsLayerItemMaster.setITEM_NAME(current.getITEM_NAME());
                clsLayerItemMaster.setITEM_CODE(current.getITEM_CODE());
                clsLayerItemMaster.setMIN_STOCK(current.getMIN_STOCK());
                clsLayerItemMaster.setMAX_STOCK(current.getMAX_STOCK());
                clsLayerItemMaster.setUNIT_CODE(current.getUNIT_CODE());
                clsLayerItemMaster.setHSN_SAC_CODE(current.getHSN_SAC_CODE());
                clsLayerItemMaster.setSLAB_NAME(current.getSLAB_NAME());
                clsLayerItemMaster.setTAX_SLAB_ID(current.getTAX_SLAB_ID());
                clsLayerItemMaster.setTAX_APPLY(current.getTAX_APPLY());
                clsLayerItemMaster.setRATE_PER_UNIT(current.getRATE_PER_UNIT());
                clsLayerItemMaster.setOpening_Stock(current.getOpening_Stock());
                clsLayerItemMaster.setSGST(current.getSGST());
                clsLayerItemMaster.setCGST(current.getCGST());
                clsLayerItemMaster.setIGST(current.getIGST());
                clsLayerItemMaster.setWHOLESALE_RATE(current.getWHOLESALE_RATE());
                clsLayerItemMaster.setTAX_TYPE(current.getTAX_TYPE());
                clsLayerItemMaster.setIN(current.getIN());
                clsLayerItemMaster.setOUT(current.getOUT());
                clsLayerItemMaster.setTagList(current.getTagList());
                clsLayerItemMaster.set_saleRateIncludingTax(current.get_saleRateIncludingTax());
                clsLayerItemMaster.set_wholesaleRateIncludingTax(current.get_wholesaleRateIncludingTax());
                clsLayerItemMaster.setAverageSaleRate(current.getAverageSaleRate());

                clsLayerItemMaster.setLastPurchasePrice(current.getLastPurchasePrice());

                Log.e("--Dialog--", "LastPurchase: " + current.getITEM_NAME()
                        + ": " + current.getLastPurchasePrice());


                list.add(clsLayerItemMaster);

            }


            Log.e("list_Item", " list.addAll call " + list.size());


            Gson gson = new Gson();
            String jsonInString = gson.toJson(list);
            Log.e("Result", "list---" + jsonInString);


            list = sortAndAddSections(list);
            _paging = "";

            return list;
        }


        @Override
        protected void onPostExecute(List<ClsLayerItemMaster> list) {
            super.onPostExecute(list);
            Log.e("list_Item", "list_Item call " + list.size());


//            progress_bar.setVisibility(View.GONE);
            mCu.AddItems(list);


            if (list.size() == 0) {
                Log.e("list_Item", "list_Item call");
                empty_title_text.setVisibility(View.VISIBLE);
                ll_uper_filter.setVisibility(View.GONE);
                list_rv.setVisibility(View.VISIBLE);
            } else {
                empty_title_text.setVisibility(View.GONE);
                list_rv.setVisibility(View.VISIBLE);
                ll_uper_filter.setVisibility(View.VISIBLE);
            }
//            Toast.makeText(SalesActivity.this, "End loading", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        setFooterDetail();


//        OrdersActivity ordersActivity = new OrdersActivity();
//        ordersActivity.setOnUpdateFooterValueFromOrders(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        MyClass.getBus().unregister(this);

        Log.e("onDestroy", "onDestroy cal");
        list_rv.clearOnScrollListeners();


    }
}
