package com.yogai.attempt5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PoseInfoFragment extends Fragment {
    MainActivity mainActivity;
    Context context;
    Button next;
    MainViewModel mainViewModel;
    PoseInfoFragment(Context context, MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.context=context;
    }
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mainViewModel = ((MainActivity) requireActivity()).getMainViewModel();
        View view = inflater.inflate(R.layout.fragment_pose_info, container, false);
        ImageView imageView = view.findViewById(R.id.back_button);
        ImageView poseImage = view.findViewById(R.id.pose_image);
        String pi=mainViewModel.getSelectedPoseName();


        imageView.setImageResource(R.drawable.back);
        //set the pose image based on the selected pose name
        if(pi.equals("Lotus")){
            poseImage.setImageResource(R.drawable.lotus);
        }
        else if(pi.equals("Cobra")){
            poseImage.setImageResource(R.drawable.cobra);
        }
        else if(pi.equals("Camel")){
            poseImage.setImageResource(R.drawable.camel);
        }
        else if(pi.equals("Extended Triangle")){
            poseImage.setImageResource(R.drawable.triangle);
        } else if (pi.equals("Tree")){
            poseImage.setImageResource(R.drawable.tree);
        }


        // Apply a ColorFilter to change the color to white
        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(new CameraFragment());
            }
        });
        return view;

    }

}