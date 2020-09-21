package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.CountryStateCityAdapter;
import com.demo.nspl.restaurantlite.Country.New.City.ClsCity;
import com.demo.nspl.restaurantlite.Country.New.Country.ClsCountry;
import com.demo.nspl.restaurantlite.Country.New.State.ClsState;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.CountryViewModels;
import com.demo.nspl.restaurantlite.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.countryStateCityCode;

public class CountryStateCityListActivity extends AppCompatActivity implements
        CountryStateCityAdapter.onCountryClickListener,
        CountryStateCityAdapter.onStateClickListener,
        CountryStateCityAdapter.onCityClickListener {


    CountryViewModels countryViewModels;
    private List<ClsCountry> countries = new ArrayList<>();
    private List<ClsState> states = new ArrayList<>();
    private List<ClsCity> city = new ArrayList<>();
    private CountryStateCityAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog pd;
    public static TextView tvNotFound;
    private SharedPreferences sharedPreferences;


    Toolbar toolbar;
    TextView txt_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_state_city_list);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "CountryStateCityListActivity"));
        }

        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        tvNotFound = findViewById(R.id.tvNotFound);
        txt_title = findViewById(R.id.txt_title);
        tvNotFound.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences("all_location", MODE_PRIVATE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryViewModels = ViewModelProviders.of(this)
                .get(CountryViewModels.class);

        pd = new ProgressDialog(this);


        if (ClsGlobal.CheckInternetConnection(this)) {
            try {
                if (countryStateCityCode == 1) {
//                    getSupportActionBar().setTitle("Select Country");
                    pd.setMessage("Loading Country...");
                    txt_title.setText("Select Country");

                    getCountry();
                } else if (countryStateCityCode == 2) {
//                    getSupportActionBar().setTitle("Select State");

                    pd.setMessage("Loading State...");
                    txt_title.setText("Select State");

                    // Intent intent = getIntent();
                    getState(sharedPreferences.getInt("countryId", 0));
                } else if (countryStateCityCode == 3) {
//                    getSupportActionBar().setTitle("Select City");

                    pd.setMessage("Loading City...");
                    txt_title.setText("Select City");
                    // Intent intent = getIntent();
                    getCity(sharedPreferences.getInt("stateId", 0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCountry() {

        Log.e("--countries--", "Call");

        pd.show();
        countryViewModels.getCountryResponse().observe(this, clsCountryResponse -> {
            if (clsCountryResponse != null) {
                countries = clsCountryResponse.getData();
                if (countries.size() != 0) {
                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(countries);
                    Log.e("--countries--", "countries: " + jsonInString);
                    adapter = new CountryStateCityAdapter((ArrayList<ClsCountry>)
                            countries, this, this);
                    recyclerView.setAdapter(adapter);
                    pd.dismiss();
                } else {
                    pd.dismiss();
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText("No Country Find");
                }
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    public void getState(int CountryID) {
        pd.show();
        countryViewModels.getStateResponse(CountryID).observe(this, clsStateResponce -> {
            if (clsStateResponce != null) {
                if (clsStateResponce.getSuccess().equals("1")) {
                    states = clsStateResponce.getData();
                    if (states.size() != 0) {
                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(states);
                        Log.e("states", "states---" + jsonInString);
                        adapter = new CountryStateCityAdapter((ArrayList<ClsState>)
                                states, this);
                        recyclerView.setAdapter(adapter);
                        pd.dismiss();
                    } else {
                        pd.dismiss();

                        tvNotFound.setVisibility(View.VISIBLE);
                        tvNotFound.setText("No State Find");
                        Toast.makeText(this, clsStateResponce.getSuccess() + " " + CountryID, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, clsStateResponce.getSuccess() + " " + CountryID, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Something went wrong " + CountryID, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getCity(int StateID) {
        pd.show();
        countryViewModels.getCityResponse(StateID).observe(this, clsCityResponce -> {
            if (clsCityResponce != null) {
                // if (clsCityResponce.getSuccess().equals("1")) {
                city = clsCityResponce.getData();
                if (city.size() != 0) {
                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(city);
                    Log.e("city", "city---" + jsonInString);
                    adapter = new CountryStateCityAdapter((ArrayList<ClsCity>) city, this);
                    recyclerView.setAdapter(adapter);
                    pd.dismiss();
                } else {
                    pd.dismiss();

                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText("No City Find");
                    Toast.makeText(this, clsCityResponce.getSuccess() + " " + StateID, Toast.LENGTH_SHORT).show();

                }
               /* } else {
                    pd.dismiss();
                    Toast.makeText(this, clsCityResponce.getSuccess() + " " + StateID, Toast.LENGTH_SHORT).show();
                }*/
            } else {
                Toast.makeText(this, "Something went wrong" + " " + StateID, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {

            searchView.setQuery("", false);
            searchView.clearFocus();
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ClsGlobal.textAvailable) {
            if (countryStateCityCode == 1) {
                countryStateCityCode = 0;

            } else if (countryStateCityCode == 2) {
                countryStateCityCode = 1;

            } else if (countryStateCityCode == 3) {
                countryStateCityCode = 2;
            }
            ClsGlobal.textAvailable = false;
        }
    }

    public static void checkArrayListCountry(List<ClsCountry> arrayList) {
        try {
            if (arrayList.size() == 0) {
                tvNotFound.setVisibility(View.VISIBLE);
                tvNotFound.setText("Not Found");
            } else {
                tvNotFound.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkArrayListState(List<ClsState> arrayList) {
        try {
            if (arrayList.size() == 0) {
                tvNotFound.setVisibility(View.VISIBLE);
                tvNotFound.setText("Not Found");
            } else {
                tvNotFound.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void checkArrayListCity(List<ClsCity> arrayList) {
        try {
            if (arrayList.size() == 0) {
                tvNotFound.setVisibility(View.VISIBLE);
                tvNotFound.setText("Not Found");
            } else {
                tvNotFound.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCountryClick(ClsCountry clsCountry) {

        countryStateCityCode = 1;
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("countryId", clsCountry.getCountryID());
        intent.putExtra("countryName", clsCountry.getCountryName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onStateClick(ClsState clsState) {

        countryStateCityCode = 2;
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("stateId", clsState.getStateID());
        intent.putExtra("stateName", clsState.getStateName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCityClick(ClsCity clsCity) {
        countryStateCityCode = 3;
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("cityId", clsCity.getCityID());
        intent.putExtra("cityName", clsCity.getCityName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
