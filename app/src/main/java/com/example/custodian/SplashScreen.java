package com.example.custodian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences onboardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(5000);

                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    onboardingScreen = getSharedPreferences("OnBoardingScreen",MODE_PRIVATE);
                    boolean isfirstTime = onboardingScreen.getBoolean("FirstTime",true);
                    if (isfirstTime){
                        SharedPreferences.Editor editor = onboardingScreen.edit();
                        editor.putBoolean("FirstTime",false);
                        editor.commit();
                        startActivity(new Intent(SplashScreen.this, OnBoardingScreen.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(SplashScreen.this, LoginUser.class));
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}