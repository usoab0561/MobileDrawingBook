package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> UserData = new MutableLiveData<>();
    private final MutableLiveData<String> PassData = new MutableLiveData<>();
    public LiveData<String> getUserData(){
        return UserData;
    }
    public void setUserData(String str){
        UserData.setValue(str);
    }
    public LiveData<String> getPassData(){
        return PassData;
    }
    public void setPassData(String str){
        PassData.setValue(str);
    }
}
