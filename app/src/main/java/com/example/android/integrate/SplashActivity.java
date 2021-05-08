package com.example.android.integrate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        },2000);
    }
}
