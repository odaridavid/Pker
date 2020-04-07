package com.github.odaridavid.pker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.odaridavid.pker.R
import com.github.odaridavid.pker.utils.CameraUtils
import com.github.odaridavid.pker.utils.PermissionUtils.RQ_PERMISSIONS
import com.github.odaridavid.pker.utils.PermissionUtils.allPermissionsGranted
import com.github.odaridavid.pker.utils.setTransparentBars
import com.github.odaridavid.pker.utils.showToast
import com.google.common.util.concurrent.ListenableFuture
import org.koin.android.ext.android.inject

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
class CameraActivity : AppCompatActivity(R.layout.activity_camera) {

    private lateinit var viewFinder: PreviewView
    private val preview: Preview by inject()
    private val cameraProvider: ListenableFuture<ProcessCameraProvider> by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransparentBars(this)

        viewFinder = findViewById(R.id.preview_view)

        if (allPermissionsGranted(this, CameraUtils.CAMERA_PERMISSIONS)) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                this, CameraUtils.CAMERA_PERMISSIONS, RQ_PERMISSIONS
            )
        }
    }


    private fun startCamera() {
        cameraProvider.addListener(Runnable {
            val cameraProvider = cameraProvider.get()

            val cameraSelector = CameraUtils.buildCameraSelector(CameraSelector.LENS_FACING_FRONT)

            val imageCapture =
                CameraUtils.buildImageCapture(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)

            // Attach use cases to the camera with the same lifecycle owner
            val camera = cameraProvider.bindToLifecycle(
                this@CameraActivity, cameraSelector, preview, imageCapture
            )

            // Connect the preview use case to the previewView
            preview.setSurfaceProvider(viewFinder.createSurfaceProvider(camera.cameraInfo))

        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RQ_PERMISSIONS) {
            if (allPermissionsGranted(this, CameraUtils.CAMERA_PERMISSIONS)) {
                viewFinder.post { startCamera() }
            } else {
                showToast(getString(R.string.info_permissions_not_granted))
                finish()
            }
        }
    }

}
