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

import androidx.lifecycle.ViewModel

/**
 *  This ViewModel is used to store pose landmarker helper settings
 */
class MainViewModel : ViewModel() {

    public val poseList: MutableList<Pose> = mutableListOf()
    public val poseListPerformed: MutableList<Pose> = mutableListOf()

    init {
        // Initialize your poseList with Pose objects
        poseList.add(Pose(R.drawable.tree_detail, "Tree"))
        poseList.add(Pose(R.drawable.cobra, "Cobra"))
        poseList.add(Pose(R.drawable.camel, "Camel"))
        poseList.add(Pose(R.drawable.triangle, "Extended Triangle"))
        poseListPerformed.add(Pose(R.drawable.tree_detail, "Tree"))
        // Add more poses as needed
    }

    fun getPoses(): List<Pose> {
        return poseList
    }
    fun getPosesPerformed(): List<Pose> {
        return poseListPerformed
    }

    private var _model = PoseLandmarkerHelper.MODEL_POSE_LANDMARKER_FULL
    private var _delegate: Int = PoseLandmarkerHelper.DELEGATE_CPU
    private var _minPoseDetectionConfidence: Float =
        PoseLandmarkerHelper.DEFAULT_POSE_DETECTION_CONFIDENCE
    private var _minPoseTrackingConfidence: Float = PoseLandmarkerHelper
        .DEFAULT_POSE_TRACKING_CONFIDENCE
    private var _minPosePresenceConfidence: Float = PoseLandmarkerHelper
        .DEFAULT_POSE_PRESENCE_CONFIDENCE
    private var _isHandCurled: Boolean = false;
    private var _selectedPoseId: Int = 0
    private var _selectedPoseName: String? = null
    private var _isHandUp: Boolean = false
    private var _poseDuration: String = "0"



    private var _accuracy: Double = 0.0

    val currentAccuracy: Double get() = _accuracy
    val currentDelegate: Int get() = _delegate
    val currentModel: Int get() = _model
    val currentMinPoseDetectionConfidence: Float
        get() =
            _minPoseDetectionConfidence
    val currentMinPoseTrackingConfidence: Float
        get() =
            _minPoseTrackingConfidence
    val currentMinPosePresenceConfidence: Float
        get() =
            _minPosePresenceConfidence

    val isHandCurled: Boolean
        get()
            =_isHandCurled

    val isHandUp: Boolean
        get()
        =_isHandUp

    val poseDuration: String
        get()
        =_poseDuration

//    val selectedPoseName: String?
//        get() = _selectedPoseName
    fun setSelectedPoseName(poseName: String) {
        _selectedPoseName = poseName
    }
    fun setAccuracy(accuracy: Double){
        _accuracy = accuracy
    }

    fun getAccuracy(): Double {
        return _accuracy
    }

    fun getSelectedPoseId(): Int {
        return _selectedPoseId
    }

    fun getSelectedPose(): Pose {
        return poseList[_selectedPoseId]
    }

    fun setSelectedPoseId(id: Int) {
        _selectedPoseId = id
    }

    fun setPoseDuration(duration: String){
        _poseDuration = duration
    }

    fun setDelegate(delegate: Int) {
        _delegate = delegate
    }

    fun setHandCurled(isHandCurled: Boolean) {
        _isHandCurled = isHandCurled
    }

    fun setHandUp(isHandUp: Boolean) {
        _isHandUp = isHandUp
    }

    fun setMinPoseDetectionConfidence(confidence: Float) {
        _minPoseDetectionConfidence = confidence
    }

    fun setMinPoseTrackingConfidence(confidence: Float) {
        _minPoseTrackingConfidence = confidence
    }

    fun setMinPosePresenceConfidence(confidence: Float) {
        _minPosePresenceConfidence = confidence
    }

    fun setModel(model: Int) {
        _model = model
    }

    fun getSelectedPoseName(): String? {
        return poseList[_selectedPoseId].poseName
    }


}