package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddItemActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;

import java.util.ArrayList;
import java.util.List;

public class LayerNameAdapter extends RecyclerView.Adapter<LayerNameAdapter.MyViewHolder> {

    List<ClsInventoryLayer> layerList = new ArrayList<>();
    Context context;
    View itemView;
    LayoutInflater inflater;

    public LayerNameAdapter(Context context, List<ClsInventoryLayer> layerList) {
        this.context = context;
        this.layerList = layerList;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layer_name_list, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ClsInventoryLayer current = layerList.get(i);
        Log.e("listLayerNameUpdate:-- ", "STEP1");
        myViewHolder.txt_Layer_name.setText(current.getInventoryLayerName() + ": ");
        myViewHolder.Edit_Txt_Layer_Namer.setText(current.getLayerValue());

        myViewHolder.Edit_Txt_Layer_Namer.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("listLayerNameUpdate:-- ", "afterTextChanged");

                Log.e("--Layer--", "Step:1");

                String txtValue = "";

                Log.e("--Layer--", "Step:2");

                if (s != null && !s.toString().isEmpty() && s.toString() != "") {
                    Log.e("--Layer--", "Step:3");

                    txtValue = s.toString();

                    Log.e("--Layer--", "Step:4" + txtValue);

                }
                Log.e("--Layer--", "Step:5");


                //update to activity LIST
                setCustomerInfo(i, current.getInventoryLayerName(), txtValue);

                Log.e("--Layer--", "Step:6");

                //set value in currunt textBOX
                //myViewHolder.Edit_Txt_Layer_Namer.setText(current.getLayerValue());
            }
        });

    }

    @Override
    public int getItemCount() {
        return layerList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public static EditText Edit_Txt_Layer_Namer;
        TextView txt_Layer_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_Layer_name = itemView.findViewById(R.id.txt_Layer_name);
            Edit_Txt_Layer_Namer = itemView.findViewById(R.id.Edit_Txt_Layer_Namer);


        }
    }

    void setCustomerInfo(int _position, String layerName, String value) {


        Log.e("--Layer--", "Step:7");

        for (ClsInventoryLayer OBJLayer : layerList) {

            Log.e("--Layer--", "Step:8");

            Log.e("--OBJLayer--", "LayerName: " + OBJLayer.getInventoryLayerName() + " : " + OBJLayer.getLayerValue());

            if (_position == layerList.indexOf(OBJLayer)) {

                Log.e("--Layer--", "Step:9");

                if (layerName.equalsIgnoreCase(OBJLayer.getInventoryLayerName())) {

                    Log.e("--Layer--", "Step:10");

                    OBJLayer.setLayerValue(value);

                    Log.e("--Layer--", "Step:11");

                }

                layerList.set(_position, OBJLayer);

                Log.e("--Layer--", "Step:12");

            }


            Log.e("--Layer--", "Step:13");

        }

        Log.e("--Layer--", "Step:14");

        AddItemActivity.updateList(layerList);
    }
}
