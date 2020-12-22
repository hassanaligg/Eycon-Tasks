package com.example.architectureexample.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.example.architectureexample.R;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.BuildConfig;

public class ServiceActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void startService(View view) {

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ServiceActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            startLocationService();
        }

    }
    private void startLocationService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
        else{
            startService(serviceIntent);
        }
    }

    public void stopService(View view) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else { // Case 2. Permission was refused
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    dialogbox();
                } else { // Case 3. Permission was Permanentlt denied
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Permission denied Permanently Go to App Settings To Enable the Permission",
                                    Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent)).setAction("Settings", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

                                }
                            });
                    snackbar.show();
                    //Toast.makeText(this, "You have denied Permission Permanently", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void dialogbox() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("Permission  Must Needed for this Functionality")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(ServiceActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION_PERMISSION);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    public void showLocation(View view) {
        Intent intent=new Intent(ServiceActivity.this,ShowLocationActivity.class);
        startActivity(intent);
    }
}