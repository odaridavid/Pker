package com.github.odaridavid.pker.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.odaridavid.pker.R
import com.github.odaridavid.pker.utils.CameraUtils
import com.github.odaridavid.pker.utils.PermissionUtils.RQ_PERMISSIONS
import com.github.odaridavid.pker.utils.PermissionUtils.allPermissionsGranted
import com.github.odaridavid.pker.utils.setTransparentBars
import com.github.odaridavid.pker.utils.showToast
import com.google.common.util.concurrent.ListenableFuture
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    //TODO Animate Switching Cameras
    //TODO Show Preview of captured image
    private lateinit var viewFinder: PreviewView
    private lateinit var btnSwitchCamera: ImageButton
    private lateinit var btnTakePicture: ImageButton
    private lateinit var btnControlFlash: ImageButton
    private val preview: Preview by inject()
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> by inject()
    private val cameraViewModel: CameraViewModel by viewModel()
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransparentBars(this)

        initViews()
    }

    override fun onStart() {
        super.onStart()
        checkPermissionsAndInit(onPermissionNotGranted = {
            ActivityCompat.requestPermissions(
                this, CameraUtils.CAMERA_PERMISSIONS, RQ_PERMISSIONS
            )
        })
        observeOnImageCaptured()
    }


    private fun checkPermissionsAndInit(onPermissionNotGranted: () -> Unit) {
        if (allPermissionsGranted(this, CameraUtils.CAMERA_PERMISSIONS)) {
            cameraViewModel.cameraConfig.observe(this, Observer { config ->
                //Adds runnable to the message queue which will be handled on the apps main thread
                viewFinder.post { startCamera(config.lens, config.flash) }
            })
        } else onPermissionNotGranted()
    }

    private fun initViews() {
        viewFinder = findViewById(R.id.preview_view)
        btnSwitchCamera = findViewById(R.id.switch_camera_button)
        btnTakePicture = findViewById(R.id.take_picture_button)
        btnControlFlash = findViewById(R.id.control_flash_button)
    }

    private fun startCamera(lens: Int, flashMode: Int) {
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()

            bindPreview(lens, flashMode)

        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(lens: Int, flashMode: Int) {
        //closes any current camera use cases,if unspecified will crush on switching cameras
        cameraProvider.unbindAll()

        val cameraSelector = CameraUtils.buildCameraSelector(lens)

        //Set Flash Icon
        val flashCamIcon = getDrawable(cameraViewModel.setFlashIcon(flashMode))
        btnControlFlash.setImageDrawable(flashCamIcon)

        imageCapture = CameraUtils.buildImageCapture(
            ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY,
            flashMode
        )

        // Attach use cases to the camera with the same lifecycle owner
        val camera = cameraProvider.bindToLifecycle(
            this@CameraActivity, cameraSelector, preview, imageCapture
        )

        // Connect the preview use case to the previewView
        preview.setSurfaceProvider(viewFinder.createSurfaceProvider(camera.cameraInfo))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RQ_PERMISSIONS) {
            checkPermissionsAndInit(onPermissionNotGranted = {
                showToast(getString(R.string.info_permissions_not_granted))
                finish()
            })
        }
    }

    /**
     * Sets current flash mode to use
     */
    fun changeFlashMode(view: View) {
        cameraViewModel.switchFlashMode(
            when (cameraViewModel.cameraConfig.value!!.flash) {
                ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_OFF
                ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_AUTO
                ImageCapture.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_ON
                else -> ImageCapture.FLASH_MODE_OFF
            }
        )
    }

    /**
     * Switches camera lens depending on current lens being used
     */
    fun switchCamera(view: View) {
        cameraViewModel.switchCameraFacingLense(
            if (cameraViewModel.cameraConfig.value!!.lens == CameraSelector.LENS_FACING_FRONT)
                CameraSelector.LENS_FACING_BACK
            else CameraSelector.LENS_FACING_FRONT
        )
    }

    /**
     * Takes the current preview shot
     */
    fun takePicture(view: View) {
        cameraViewModel.takePicture(imageCapture, externalMediaDirs.first())
    }

    /**
     * Changes zoom level to the ones provided
     */
    fun changeZoomLevel(view: View) {
        //TODO Implement Zoom
    }

    private fun observeOnImageCaptured() {
        cameraViewModel.onImageCapturedResult.observe(this, Observer { msg ->
            showToast(msg)
        })
    }
}
