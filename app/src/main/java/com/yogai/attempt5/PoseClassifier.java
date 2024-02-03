package com.yogai.attempt5;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;
import static java.lang.Math.atan2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import java.util.Locale;

public class PoseClassifier {
    //in this class we classify different poses and the angles needed to achieve the poses
    //user selects the pose they want to perform from ui button , the id button is passed as an arguement to the method, that id is matched and angles for that particular pose are checked as specified.
    private PoseLandmarkerResult results;
    private float scaleFactor = 1f;
    private int imageWidth = 1;
    private int imageHeight = 1;

    private View v;

    private int selectedPose;

    MainViewModel viewModel = new MainViewModel();

    private TextToSpeech t1;

    private long handUpTime=0;

    @SuppressLint("RestrictedApi")
    public PoseClassifier(Context context) {
        t1 = new TextToSpeech(getApplicationContext(context), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    private Double calculateAngle(PointF pointA, PointF pointB, PointF pointC){
        Double angleA = atan2(pointB.y - pointA.y, pointB.x - pointA.x);
        Double angleB = atan2(pointC.y - pointB.y, pointC.x - pointB.x);
        Double angle = Math.toDegrees((angleB - angleA));
        if (angle < 0)
            angle=angle + 360;
        return angle;
    }

    private PointF getLandmarkPosition(PoseLandmarkerResult poseLandmarkerResult ,int landmark) {
        return new PointF(
                poseLandmarkerResult.landmarks().get(0).get(landmark).x() * imageWidth * scaleFactor,
                poseLandmarkerResult.landmarks().get(0).get(landmark).y() * imageHeight * scaleFactor
        );
    }

    public int classifyPose(PoseLandmarkerResult results,String selectedPose) {
        if(results != null && results.landmarks().size() > 0) {
            switch (selectedPose) {
                case "Tree Pose":
                    tree(results);
                case "downwarddog":
                    downwarddog();
                default:
                    return 0;
            }
        }
        return 0;
    }


    private int plank() {
        return 0;
    }

    private int cobra() {
        return 0;
    }

    private int downwarddog() {
        return 0;
    }

    private int tree(PoseLandmarkerResult results) {

        PointF wrist = getLandmarkPosition(results, 11);
        PointF elbow = getLandmarkPosition(results, 13);
        PointF shoulder = getLandmarkPosition(results, 15);
        PointF hip = getLandmarkPosition(results, 23);

        Double angle = calculateAngle(wrist, elbow, shoulder);
        Double hipAngle = calculateAngle(elbow, shoulder, hip);
        Float visibleHand = results.landmarks().get(0).get(13).visibility().get();


        if (visibleHand > 0.5) {
            Long currentTime= System.currentTimeMillis();

            boolean previousHandState = viewModel.isHandUp();
                if (hipAngle> 190) {
                    boolean currentHandState = true;
                    if (!previousHandState) {
                        //t1.speak("your Hand is up", TextToSpeech.QUEUE_FLUSH, null);
                        handUpTime = currentTime;
                    }
                    else {
                        long elapsedTime= currentTime - handUpTime;
                        long seconds=elapsedTime/1000;
                        String time=""+seconds;
                        //t1.speak(time,TextToSpeech.QUEUE_FLUSH,null);
                        Log.e(null, time );

                    }
                } else if (hipAngle < 180) {
                    boolean currentHandState = false;
                    if (previousHandState != currentHandState) {
                        t1.speak("your Hand is down", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
        }
        return 0;
    }

    private int triangle() {
        return 0;
    }

    private int warrior1() {
        return 0;
    }


}
