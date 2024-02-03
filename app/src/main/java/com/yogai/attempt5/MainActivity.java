package com.yogai.attempt5;

import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // Create an instance of PoseSelectorFragment
        PoseSelectorFragment poseSelectorFragment = new PoseSelectorFragment(this,this);
        // Replace the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, poseSelectorFragment);
        transaction.commit();
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
