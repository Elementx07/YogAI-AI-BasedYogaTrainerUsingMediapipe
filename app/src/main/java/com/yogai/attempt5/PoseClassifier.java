package com.yogai.attempt5;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;
import static java.lang.Math.atan2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
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


    private boolean isRunning = false;
    private int seconds = 0;

    private Handler handler = new Handler();
    private Runnable runnable;

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
                case "Lotus":
                    tree(results);
                case "Cobra":
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

        Double elbowAngle = calculateAngle(wrist, elbow, shoulder);
        Double shoulderAngle = calculateAngle(elbow, shoulder, hip);
        Float visibleHand = results.landmarks().get(0).get(13).visibility().get();

        Log.e(null, ""+viewModel.isHandUp() );
        if (visibleHand > 0.5)
        {
            if (shoulderAngle > 190 && elbowAngle > 300 && elbowAngle<360 && shoulderAngle<210 && !viewModel.isHandUp())
            {
                t1.speak("your Hand is up", TextToSpeech.QUEUE_ADD, null);
                viewModel.setHandUp(true);
                startTimer();
            }
            else if(viewModel.isHandUp() && shoulderAngle < 190)
            {
                viewModel.setHandUp(false);
                stopTimer();
                resetTimer();
            }
            else if(viewModel.isHandUp() && elbowAngle <300){
                viewModel.setHandUp(false);
                stopTimer();
                resetTimer();
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

    private void startTimer() {
        if (!isRunning) {
            isRunning = true;
            runnable = new Runnable() {
                @Override
                public void run() {
                    seconds++;
                    String time=updateTimerText();
                    t1.speak(time,TextToSpeech.QUEUE_ADD,null);
                    handler.postDelayed(this, 1000); // Update every 1 second
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    private String updateTimerText() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        //String time = String.format("%02d:%02d", minutes, remainingSeconds);
        return ""+remainingSeconds;

    }

    private void stopTimer() {
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }

    private void resetTimer() {
        seconds = 0;
        updateTimerText();
    }
}
