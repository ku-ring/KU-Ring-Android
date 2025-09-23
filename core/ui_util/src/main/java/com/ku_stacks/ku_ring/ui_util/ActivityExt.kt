package com.ku_stacks.ku_ring.ui_util

import android.app.Activity
import android.app.Activity.OVERRIDE_TRANSITION_CLOSE
import android.app.Activity.OVERRIDE_TRANSITION_OPEN
import android.os.Build
import androidx.annotation.AnimRes

enum class TransitionType {
    OPEN, CLOSE
}

fun Activity.setActivityTransition(
    transitionType: TransitionType,
    @AnimRes enterAnim: Int,
    @AnimRes exitAnim: Int
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        val overrideType = when(transitionType) {
            TransitionType.OPEN -> OVERRIDE_TRANSITION_OPEN
            TransitionType.CLOSE -> OVERRIDE_TRANSITION_CLOSE

        }
        overrideActivityTransition(overrideType, enterAnim, exitAnim)
    } else {
        overridePendingTransition(enterAnim, exitAnim)
    }
}