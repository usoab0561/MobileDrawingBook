package com.example.myapplication.ui.BookList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BookViewModel {
    private MutableLiveData<String> mText;

    public BookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
