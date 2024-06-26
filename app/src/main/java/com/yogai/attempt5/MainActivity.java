package com.yogai.attempt5;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yogai.attempt5.databinding.ActivityMainBinding;

import org.eazegraph.lib.charts.PieChart;

public class MainActivity extends AppCompatActivity   {
    BottomNavigationView nv;
    ActivityMainBinding binding;
    PieChart pc;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment(MainActivity.this));
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        createNotificationChannel();
        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    replaceFragment(new HomeFragment(MainActivity.this));
                    return true;
                } else if (item.getItemId() == R.id.poses) {
                    replaceFragment(new PoseSelectorFragment(MainActivity.this, MainActivity.this));
                    return true;
                } else if (item.getItemId()==R.id.progress) {
                    replaceFragment(new ProgressFragment(MainActivity.this,MainActivity.this));
                    return true;
                }
                return false;
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyCode", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public MainViewModel getMainViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ((FragmentTransaction) transaction).replace(R.id.fragment_container, fragment);
        transaction.commit();
    }



    public void hideNavigationBar() {
        nv = findViewById(R.id.bottomNavigationView);
        nv.animate().translationY(nv.getHeight()).setDuration(300);
    }

    public void showNavigationBar() {
        nv = findViewById(R.id.bottomNavigationView);
        nv.animate().translationY(0).setDuration(300);
    }

    public void typeWriter(TextView msg) {
        String s = "Namaste\nJay";
        for (int i = 0; i < s.length(); i++) {
            final int finalI = i;
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            msg.setText(s.substring(0, finalI + 1) + "_");
                        }
                    },
                    200 * i
            );
        }
        msg.setText(s);
    }
}
