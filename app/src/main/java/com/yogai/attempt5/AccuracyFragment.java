package com.yogai.attempt5;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class AccuracyFragment extends Fragment {

    double accuracy;
    ProgressBar accuracyMeter;

    View view;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accuracy, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accuracyMeter = view.findViewById(R.id.accuracyMeter);
    }

    public double calculateAngleAccuracy(double desiredAngle, double angle) {
        // Ensure angles start from zero
        angle = Math.max(0, angle);
        // Calculate angle difference
        double angleDifference = Math.abs(angle - desiredAngle);
        // Calculate accuracy percentage
        accuracy = 100 - (angleDifference / desiredAngle * 100);
        // Ensure accuracy value is within range [0, 100]
        accuracy = Math.min(Math.max(accuracy, 0), 100);
        return accuracy;
    }

    public void setAccuracyMeter(double accuracy) {
        accuracyMeter.setProgress((int) accuracy);
    }
}