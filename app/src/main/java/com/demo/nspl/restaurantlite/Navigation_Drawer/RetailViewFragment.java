package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Adapter.OrderAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickListenerClsItem;

import com.demo.nspl.restaurantlite.activity.OrdersActivity;
import com.demo.nspl.restaurantlite.activity.RecentOrderActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsItem;
import com.demo.nspl.restaurantlite.classes.ClsOrder;
import com.demo.nspl.restaurantlite.classes.ClsOrderDetailMaster;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetailViewFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {


    private List<ClsItem> list_Item;
    private OrderAdapter cu;
    StickyListHeadersListView rv;

    Toolbar toolbar;
    ImageButton back_buuton, filter;
    TextView toolbar_title ,empty_title_text,empty_subtitle_text;
    int CurrentId;
    private int get_Table_Id;
    private double mget_total_amount;
   // public static String _OrderNo = "";


    public RetailViewFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ClsGlobal.isFristFragment = true;

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "RetailViewFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Retail");

//        get_Table_Id = getArguments().getInt("Table_id");
//
//        Log.i("get_Table_Id", String.valueOf(get_Table_Id));

    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_retail_view, container, false);
        rv = v.findViewById(R.id.rv);

        empty_title_text = v.findViewById(R.id.empty_title_text);

      //  ClsGlobal.isFristFragment = true;

        ViewData("");
        if (getActivity() != null) {
            CurrentId = new ClsOrder(getContext()).GetMaxItemId();
            Log.e("CurrentID", String.valueOf(CurrentId));

        }
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_retails, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.recent_orders) {
            Intent intent = new Intent(getActivity(), RecentOrderActivity.class);
            intent.putExtra("SOURCE", "retail");
            startActivity(intent);
            return true;
        }

        if (id == R.id.view_orders) {
            if (ClsGlobal._OrderNo.matches("")) {
                Toast.makeText(getContext(), "There is No Order Place It!", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getActivity(), OrdersActivity.class);
                intent.putExtra("OrderId", ClsGlobal._OrderNo);
                intent.putExtra("Source", "RetailView");
                startActivity(intent);
                //RetailViewFragment._OrderNo="";
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ViewData(String search) {


        list_Item = new ArrayList<>();
        list_Item = new ClsItem(getActivity()).RetailViewGetList(search);

        if (list_Item.size() == 0){
            Log.e("list_Item","list_Item call");
            empty_title_text.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else {
            empty_title_text.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.VISIBLE);
        }

        Log.e("list_Item", String.valueOf(list_Item.size()));

        cu = new OrderAdapter(getActivity(), (ArrayList<ClsItem>) list_Item, new OnClickListenerClsItem() {
            @Override
            public void OnClick(ClsItem clsItem) {
                Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_retail_view);
                dialog.setCancelable(true);



                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);


                double get_item_price = clsItem.getPrice();

                // Display Item_Name example
                TextView Item_Name = dialog.findViewById(R.id.Item_Name);
                Item_Name.setText(clsItem.getItem_name());

                TextView txt_price = dialog.findViewById(R.id.txt_price);
                txt_price.setText("Price: " + String.valueOf(get_item_price));

                TextView Count_Text = dialog.findViewById(R.id.Count_Text);
                Count_Text.setText(String.valueOf(1));

                TextView txt_item_amount = dialog.findViewById(R.id.txt_item_amount);
                txt_item_amount.setText("Total Amount: " + String.valueOf(get_item_price));
                txt_item_amount.setTag(String.valueOf(get_item_price));//140.0

                // On minus Button Order Click.
                Button minus_btn = dialog.findViewById(R.id.minus_btn);
                minus_btn.setOnClickListener(view1 -> {

                    int counter = 1;
                    counter = Integer.parseInt(Count_Text.getText().toString());
                    if (counter != 1) {
                        counter--;
                        mget_total_amount = get_item_price * counter;
                        Count_Text.setText(String.valueOf(counter));
                        txt_item_amount.setText("Total Amount: " + String.valueOf(mget_total_amount));
                    }

                });

                // On plus Button Order Click.
                Button plus_btn = dialog.findViewById(R.id.plus_btn);
                plus_btn.setOnClickListener(view12 -> {

                    int counter = 1;
                    counter = Integer.parseInt(Count_Text.getText().toString());
                    if (counter != 10) {
                        counter++;
                        mget_total_amount = get_item_price * counter;
                        Count_Text.setText(String.valueOf(counter));
                        txt_item_amount.setText("Total Amount: " + String.valueOf(mget_total_amount));
                    }

                });

                //generate Random Order No.
                //insert into order detail master
                // On Add Order Click.
                Button btn_add_order = dialog.findViewById(R.id.btn_add_order);
                btn_add_order.setOnClickListener((View view13) -> {
                    if (ClsGlobal._OrderNo == null || ClsGlobal._OrderNo.isEmpty() ||ClsGlobal._OrderNo.matches("")) {
                        ClsGlobal._OrderNo = String.valueOf(ClsInventoryOrderMaster.getNextOrderNo(getContext()));//ClsSales.getNextOrderNo();
                       // ClsGlobal._OrderNo = ClsGlobal.getRandom();
                    }

                    String show = " Count: " + Count_Text.getText().toString() +
                            "Total Amount: " + txt_item_amount.getText().toString()
                            + "Price: " + txt_price.getText().toString() +
                            "Item Name:" + Item_Name.getText().toString();


                    ClsOrderDetailMaster clsOrderDetailMaster = new ClsOrderDetailMaster(getContext());

                    // OrderID = clsOrderDetailMaster.getOrderId();
                    clsOrderDetailMaster.setOrderId(0);
                    clsOrderDetailMaster.setOrderNo(ClsGlobal._OrderNo);
                    clsOrderDetailMaster.setItem_ID(clsItem.getItem_id());
                    clsOrderDetailMaster.setItem_Name(Item_Name.getText().toString());

                    Double itemTotalAmount = get_item_price * Double.valueOf(Count_Text.getText().toString()); //rate * qty txt_item_amount.getText().toString();
                    //String final_Total_Amount = Total_Amount.replace("Total Amount: ", "");

                    clsOrderDetailMaster.setTotal_Amount(itemTotalAmount);
                    clsOrderDetailMaster.setQty(Double.valueOf(Count_Text.getText().toString()));
                    clsOrderDetailMaster.setRate(get_item_price);
                    clsOrderDetailMaster.setOtherTax1("");
                    clsOrderDetailMaster.setOtherTax2("");
                    clsOrderDetailMaster.setOtherTax3("");
                    clsOrderDetailMaster.setOtherTax4("");
                    clsOrderDetailMaster.setOtherTax5("");
                    clsOrderDetailMaster.setOtherTaxVal1(0.0);
                    clsOrderDetailMaster.setOtherTaxVal2(0.0);
                    clsOrderDetailMaster.setOtherTaxVal3(0.0);
                    clsOrderDetailMaster.setOtherTaxVal4(0.0);
                    clsOrderDetailMaster.setOtherTaxVal5(0.0);
                    clsOrderDetailMaster.setEntryDateTime(ClsGlobal.getEntryDateTime());
                    clsOrderDetailMaster.setType("retail");
                    clsOrderDetailMaster.setStatus("Complete");
                    clsOrderDetailMaster.setTotalTaxAmount(0.0);
                    clsOrderDetailMaster.setGrand_Total(0.0);

                    int getColumnCounts = ClsOrderDetailMaster.Insert(clsOrderDetailMaster);

                    Toast.makeText(getActivity(), "Insert: " + String.valueOf(getColumnCounts), Toast.LENGTH_LONG).show();
                    //Toast.makeText(context, show , Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                });


                dialog.show();
            }
        });


        rv.setAdapter(cu);
        cu.notifyDataSetChanged();


    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        ClsGlobal.isFristFragment = false;
//    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String where = "";
        if (newText != "") {
            where = " AND [itemaster].[ITEM_NAME] like " + "'%" + newText + "%'";
        }
        ViewData(where);


        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }
}
