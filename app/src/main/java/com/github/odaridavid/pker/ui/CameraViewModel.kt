package com.github.odaridavid.pker.ui

import androidx.annotation.DrawableRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.odaridavid.pker.R
import com.github.odaridavid.pker.annotations.FlashMode
import com.github.odaridavid.pker.annotations.LensType
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors

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
class CameraViewModel : ViewModel() {

    private val _cameraConfig = MutableLiveData<CameraConfig>()
    val cameraConfig: LiveData<CameraConfig>
        get() = _cameraConfig

    private val _onImageCapturedResult = MutableLiveData<String>()
    val onImageCapturedResult: LiveData<String>
        get() = _onImageCapturedResult

    init {
        //Default Config
        _cameraConfig.value = CameraConfig(
            lens = CameraSelector.LENS_FACING_FRONT,
            flash = ImageCapture.FLASH_MODE_ON
        )
    }

    fun setOnImageCapturedResult(msg: String) {
        _onImageCapturedResult.postValue(msg)
    }

    fun switchCameraFacingLense(@LensType lens: Int) {
        val updatedConfig = _cameraConfig.value!!.copy(lens = lens)
        _cameraConfig.value = updatedConfig
    }

    fun switchFlashMode(@FlashMode mode: Int) {
        val updatedConfig = _cameraConfig.value!!.copy(flash = mode)
        _cameraConfig.value = updatedConfig
    }

    /**
     * Takes in current mode and displays icon to switch to alternative mode
     */
    @DrawableRes
    fun setFlashIcon(@FlashMode mode: Int): Int {
        return when (mode) {
            ImageCapture.FLASH_MODE_ON -> R.drawable.ic_flash_on_black_24dp
            ImageCapture.FLASH_MODE_OFF -> R.drawable.ic_flash_off_black_24dp
            ImageCapture.FLASH_MODE_AUTO -> R.drawable.ic_flash_auto_black_24dp
            else -> R.drawable.ic_flash_on_black_24dp
        }
    }

    fun takePicture(imageCapture: ImageCapture, parent: File) {
        imageCapture.takePicture(
            ImageCapture.OutputFileOptions.Builder(
                    File(
                        parent,
                        "${System.currentTimeMillis()}.jpg"
                    )
                )
                .build(),
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded"
                    Timber.d(msg)
                    setOnImageCapturedResult(msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exception.message}"
                    Timber.e(msg)
                    setOnImageCapturedResult(msg)
                }
            }
        )
    }

}

data class CameraConfig(val lens: Int, val flash: Int)