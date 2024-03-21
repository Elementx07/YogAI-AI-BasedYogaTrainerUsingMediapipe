/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yogai.attempt5

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: PoseLandmarkerResult? = null
    private var pointPaint = Paint()
    private var linePaint = Paint()

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    private var viewModel: MainViewModel?= MainViewModel()
    private val pc = PoseClassifier.getInstance(context)


    init {
        initPaints()
    }
    fun clear() {
        results = null
        pointPaint.reset()
        linePaint.reset()
        invalidate()
        initPaints()
    }



    private fun initPaints() {
        linePaint.color = Color.RED
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        pointPaint.color = Color.YELLOW
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = Paint.Style.FILL
    }


//    override fun draw(canvas: Canvas) {
//        super.draw(canvas)
//        results?.let { poseLandmarkerResult ->
//            //retrieve the selected pose name from the view model
//            val selectedPose = viewModel?.getSelectedPoseName()
//            if(selectedPose != null) {
//                val handler = Handler()
//                val runnable = Runnable {
//                    pc.classifyPose(poseLandmarkerResult, selectedPose)
//                    Log.e(TAG, "draw: bruhh")
//                }
//                handler.postDelayed(runnable, 1000)
//            }
//            for (landmark in poseLandmarkerResult.landmarks()) {
//                for (normalizedLandmark in landmark) {
//                    // Invert Y-coordinate
//                    val invertedY =normalizedLandmark.y() * imageHeight * scaleFactor
//
//                    // Invert X-coordinate if needed
//                    val invertedX = (1-normalizedLandmark.x()) * imageWidth * scaleFactor - 250
//
//                    canvas.drawPoint(invertedX, invertedY, pointPaint)
//                }
//
//                PoseLandmarker.POSE_LANDMARKS.forEach {
//                    // Invert Y-coordinates for lines
//                    val startY = (poseLandmarkerResult.landmarks()[0].get(it!!.start()).y() * imageHeight * scaleFactor)
//                    val endY = (poseLandmarkerResult.landmarks()[0].get(it.end()).y() * imageHeight * scaleFactor)
//
//                    // Invert X-coordinates if needed
//                    val startX = ((1-poseLandmarkerResult.landmarks()[0].get(it.start()).x()) * imageWidth * scaleFactor)-250
//                    val endX = ((1-poseLandmarkerResult.landmarks()[0].get(it.end()).x()) * imageWidth * scaleFactor)-250
//
//                    canvas.drawLine(startX, startY, endX, endY, linePaint)
//                }
//            }
//
//        }
//    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let { poseLandmarkerResult ->
            // Move the postDelayed block outside the loop
            val selectedPose = viewModel?.getSelectedPoseName()
            if(selectedPose != null) {
                val handler = Handler()
                val runnable = Runnable {
                    pc.classifyPose(poseLandmarkerResult, selectedPose)
                }
                handler.postDelayed(runnable, 1000)
            }

            for (landmark in poseLandmarkerResult.landmarks()) {
                for (normalizedLandmark in landmark) {
                    // Invert Y-coordinate
                    val invertedY =normalizedLandmark.y() * imageHeight * scaleFactor

                    // Invert X-coordinate if needed
                    val invertedX = (1-normalizedLandmark.x()) * imageWidth * scaleFactor - 250

                    canvas.drawPoint(invertedX, invertedY, pointPaint)
                }

                PoseLandmarker.POSE_LANDMARKS.forEach {
                    // Invert Y-coordinates for lines
                    val startY = (poseLandmarkerResult.landmarks()[0].get(it!!.start()).y() * imageHeight * scaleFactor)
                    val endY = (poseLandmarkerResult.landmarks()[0].get(it.end()).y() * imageHeight * scaleFactor)

                    // Invert X-coordinates if needed
                    val startX = ((1-poseLandmarkerResult.landmarks()[0].get(it.start()).x()) * imageWidth * scaleFactor)-250
                    val endX = ((1-poseLandmarkerResult.landmarks()[0].get(it.end()).x()) * imageWidth * scaleFactor)-250

                    canvas.drawLine(startX, startY, endX, endY, linePaint)
                }
            }


        }
    }


    fun setResults(
        poseLandmarkerResults: PoseLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE
    ) {
        results = poseLandmarkerResults

        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                min(width * 1f / imageWidth, height * 1f / imageHeight)
            }
            RunningMode.LIVE_STREAM -> {
                // PreviewView is in FILL_START mode. So we need to scale up the
                // landmarks to match with the size that the captured images will be
                // displayed.
                max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
        }
        invalidate()
    }

    companion object {
        private const val LANDMARK_STROKE_WIDTH = 12F
    }
}