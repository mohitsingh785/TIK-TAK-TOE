package com.mohit.tictaktao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mohit.tictaktao.login.Login;
import com.mohit.tictaktao.onevsone.OnevsOne;
import com.mohit.tictaktao.singup.signup;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        Log.e("splash screen", ""+sp.getBoolean("success", false));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (!sp.getBoolean("onboard", false)) {
                    Log.e("OnBoardingPage", "splash screen is running");
                    startActivity(new Intent(splash_screen.this, BottomActivity.class));


                }
                else if (sp.getBoolean("success", false)) {
                    Log.e("OnBoardingPage", "splash screen is running");
                    startActivity(new Intent(splash_screen.this, MainActivity.class));


                } else {
                    startActivity(new Intent(splash_screen.this, Login.class));

                }
            }
        }, 3500); // Delay


    }
}