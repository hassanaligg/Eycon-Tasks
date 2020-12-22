package com.example.architectureexample.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureexample.R;
import com.example.architectureexample.service.ServiceActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class ForegroundService extends Service {

    private static final String TAG = "MyForegroundService";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private LocationCallback locationCallback;
    Calendar calendar;
    int hours,mins,seconds;
    LocationModel locationModel;
    LocationRepository locationRepository;

    @Override
    public void onCreate() {
        locationModel=new LocationModel();
        locationRepository=new LocationRepository();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String input = intent.getStringExtra("inputExtra");
            createNotificationChannel();
            Intent notificationIntent = new Intent(this, ServiceActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Foreground Service")
                    .setContentText(input)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (locationResult != null && locationResult.getLastLocation() != null) {
                            double latitude = locationResult.getLastLocation().getLatitude();
                            double longitude = locationResult.getLastLocation().getLongitude();
                            Log.d(TAG, latitude + "," + longitude);
                            calendar = Calendar.getInstance();
                            hours = calendar.get(Calendar.HOUR_OF_DAY);
                            mins = calendar.get(Calendar.MINUTE);
                            seconds = calendar.get(Calendar.SECOND);
                            Log.d(TAG, "Time "+hours+":"+mins+":"+seconds);
                            String time=+hours+":"+mins+":"+seconds;
                            locationModel.setLatitude(latitude);
                            locationModel.setLongitude(longitude);
                            locationModel.setTime(time);
                            locationRepository.insert(locationModel,getApplicationContext());
                        }
                    }
                };
                startListenLocation();
            }
        }).start();



        //do heavy work on a background thread
        //stopSelf();

        return START_STICKY;
    }

    private void startListenLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000); //interval to get location
        locationRequest.setFastestInterval(2000); //if location Available you will get it earlier
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }


    @Override
    public void onDestroy() {
        stopLocation();
        super.onDestroy();
    }

    private void stopLocation() {
        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                .removeLocationUpdates(locationCallback);
        Log.d(TAG, "Location Updates Stop: ");
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

