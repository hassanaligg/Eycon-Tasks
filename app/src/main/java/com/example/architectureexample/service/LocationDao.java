package com.example.architectureexample.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.architectureexample.mvvm.Note;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert
    void insertlocation(LocationModel locationModel);
    @Query("Select * From LocationModel")
    List<LocationModel> getAlldetails();
}
