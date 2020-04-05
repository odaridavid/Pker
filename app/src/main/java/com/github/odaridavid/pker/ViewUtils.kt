package com.github.odaridavid.pker

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

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


fun Context.showToast(msg: String?) {
    Toast.makeText(this, msg ?: "", Toast.LENGTH_LONG).show()
}

fun showSnackbar(attachOn: View, msg: String) {
    Snackbar.make(attachOn, msg, Snackbar.LENGTH_LONG)
}

