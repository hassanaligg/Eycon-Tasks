package com.example.architectureexample.service;

import android.content.Context;

import com.example.architectureexample.mvvm.NoteDatabase;

public class LocationRepository {

    public void insert(LocationModel locationModel, Context context) {
        NoteDatabase.getInstance(context).locationDao().insertlocation(locationModel);
    }
}
