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

    init {
        // Initialize your poseList with Pose objects
        poseList.add(Pose(R.drawable.tree, "Tree Pose"))
        poseList.add(Pose(R.drawable.downwarddog, "Downward Dog"))
        // Add more poses as needed
    }

    fun getPoses(): List<Pose> {
        return poseList
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

//    val selectedPoseName: String?
//        get() = _selectedPoseName
    fun setSelectedPoseName(poseName: String) {
        _selectedPoseName = poseName
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