package com.yogai.attempt5;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class ProgressFragment extends Fragment {
    Button time;
    Context context;
    MainViewModel viewmodel;
    TextView timeTitle;
    MainActivity mainActivity;
    PoseClassifier pc;
    ProgressFragment(Context context, MainActivity mainActivity){
        this.context=context;
        this.mainActivity = mainActivity;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ListView poses= view.findViewById(R.id.lv2);
        viewmodel= ((MainActivity) requireActivity()).getMainViewModel();
        ArrayList<Pose> poseArrayList = (ArrayList<Pose>) viewmodel.getPosesPerformed();
        PoseAdapter pa= new PoseAdapter(context,poseArrayList,2);
        poses.setAdapter(pa);

        time=view.findViewById(R.id.idTimePickBtn);
        timeTitle=view.findViewById(R.id.idTimeSpent);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        // Fetch totalPoseDuration from the database
        long totalPoseDuration = databaseHelper.getTotalPoseDuration();
        Log.d("TotalPoseDuration", String.valueOf(totalPoseDuration));

        pc=PoseClassifier.getInstance(context);
        timeTitle.setText("Time Spent:"+pc.getTime());
        mainActivity.showNavigationBar();

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display time picker dialog and setup a notification/alerts for the user
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay + ":" + minute);
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        startAlarm(c);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
        return view;
        }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}