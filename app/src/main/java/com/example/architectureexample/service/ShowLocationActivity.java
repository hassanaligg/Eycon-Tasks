package com.example.architectureexample.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.architectureexample.R;
import com.example.architectureexample.mvvm.NoteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowLocationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);
        recyclerView=findViewById(R.id.LocationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<LocationModel> locatiolist= NoteDatabase.getInstance(ShowLocationActivity.this).locationDao().getAlldetails();
        serviceAdapter=new ServiceAdapter(locatiolist);
        recyclerView.setAdapter(serviceAdapter);
    }
}