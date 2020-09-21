package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.demo.nspl.restaurantlite.R;

import com.demo.nspl.restaurantlite.classes.ClsTaxItem;
import com.demo.nspl.restaurantlite.classes.ClsTaxes;

import java.util.ArrayList;
import java.util.List;

import static com.demo.nspl.restaurantlite.activity.AddExpenseActivity.setTaxValueForSave;
//
/**
 * Created by Desktop on 3/30/2018.
 */

public class TaxesAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater = null;
    private List<ClsTaxItem> lsClsTaxItems = new ArrayList<>();


    public TaxesAdapter(Context context, List<ClsTaxItem> lsClsTaxItems) {
        super();
        this.lsClsTaxItems = lsClsTaxItems;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public TaxesAdapter() {
    }


    @Override
    public int getCount() {
        return lsClsTaxItems.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return lsClsTaxItems.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_tax_item_type, viewGroup, false);
            holder = new ViewHolder();

            holder.ll_layout = convertView.findViewById(R.id.ll_layout);
            holder.txt_type = convertView.findViewById(R.id.txt_type);
            holder.txt_label = convertView.findViewById(R.id.txt_label);
            holder.edt_value = convertView.findViewById(R.id.edt_value);


            Log.e("TaxVal","---"+holder.edt_value.getText().toString());



//            for (int i = 0; i < 10; i++) {
//                holder.edt_value.setId(i);
//                Log.e("Counter", "edt_valueID-" + i);
//            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClsTaxItem objTax = lsClsTaxItems.get(position);

        holder.txt_type.setText(objTax.getType());
        holder.txt_label.setText(objTax.getTaxName());
//        holder.edt_value.setText(String.valueOf(objTax.get_TaxAmount()));
        if (objTax.get_TaxAmount() > 0) {

            holder.edt_value.setText(String.valueOf(objTax.get_TaxAmount()));
        }
        holder.edt_value.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Double txtValue = 0.0;
                if (s != null && !s.toString().isEmpty() && s.toString() != ""
                        && !s.toString().equalsIgnoreCase(".")) {
                    txtValue = Double.valueOf(s.toString());
                    //Toast.makeText(context, "txtValue- " + txtValue, Toast.LENGTH_SHORT).show();
                }
                setTaxValue(position, String.valueOf(txtValue));


//                Double txtValue = 0.0;
//                if (s != null && !s.toString().isEmpty()) {
//                    txtValue = Double.valueOf(s.toString());
//                }
//                setTaxValue(position,String.valueOf( txtValue));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {

                }

            }
        });


//        Intent intent = new Intent(context, AddExpenseMasterNew.class);
//        intent.putExtra("TaxName", i.getTaxName());
//        intent.putExtra("TaxValue", i.getTaxValue());
//        context.startActivity(intent);

        return convertView;
    }


    static class ViewHolder {
        LinearLayout ll_layout;
        TextView txt_type;
        TextView txt_label;
        EditText edt_value;

    }

    void setTaxValue(int _position, String _txtValue) {
        for (ClsTaxItem OBJsize : lsClsTaxItems) {
            if (_position == lsClsTaxItems.indexOf(OBJsize)) {
                OBJsize.set_TaxAmount(Double.valueOf(_txtValue));
            }
            lsClsTaxItems.set(lsClsTaxItems.indexOf(OBJsize), OBJsize);
        }
        setTaxValueForSave(lsClsTaxItems);
    }

}
