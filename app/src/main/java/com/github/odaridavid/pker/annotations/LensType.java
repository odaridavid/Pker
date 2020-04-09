package com.github.odaridavid.pker.annotations;


import androidx.annotation.IntDef;
import androidx.camera.core.CameraSelector;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * Copyright 2020 David Odari
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/
@Retention(value = AnnotationRetention.SOURCE)
@IntDef(value = {CameraSelector.LENS_FACING_BACK, CameraSelector.LENS_FACING_FRONT})
public @interface LensType {
}
