package com.demo.nspl.restaurantlite.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    MutableLiveData<String> current_date_list = new MutableLiveData<String>();


    public void init(){
        Log.i("init","init call");
        current_date_list.setValue("No Date Selected");
    }


    public void setDate(String date) {
        Log.e("setDate",date);
        current_date_list.postValue(date);
    }

    public LiveData<String> getDate() {
        Log.e("getDate" ,"getDate call");
        return current_date_list;
    }


}
