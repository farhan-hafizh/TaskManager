package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.taskmanager.Utils.SessionManagement;

public class SplashActivity extends AppCompatActivity {
    SessionManagement session;
    Intent i=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();//get action bar then hide it
        //connect 2 activity with intent
        session = new SessionManagement(getApplicationContext());
        if(session.isLoggedIn()) {
            i = new Intent(SplashActivity.this, MainActivity.class);
            //run the intent with delay 1000 ms

        }else{
            i = new Intent(SplashActivity.this, QuickStartActivity.class);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}