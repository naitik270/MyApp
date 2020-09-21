package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CustomAutoSuggestAdapter extends ArrayAdapter<ClsCustomerMaster> {

    ArrayList<ClsCustomerMaster> userDetails, tempUserDetails, suggestions;

    public CustomAutoSuggestAdapter(Context context, ArrayList<ClsCustomerMaster> objects) {
        super(context, 0, objects);
        this.userDetails = objects;
        this.tempUserDetails = new ArrayList<ClsCustomerMaster>(objects);
        this.suggestions = new ArrayList<ClsCustomerMaster>(objects);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClsCustomerMaster current = getItem(position);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(current);
        Log.d("Check", "ClsCustomerMaster current- " + jsonInString);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.retail_row_vendor_list, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.txt_vendor_name);
        TextView tvNumber = convertView.findViewById(R.id.txt_vendor_mobile);

        if (current != null) {
            if (tvName != null) {
                tvName.setText(current.getmName());
            }

            if (tvNumber != null) {
                tvNumber.setText(current.getmMobile_No());
            }
        }


        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }


    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ClsCustomerMaster userDetails = (ClsCustomerMaster) resultValue;
            return userDetails.getmName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ClsCustomerMaster userDetails : tempUserDetails) {
                    if (userDetails.getmName().toLowerCase().contains(constraint.toString().toLowerCase())
                            || userDetails.getmMobile_No().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(userDetails);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<ClsCustomerMaster> c = (ArrayList<ClsCustomerMaster>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ClsCustomerMaster cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
