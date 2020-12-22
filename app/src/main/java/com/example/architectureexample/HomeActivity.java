package com.example.architectureexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.architectureexample.demoUi.Demo_Ui_Activity;
import com.example.architectureexample.getlocation.CheckLocationActivity;
import com.example.architectureexample.mvvm.MainActivity;
import com.example.architectureexample.service.ServiceActivity;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeActivityAdapter homeActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        String [] work={"C R U D","Location Service","Get Location","UI Demo","Memorable Places","Firebase CRUD"};
        String [] disc ={"Create , Delete ,Update ,Read Using Room DataBase",
                "Getting location Via Services",
                "Shows Your Accurate Location With Details","Responsive UI Demo of Google Play Movies","Save your Memorable places","CRUD using RealTime FireBase"};
        homeActivityAdapter=new HomeActivityAdapter(work,disc);
        recyclerView.setAdapter(homeActivityAdapter);
    }

//    public void crudActivity(View view) {
//        Intent intent=new Intent(HomeActivity.this, MainActivity.class);
//        startActivity(intent);
//    }
//
//    public void foregroundActivity(View view) {
//        Intent intent=new Intent(HomeActivity.this, ServiceActivity.class);
//        startActivity(intent);
//    }
//
//    public void checkLocation(View view) {
//        Intent intent=new Intent(HomeActivity.this, CheckLocationActivity.class);
//        startActivity(intent);
//    }
//
//    public void Demo_ui(View view) {
//        Intent intent=new Intent(HomeActivity.this, Demo_Ui_Activity.class);
//        startActivity(intent);
//    }
}