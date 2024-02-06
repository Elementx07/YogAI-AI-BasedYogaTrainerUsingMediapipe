package com.yogai.attempt5;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;

public class MainActivity extends AppCompatActivity   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        PoseSelectorFragment poseSelectorFragment = new PoseSelectorFragment(this,this);
        CameraFragment cf = new CameraFragment();


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        // Home tab selected
                        selectedFragment = cf;
                        break;
                    case 1:
                        // Classes tab selected
                        selectedFragment = poseSelectorFragment;
                        break;
                    // Add more cases if there are more tabs
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });

    }


    public MainViewModel getMainViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    public void replaceFragmentWithCamera() {
        Log.d(null, "replaceFragmentWithCamera: ");
        CameraFragment cf = new CameraFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,cf );
        transaction.commit();
    }


}
