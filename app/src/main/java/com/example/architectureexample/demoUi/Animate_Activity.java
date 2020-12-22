package com.example.architectureexample.demoUi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.example.architectureexample.R;

public class Animate_Activity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSetOld=new ConstraintSet();
    ConstraintSet constraintSetNew=new ConstraintSet();
    boolean altLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_);
        getSupportActionBar().hide();
        constraintLayout=findViewById(R.id.layout);

        constraintSetOld.clone(constraintLayout);
        constraintSetNew.clone(this,R.layout.activity_animate_atl);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void swapView(View view) {
        Transition changeBounds = new ChangeBounds();
        changeBounds.setInterpolator(new OvershootInterpolator());

        TransitionManager.beginDelayedTransition(constraintLayout,changeBounds);
        if(!altLayout) {
            constraintSetNew.applyTo(constraintLayout);
            altLayout = true;
        }else{
            constraintSetOld.applyTo(constraintLayout);
            altLayout=false;
        }
    }

}