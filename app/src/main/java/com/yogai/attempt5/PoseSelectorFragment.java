package com.yogai.attempt5;//package com.yogai.attempt5;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class PoseSelectorFragment extends Fragment {
    private MainViewModel mainViewModel;
    Context context;


    MainActivity mainActivity;
    public PoseSelectorFragment(Context context,MainActivity mainActivity){
        // Required empty public constructor
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pose_selector, container, false);

        // Initialize your grid view and set an adapter
        ListView poses = view.findViewById(R.id.gridView);
        // Initialize the MainViewModel
        mainViewModel = ((MainActivity) requireActivity()).getMainViewModel();
        ArrayList<Pose> poseArrayList= (ArrayList<Pose>) mainViewModel.getPoseList();
        // Create an instance of PoseAdapter and set it as the adapter of the GridView
        PoseAdapter adapter = new PoseAdapter(context,poseArrayList,1);
        poses.setAdapter(adapter);




        // Set item click listener
        poses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected pose information (you need to implement this)
                mainViewModel.setSelectedPoseName(poseArrayList.get(position).getPoseName());
                Log.d(null, "onItemClick: "+mainViewModel.getSelectedPoseName());
                Activity activity = getActivity();
                if (activity != null) {
                    mainActivity.replaceFragment(new PoseInfoFragment(context,mainActivity),0);
                }
                else {
                    Log.d(null, "activity null"+activity);
                }

            }
        });

        return view;
    }



}
