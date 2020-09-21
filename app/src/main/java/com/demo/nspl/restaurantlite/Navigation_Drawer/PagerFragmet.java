package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.FilterAdpater;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsItemFilter;
import com.demo.nspl.restaurantlite.classes.Filters;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



@SuppressLint("ValidFragment")
public class PagerFragmet extends Fragment {

    private static final String KEY_TITLE = "key_titile";
    private static final String KEY_PAGE_ID = "key_page_id";

    public static List<Filters> SelectedFilter = new ArrayList<>();
    private FilterAdpater mCv;
    private RecyclerView recyclerView;
    private String title;
    private LinearLayout nodata_layout;
    int page_id;
    private List<ClsItemFilter.LayerItem> itemList;
    public List<ClsItemFilter.LayerItem> checkedItemList;
    public static List<String> selectedItems;
    private boolean isSelected[];


    List<ClsItemFilter.LayerItem> filterList = new ArrayList();

    @SuppressLint("ValidFragment")
    private PagerFragmet(List<ClsItemFilter.LayerItem> _itemList) {
        // Required empty public constructor
        this.itemList = _itemList;//9
        this.checkedItemList = _itemList;//4
    }


    EditText edit_search;
    ImageView img_clear;


    private static final int[] colors = new int[]{
            Color.CYAN,
            Color.WHITE,
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.GRAY,
            Color.YELLOW
    };

    public static androidx.fragment.app.Fragment newInstance(String title, int page_id, List<ClsItemFilter.LayerItem> _itemList) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_PAGE_ID, page_id);
        // itemList = _itemList;
        PagerFragmet fragment = new PagerFragmet(_itemList);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.title = bundle.getString(KEY_TITLE);
            this.page_id = bundle.getInt(KEY_PAGE_ID);
        }

        SelectedFilter.clear();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pager_fragmet, container, false);
        // Inflate the layout for this fragment

        recyclerView = view.findViewById(R.id.rv);
        nodata_layout = view.findViewById(R.id.nodata_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        isSelected = new boolean[checkedItemList.size()];
//        isSelected = new boolean[itemList.size()];

        if (itemList.size() == 0) {
            nodata_layout.setVisibility(View.VISIBLE);
        } else {
            nodata_layout.setVisibility(View.GONE);
        }

        mCv = new FilterAdpater(getActivity(), itemList, title);

        mCv.SetOnClickListener((myviewholder, currentObj, position, _mainList) -> {
            Log.e("Filters", "positionMain" + position);
            int mainListPosition = 0;

            for (ClsItemFilter.LayerItem obj : checkedItemList) {//silver
                if (obj.getItemName().equalsIgnoreCase(currentObj.getItemName())) {//silver
                    mainListPosition =  itemList.indexOf(obj);
                    break;
                }
            }
            Log.e("Filters", "positionNEW" + mainListPosition);


            selectedItems = new ArrayList<>();
            // set the check text view
            boolean flag = myviewholder.txt_label.isChecked();
            myviewholder.txt_label.setChecked(!flag);
            isSelected[mainListPosition] = !isSelected[mainListPosition];


            if (myviewholder.txt_label.isChecked()) {
                //   FilterDialogActivity.getSelectedList.add(itemList.get(position).getItemName());
                SelectedFilter.add(new Filters(title, itemList.get(position).getItemName()));
                myviewholder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                myviewholder.ic_Check.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                //     FilterDialogActivity.getSelectedList.remove(itemList.get(position).getItemName());

                myviewholder.ic_Check.setColorFilter(ContextCompat.getColor(getContext(), R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                Iterator<Filters> iter = SelectedFilter.iterator();
                while (iter.hasNext()) {
                    Filters current = iter.next();
                    if (current.getLAYERNAME().contains(title) &&
                            current.getLAYERVALUE().contains(itemList.get(position).getItemName())) {
                        Log.e("Filters", "Filters call");
                        //Use iterator to remove this User object.
                        iter.remove();
                    }
                }

                myviewholder.linear_layout.setBackgroundResource(0);
            }

            for (int i = 0; i < isSelected.length; i++) {
                ClsItemFilter.LayerItem Obj = itemList.get(i);

                if (isSelected[i]) {
                    selectedItems.add(itemList.get(i).getItemName());
                    Obj.setSelected(true);
                } else
                    Obj.setSelected(false);

                itemList.set(i, Obj);
            }


        });

        recyclerView.setAdapter(mCv);


        edit_search = view.findViewById(R.id.edit_search);
        img_clear = view.findViewById(R.id.img_clear);
        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("--Text--", "step1");
                edit_search.setText("");


            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.e("--Text--", "--AFTER--" + s.toString());

                filter(s.toString().trim().toUpperCase());
            }
        });

        return view;
    }


    void filter(String text) {
        Log.e("--Text--", "step2");
        filterList = new ArrayList<>();
        if (text != null && text != "") {
            for (ClsItemFilter.LayerItem obj : itemList) {
                Log.e("--Text--", "--getItemName--" + obj.getItemName());
                if (obj.getItemName().toLowerCase().contains(text.toLowerCase())) {

                    filterList.add(obj);
                }
            }
        }
        //update recyclerview
        if (filterList.size() != 0) {
            mCv.updateList(filterList);
        } else {
            mCv.updateList(itemList);
        }
    }



}
