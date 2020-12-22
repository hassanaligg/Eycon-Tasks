package com.example.architectureexample.memorablePlaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.architectureexample.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemorablePlacesActivity extends AppCompatActivity {
    ListView listView;
    static List<String> places =new ArrayList<>();
    static List<LatLng> location= new ArrayList<>();
    static ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorable_places);
        listView = findViewById(R.id.listView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);
        ArrayList<String> latitude = new ArrayList<>();
        ArrayList<String> longitude = new ArrayList<>();
        places.clear();
        latitude.clear();
        longitude.clear();
        location.clear();
        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));
            latitude = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitude", ObjectSerializer.serialize(new ArrayList<String>())));
            longitude = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitude", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (places.size() > 0 && latitude.size() > 0 && longitude.size() > 0) {
            if (places.size() == latitude.size() && latitude.size() == longitude.size()) {
                for (int i = 0; i < latitude.size(); i++) {
                    location.add(new LatLng(Double.parseDouble(latitude.get(i)), Double.parseDouble(longitude.get(i))));
                }
            }
        } else {
            places.add("Add new Place");
            location.add(new LatLng(0.0, 0.0));
        }

        adapter = new ArrayAdapter(MemorablePlacesActivity.this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}