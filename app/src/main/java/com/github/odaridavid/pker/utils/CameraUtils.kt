package com.github.odaridavid.pker.utils

import android.Manifest
import android.content.Context
import androidx.annotation.IntRange
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import java.io.File

/**
 *
 * Copyright 2020 David Odari
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *            http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 **/
object CameraUtils {

    /**
     * Permissions required to use the camera
     */
    val CAMERA_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /**
     * Helper function that defines save location for image captured
     */
    fun getSaveLocation(context: Context) = File(
        context.externalMediaDirs.first(),
        "${System.currentTimeMillis()}.jpg"
    )

    /**
     * Set up the capture use case by defining a capture mode  to allow users to take photos.
     *
     * We don't set a resolution for image capture; instead, we select a capture mode which will
     * infer the appropriate resolution based on aspect ration and requested mode.
     *
     * @see ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY = 0
     * @see ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY = 1
     *
     */
    fun buildImageCapture(
        @IntRange(from = 0, to = 1) captureMode: Int,
        @IntRange(from = 0, to = 2) flashMode: Int
    ): ImageCapture {
        return ImageCapture.Builder()
            .apply {
                setCaptureMode(captureMode)
                //Flash will be used when taking a picture if activated
                setFlashMode(flashMode)
            }.build()
    }

    /**
     * Helps choose a camera by specifying the lens to use
     *
     * @see CameraSelector.LENS_FACING_BACK = 1
     * @see CameraSelector.LENS_FACING_FRONT = 0
     */
    fun buildCameraSelector(@IntRange(from = 0, to = 1) lens: Int): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(lens)
            .build()
    }

}
