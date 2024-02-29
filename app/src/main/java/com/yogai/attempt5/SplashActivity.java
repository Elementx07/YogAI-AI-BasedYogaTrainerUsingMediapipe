package com.yogai.attempt5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private final int[] imageArray = {
            R.drawable.lotus,
            R.drawable.sitting,
            R.drawable.cobra,
            R.drawable.camel
    };
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView splashImage = findViewById(R.id.splashImage);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                splashImage.setImageResource(imageArray[currentIndex]);
                currentIndex = (currentIndex + 1) % imageArray.length;
                handler.postDelayed(this, 1000); // Change the delay as per your requirement
            }
        };

        handler.post(runnable);

        // Navigate to the main activity after a certain delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 10000); // Change the delay as per your requirement
    }
}
