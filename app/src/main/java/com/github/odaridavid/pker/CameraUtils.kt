package com.github.odaridavid.pker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Size
import android.view.TextureView
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import timber.log.Timber
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

    val CAMERA_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun getSaveLocation(context: Context) = File(
        context.externalMediaDirs.first(),
        "${System.currentTimeMillis()}.jpg"
    )

    fun buildImageCapture(): ImageCapture {
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                // We don't set a resolution for image capture; instead, we
                // select a capture mode which will infer the appropriate
                // resolution based on aspect ration and requested mode
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            }.build()
        return ImageCapture(imageCaptureConfig)
    }

    fun buildPreview(activity: Activity): Preview {
        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(activity.screenWidth, activity.screenHeight))
        }.build()

        // Build the viewfinder use case
        return Preview(previewConfig)
    }

}

class ImageSaveListener(val context: Context, private val viewFinder: TextureView) :
    ImageCapture.OnImageSavedListener {

    override fun onError(
        imageCaptureError: ImageCapture.ImageCaptureError,
        message: String,
        exc: Throwable?
    ) {
        val msg = "Photo capture failed: $message"
        Timber.e(exc, msg)
        viewFinder.post { context.showToast(msg) }
    }

    override fun onImageSaved(file: File) {
        val msg = "Photo capture succeeded: ${file.absolutePath}"
        Timber.e(msg)
        viewFinder.post {
            context.showToast(msg)
        }
    }
}