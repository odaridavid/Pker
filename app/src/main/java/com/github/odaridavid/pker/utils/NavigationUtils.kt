package com.github.odaridavid.pker.utils

import android.app.Activity
import android.content.Context
import android.content.Intent


/**
 * Helper extension function to navigate from one activity to another
 */
inline fun <reified T : Activity> Context.navigateTo(noinline block: ((Intent) -> Unit)? = null) {
    val intent = Intent(this, T::class.java)
    block?.run { block(intent) }
    startActivity(intent)
}