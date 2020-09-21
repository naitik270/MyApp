package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.nspl.restaurantlite.Country.New.City.ClsCityResponce;
import com.demo.nspl.restaurantlite.Country.New.Country.ClsCountryResponse;
import com.demo.nspl.restaurantlite.Country.New.State.ClsStateResponce;
import com.demo.nspl.restaurantlite.Interface.InterfaceCountryStateCity;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private Context context;
    private SharedPreferences sharedPreferences;

    public Repository(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
    }

    public LiveData<ClsCountryResponse> getCountry() {

        final MutableLiveData<ClsCountryResponse> countryResponseList = new MutableLiveData<>();
        InterfaceCountryStateCity interfaceWaiting =
                ApiClient.getRetrofitInstance().create(InterfaceCountryStateCity.class);
        Call<ClsCountryResponse> call = interfaceWaiting.GetCountryList();

        Log.e("--countries--", "URL: " + call.request().url());

        call.enqueue(new Callback<ClsCountryResponse>() {
            @Override
            public void onResponse(Call<ClsCountryResponse> call, Response<ClsCountryResponse> response) {

                Log.e("--countries--", "onRequestResponse: " + response.code());
                if (response.body() != null && response.code() == 200) {

                    countryResponseList.setValue(response.body());

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Log.d("--URL--", "onResponse----GetSpecialRequestlist---: " + jsonInString);
                }
            }

            @Override
            public void onFailure(Call<ClsCountryResponse> call, Throwable t) {
                Log.e("--countries--", "onFailure: GetSpecialRequestlist" + t.getMessage());
                try {
                    countryResponseList.setValue(null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("--countries--", "onFailure: " + t.toString());
            }
        });

        return countryResponseList;
    }

    public LiveData<ClsStateResponce> getState(int countryId) {

        final MutableLiveData<ClsStateResponce> stateResponseList = new MutableLiveData<>();
        InterfaceCountryStateCity contryInterface =
                ApiClient.getRetrofitInstance().create(InterfaceCountryStateCity.class);
        Call<ClsStateResponce> call = contryInterface.GetStateList(countryId);

        Log.e("--URL--", "************  before call : " + call.request().url());

        call.enqueue(new Callback<ClsStateResponce>() {
            @Override
            public void onResponse(Call<ClsStateResponce> call, Response<ClsStateResponce> response) {

                Log.e("--URL--", "onResponse: " + response.code());
                if (response.body() != null && response.code() == 200) {

                    stateResponseList.setValue(response.body());

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Log.d("--URL--", "onResponse----GetWatingPersonList---: " + jsonInString);
                }
            }

            @Override
            public void onFailure(Call<ClsStateResponce> call, Throwable t) {

                try {
                    stateResponseList.setValue(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("--URL--", "onFailure:GetWatingPersonList " + t.toString());
            }
        });

        return stateResponseList;
    }

    public LiveData<ClsCityResponce> getCity(int StateID) {

        final MutableLiveData<ClsCityResponce> cityResponseList = new MutableLiveData<>();
        InterfaceCountryStateCity contryInterface =
                ApiClient.getRetrofitInstance().create(InterfaceCountryStateCity.class);
        Call<ClsCityResponce> call = contryInterface.GetCityList(StateID);

        Log.e("--URL--", "************  before call : " + call.request().url());

        call.enqueue(new Callback<ClsCityResponce>() {
            @Override
            public void onResponse(Call<ClsCityResponce> call, Response<ClsCityResponce> response) {

                Log.e("--URL--", "onResponse: " + response.code());
                if (response.body() != null && response.code() == 200) {

                    cityResponseList.setValue(response.body());

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Log.d("--URL--", "onResponse----GetWatingPersonList---: " + jsonInString);
                }
            }

            @Override
            public void onFailure(Call<ClsCityResponce> call, Throwable t) {

                try {
                    cityResponseList.setValue(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("--URL--", "onFailure:GetWatingPersonList " + t.toString());
            }
        });

        return cityResponseList;
    }

}
