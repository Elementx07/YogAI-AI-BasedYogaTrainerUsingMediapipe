package com.yogai.attempt5;

import static android.content.ContentValues.TAG;
import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;
import static com.google.android.material.internal.ContextUtils.getActivity;
import static java.lang.Math.atan2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;
import com.yogai.attempt5.databinding.FragmentCameraBinding;

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
    FragmentCameraBinding binding;
    private TextToSpeech t1;
    Context context;
    private boolean isRunning = false;
    private int seconds = 0;
    private Handler handler = new Handler();
    private Runnable runnable;
    boolean stage1,stage2 = false;
    boolean flag1=false;
    boolean flag2=false;
    boolean flag3=false;
    boolean poseComplete = false;
    boolean session = false;
    double accuracy;
    double averageAccuracy=0;
    CameraFragment cf;
    AccuracyFragment af;
    Button end;
    String time=" 00:00";
    private static PoseClassifier instance;
    public static synchronized PoseClassifier getInstance(Context context) {
        if (instance == null) {
            instance = new PoseClassifier(context);
        }
        return instance;
    }

    public String getTime() {
        return time;
    }

    @SuppressLint("RestrictedApi")
    public PoseClassifier(Context context) {
        this.context = context;
        this.t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    t1.setSpeechRate(0.5f);
                }
            }
        });
        this.cf=new CameraFragment();
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
                case "Tree":
                    tree(results);
                    break;
                case "Cobra":
                    triangle();
                    break;
                case "Warrior":
                    warrior();
                    break;
                default:
                    return 0;
            }
        }
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
        } else if (session && stage1 && !stage2 && !flag2) {
            t1.speak("Now, bring your arms straight up and touch your palms.", TextToSpeech.QUEUE_ADD, null);
            flag2=true;
        }

        if (visibleHand > 0.5 && session ) {
            boolean previousState = viewModel.isHandUp();
            boolean currentState = (shoulderAngle > 180 && elbowAngle < 340 && elbowAngle > 285 && shoulderAngle < 230);
            if(currentState && !flag3 && stage1 && !stage2) {
                t1.speak("Your pose is complete. Hold this position as long as possible.", TextToSpeech.QUEUE_ADD, null);
                startTimer();
                t1.speak("Your hand is up.", TextToSpeech.QUEUE_ADD, null);
                stage2 = true;
                viewModel.setHandUp(true);
            }
            if((shoulderAngle>0 || shoulderAngle <350) && shoulderAngle<160 && stage2){
                t1.speak("Your hand is down.", TextToSpeech.QUEUE_FLUSH, null);
                Log.d(TAG, "Hand down");
                viewModel.setHandUp(false);
                stopTimer();
                resetTimer();
                stage2 = false;
                flag2=false;
            }
            if (kneeAngle < 210 && kneeAngle > 190 && !stage1) {
                // Additional condition logic if needed
                t1.speak("Your knee is bent.", TextToSpeech.QUEUE_ADD, null);
                stage1 = true;
                flag1=true;
            }
            if ((kneeAngle>350 || (kneeAngle > 0 && kneeAngle<180)) && stage1) {
                t1.speak("Your knee is straight.", TextToSpeech.QUEUE_FLUSH, null);
                stage1 = false;
                flag1=false;
            }
        }



    }

    private int triangle() {
        //TODO: Implement triangle pose
        PointF leftAnkle = getLandmarkPosition(results, 27);
        PointF leftKnee = getLandmarkPosition(results, 25);
        PointF leftHip = getLandmarkPosition(results, 23);
        PointF rightAnkle = getLandmarkPosition(results, 28);
        PointF rightKnee = getLandmarkPosition(results, 26);
        PointF rightHip = getLandmarkPosition(results, 24);
        PointF leftShoulder = getLandmarkPosition(results, 15);

        Double leftKneeAngle = calculateAngle(leftAnkle, leftKnee, leftHip);
        Double rightKneeAngle = calculateAngle(rightAnkle, rightKnee, rightHip);
        Double leftHipAngle = calculateAngle(leftKnee, leftHip, leftShoulder);

        if(!session) {
            t1.speak("Start by standing with your feet apart, about 3-4 feet. Turn your right foot out 90 degrees and your left foot in about 15 degrees. Align your right heel with your left heel. Raise your arms parallel to the floor and reach them actively out to the sides, shoulder blades wide, palms down.", TextToSpeech.QUEUE_ADD, null);
            session = true;
        } else if (session && !stage1 && !flag1) {
            t1.speak("Now, bend your right knee over your right ankle, so that your shin is perpendicular to the floor. Stretch your left arm toward the ceiling, and bring your right hand to the floor.", TextToSpeech.QUEUE_ADD, null);
            flag1 = true;
        } else if (session && stage1 && !stage2 && !flag2) {
            t1.speak("Now, straighten your right leg and turn your left foot out to the left 90 degrees. Align your left heel with your right heel. Stretch your right arm toward the ceiling, and bring your left hand to the floor.", TextToSpeech.QUEUE_ADD, null);
            flag2 = true;
        }

        if (leftKneeAngle > 160 && leftKneeAngle < 200 && rightKneeAngle > 160 && rightKneeAngle < 200 && leftHipAngle > 160 && leftHipAngle < 200) {
            t1.speak("Your pose is complete. Hold this position as long as possible.", TextToSpeech.QUEUE_FLUSH, null);
            startTimer();
            poseComplete = true;

        }
        if(leftKneeAngle > 160 && leftKneeAngle < 200 && rightKneeAngle > 160 && rightKneeAngle < 200 && leftHipAngle < 160 && leftHipAngle > 200) {
            t1.speak("Your left hip is not aligned with your left knee. Please try again.", TextToSpeech.QUEUE_FLUSH, null);
            stage1 = stage2 = false;
            flag1 = flag2 = flag3 = false;
            poseComplete = false;
            stopTimer();
            resetTimer();
        }
        if(leftKneeAngle < 160 && leftKneeAngle > 200 && rightKneeAngle < 160 && rightKneeAngle > 200 && leftHipAngle < 160 && leftHipAngle > 200) {
            t1.speak("Your pose is incorrect. Please try again.", TextToSpeech.QUEUE_FLUSH, null);
            stage1 = stage2 = false;
            flag1 = flag2 = flag3 = false;
            poseComplete = false;
            stopTimer();
            resetTimer();
        }
        return 0;
    }

    private int warrior() {
        //TODO: Implement warrior 1 pose
        PointF leftAnkle = getLandmarkPosition(results, 27);
        PointF leftKnee = getLandmarkPosition(results, 25);
        PointF leftHip = getLandmarkPosition(results, 23);
        PointF rightAnkle = getLandmarkPosition(results, 28);
        PointF rightKnee = getLandmarkPosition(results, 26);
        PointF rightHip = getLandmarkPosition(results, 24);
        PointF leftShoulder = getLandmarkPosition(results, 15);

        Double leftKneeAngle = calculateAngle(leftAnkle, leftKnee, leftHip);
        Double rightKneeAngle = calculateAngle(rightAnkle, rightKnee, rightHip);
        Double leftHipAngle = calculateAngle(leftKnee, leftHip, leftShoulder);

        if(!session) {
            t1.speak("Start by standing with your feet apart, about 3-4 feet. Turn your right foot out 90 degrees and your left foot in about 15 degrees. Align your right heel with your left heel. Raise your arms parallel to the floor and reach them actively out to the sides, shoulder blades wide, palms down.", TextToSpeech.QUEUE_ADD, null);
            session = true;
        } else if (session && !stage1 && !flag1) {
            t1.speak("Now, bend your right knee over your right ankle, so that your shin is perpendicular to the floor. Stretch your left arm toward the ceiling, and bring your right hand to the floor.", TextToSpeech.QUEUE_ADD, null);
            flag1 = true;
        } else if (session && stage1 && !stage2 && !flag2) {
            t1.speak("Now, straighten your right leg and turn your left foot out to the left 90 degrees. Align your left heel with your right heel. Stretch your right arm toward the ceiling, and bring your left hand to the floor.", TextToSpeech.QUEUE_ADD, null);
            flag2 = true;
        }

        if (leftKneeAngle > 160 && leftKneeAngle < 200 && rightKneeAngle > 160 && rightKneeAngle < 200 && leftHipAngle > 160 && leftHipAngle < 200) {
            t1.speak("Your pose is complete. Hold this position as long as possible.", TextToSpeech.QUEUE_FLUSH, null);
            startTimer();
            poseComplete = true;

        }
        if(leftKneeAngle > 160 && leftKneeAngle < 200 && rightKneeAngle > 160 && rightKneeAngle < 200 && leftHipAngle < 160 && leftHipAngle > 200) {
            t1.speak("Your left hip is not aligned with your left knee. Please try again.", TextToSpeech.QUEUE_FLUSH, null);
            stage1 = stage2 = false;
            flag1 = flag2 = flag3 = false;
            poseComplete = false;
            stopTimer();
            resetTimer();
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
        averageAccuracy=(averageAccuracy+accuracy)/remainingSeconds;
        time = String.format("%02d:%02d", minutes, remainingSeconds);
        Log.e(TAG, "updateTimerText: "+time );
        viewModel.setPoseDuration(time);
        return ""+remainingSeconds;
    }

    public void stopTimer() {
        Log.e(TAG, "stopTimer: ",null );
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }

    private void resetTimer() {
        seconds = 0;
        updateTimerText();
    }

    public void stopTTS(){
        t1.shutdown();
    }
}
