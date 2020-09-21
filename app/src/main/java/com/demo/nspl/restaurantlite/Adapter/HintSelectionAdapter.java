package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsSelectionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HintSelectionAdapter extends RecyclerView.Adapter<HintSelectionAdapter.ViewHolder> {


    private LayoutInflater inflater = null;
    Context context;
    View itemView;
    List<ClsSelectionModel> data = new ArrayList<>();
    List<ClsSelectionModel> selectedList = new ArrayList<>();
    private boolean isSelected[];

    public HintSelectionAdapter(Context context, List<ClsSelectionModel> data) {
        super();
        this.data = data;
        this.context = context;
        isSelected = new boolean[data.size()];
        inflater = LayoutInflater.from(context);
    }

    public void addItems(List<ClsSelectionModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hint_selection, parent, false);
        ViewHolder myViewHolder = new ViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClsSelectionModel obj = data.get(position);

        holder.txt_hint.setText(obj.get_character());

        holder.ckb_hint_selection.setChecked(obj.isSelected());
        holder.ckb_hint_selection.setTag(data.get(position));


        holder.ll_multiple_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = holder.ckb_hint_selection.isChecked();
                holder.ckb_hint_selection.setChecked(!flag);
                isSelected[position] = !isSelected[position];
            }
        });


        holder.ckb_hint_selection.setOnClickListener(v -> isSelected[position] = !isSelected[position]);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_hint_id;
        private LinearLayout ll_multiple_item;
        private TextView txt_hint;
        private CheckBox ckb_hint_selection;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            txt_hint_id = itemLayoutView.findViewById(R.id.txt_hint_id);
            txt_hint = itemLayoutView.findViewById(R.id.txt_hint);
            ll_multiple_item = itemLayoutView.findViewById(R.id.ll_multiple_item);
            ckb_hint_selection = itemLayoutView.findViewById(R.id.ckb_hint_selection);

        }
    }

    public boolean[] getSelectedFlags() {
        return isSelected;
    }

    public void getSelectedFlagsClear() {


        Arrays.fill(isSelected, true);
    }


}
