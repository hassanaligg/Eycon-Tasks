package com.example.architectureexample.getlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.architectureexample.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CheckLocationActivity extends AppCompatActivity {
    TextView latitude, longitue, countryName, Locatity, Address;
    Button bt_Location;
    FusedLocationProviderClient fusedLocationProviderClient;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_location);
        latitude = findViewById(R.id.textview1);
        longitue = findViewById(R.id.textview2);
        countryName = findViewById(R.id.textview3);
        Locatity = findViewById(R.id.textview4);
        Address = findViewById(R.id.textview5);
        bt_Location = findViewById(R.id.button);
        progressBar = findViewById(R.id.progrss_bar);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        bt_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (ActivityCompat.checkSelfPermission(CheckLocationActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getmlocation();
                } else if (ActivityCompat.checkSelfPermission(CheckLocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CheckLocationActivity.this
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }

            private void getmlocation() {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(CheckLocationActivity.this,
                                        Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1
                                );
                                progressBar.setVisibility(View.GONE);
                                latitude.setText("" + addresses.get(0).getLatitude());
                                longitue.setText("" + addresses.get(0).getLongitude());
                                countryName.setText("" + addresses.get(0).getCountryName());
                                Locatity.setText("" + addresses.get(0).getLocality());
                                Address.setText("" + addresses.get(0).getAddressLine(0));

                            } catch (IOException E) {
                                E.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
}