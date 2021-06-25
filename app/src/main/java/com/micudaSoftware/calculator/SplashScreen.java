package com.micudaSoftware.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends Activity {

    boolean shouldStart = true;
    Runnable runnable = () -> {
        if (shouldStart) {
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            SplashScreen.this.startActivity(i);
            SplashScreen.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onPause() {
        shouldStart = false;
        super.onPause();
        this.finish();
    }
}
