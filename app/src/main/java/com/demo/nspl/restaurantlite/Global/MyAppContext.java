package com.demo.nspl.restaurantlite.Global;

import android.app.Application;
import android.util.Log;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCitys;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsState;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceCitys;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceStates;
import com.demo.nspl.restaurantlite.classes.ClsCityMaster;
import com.demo.nspl.restaurantlite.classes.ClsStateMaster;
import com.google.gson.Gson;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("Check", "MyAppContext onCreate");

        new Thread(() -> {

            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
            io.requery.android.database.sqlite.SQLiteDatabase
                    db = databaseHelper.openDatabase();

            // Create State_MASTER and City_MASTER if not exists.
            CreateTables createTables = new CreateTables();
            createTables.createStates(db);
            createTables.createCity(db);
            databaseHelper.closeDatabase();

            // For Testing.
//            ClsCityMaster.DeleteAll(getApplicationContext());
//            ClsStateMaster.DeleteAll(getApplicationContext());

            // Check if there are records in State_MASTER and City_MASTER.
            int CityCounts = ClsCityMaster.getCityCounts(getApplicationContext());
            int StateCounts = ClsStateMaster.getStatesCounts(getApplicationContext());


            // if there are not records then call api and insert into tables.
            if (CityCounts <= 0 && StateCounts <= 0) {
                InterfaceCitys interfaceStates = ApiClient
                        .getRetrofitInstance()
                        .create(InterfaceCitys.class);

                Call<List<ClsCitys>> call = interfaceStates.getCitys();
                Log.e("Check", "************************  before call : " + call.request().url());

                call.enqueue(new Callback<List<ClsCitys>>() {
                    @Override
                    public void onResponse(Call<List<ClsCitys>> call, Response<List<ClsCitys>> response) {
                        if (response.body() != null) {
                            final List<ClsCitys>[] listCity = new List[]{response.body()};
                            Log.e("Check", "ClsCitys: " + listCity[0].size());


                            // if there are not records then call api and insert into tables.
                            InterfaceStates interfaceStates = ApiClient
                                    .getRetrofitInstance()
                                    .create(InterfaceStates.class);

                            Call<List<ClsState>> call1 = interfaceStates.getStates();
                            Log.e("Check", "************************  before call : " + call.request().url());

                            call1.enqueue(new Callback<List<ClsState>>() {
                                @Override
                                public void onResponse(Call<List<ClsState>> call, Response<List<ClsState>> response) {
                                    if (response.body() != null) {
                                        final List<ClsState>[] listState = new List[]{response.body()};
                                        Log.e("Check", "ClsState: " + listState[0].size());

                                        new Thread(() -> {

                                            // filter only indian states.
                                            listState[0] = StreamSupport.stream(listState[0])
                                                    .filter(str -> str.getCountryId().contains("101"))
                                                    .collect(Collectors.toList());

                                            Gson gson2 = new Gson();
                                            String jsonInString2 = gson2.toJson(listState);
                                            Log.d("Check", "listState- " + jsonInString2);

                                            // filter only indian states ids.
                                            List<String> states_id_list = StreamSupport.stream(listState[0])
                                                    .filter(str -> str.getCountryId().contains("101"))
                                                    .map(ClsState::getId)
                                                    .collect(Collectors.toList());

                                            Gson gson = new Gson();
                                            String jsonInString = gson.toJson(states_id_list);
                                            Log.d("Check", "states_id_list- " + jsonInString);

                                            // Insert only indian State's.
                                            ClsStateMaster.InsertList(listState[0], getApplicationContext());

                                            // filter only indian City's.
                                            listCity[0] = StreamSupport.stream(listCity[0])
                                                    .filter(str -> states_id_list.contains(str.getStateId()))
                                                    .collect(Collectors.toList());

                                            Gson gson1 = new Gson();
                                            String jsonInString1 = gson1.toJson(listCity);
                                            Log.d("Check", "listCity- " + jsonInString1);



                                            // Insert only indian City's.
                                            ClsCityMaster.InsertList(listCity[0], getApplicationContext());

                                        }).start();


                                    }

                                }

                                @Override
                                public void onFailure(Call<List<ClsState>> call, Throwable t) {
                                    Log.e("Check", "getMessage state: " + t.getMessage());
                                }
                            });
                        }


                    }

                    @Override
                    public void onFailure(Call<List<ClsCitys>> call, Throwable t) {
                        Log.e("Check", "getMessage city: " + t.getMessage());
                    }
                });
            }


//

        }).start();
    }


}
