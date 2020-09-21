//package com.demo.nspl.restaurantlite.Adapter;
//
//import android.content.Context;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.demo.nspl.restaurantlite.R;
//import com.demo.nspl.restaurantlite.activity.AddTaxSlabActivity;
//import com.demo.nspl.restaurantlite.classes.ClsTaxes;
//
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class TaxesNameAdapter extends RecyclerView.Adapter<TaxesNameAdapter.MyViewHolder> {
//
//
//    List<ClsTaxes> list;
//    Context context;
//    View itemView;
//    private LayoutInflater inflater = null;
//
//    public TaxesNameAdapter(Context context, List<ClsTaxes> List){
//        this.context = context;
//        this.list = List;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.taxes_list, parent, false);
//        TaxesNameAdapter.MyViewHolder myViewHolder = new TaxesNameAdapter.MyViewHolder(itemView);
//        return myViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        ClsTaxes currentObj = list.get(position);
//        holder.txt_Taxes_Name.setText(currentObj.getTax_name());
//        if (currentObj.getTax_value() != null){
//            Log.e("insert","insert double call");
//            holder.Edit_Taxes_Value.setText(String.valueOf(currentObj.getTax_value()));
//        }
//
//        holder.Edit_Taxes_Value.addTextChangedListener(new TextWatcher() {
//
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.e("listLayerNameUpdate:-- ", "afterTextChanged");
//                String txtValue = "";
//                if (s != null && !s.toString().isEmpty() && s.toString() != "") {
//                    txtValue = s.toString();
//                    Log.e("listLayerNameUpdate:-- ", "txtValue = s.toString();");
//                }
//
//                //update to activity LIST
//                setCustomerInfo(position, currentObj.getTax_name(), txtValue);
//
//                //set value in currunt textBOX
//                //myViewHolder.Edit_Txt_Layer_Namer.setText(current.getLayerValue());
//                Log.e("listLayerNameUpdate:-- ", "txtValue = s.toString();");
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public static EditText Edit_Taxes_Value;
//        TextView txt_Taxes_Name;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//
//            txt_Taxes_Name = itemView.findViewById(R.id.txt_Taxes_Name);
//            Edit_Taxes_Value = itemView.findViewById(R.id.Edit_Taxes_Value);
//
//        }
//    }
//
//    void setCustomerInfo(int _position, String layerName, String value) {
//        Log.e("setCustomerInfo:-- ", "layerName:"+layerName);
//        Log.e("setCustomerInfo:-- ", "value:"+value);
//        Log.e("setCustomerInfo:-- ", "_position"+_position);
//        for (ClsTaxes OBJLayer : list) {
//            if (_position == list.indexOf(OBJLayer)) {
//                if (layerName.equalsIgnoreCase(OBJLayer.getTax_name())) {
//                    OBJLayer.setTax_value(value.equalsIgnoreCase("") ? 0.0 : Double.valueOf(value));
//                }
//                list.set(_position, OBJLayer);
//            }
//        }
//        Log.e("setCustomerInfo:-- ", "setCustomerInfoEND");
//        AddTaxSlabActivity.updateList(list);
//    }
//}
