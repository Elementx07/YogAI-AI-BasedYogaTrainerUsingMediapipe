package com.yogai.attempt5;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.yogai.attempt5.databinding.ActivityMainBinding;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class HomeFragment extends Fragment {
    MainActivity mainActivity;
    PieChart pc;
    private View mPressedView;
    Handler mHandler=new Handler();
    TextView t1,t2,t3,t4,msg;
    ImageView img1,img2,img3;
    Boolean mLongPressed=false;
    HomeFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pc = view.findViewById(R.id.piechart); // Find the PieChart within the inflated layout
        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        t3=view.findViewById(R.id.t3);
        t4=view.findViewById(R.id.t4);
        msg=view.findViewById(R.id.greet);
        img1=view.findViewById(R.id.img1);
        img2=view.findViewById(R.id.img2);
        img3=view.findViewById(R.id.img3);
        img1.setOnTouchListener((view1,motionEvent)->{
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mPressedView=view1;
                    mHandler.postDelayed(longPressRunnable,500);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mHandler.removeCallbacks(longPressRunnable);
                    if(mLongPressed){
                        Animation rstore= AnimationUtils.loadAnimation(getContext(),R.anim.scale_restore);
                        view1.startAnimation(rstore);
                        mLongPressed=false;
                    }
                    break;
            }
            return true;
        });
        //Animate msg text in type writer effect
        mainActivity.typeWriter(msg);
        t1.setText("Tree");
        t2.setText("Cobra");
        t3.setText("Camel");
        t4.setText("Triangle");

        pc.addPieSlice(new PieModel("Tree", 15, Color.parseColor("#FFA726")));
        pc.addPieSlice(new PieModel("Cobra", 25, Color.parseColor("#66BB6A")));
        pc.addPieSlice(new PieModel("Camel", 20, Color.parseColor("#EF5350")));
        pc.addPieSlice(new PieModel("Triangle", 9, Color.parseColor("#29B6F6")));
        pc.startAnimation();
        //show navigation bar from hidden
        mainActivity.showNavigationBar();
        return view;

    }

    @SuppressLint("ClickableViewAccessibility")
    public void anime(){

    }

    private Runnable longPressRunnable=()->{
        if(!mLongPressed){
            mLongPressed=true;
            AnimationSet aset= new AnimationSet(true);
            Animation scale= new ScaleAnimation(1f,2f,1f,2f,Animation.RELATIVE_TO_SELF,0.5f);
            scale.setDuration(2000);
            aset.addAnimation(scale);
            mPressedView.startAnimation(aset);
        }
    };
}