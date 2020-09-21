package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Adapter.CategoryAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddCategoryActivity;
import com.demo.nspl.restaurantlite.classes.ClsCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private List<ClsCategory> list_Categories;
    private CategoryAdapter cu;
    RecyclerView rv;
    private TextView empty_title_text;


    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "CategoryFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Categories");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        ClsGlobal.isFristFragment = true;

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
                startActivity(intent);
            }
        });


        rv = v.findViewById(R.id.rv);
        empty_title_text = v.findViewById(R.id.empty_title_text);

        ViewData();


        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewData();

    }

    private void ViewData() {

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        list_Categories = new ArrayList<>();
        list_Categories = new ClsCategory(getActivity()).getList("");


        if (list_Categories.size() == 0) {
            empty_title_text.setVisibility(View.VISIBLE);
        } else {
            empty_title_text.setVisibility(View.INVISIBLE);
        }

        cu = new CategoryAdapter((AppCompatActivity) getActivity(), getActivity(), (ArrayList<ClsCategory>) list_Categories);
        rv.setAdapter(cu);
        cu.notifyDataSetChanged();

    }


}
