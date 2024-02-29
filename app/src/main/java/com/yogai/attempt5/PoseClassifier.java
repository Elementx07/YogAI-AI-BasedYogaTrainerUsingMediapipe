package com.yogai.attempt5;

import static android.content.ContentValues.TAG;
import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;
import static java.lang.Math.atan2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
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


    private boolean isRunning = false;
    private int seconds = 0;

    private Handler handler = new Handler();
    private Runnable runnable;

    boolean stage1,stage2 = false;
    boolean flag1=false;
    boolean poseComplete = false;
    boolean session = false;

    @SuppressLint("RestrictedApi")
    public PoseClassifier(Context context) {
        t1 = new TextToSpeech(getApplicationContext(context), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    float speechRate = 0.5f; // Adjust the value as needed (0.5 is slower, 2.0 is faster)
                    t1.setSpeechRate(speechRate);

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

    private void tree(PoseLandmarkerResult results) {
        PointF wrist = getLandmarkPosition(results, 11);
        PointF elbow = getLandmarkPosition(results, 13);
        PointF shoulder = getLandmarkPosition(results, 15);
        PointF hip = getLandmarkPosition(results, 23);

        Double elbowAngle = calculateAngle(wrist, elbow, shoulder);
        Double shoulderAngle = calculateAngle(elbow, shoulder, hip);
        Float visibleHand = results.landmarks().get(0).get(13).visibility().get();

        PointF leftAnkle = getLandmarkPosition(results, 27);
        PointF leftKnee = getLandmarkPosition(results, 25);
        Double kneeAngle = calculateAngle(leftAnkle, leftKnee, hip);

        if (!session) {
            t1.speak("Start by standing with your feet together, distributing the weight evenly. Align your spine with your neck and head. Relax your arms by your sides.", TextToSpeech.QUEUE_ADD, null);
            session = true;
        } else if (session && !stage1 && !flag1){
            t1.speak("Now, lift your right foot and place it on the inner left thigh, ensuring the sole is firmly against the thigh and toes point downward.", TextToSpeech.QUEUE_ADD, null);
            flag1= true;
        } else if (session && stage1 && !stage2) {
            t1.speak("Now, bring your arms straight up and touch your palms.", TextToSpeech.QUEUE_ADD, null);
            stage2 = true;
        }

        if (visibleHand > 0.5 && session && !poseComplete) {
            boolean previousState = viewModel.isHandUp();
            if (shoulderAngle > 180 && elbowAngle < 340 && elbowAngle > 285 && shoulderAngle < 230) {
                boolean currentState = true;
                if (previousState != currentState) {
                    if (stage1) {
                        t1.speak("Your pose is complete. Hold this position as long as possible.", TextToSpeech.QUEUE_ADD, null);
                        poseComplete=true;
                        startTimer();
                    }
                    t1.speak("Your hand is up.", TextToSpeech.QUEUE_ADD, null);
                    stage2 = true;
                    viewModel.setHandUp(true);
                }
            } else {
                boolean currentState = false;
                if (previousState != currentState) {
                    t1.speak("Your hand is down.", TextToSpeech.QUEUE_FLUSH, null);
                    Log.d(TAG, "tree: Hand down");
                    viewModel.setHandUp(false);
                    stopTimer();
                    resetTimer();
                    stage2 = false;
                    poseComplete = false;
                }
            }
            if (kneeAngle < 210 && kneeAngle > 190 && !stage1 ) {
                // Additional condition logic if needed
                t1.speak("Your knee is bent.", TextToSpeech.QUEUE_ADD, null);
                stage1 = true;
            }
        }
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
