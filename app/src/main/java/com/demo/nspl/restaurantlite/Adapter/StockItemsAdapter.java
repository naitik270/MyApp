package com.demo.nspl.restaurantlite.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddInventoryStockActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Desktop on 4/24/2018.
 */

public class StockItemsAdapter extends BaseAdapter {
    List<ClsInventoryStock> list= new ArrayList<>();
    Context context;
    private LayoutInflater inflater;


    public StockItemsAdapter(Context context, List<ClsInventoryStock> lstClsNameValues) {
        inflater = LayoutInflater.from(context);
        this.list = lstClsNameValues;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.bill_wise_items, viewGroup, false);

        ClsInventoryStock clsInventoryStock=new ClsInventoryStock();
        clsInventoryStock=list.get(i);



        TextView txt_itemname =view.findViewById(R.id.txt_itemname);
        TextView txt_qty =view.findViewById(R.id.txt_qty);
        TextView txt_amount =view.findViewById(R.id.txt_amount);
        final LinearLayout linear=view.findViewById(R.id.linear);


        txt_itemname.setText(clsInventoryStock.getInventory_item_name());
        txt_qty.setText(String.valueOf(clsInventoryStock.getQty()).concat(" ").concat(clsInventoryStock.getUnitname()));
        txt_amount.setText(String.valueOf(clsInventoryStock.getAmount()).concat(" ").concat("\u20B9"));


//        final ClsInventoryStock finalClsInventoryStock = clsInventoryStock;

        linear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);



                RelativeLayout lyout_update = dialog.findViewById(R.id.lyout_update);
                RelativeLayout lyout_delete = dialog.findViewById(R.id.lyout_delete);



                lyout_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();
                        ClsInventoryStock finalClsInventoryStock=new ClsInventoryStock();
                        try {
                            finalClsInventoryStock=list.get(i);
                            Intent intent = new Intent(context, AddInventoryStockActivity.class);
                            Log.e("ITEMS","ADapter-->>"+ finalClsInventoryStock.getStock_id());
                            intent.putExtra("StockID", finalClsInventoryStock.getStock_id());
                            intent.putExtra("SOURCE", "BILL ENTRY");
                            context.startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.getMessage();
                            Log.e("UPDATE",e.getMessage());
                        }
                    }
                });
                dialog.show();
                lyout_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Confirm Delete...");
                        alertDialog.setMessage("Are you sure you want delete?");
                        alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ClsInventoryStock itemGetSet = new ClsInventoryStock();
                                itemGetSet = list.get(i);
                                int result = 0;
                                ClsInventoryStock Obj = new ClsInventoryStock(context);
                                Obj.setStock_id(itemGetSet.getStock_id());
                                Log.e("InventoryItem", String.valueOf(itemGetSet.getStock_id()));
                                result = ClsInventoryStock.Delete(Obj);
                                Log.e("UpdateTaG", String.valueOf(result));
                                if (result == 1) {
                                    list.remove(i);
                                    notifyDataSetChanged();
                                    notifyDataSetInvalidated();
                                }


                                Log.e("UpdateTaG", String.valueOf(result));

                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();

                    }
                });
                return false;
            }
        });

        return view;
    }

}
