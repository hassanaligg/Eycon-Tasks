package com.example.architectureexample.memorablePlaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.architectureexample.R;
import com.example.architectureexample.mvvm.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    LocationManager locationManager;
    LocationListener locationListener;
    public static final int LOCATION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void centerMapOnLocation(Location location, String title) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.clear();
        if (title != "Your Location") {
            map.addMarker(new MarkerOptions().position(userLocation).title(title));
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Intent intent = getIntent();
        if (intent.getIntExtra("position", 0) == 0) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    centerMapOnLocation(location, "Your Location");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (Build.VERSION.SDK_INT < 23) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");
            }
        } else {
            Location placelocaction = new Location(LocationManager.GPS_PROVIDER);
            placelocaction.setLatitude(MemorablePlacesActivity.location.get(intent.getIntExtra("position", 0)).latitude);
            placelocaction.setLongitude(MemorablePlacesActivity.location.get(intent.getIntExtra("position", 0)).longitude);
            centerMapOnLocation(placelocaction, MemorablePlacesActivity.places.get(intent.getIntExtra("position", 0)));
        }
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String clickAddress = "";
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        if (addresses.get(0).getThoroughfare() != null) {
                            if (addresses.get(0).getSubThoroughfare() != null) {
                                clickAddress += addresses.get(0).getSubThoroughfare() + " ";
                                if (addresses.get(0).getPostalCode() != null) {
                                    clickAddress += addresses.get(0).getPostalCode() + " ";
                                }
                            }
                            clickAddress += addresses.get(0).getThoroughfare();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (clickAddress == "") {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:MM YYYY-MM-DD");
                    clickAddress = simpleDateFormat.format(new Date());
                }

                map.addMarker(new MarkerOptions().position(latLng).title(clickAddress));
                MemorablePlacesActivity.places.add(clickAddress);
                MemorablePlacesActivity.location.add(latLng);
                MemorablePlacesActivity.adapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = MapActivity.this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);
                try {
                    ArrayList<String> latitude = new ArrayList<>();
                    ArrayList<String> longitude = new ArrayList<>();
                    for (LatLng coordiantes : MemorablePlacesActivity.location) {
                        latitude.add(Double.toString(coordiantes.latitude));
                        longitude.add(Double.toString(coordiantes.longitude));
                    }
                    sharedPreferences.edit().putString("places", ObjectSerializer.serialize((Serializable) MemorablePlacesActivity.places)).apply();
                    sharedPreferences.edit().putString("latitude", ObjectSerializer.serialize(latitude)).apply();
                    sharedPreferences.edit().putString("longitude", ObjectSerializer.serialize(longitude)).apply();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                Toast.makeText(MapActivity.this, "LocationSaved", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");
            }
        }
    }
}