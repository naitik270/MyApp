package com.demo.nspl.restaurantlite.Global;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.demo.nspl.restaurantlite.Country.New.City.ClsCityResponce;
import com.demo.nspl.restaurantlite.Country.New.Country.ClsCountryResponse;
import com.demo.nspl.restaurantlite.Country.New.State.ClsStateResponce;

public class CountryViewModels extends AndroidViewModel {
    private Repository repository;

    public CountryViewModels(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
    }

    public LiveData<ClsCountryResponse> getCountryResponse() {

        return repository.getCountry();
    }

    public LiveData<ClsStateResponce> getStateResponse(int CountryID) {
        return repository.getState(CountryID);
    }

    public LiveData<ClsCityResponce> getCityResponse(int StateID) {
        return repository.getCity(StateID);
    }


}
