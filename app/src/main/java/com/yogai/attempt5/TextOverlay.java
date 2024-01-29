package com.yogai.attempt5;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;
import static java.lang.Math.atan2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import org.jetbrains.annotations.TestOnly;

import java.util.Locale;

public class TextOverlay extends View {
    @SuppressLint("RestrictedApi")
    public TextOverlay(Context context, AttributeSet attrs) {
        super(context,attrs);
        initPaints();
        t1 = new TextToSpeech(getApplicationContext(context), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            t1.setLanguage(Locale.UK);
                        }
                    }
                });
    }

    private PoseLandmarkerResult results;
    private Paint pointPaint2;

    private int previousState=1;

    private float scaleFactor = 1f;
    private int imageWidth = 1;
    private int imageHeight = 1;
    private TextToSpeech t1;
    private MainViewModel viewModel = new MainViewModel();


    public void clear() {
        results = null;
        pointPaint2.reset();
        invalidate();
        initPaints();
    }

    private void initPaints() {
        pointPaint2 = new Paint();
        pointPaint2.setColor(Color.YELLOW);
        pointPaint2.setStrokeWidth(LANDMARK_STROKE_WIDTH);
        pointPaint2.setStyle(Paint.Style.FILL);
    }

    private static final float LANDMARK_STROKE_WIDTH = 12F;

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if(results != null && results.landmarks().size() > 0) {
            // Example: Calculate and draw the angle between left wrist, elbow, and shoulder
            PointF wrist = getLandmarkPosition(results, 11);
            PointF elbow = getLandmarkPosition(results, 13);
            PointF shoulder = getLandmarkPosition(results, 15);
            PointF hip = getLandmarkPosition(results, 23);

            Double angle = calculateAngle(wrist, elbow, shoulder);
            Double hipAngle = calculateAngle(elbow, shoulder, hip);
            Paint pointPaint2 = new Paint();
            pointPaint2.setTextSize(90f);


            Float visibleHand = results.landmarks().get(0).get(13).visibility().get();
            if (visibleHand > 0.5) {

                boolean previousHandState = viewModel.isHandCurled();

                String hipAngleText =
                        String.format(Locale.US, "hip Angle: %.2f degrees", hipAngle);
                canvas.drawText(hipAngleText, 20f, 370f, pointPaint2);
                if (hipAngle > 130 && hipAngle < 270) {
                    if (angle > 90 && angle < 210) {
                        boolean currentHandState = true;
                        if (previousHandState != currentHandState) {
                            t1.speak("your Hand is curled", TextToSpeech.QUEUE_FLUSH, null);
                            viewModel.setHandCurled(true);
                        }
                        String straighthand = String.format(Locale.US, "Hand is curled");
                        canvas.drawText(straighthand, 20f, 120f, pointPaint2);
                    } else if (angle > 300) {
                        boolean currentHandState = false;
                        if (previousHandState != currentHandState) {
                            t1.speak("your Hand is straight", TextToSpeech.QUEUE_FLUSH, null);
                            viewModel.setHandCurled(false);
                        }
                        canvas.drawText("hand is straight", 20f, 120f, pointPaint2);
                    }
                }
            }
            // Draw the angle on the canvas
            String angleText = String.format(Locale.US, "Angle: %.2f degrees", angle);
            canvas.drawText(angleText, 20f, 60f, pointPaint2);
        }
    }

    private Double calculateAngle(PointF pointA,PointF pointB,PointF pointC){
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

    public void setResults(PoseLandmarkerResult pResults, int imageWidth, int imageHeight, RunningMode runningMode) {
        results = pResults;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        scaleFactor = runningMode == RunningMode.LIVE_STREAM
                ? Math.max(getWidth() * 1f / imageWidth, getHeight() * 1f / imageHeight)
                : Math.min(getWidth() * 1f / imageWidth, getHeight() * 1f / imageHeight);

        invalidate();
    }
}
