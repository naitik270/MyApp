package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmployee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/15/2018.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {

    Context context;
    private View view;
    List<ClsEmployee> listEmployee = new ArrayList<ClsEmployee>();
    OnEmployeeClick  onEmployeeClick;


    public EmployeeAdapter(Context context) {
        this.context = context;
    }

    public void AddItems(List<ClsEmployee> listEmployee) {
        this.listEmployee = listEmployee;
        notifyDataSetChanged();
    }

    public void RemoveItem(int position) {
        listEmployee.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnEmployeeClick  onEmployeeClick) {
        this.onEmployeeClick = onEmployeeClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_employee_master, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    public interface OnEmployeeClick {
        void OnClick(ClsEmployee clsEmployee, int position);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ClsEmployee objClsEmployee = listEmployee.get(position);

        holder.lbl_employee_name.setText(objClsEmployee.getEmployee_name());
        holder.lbl_contact_no.setText(objClsEmployee.getContact_no());
        holder.lbl_address.setText(objClsEmployee.getAddress());
        holder.lbl_salary.setText(String.valueOf(objClsEmployee.getSalary()));
        holder.lbl_active.setText(String.valueOf(objClsEmployee.getActive()));
        holder.lbl_srno.setText(String.valueOf(objClsEmployee.getSort_no()));
        holder.lbl_remark.setText(objClsEmployee.getRemark());
        holder.lbl_dob.setText(objClsEmployee.getDob());


        holder.Bind(objClsEmployee, onEmployeeClick, position);
    }

    @Override
    public int getItemCount() {
        return listEmployee.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_employee_name, lbl_contact_no, lbl_address, lbl_salary,
                lbl_active, lbl_srno, lbl_remark, lbl_dob;

        //  ImageView lbl_emp_image;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            lbl_employee_name = itemView.findViewById(R.id.lbl_employee_name);
            lbl_contact_no = itemView.findViewById(R.id.lbl_contact_no);
            lbl_address = itemView.findViewById(R.id.lbl_address);
            lbl_salary = itemView.findViewById(R.id.lbl_salary);
            lbl_active = itemView.findViewById(R.id.lbl_active);
            lbl_srno = itemView.findViewById(R.id.lbl_srno);
            lbl_remark = itemView.findViewById(R.id.lbl_remark);
            lbl_dob = itemView.findViewById(R.id.lbl_dob);
            //  lbl_emp_image=itemView.findViewById(R.id.lbl_emp_image);
        }


        void Bind(ClsEmployee clsEmployee, OnEmployeeClick onEmployeeClick, int position) {
            ll_header.setOnClickListener(v -> onEmployeeClick.OnClick(clsEmployee, position));
        }

    }
}
