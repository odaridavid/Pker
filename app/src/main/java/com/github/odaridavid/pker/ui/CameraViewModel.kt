package com.github.odaridavid.pker.ui

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.odaridavid.pker.R

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


    init {
        _cameraConfig.value = CameraConfig(
            lens = CameraSelector.LENS_FACING_FRONT,
            flash = ImageCapture.FLASH_MODE_ON
        )
    }

    fun switchCameraFacingLense(@IntRange(from = 0, to = 1) mode: Int) {
        val updatedConfig = _cameraConfig.value!!.copy(lens = mode)
        _cameraConfig.value = updatedConfig
    }

    fun switchFlashMode(@IntRange(from = 0, to = 2) mode: Int) {
        val updatedConfig = _cameraConfig.value!!.copy(flash = mode)
        _cameraConfig.value = updatedConfig
    }

    /**
     * Takes in current mode and displays icon to switch to alternative mode
     */
    @DrawableRes
    fun setFlashIcon(@IntRange(from = 0, to = 2) mode: Int): Int {
        return when (mode) {
            ImageCapture.FLASH_MODE_ON -> R.drawable.ic_flash_on_black_24dp
            ImageCapture.FLASH_MODE_OFF -> R.drawable.ic_flash_off_black_24dp
            ImageCapture.FLASH_MODE_AUTO -> R.drawable.ic_flash_auto_black_24dp
            else -> R.drawable.ic_flash_on_black_24dp
        }
    }

}

data class CameraConfig(val lens: Int, val flash: Int)