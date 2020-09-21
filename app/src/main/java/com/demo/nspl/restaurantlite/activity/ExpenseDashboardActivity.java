package com.demo.nspl.restaurantlite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.Adapter.TopFiveExpAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.TabsActivity.Tab_1_Activity;
import com.demo.nspl.restaurantlite.TabsActivity.Tab_2_Activity;
import com.demo.nspl.restaurantlite.TabsActivity.Tab_4_Activity;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDashboardActivity extends AppCompatActivity implements AppCompatCallback {

    TextView txt_nodata, txt_all_exp;
    List<ClsExpenseMasterNew> lstClsExpenseMasterNew;
    RecyclerView rv_topfive_exp;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int dotscount;
    private ImageView[] dots;
    LinearLayout sliderDotspanel;
    Toolbar toolbar;

    public ExpenseDashboardActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_dashboard);
        Log.e("onCreate", "onCreate call");

        ClsGlobal.isFristFragment = true;

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Expenses");
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ExpenseDashboardActivity"));
        }
        main();

        return;

    }


    private void main() {
        txt_nodata = findViewById(R.id.txt_nodata);
        txt_all_exp = findViewById(R.id.txt_all_exp);

        txt_all_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AllExpensesActivity.class);
                startActivity(intent);
            }
        });


        rv_topfive_exp = findViewById(R.id.rv_topfive_exp);
        rv_topfive_exp.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ViewTopFiveExp();

        sliderDotspanel = findViewById(R.id.SliderDots);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_not_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            Intent intent = new Intent(getApplicationContext(), AddExpenseActivity.class);
            intent.putExtra("_ID", 0);
            startActivity(intent);

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        ViewTopFiveExp();
    }



    private void ViewTopFiveExp() {
        Log.e("ViewTopFiveExp", "ViewTopFiveExp call");
        lstClsExpenseMasterNew = new ArrayList<>();
        lstClsExpenseMasterNew = new ClsExpenseMasterNew(getApplicationContext()).getListTopExp("");
        Log.e("lstClsExpenseMasterNew", String.valueOf(lstClsExpenseMasterNew.size()));
//        Gson gson = new Gson();
//        String jsonInString = gson.toJson(lstClsExpenseMasterNew);
//        Log.e("Gson", "ExpenseDashboardActivity-- " + jsonInString);


        if (lstClsExpenseMasterNew != null && lstClsExpenseMasterNew.size() != 0) {
            txt_nodata.setVisibility(View.GONE);
            TopFiveExpAdapter topFiveExpAdapter =
                    new TopFiveExpAdapter(ExpenseDashboardActivity.this, lstClsExpenseMasterNew);
            rv_topfive_exp.setAdapter(topFiveExpAdapter);
            topFiveExpAdapter.notifyDataSetChanged();
        } else {
            txt_nodata.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab_1_Activity(), "Tab 1");
        adapter.addFragment(new Tab_2_Activity(), "Tab 2");
//        adapter.addFragment(new Tab_3_Activity(), "Tab 3");
        adapter.addFragment(new Tab_4_Activity(), "Tab 4");

        viewPager.setAdapter(adapter);

        dotscount = adapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_not_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(4, 0, 4, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
