package com.yogai.attempt5;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.yogai.attempt5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CameraFragment()) // This line creates a new instance using Kotlin's constructor
                    .commit();
        }
    }
}
