package com.example.architectureexample.demoUi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.architectureexample.R;

public class Demo_Ui_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo__ui_);
        getSupportActionBar().hide();
    }


    public void animateScreen(View view) {
        Intent intent=new Intent(Demo_Ui_Activity.this,Animate_Activity.class);
        startActivity(intent);
    }
}