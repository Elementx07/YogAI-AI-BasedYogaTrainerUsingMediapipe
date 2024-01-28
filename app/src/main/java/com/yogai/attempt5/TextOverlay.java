package com.yogai.attempt5;

import static java.lang.Math.atan2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import java.util.Locale;

public class TextOverlay extends View {
    public TextOverlay(Context context, AttributeSet attrs) {
        super(context,attrs);
        initPaints();
    }

    private PoseLandmarkerResult results;
    private Paint pointPaint2;

    private float scaleFactor = 1f;
    private int imageWidth = 1;
    private int imageHeight = 1;


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
    }

    public void text() {
        // Example: Calculate and draw the angle between left wrist, elbow, and shoulder
        PointF wrist = getLandmarkPosition(results, 11);
        PointF elbow = getLandmarkPosition(results, 13);
        PointF shoulder = getLandmarkPosition(results, 15);
        PointF hip = getLandmarkPosition(results, 23);

        PointF angle = calculateAngle(wrist, elbow, shoulder);
        PointF hipAngle = calculateAngle(elbow, shoulder, hip);
        PointF pointPaint2 = Paint();
        pointPaint2.textSize = 90f;


        Float visibleHand = results.landmarks().get(0).get(13).visibility().get();
        if (visibleHand > 0.5) {
            String hipAngleText =
                    String.format(Locale.US, "hip Angle: %.2f degrees", hipAngle);
            canvas.drawText(hipAngleText, 20f, 370f, pointPaint2);
            if (hipAngle > 130 && hipAngle < 250) {
                if (angle > 90 && angle < 210) {
                    val straighthand = String.format(Locale.US, "Hand is curled");
                    canvas.drawText(straighthand, 20f, 120f, pointPaint2);
                } else if (angle > 300) {
                    canvas.drawText("hand is straight", 20f, 120f, pointPaint2);
                }
            }
        }

        // Draw the angle on the canvas
        val angleText = String.format(Locale.US, "Angle: %.2f degrees", angle);
        canvas.drawText(angleText, 20f, 60f, pointPaint2)

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
        NormalizedLandmark landmarkObject = poseLandmarkerResult.landmarks().get(0).get(landmark);
        return new PointF(landmarkObject.x() * imageWidth * scaleFactor, landmarkObject.y() * imageHeight * scaleFactor);
    }
}
