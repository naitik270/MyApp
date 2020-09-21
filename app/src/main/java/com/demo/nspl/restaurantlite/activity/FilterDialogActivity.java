package com.demo.nspl.restaurantlite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.Adapter.PageAdapter;
import com.demo.nspl.restaurantlite.Adapter.TabAdapter;
import com.demo.nspl.restaurantlite.Interface.FragmentInterface;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsItemFilter;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.demo.nspl.restaurantlite.Navigation_Drawer.PagerFragmet.SelectedFilter;

public class FilterDialogActivity extends AppCompatActivity implements TabAdapter.OnItemClickListener, ViewPager.OnPageChangeListener {


    private Button button_Apply, button_Close;
    ViewPager viewPager;
    ListView tabs;
    private TabAdapter tabAdapter;
    private String getFilterList;
    List<ClsLayerItemMaster> list;
    private List<ClsItemFilter.Layer> filterList;
    private FragmentInterface fragmentInterfaceListener;
    private ImageView image_Btn;
    String _where = "";
    String saleMode = "";
    String entryMode = "";
    String editSource = "";

    public void setOnDataListener(FragmentInterface fragmentInterface) {
        this.fragmentInterfaceListener = fragmentInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_dialog);

        viewPager = findViewById(R.id.vertical_viewPager);
        tabs = findViewById(R.id.lv_tabs);
        button_Apply = findViewById(R.id.button_Apply);
        image_Btn = findViewById(R.id.image_Btn);
        button_Close = findViewById(R.id.button_Close);

        getFilterList = getIntent().getStringExtra("FilterList");

        Log.e("--saleMode--", "getFilterList:  " + getFilterList);


        saleMode = getIntent().getStringExtra("saleMode");
        entryMode = getIntent().getStringExtra("entryMode");
        editSource = getIntent().getStringExtra("editSource");

        Log.e("--saleMode--", "saleMode:  " + saleMode);
        Log.e("--saleMode--", "editSource:  " + editSource);


        Log.e("--Test--", "Filter_entryMode: " + entryMode);


        Type listType = new TypeToken<ArrayList<ClsItemFilter.Layer>>() {
        }.getType();

        filterList = new ArrayList<>();
        filterList = new Gson().fromJson(getFilterList, listType);


    /*    Gson gson = new Gson();
        String jsonInString = gson.toJson(filterList);
        Log.e("jsonInString:-- ", jsonInString);

*/
        button_Apply.setOnClickListener(v -> {
            Gson gson = new Gson();
            String jsonInString = gson.toJson(SelectedFilter);
            Log.e("jsonInString:-- ", jsonInString);

            Intent intent = new Intent(FilterDialogActivity.this, SalesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("SelectedList", jsonInString);
            intent.putExtra("saleMode", saleMode);
            intent.putExtra("entryMode", entryMode);
            intent.putExtra("editSource", editSource);
            startActivity(intent);

            finish();

        });

        button_Close.setOnClickListener(v -> {
            finish();
        });

        image_Btn.setOnClickListener(v -> {
            finish();
        });

        initView();

    }


//    private void initData() {
//        this.data = new String[]{
//                "Page 1",
//                "Page 2",
//                "Page 3",
//                "Page 4",
//                "Page 5",
//                "Page 6",
//                "Page 7"
//        };
//    }


    private void initView() {

        List<String> pageList = new ArrayList<>();

        for (ClsItemFilter.Layer objCurrunt : filterList) {
            pageList.add(objCurrunt.getLayerName());
        }

        viewPager.setOffscreenPageLimit(pageList.size());
        Gson gsonFilterList = new Gson();
        String jsonInFilterList = gsonFilterList.toJson(filterList);
        Log.e("pageList", "pageList:--- " + jsonInFilterList);

        PageAdapter viewPageAdapter = new PageAdapter(getSupportFragmentManager(), filterList);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(this);

        if (tabs != null) {
            // landscape mode
            this.tabAdapter = new TabAdapter(pageList, tabs, this);
            tabs.setAdapter(tabAdapter);
            tabs.setDivider(null);
        }


    }

    @Override
    public void selectItem(int position) {
        viewPager.setCurrentItem(position, true);
    }


    // Implements ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (tabAdapter != null) {
            tabAdapter.setCurrentSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
