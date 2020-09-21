package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.ExpenseTypeAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddExpenseTypeActivity;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseTypeFragment extends Fragment {

    private List<ClsExpenseType> list_expensetypes;
    private ExpenseTypeAdapter cu;
    RecyclerView rv;
    private TextView empty_title_text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "ExpenseTypeFragment"));
        }
    }

    public ExpenseTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Expense Types");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense_type, container, false);
        ClsGlobal.isFristFragment = true;
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddExpenseTypeActivity.class);
            startActivity(intent);
        });

        rv = v.findViewById(R.id.rv);
        empty_title_text = v.findViewById(R.id.empty_title_text);
        empty_title_text.setVisibility(View.INVISIBLE);
        ViewData();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    private void ViewData() {

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        list_expensetypes = new ArrayList<>();
        list_expensetypes = new ClsExpenseType(getActivity()).getList("");

        if (list_expensetypes.size() == 0) {
            empty_title_text.setVisibility(View.VISIBLE);
        } else {
            empty_title_text.setVisibility(View.INVISIBLE);
        }

        cu = new ExpenseTypeAdapter(getContext(), list_expensetypes);
        rv.setAdapter(cu);
        cu.notifyDataSetChanged();


    }

}
